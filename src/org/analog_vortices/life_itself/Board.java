package org.analog_vortices.life_itself;

import java.util.AbstractMap.SimpleEntry;
import java.util.Random;

//TODO functional interface for underlying structure
//Functional interface for neighbour checking
//Functional interface for rule set
//Default implementation of iteration
public final class Board {
 interface StepFunction {
  void step(final Board board);
 }

 private Boolean[][] board;

 public Board(final int width, final int height) {
  board = new Boolean[width][height];
  clear();
 }

 public void generateRandomBoard() {
  final Random random = new Random();
  for (int i = 0; i < board.length; i++) {
   for (int j = 0; j < board[i].length; j++) {
//    board[i][j] = random.nextBoolean();
    //Add (1/4) bias (on/off)
    board[i][j] = (random.nextInt(100000) + 1) < 25000;
   }
  }
 }

 public void clear() {
  for (int i = 0; i < board.length; i++) {
   for (int j = 0; j < board[i].length; j++) {
    board[i][j] = false;
   }
  }
 }

 //TODO there has to be a better way to do this
 public SimpleEntry<Integer, Integer> wrap(int i, int j) {
  if (i < 0) {
   while ((i += board.length) < 0);
  } else if (i > board.length - 1) {
   while ((i -= board.length) >= board.length - 1);
  }
  if (j < 0) {
   while ((j += board[0].length) < 0);
  } else if (j > board[0].length - 1) {
   while ((j -= board[0].length) >= board[0].length);
  }
  return new SimpleEntry<>(i, j);
 }

 public Boolean[][] step(final StepFunction... sfs) {
  for (final StepFunction sf : sfs) {
   sf.step(this);
  }
  return board;
 }

 public Boolean[][] peek() {
//  return Arrays.copyOf(board, board.length);
  return board;
 }

 public void set(int i, int j, boolean value) {
  final SimpleEntry<Integer, Integer> coords = wrap(i, j);
  board[coords.getKey()][coords.getValue()] = value;
 }
}
