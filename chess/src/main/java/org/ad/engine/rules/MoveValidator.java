package org.ad.engine.rules;

import org.ad.engine.GameLogic;
import org.ad.model.Board;
import org.ad.model.Piece;
import org.ad.model.PieceColor;
import org.ad.model.PieceType;

/**
 * Предоставляет методы для проверки допустимости хода.
 */
public class MoveValidator {

  private final GameLogic gameLogic;
  private final Board board;

  public MoveValidator(GameLogic gameLogic, Board board) {
    this.gameLogic = gameLogic;
    this.board = board;
  }

  public boolean isValidMove(Piece piece, int endX, int endY) {
    // 1. Проверка на валидность хода по правилам игры
    if (!isValidMoveRules(piece, piece.getX(), piece.getY(), endX, endY)) {
      return false;
    }

    // 2. Проверка на шах после хода
    Board tempBoard = gameLogic.copyBoard(board);
    Piece tempPiece = tempBoard.getPieceAt(piece.getX(), piece.getY());
    tempBoard.removePiece(piece.getX(), piece.getY());
    if (tempPiece != null) {
      tempPiece.setX(endX);
      tempPiece.setY(endY);
      tempBoard.addPiece(tempPiece);
    }

    if (gameLogic.checkmateChecker.isKingUnderCheck(piece.getColor(), tempBoard)) {
      return false;
    }

    return true;
  }

  // Метод для проверки правил перемещения каждой фигуры (без учета шаха)
  private boolean isValidMoveRules(Piece piece, int startX, int startY, int endX, int endY) {
    if (endX < 0 || endX > 7 || endY < 0 || endY > 7) {
      return false;
    }
    if (startX == endX && startY == endY) {
      return false;
    }
    switch (piece.getType()) {
      case PAWN -> {
        return isValidPawnMove(piece, startX, startY, endX, endY);
      }
      case ROOK -> {
        return isValidRookMove(piece, startX, startY, endX, endY);
      }
      case KNIGHT -> {
        return isValidKnightMove(piece, startX, startY, endX, endY);
      }
      case BISHOP -> {
        return isValidBishopMove(piece, startX, startY, endX, endY);
      }
      case QUEEN -> {
        return isValidQueenMove(piece, startX, startY, endX, endY);
      }
      case KING -> {
        return isValidKingMove(piece, startX, startY, endX, endY);
      }
      default -> throw new IllegalStateException("Unexpected value: " + piece.getType());
    }
  }

  public boolean isValidPawnMove(Piece piece, int startX, int startY, int endX, int endY) {
    int dx = endX - startX;
    int dy = endY - startY;
    PieceColor color = piece.getColor();
    int direction = color == PieceColor.WHITE ? -1 : 1;

    if (dx == 0) {
      if (dy == direction) {
        return board.getPieceAt(endX, endY) == null;
      } else if (dy == 2 * direction && startY == (color == PieceColor.WHITE ? 6 : 1)) {
        return board.getPieceAt(endX, endY) == null &&
            board.getPieceAt(startX, startY + direction) == null;
      }
    } else if (Math.abs(dx) == 1 && dy == direction) {
      return board.getPieceAt(endX, endY) != null &&
          board.getPieceAt(endX, endY).getColor() != color;
    }

    return false;
  }

  private boolean isValidRookMove(Piece piece, int startX, int startY, int endX, int endY) {
    if (startX != endX && startY != endY) {
      return false;
    }

    int dx = Integer.compare(endX, startX);
    int dy = Integer.compare(endY, startY);

    int x = startX + dx;
    int y = startY + dy;
    while (x != endX || y != endY) {
      if (board.getPieceAt(x, y) != null) {
        return false;
      }
      x += dx;
      y += dy;
    }

    Piece targetPiece = board.getPieceAt(endX, endY);
    return targetPiece == null || targetPiece.getColor() != piece.getColor();
  }

  private boolean isValidKnightMove(Piece piece, int startX, int startY, int endX, int endY) {
    int dx = Math.abs(endX - startX);
    int dy = Math.abs(endY - startY);
    return (dx == 2 && dy == 1 || dx == 1 && dy == 2) &&
        (board.getPieceAt(endX, endY) == null ||
            board.getPieceAt(endX, endY).getColor() != piece.getColor());
  }

  private boolean isValidBishopMove(Piece piece, int startX, int startY, int endX, int endY) {
    if (Math.abs(endX - startX) != Math.abs(endY - startY)) {
      return false;
    }
    int dx = Integer.compare(endX, startX);
    int dy = Integer.compare(endY, startY);
    int x = startX + dx;
    int y = startY + dy;
    while (x != endX) {
      if (board.getPieceAt(x, y) != null) {
        return false;
      }
      x += dx;
      y += dy;
    }
    Piece targetPiece = board.getPieceAt(endX, endY);
    return targetPiece == null || targetPiece.getColor() != piece.getColor();
  }

  private boolean isValidQueenMove(Piece piece, int startX, int startY, int endX, int endY) {
    return isValidRookMove(piece, startX, startY, endX, endY) ||
        isValidBishopMove(piece, startX, startY, endX, endY);
  }

  private boolean isValidKingMove(Piece piece, int startX, int startY, int endX, int endY) {
    int dx = Math.abs(endX - startX);
    int dy = Math.abs(endY - startY);
    if (dx > 1 || dy > 1) {
      if (piece.getColor() == PieceColor.WHITE && !gameLogic.isWhiteKingMoved()) {
        if (endX == 6 && startY == 7 && board.getPieceAt(7, 7) != null &&
            board.getPieceAt(7, 7).getType() == PieceType.ROOK &&
            !gameLogic.isWhiteRookRightMoved()) {
          for (int i = 5; i <= 6; i++) {
            if (board.getPieceAt(i, 7) != null) {
              return false;
            }
          }
          return true;
        } else if (endX == 2 && startY == 7 && board.getPieceAt(0, 7) != null &&
            board.getPieceAt(0, 7).getType() == PieceType.ROOK &&
            !gameLogic.isWhiteRookLeftMoved()) {
          for (int i = 1; i <= 3; i++) {
            if (board.getPieceAt(i, 7) != null) {
              return false;
            }
          }
          return true;
        }

      } else if (piece.getColor() == PieceColor.BLACK && !gameLogic.isBlackKingMoved()) {
        if (endX == 6 && startY == 0 && board.getPieceAt(7, 0) != null &&
            board.getPieceAt(7, 0).getType() == PieceType.ROOK &&
            !gameLogic.isBlackRookRightMoved()) {
          for (int i = 5; i <= 6; i++) {
            if (board.getPieceAt(i, 0) != null) {
              return false;
            }
          }
          return true;
        } else if (endX == 2 && startY == 0 && board.getPieceAt(0, 0) != null &&
            board.getPieceAt(0, 0).getType() == PieceType.ROOK &&
            !gameLogic.isBlackRookLeftMoved()) {
          for (int i = 1; i <= 3; i++) {
            if (board.getPieceAt(i, 0) != null) {
              return false;
            }
          }
          return true;
        }
      }
      return false;
    }
    return board.getPieceAt(endX, endY) == null ||
        board.getPieceAt(endX, endY).getColor() != piece.getColor();
  }
}