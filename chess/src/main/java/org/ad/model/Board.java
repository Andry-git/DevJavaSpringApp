package org.ad.model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * Представляет шахматную доску.
 */
public class Board {

  private final int width = 8;
  private final int height = 8;
  private final List<Piece> pieces;
  private Piece enPassantPawn;
  private int enPassantTurn;

  public Board() {
    pieces = new ArrayList<>();
    setupPieces();
  }

  public void addPiece(Piece piece) {
    pieces.add(piece);
  }

  public void removePiece(int x, int y) {
    pieces.removeIf(piece -> piece.getX() == x && piece.getY() == y);
  }

  public Piece getPieceAt(int x, int y) {
    for (Piece piece : pieces) {
      if (piece.getX() == x && piece.getY() == y) {
        return piece;
      }
    }
    return null;
  }

  public List<Piece> getPieces() {
    return pieces;
  }


  public void draw(Graphics g, int tileSize) {
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        if ((row + col) % 2 == 0) {
          g.setColor(new Color(240, 217, 181)); // светлая клетка
        } else {
          g.setColor(new Color(181, 136, 99)); // темная клетка
        }
        g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);

        Piece piece = getPieceAt(col, row);
        if (piece != null) {
          piece.draw(g, tileSize);
        }
      }
    }
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public void setEnPassantPawn(Piece enPassantPawn) {
    this.enPassantPawn = enPassantPawn;
  }

  public void setEnPassantTurn(int enPassantTurn) {
    this.enPassantTurn = enPassantTurn;
  }
  public Piece getEnPassantPawn() {
    return enPassantPawn;
  }

  public int getEnPassantTurn() {
    return enPassantTurn;
  }

  private void setupPieces() {
    // Белые фигуры
    pieces.add(new Piece(PieceType.ROOK, PieceColor.WHITE, 0, 7));
    pieces.add(new Piece(PieceType.KNIGHT, PieceColor.WHITE, 1, 7));
    pieces.add(new Piece(PieceType.BISHOP, PieceColor.WHITE, 2, 7));
    pieces.add(new Piece(PieceType.QUEEN, PieceColor.WHITE, 3, 7));
    pieces.add(new Piece(PieceType.KING, PieceColor.WHITE, 4, 7));
    pieces.add(new Piece(PieceType.BISHOP, PieceColor.WHITE, 5, 7));
    pieces.add(new Piece(PieceType.KNIGHT, PieceColor.WHITE, 6, 7));
    pieces.add(new Piece(PieceType.ROOK, PieceColor.WHITE, 7, 7));
    for (int i = 0; i < 8; i++) {
      pieces.add(new Piece(PieceType.PAWN, PieceColor.WHITE, i, 6));
    }

    // Черные фигуры
    pieces.add(new Piece(PieceType.ROOK, PieceColor.BLACK, 0, 0));
    pieces.add(new Piece(PieceType.KNIGHT, PieceColor.BLACK, 1, 0));
    pieces.add(new Piece(PieceType.BISHOP, PieceColor.BLACK, 2, 0));
    pieces.add(new Piece(PieceType.QUEEN, PieceColor.BLACK, 3, 0));
    pieces.add(new Piece(PieceType.KING, PieceColor.BLACK, 4, 0));
    pieces.add(new Piece(PieceType.BISHOP, PieceColor.BLACK, 5, 0));
    pieces.add(new Piece(PieceType.KNIGHT, PieceColor.BLACK, 6, 0));
    pieces.add(new Piece(PieceType.ROOK, PieceColor.BLACK, 7, 0));
    for (int i = 0; i < 8; i++) {
      pieces.add(new Piece(PieceType.PAWN, PieceColor.BLACK, i, 1));
    }
  }
}