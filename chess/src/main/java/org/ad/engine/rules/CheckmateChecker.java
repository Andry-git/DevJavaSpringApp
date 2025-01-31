package org.ad.engine.rules;

import java.util.ArrayList;
import java.util.List;
import org.ad.engine.GameLogic;
import org.ad.model.Board;
import org.ad.model.Piece;
import org.ad.model.PieceColor;
import org.ad.model.PieceType;

/**
 * Предоставляет методы для проверки шаха и мата.
 */
public class CheckmateChecker {
  private static Board board;
  private static MoveValidator moveValidator;
  private final GameLogic gameLogic;

  public CheckmateChecker(Board board,GameLogic gameLogic, MoveValidator moveValidator) {
    this.board = board;
    this.moveValidator = moveValidator;
    this.gameLogic = gameLogic;
  }

  public boolean isKingUnderCheck(PieceColor color, Board board) {
    Piece king = findKing(color, board);
    if (king == null) {
      return false;
    }
    for (Piece piece : board.getPieces()) {
      if (piece.getColor() != color) {
        int startX = piece.getX();
        int startY = piece.getY();
        int endX = king.getX();
        int endY = king.getY();
        switch (piece.getType()) {
          case PAWN -> {
            if (isValidPawnMove(piece, startX, startY, endX, endY, board)) {
              return true;
            }
          }
          case ROOK -> {
            if (isValidRookMove(piece, startX, startY, endX, endY, board)) {
              return true;
            }
          }
          case KNIGHT -> {
            if (isValidKnightMove(piece, startX, startY, endX, endY)) {
              return true;
            }
          }
          case BISHOP -> {
            if (isValidBishopMove(piece, startX, startY, endX, endY, board)) {
              return true;
            }
          }
          case QUEEN -> {
            if (isValidQueenMove(piece, startX, startY, endX, endY, board)) {
              return true;
            }
          }
          case KING -> {
            if (isValidKingMove(piece, startX, startY, endX, endY)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  public boolean isKingUnderCheck(PieceColor color) {
    Piece king = findKing(color, board);
    if (king == null) {
      return false;
    }
    for (Piece piece : board.getPieces()) {
      if (piece.getColor() != color) {
        int startX = piece.getX();
        int startY = piece.getY();
        int endX = king.getX();
        int endY = king.getY();
        switch (piece.getType()) {
          case PAWN -> {
            if (isValidPawnMove(piece, startX, startY, endX, endY, board)) {
              return true;
            }
          }
          case ROOK -> {
            if (isValidRookMove(piece, startX, startY, endX, endY, board)) {
              return true;
            }
          }
          case KNIGHT -> {
            if (isValidKnightMove(piece, startX, startY, endX, endY)) {
              return true;
            }
          }
          case BISHOP -> {
            if (isValidBishopMove(piece, startX, startY, endX, endY, board)) {
              return true;
            }
          }
          case QUEEN -> {
            if (isValidQueenMove(piece, startX, startY, endX, endY, board)) {
              return true;
            }
          }
          case KING -> {
            if (isValidKingMove(piece, startX, startY, endX, endY)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  private boolean isValidPawnMove(Piece piece, int startX, int startY, int endX, int endY,
                                  Board board) {
    int direction = piece.getColor() == PieceColor.WHITE ? 1 : -1;
    if (startX == endX && Math.abs(endY - startY) == 1 && board.getPieceAt(endX, endY) == null) {
      return true;
    }

    if (startX == endX && Math.abs(endY - startY) == 2 &&
        ((piece.getColor() == PieceColor.WHITE && startY == 1) ||
            (piece.getColor() == PieceColor.BLACK && startY == 6)) &&
        board.getPieceAt(endX, endY) == null &&
        board.getPieceAt(startX, startY + direction) == null) {
      return true;
    }

    if (Math.abs(endX - startX) == 1 && Math.abs(endY - startY) == 1 &&
        board.getPieceAt(endX, endY) != null &&
        board.getPieceAt(endX, endY).getColor() != piece.getColor()) {
      return true;
    }
    if (Math.abs(endX - startX) == 1 && Math.abs(endY - startY) == 1) {
      Piece enPassantPawn = board.getEnPassantPawn();
      if (enPassantPawn != null &&
          endX == enPassantPawn.getX() &&
          endY == enPassantPawn.getY() + direction &&
          board.getEnPassantTurn() == gameLogic.getTurn() - 1) {
        return true;
      }
    }
    return false;
  }

  private boolean isValidRookMove(Piece piece, int startX, int startY, int endX, int endY,
                                  Board board) {
    if (startX != endX && startY != endY) {
      return false;
    }

    int stepX = startX < endX ? 1 : startX > endX ? -1 : 0;
    int stepY = startY < endY ? 1 : startY > endY ? -1 : 0;
    int x = startX + stepX;
    int y = startY + stepY;
    while (x != endX || y != endY) {
      if (board.getPieceAt(x, y) != null) {
        return false;
      }
      x += stepX;
      y += stepY;
    }
    return true;
  }

  private boolean isValidBishopMove(Piece piece, int startX, int startY, int endX, int endY,
                                    Board board) {
    if (Math.abs(endX - startX) != Math.abs(endY - startY)) {
      return false;
    }
    int stepX = startX < endX ? 1 : -1;
    int stepY = startY < endY ? 1 : -1;
    int x = startX + stepX;
    int y = startY + stepY;
    while (x != endX) {
      if (board.getPieceAt(x, y) != null) {
        return false;
      }
      x += stepX;
      y += stepY;
    }
    return true;
  }

  private boolean isValidQueenMove(Piece piece, int startX, int startY, int endX, int endY,
                                   Board board) {
    return isValidRookMove(piece, startX, startY, endX, endY, board) ||
        isValidBishopMove(piece, startX, startY, endX, endY, board);
  }

  private boolean isValidKingMove(Piece piece, int startX, int startY, int endX, int endY) {
    int diffX = Math.abs(endX - startX);
    int diffY = Math.abs(endY - startY);
    return diffX <= 1 && diffY <= 1;
  }

  private boolean isValidKnightMove(Piece piece, int startX, int startY, int endX, int endY) {
    int diffX = Math.abs(endX - startX);
    int diffY = Math.abs(endY - startY);
    return (diffX == 2 && diffY == 1) || (diffX == 1 && diffY == 2);
  }

  private static Piece findKing(PieceColor color, Board board) {
    for (Piece piece : board.getPieces()) {
      if (piece.getType() == PieceType.KING && piece.getColor() == color) {
        return piece;
      }
    }
    return null;
  }

  private static Piece findKing(PieceColor color) {
    for (Piece piece : board.getPieces()) {
      if (piece.getType() == PieceType.KING && piece.getColor() == color) {
        return piece;
      }
    }
    return null;
  }

  public static List<Piece> getCheckers(PieceColor color, GameLogic gameLogic) {
    List<Piece> checkers = new ArrayList<>();
    Piece king = findKing(color);
    if (king == null) {
      return checkers;
    }
    for (Piece piece : board.getPieces()) {
      if (piece.getColor() != color && moveValidator.isValidMove(piece, king.getX(), king.getY())) {
        checkers.add(piece);
      }
    }
    return checkers;
  }

  public boolean isCheckmate(PieceColor color) {
    if (!isKingUnderCheck(color)) {
      return false;
    }
    List<Board> allPossibleMoves = getAllPossibleMoves(color);
    for (Board board : allPossibleMoves) {
      if (!isKingUnderCheck(color, board)) {
        return false;
      }
    }
    return true;
  }

  private List<Board> getAllPossibleMoves(PieceColor color) {
    List<Board> possibleMoves = new ArrayList<>();
    for (Piece piece : board.getPieces()) {
      if (piece.getColor() == color) {
        for (int row = 0; row < board.getHeight(); row++) {
          for (int col = 0; col < board.getWidth(); col++) {
            if (moveValidator.isValidMove(piece, col, row)) {
              Board tempBoard = copyBoard(board);
              Piece tempPiece = tempBoard.getPieceAt(piece.getX(), piece.getY());
              if (tempPiece != null) {
                tempBoard.removePiece(piece.getX(), piece.getY());
                tempPiece.setX(col);
                tempPiece.setY(row);
                tempBoard.addPiece(tempPiece);
                possibleMoves.add(tempBoard);
              }
            }
          }
        }
      }
    }
    return possibleMoves;
  }

  public Board copyBoard(Board board) {
    Board newBoard = new Board();
    for (Piece piece : board.getPieces()) {
      Piece newPiece = new Piece(piece.getType(), piece.getColor(), piece.getX(), piece.getY());
      newBoard.addPiece(newPiece);
    }
    newBoard.setEnPassantPawn(board.getEnPassantPawn());
    newBoard.setEnPassantTurn(board.getEnPassantTurn());
    return newBoard;
  }

  public boolean isStalemate(PieceColor color) {
    if (isKingUnderCheck(color)) {
      return false;
    }
    for (Piece piece : board.getPieces()) {
      if (piece.getColor() == color) {
        for (int i = 0; i < 8; i++) {
          for (int j = 0; j < 8; j++) {
            if (moveValidator.isValidMove(piece, i, j)) {
              return false;
            }
          }
        }
      }
    }
    return true;
  }
}