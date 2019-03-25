package org.analog_vortices.life_itself;

import processing.core.PApplet;

import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedHashMap;

public class Main extends PApplet {
 public static void main(String[] args) {
  PApplet.main(Main.class.getCanonicalName());
 }

 public static final Board.StepFunction[] FUNCTIONS = new Board.StepFunction[] {
  //Conway
  b -> {
   final Boolean[][] board = b.peek();
   final LinkedHashMap<SimpleEntry<Integer, Integer>, Boolean> changes = new LinkedHashMap<>();
   for (int i = 0; i < board.length; i++) {
    for (int j = 0; j < board[i].length; j++) {
     //Count neighbours
     int neighbours = 0;
     for (int l = -1; l < 2; l++) {
      for (int m = -1; m < 2; m++) {
       final SimpleEntry<Integer, Integer> indices = b.wrap(i + l, j + m);
       if (board[indices.getKey()][indices.getValue()]) {
        neighbours++;
       }
      }
     }
     if (board[i][j]) {
      neighbours -= 1;
     }
     if (neighbours == 2) {
      changes.put(new SimpleEntry<>(i, j), board[i][j]);
     } else if (neighbours == 3) {
      changes.put(new SimpleEntry<>(i, j), true);
     } else {
      changes.put(new SimpleEntry<>(i, j), false);
     }
    }
   }
   for (final SimpleEntry<Integer, Integer> change : changes.keySet()) {
    board[change.getKey()][change.getValue()] = changes.get(change);
   }
  },
  //Anti-Conway
  b -> {
   final Boolean[][] board = b.peek();
   for (int i = 0; i < board.length; i++) {
    for (int j = 0; j < board[i].length; j++) {
     //Count neighbours
     int neighbours = 0;
     for (int l = -1; l < 2; l++) {
      for (int m = -1; m < 2; m++) {
       if (!(l == i && m == j)) {
        final SimpleEntry<Integer, Integer> indices = b.wrap(i + l, j + m);
        if (board[indices.getKey()][indices.getValue()]) {
         neighbours++;
        }
       }
      }
     }
//    if (neighbours <= 2 || neighbours > 3) {
//    if (neighbours < 2 || neighbours > 3) {
     if (neighbours == 2 || neighbours == 3) {
      board[i][j] = true;
     } else {
      board[i][j] = false;
     }
    }
   }
  },
  //Ulam-Warburton
  b -> {
   final Boolean[][] board = b.peek();
   final LinkedHashMap<SimpleEntry<Integer, Integer>, Boolean> changes = new LinkedHashMap<>();
   for (int i = 0; i < board.length; i++) {
    for (int j = 0; j < board[i].length; j++) {
     if (board[i][j]) {
      continue;
     }
     int neighbours = 0;
     for (int k = -1; k < 2; k++) {
      if (k == 0) {
       continue;
      }
      final SimpleEntry<Integer, Integer>
       vert_indices = b.wrap(i + k, j),
       hor_indices = b.wrap(i, j + k);
      if (board[vert_indices.getKey()][vert_indices.getValue()]) {
       neighbours++;
      }
      if (board[hor_indices.getKey()][hor_indices.getValue()]) {
       neighbours++;
      }
     }
     if (neighbours == 1) {
      changes.put(new SimpleEntry<>(i, j), true);
     }
    }
   }
   for (final SimpleEntry<Integer, Integer> change : changes.keySet()) {
    board[change.getKey()][change.getValue()] = changes.get(change);
   }
  },
  //Anti-Ulam-Warburton
  b -> {
   final Boolean[][] board = b.peek();
   final LinkedHashMap<SimpleEntry<Integer, Integer>, Boolean> changes = new LinkedHashMap<>();
   for (int i = 0; i < board.length; i++) {
    for (int j = 0; j < board[i].length; j++) {
     int neighbours = 0;
     for (int k = -1; k < 2; k++) {
      if (k == 0) {
       continue;
      }
      final SimpleEntry<Integer, Integer>
       vert_indices = b.wrap(i + k, j),
       hor_indices = b.wrap(i, j + k);
      if (board[vert_indices.getKey()][vert_indices.getValue()]) {
       neighbours++;
      }
      if (board[hor_indices.getKey()][hor_indices.getValue()]) {
       neighbours++;
      }
     }
     changes.put(new SimpleEntry<>(i, j), neighbours == 1);
    }
   }
   for (final SimpleEntry<Integer, Integer> change : changes.keySet()) {
    board[change.getKey()][change.getValue()] = changes.get(change);
   }
  }
 };

