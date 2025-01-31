package org.ad.engine;

import org.ad.engine.rules.CheckmateChecker;
import org.ad.engine.rules.MoveValidator;
import org.ad.model.Board;
import org.ad.model.Piece;
import org.ad.model.PieceColor;
import org.ad.model.PieceType;
import org.ad.model.Player;

public class GameLogic {

  private Board board;
  private Player whitePlayer;
  private Player blackPlayer;
  private Player currentPlayer;
  private MoveValidator moveValidator;
  public CheckmateChecker checkmateChecker;

  private Game game;

  private Piece enPassantPawn;

  private int enPassantTurn = -1;

  private boolean whiteKingMoved = false;

  private boolean blackKingMoved = false;
  private boolean whiteRookRightMoved = false;
  private boolean whiteRookLeftMoved = false;
  private boolean blackRookRightMoved = false;
  private boolean blackRookLeftMoved = false;

  private int turn = 0;

  public GameLogic(Game game) {
    this.board = new Board();
    this.whitePlayer = new Player(PieceColor.WHITE);
    this.blackPlayer = new Player(PieceColor.BLACK);
    this.currentPlayer = whitePlayer;
    this.game = game;
    this.moveValidator = new MoveValidator(this, board);
    this.checkmateChecker = new CheckmateChecker(board, this, moveValidator);
  }

  public boolean movePiece(int startX, int startY, int endX, int endY) {
    Piece piece = board.getPieceAt(startX, startY);

    if (piece == null || piece.getColor() != currentPlayer.getColor()) {
      return false;
    }

    // Код для рокировки
    if (piece.getType() == PieceType.KING) {
      if (piece.getColor() == PieceColor.WHITE) {
        whiteKingMoved = true;
      } else {
        blackKingMoved = true;
      }
    } else if (piece.getType() == PieceType.ROOK) {
      if (piece.getColor() == PieceColor.WHITE) {
        if (startX == 0) {
          whiteRookLeftMoved = true;
        } else if (startX == 7) {
          whiteRookRightMoved = true;
        }
      } else {
        if (startX == 0) {
          blackRookLeftMoved = true;
        } else if (startX == 7) {
          blackRookRightMoved = true;
        }
      }
    }

    if (piece.getType() == PieceType.PAWN && Math.abs(endY - startY) == 2) {
      board.setEnPassantPawn(piece);
      board.setEnPassantTurn(turn);
    } else {
      board.setEnPassantPawn(null);
      board.setEnPassantTurn(-1);
    }
    PieceColor opponentColor =
        currentPlayer.getColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
    if (findKing(opponentColor) == null) {
      this.game.setGameOver(true);
      String winnerColor = currentPlayer.getColor() == PieceColor.WHITE ? "Черный" : "Белый";
      this.game.setWinner(winnerColor);
      System.out.println("Король был съеден! Побеждает " + winnerColor);
      return true;
    }
    if (moveValidator.isValidMove(piece, endX, endY)) {
      Piece capturedPiece = board.getPieceAt(endX, endY);
      Board tempBoard = copyBoard(board);
      Piece tempPiece = tempBoard.getPieceAt(startX, startY);
      tempBoard.removePiece(startX, startY);
      if(tempPiece != null){
        int enPassantX = -1;
        int enPassantY = -1;
        if (piece.getType() == PieceType.PAWN && board.getEnPassantPawn() != null &&
            Math.abs(endX - startX) == 1 && endY == board.getEnPassantPawn().getY() + (piece.getColor() == PieceColor.WHITE ? 1 : -1)
            && board.getEnPassantTurn() == turn - 1 && Math.abs(endY - startY) == 1) {
          enPassantX = board.getEnPassantPawn().getX();
          enPassantY = board.getEnPassantPawn().getY();
          tempBoard.removePiece(enPassantX, enPassantY);
          tempPiece.setX(endX);
          tempPiece.setY(endY);
        }else{
          tempPiece.setX(endX);
          tempPiece.setY(endY);
        }
        if(capturedPiece != null){
          tempBoard.removePiece(endX,endY);
        }
        tempBoard.addPiece(tempPiece);
        if (!checkmateChecker.isKingUnderCheck(currentPlayer.getColor(), tempBoard)) {
          if (enPassantX != -1) {
            board.removePiece(enPassantX, enPassantY);
          }
          board.removePiece(startX, startY);
          if (capturedPiece != null) {
            board.removePiece(endX, endY);
          }
          // Проверка на превращение пешки
          if (piece.getType() == PieceType.PAWN &&
              (endY == 0 || endY == 7)) {
            Piece queen = new Piece(PieceType.QUEEN, piece.getColor(), endX, endY);
            board.addPiece(queen);
          } else {
            piece.setX(endX);
            piece.setY(endY);
            board.addPiece(piece);
          }

          switchTurn();
          // Код для проверки на мат и пат
          if (checkmateChecker.isStalemate(opponentColor)) {
            this.game.setGameOver(true);
            System.out.println("Пат! Ничья!");
            return true;
          }
          if (checkmateChecker.isCheckmate(currentPlayer.getColor())) {
            this.game.setGameOver(true);
            System.out.println("Мат! Побеждает " + currentPlayer.getColor().toString());
            this.game.setWinner(currentPlayer.getColor().toString());
            return true;
          }
          turn++;
          return true;
        }
      }
    }
    return false;
  }
  public Board copyBoard(Board board) {
    Board newBoard = new Board();
    for (Piece piece : board.getPieces()) {
      Piece newPiece = new Piece(piece.getType(), piece.getColor(), piece.getX(), piece.getY());
      newBoard.addPiece(newPiece);
    }
    newBoard.setEnPassantPawn(this.enPassantPawn);
    newBoard.setEnPassantTurn(this.enPassantTurn);
    return newBoard;
  }

  private Piece findKing(PieceColor color) {
    for (Piece piece : board.getPieces()) {
      if (piece.getType() == PieceType.KING && piece.getColor() == color) {
        return piece;
      }
    }
    return null;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public Board getBoard() {
    return board;
  }

  public boolean isKingUnderCheck(PieceColor color) {
    return checkmateChecker.isKingUnderCheck(color);
  }

  public boolean isKingUnderCheck(PieceColor color, Board board) {
    return checkmateChecker.isKingUnderCheck(color, board);
  }

  public boolean isCheckmate(PieceColor color) {
    return checkmateChecker.isCheckmate(color);
  }

  public boolean isStalemate(PieceColor color) {
    return checkmateChecker.isStalemate(color);
  }

  private void switchTurn() {
    currentPlayer = currentPlayer == whitePlayer ? blackPlayer : whitePlayer;
    turn++;
  }

  public Piece getEnPassantPawn() {
    return enPassantPawn;
  }

  public int getEnPassantTurn() {
    return enPassantTurn;
  }

  public int getTurn() {
    return turn;
  }

  public boolean isWhiteKingMoved() {
    return whiteKingMoved;
  }

  public boolean isBlackKingMoved() {
    return blackKingMoved;
  }

  public boolean isWhiteRookLeftMoved() {
    return whiteRookLeftMoved;
  }

  public boolean isWhiteRookRightMoved() {
    return whiteRookRightMoved;
  }

  public boolean isBlackRookLeftMoved() {
    return blackRookLeftMoved;
  }

  public boolean isBlackRookRightMoved() {
    return blackRookRightMoved;
  }
}