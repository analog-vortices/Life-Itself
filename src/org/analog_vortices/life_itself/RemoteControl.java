package org.analog_vortices.life_itself;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RemoteControl {
 final WebSocketServer server;

 public RemoteControl(final Main main) {
  server = new WebSocketServer(new InetSocketAddress(9999)) {
   @Override
   public void onOpen(WebSocket conn, ClientHandshake handshake) {
    System.out.println("Opening connection!");
   }

   @Override
   public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println("Closing connection!");
   }

   public int parseInt(String str) {
    try {
     return Integer.parseInt(str);
    } catch (NumberFormatException e) {}
    return -1;
   }

   @Override
   public void onMessage(WebSocket conn, String message) {
    System.out.printf("Received message: '%s'\n", message);
    final String[] arguments = message.split(" ");
    switch(arguments[0]) {
     case "set": {
      int
       x = parseInt(arguments[1]),
       y = parseInt(arguments[2]);
      if (x == -1 || y == -1) {
       System.err.printf("Malformed set request!\n");
      }
      main.board.set(x, y, true);
      break;
     }
     case "unset": {
      int
       x = parseInt(arguments[1]),
       y = parseInt(arguments[2]);
      if (x == -1 || y == -1) {
       System.err.printf("Malformed set request!\n");
      }
      main.board.set(x, y, false);
      break;
     }
     case "boardClear": {
      main.board.clear();
      break;
     }
     case "boardRandomize": {
      main.board.generateRandomBoard();
      break;
     }
     case "pause": {
      main.haltBoard();
      break;
     }
     case "play": {
      main.resumeBoard();
      break;
     }
     case "nextMode": {
      main.key = 'm';
      main.keyPressed();
      break;
     }
     case "increaseSpeed": {
      main.key = '=';
      main.keyPressed();
      break;
     }
     case "decreaseSpeed": {
      main.key = '-';
      main.keyPressed();
      break;
     }
     case "setColour": {
      if (arguments[1].equals("rainbow")) {
       main.rainbow();
      } else {
       int
        r = parseInt(arguments[1]),
        g = parseInt(arguments[2]),
        b = parseInt(arguments[3]);
       main.setColor(r, g, b);
      }
      break;
     }
     case "clear": {
      main.setColor(255, 255,255);
      break;
     }
     default: {
      System.err.printf("Received malformed command: '%s'\n", arguments[0]);
      break;
     }
    }
   }

   @Override
   public void onError(WebSocket conn, Exception ex) {
    ex.printStackTrace();
   }

   @Override
   public void onStart() {
    System.out.println("Started on: " +  getPort());
   }
  };
  server.setReuseAddr(true);
  server.start();
 }

 public void close() throws IOException, InterruptedException {
  server.stop();
 }
}