 private int
  boardWidth = 125,
  boardHeight = 125,
  cellWidth,
  cellHeight,
  rr = 255,
  gg = 255,
  bb = 255,
  function_index = 0;

 public final Board board = new Board(boardWidth, boardHeight);

 private boolean
  mouseHeld = false,
  shouldUpdate = true,
  enableGrid = false,
  rainbow = false;

 private long
  lastStep = 0,
  updateDelay = 100;

 final RemoteControl rc = new RemoteControl(this);

 public void settings() {
  size(1000, 1000, "processing.awt.PGraphicsJava2D");
 }

 public void setup() {
  frameRate(25);
  cellWidth = Math.floorDiv(width, boardWidth);
  cellHeight = Math.floorDiv(height, boardHeight);
 }

 public void draw() {
  background(127, 127, 127);
  fill(0);
  strokeWeight(0);
  if (enableGrid) {
   stroke(63, 127, 255);
  } else {
   stroke(0);
  }
//  stroke(0);
  //Mouse input
  if (mouseHeld) {
   if (!(mouseX > width - 1 || 0 > mouseX) && !(mouseY > height - 1 || 0 > mouseY)) {
    final int
     x = Math.floorDiv(mouseX, cellWidth),
     y = Math.floorDiv(mouseY, cellHeight);
    if (mouseButton == LEFT) {
     board.set(x, y, true);
    } else if (mouseButton == RIGHT) {
     board.set(x, y, false);
    }
   }
  }
  //Draw cells
  pushMatrix();
  for (int i = 0; i < boardWidth; i++) {
   for (int j = 0; j < boardHeight; j++) {
    if (board.peek()[i][j]) {
     if (rainbow) {
      fill(
       random(0, 255),
       random(0, 255),
       random(0, 255)
      );
     } else {
      fill(rr, gg, bb);
     }
    } else {
     fill(0);
    }
    rect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
   }
  }
  popMatrix();
  if (shouldUpdate && (System.currentTimeMillis() - lastStep) > updateDelay) {
   board.step(FUNCTIONS[function_index]);
   lastStep = System.currentTimeMillis();
  }
 }

 public void keyPressed() {
  if (key == CODED) {
   //
  } else {
   if (key == ' ') {
    shouldUpdate = !shouldUpdate;
   } else if (key == 'r') {
    board.generateRandomBoard();
   } else if (key == 'R') {
    rainbow();
   } else if (key == 'c') {
    board.clear();
   } else if (key == '-') {
    updateDelay += 25;
   } else if (key == '=') {
    updateDelay = updateDelay - 25 > 0 ? updateDelay - 25 : updateDelay;
   } else if (key == 'g') {
    enableGrid = !enableGrid;
   } else if (key == 'm') {
    function_index = (function_index + 1 < FUNCTIONS.length ? function_index + 1 : 0);
   }
  }
 }

 public void mousePressed() {
  mouseHeld = true;
 }

 public void mouseReleased() {
  mouseHeld = false;
 }

 public void setColor(int r, int g, int b) {
  rainbow = false;
  rr = r;
  gg = g;
  bb = b;
 }

 public void rainbow() {
  rainbow = !rainbow;
 }

 public void haltBoard() {
  shouldUpdate = false;
 }

 public void resumeBoard() {
  shouldUpdate = true;
 }
}
