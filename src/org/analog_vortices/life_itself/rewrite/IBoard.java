//package io.github.matthewacon.board.rewrite;
//
//import java.util.LinkedHashMap;
//
//public interface IBoard<T> {
// public static class Board<T> {
//  private static final LinkedHashMap<IBoard<?>, Board<?>> boards;
//
//  static {
//   boards = new LinkedHashMap<>();
//  }
//
//  private final T[] board;
//  private final boolean copyOnUpdate;
//
//  public Board(final int length, boolean copyOnUpdate) {
//   board = new T[length];
//   this.copyOnUpdate = copyOnUpdate;
//  }
//
//  public static <T> Board<T> fetchBoard(final IBoard<T> iboard) {
//   Board<T> board;
//   try {
//    if ((board = (Board<T>)boards.get(iboard)) == null) {
//     board = new Board<>();
//     boards.put(iboard, board);
//    }
//   } catch (ClassCastException e) {
//    throw new RuntimeException("Unexpected exception!", e);
//   }
//   return board;
//  }
// }
//
// default void step() {
//  final Board<T> board =
//  update();
// }
//
// void update(final Board<T> board);
//}
//
//enum Games implements IBoard<Boolean> {
// TEST {
//  @Override
//  public void update(Board<Boolean> board) {
//
//  }
// };
//}
//
//class Test {
// public static void main(String[] args) {
//
//  Games.TEST.step();
// }
//}