package org.deeplearning4j.scalphagozero.board

import org.scalatest.FunSpec

class BoardTest extends FunSpec {

  describe("Capturing a stone on a new 19x19 Board") {
    var board = GoBoard(9)

    it("should place and confirm a black stone") {
      board = board.placeStone(BlackPlayer, Point(2, 2))
      board = board.placeStone(WhitePlayer, Point(1, 2))
      assert(board.getPlayer(Point(2, 2)).contains(BlackPlayer))
    }
    it("if black's liberties go down to two, the stone should still be there") {
      board = board.placeStone(WhitePlayer, Point(2, 1))
      assert(board.getPlayer(Point(2, 2)).contains(BlackPlayer))
    }
    it("if black's liberties go down to one, the stone should still be there") {
      board = board.placeStone(WhitePlayer, Point(2, 3))
      assert(board.getPlayer(Point(2, 2)).contains(BlackPlayer))
    }
    it("finally, if all liberties are taken, the stone should be gone") {
      board = board.placeStone(WhitePlayer, Point(3, 2))
      assert(board.getPlayer(Point(2, 2)).isEmpty)
    }
    println(board)
  }

  describe("Capturing two stones on a new 19x19 Board") {
    var board = GoBoard(9)

    it("should place and confirm two black stones") {
      board = board.placeStone(BlackPlayer, Point(2, 2))
      board = board.placeStone(BlackPlayer, Point(2, 3))
      board = board.placeStone(WhitePlayer, Point(1, 2))
      board = board.placeStone(WhitePlayer, Point(1, 3))

      assert(board.getPlayer(Point(2, 2)).contains(BlackPlayer))
      assert(board.getPlayer(Point(2, 3)).contains(BlackPlayer))
      println(board)
    }
    it("if black's liberties go down to two, the stone should still be there") {
      board = board.placeStone(WhitePlayer, Point(3, 2))
      board = board.placeStone(WhitePlayer, Point(3, 3))
      assert(board.getPlayer(Point(2, 2)).contains(BlackPlayer))
      assert(board.getPlayer(Point(2, 3)).contains(BlackPlayer))
      println(board)
    }
    it("finally, if all liberties are taken, the stone should be gone") {
      board = board.placeStone(WhitePlayer, Point(2, 1))
      board = board.placeStone(WhitePlayer, Point(2, 4))
      println(board)
      assert(board.getPlayer(Point(2, 2)).isEmpty)
      assert(board.getPlayer(Point(2, 3)).isEmpty)
    }
  }

  describe("If you capture a stone, it's not suicide") {
    var board = GoBoard(9)
    it("should regain liberties by capturing") {
      board = board.placeStone(BlackPlayer, Point(1, 1))
      board = board.placeStone(BlackPlayer, Point(2, 2))
      board = board.placeStone(BlackPlayer, Point(1, 3))
      board = board.placeStone(WhitePlayer, Point(2, 1))
      board = board.placeStone(WhitePlayer, Point(1, 2))
      assert(board.getPlayer(Point(1, 1)).isEmpty)
      assert(board.getPlayer(Point(2, 1)).contains(WhitePlayer))
      assert(board.getPlayer(Point(1, 2)).contains(WhitePlayer))
    }
    println(board)
  }

  describe("Test removing liberties:") {
    it("a stone with four liberties should end up with three if an opponent stone is added") {
      var board = GoBoard(5)
      board = board.placeStone(BlackPlayer, Point(3, 3))
      board = board.placeStone(WhitePlayer, Point(2, 2))
      val whiteString = board.getGoString(Point(2, 2)).get
      assert(whiteString.numLiberties == 4)

      board = board.placeStone(BlackPlayer, Point(3, 2))
      //val newWhiteString = board.getGoString(Point(2, 2)).get
      //assert(whiteString.numLiberties == 3)
    }
  }

  describe("Empty triangle test:") {
    it("an empty triangle in the corner with one white stone should have 3 liberties") {
      // x x
      // x o
      var board = GoBoard(5)
      board = board.placeStone(BlackPlayer, Point(1, 1))
      board = board.placeStone(BlackPlayer, Point(1, 2))
      board = board.placeStone(BlackPlayer, Point(2, 2))
      board = board.placeStone(WhitePlayer, Point(2, 1))

      val blackString: GoString = board.getGoString(Point(1, 1)).get

      assert(blackString.numLiberties == 3)
      assert(blackString.liberties.contains((3, 2)))
      assert(blackString.liberties.contains((2, 3)))
      assert(blackString.liberties.contains((1, 3)))
      println(board)
    }
  }

  describe("Test self capture:") {
    // o.o..
    // x.xo.
    it("black can't take it's own last liberty") {
      var board = GoBoard(5)
      board = board.placeStone(BlackPlayer, Point(1, 1))
      board = board.placeStone(BlackPlayer, Point(1, 3))
      board = board.placeStone(WhitePlayer, Point(2, 1))
      board = board.placeStone(WhitePlayer, Point(2, 2))
      board = board.placeStone(WhitePlayer, Point(2, 3))
      board = board.placeStone(WhitePlayer, Point(1, 4))

      assert(board.isSelfCapture(BlackPlayer, Point(1, 2)))
    }

    // o.o..
    // x.xo.
    it("but if we remove one white stone, the move becomes legal") {
      var board = GoBoard(5)
      board = board.placeStone(BlackPlayer, Point(1, 1))
      board = board.placeStone(BlackPlayer, Point(1, 3))
      board = board.placeStone(WhitePlayer, Point(2, 1))
      board = board.placeStone(WhitePlayer, Point(2, 3))
      board = board.placeStone(WhitePlayer, Point(1, 4))
      println(board)
      assert(!board.isSelfCapture(BlackPlayer, Point(1, 2)))
    }

    // xx...
    // oox..
    // x.o..
    it("if we capture a stone in the process, it's not self-play") {
      var board = GoBoard(5)
      board = board.placeStone(BlackPlayer, Point(3, 1))
      board = board.placeStone(BlackPlayer, Point(3, 2))
      board = board.placeStone(BlackPlayer, Point(2, 3))
      board = board.placeStone(BlackPlayer, Point(1, 1))
      board = board.placeStone(WhitePlayer, Point(2, 1))
      board = board.placeStone(WhitePlayer, Point(2, 2))
      board = board.placeStone(WhitePlayer, Point(1, 3))
      println(board)
      assert(!board.isSelfCapture(BlackPlayer, Point(1, 2)))
    }

  }
}
