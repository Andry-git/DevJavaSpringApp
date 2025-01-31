package org.ad.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import org.ad.engine.rules.CheckmateChecker;
import org.ad.model.Board;
import org.ad.model.Piece;
import org.ad.model.PieceColor;
import org.ad.model.PieceType;
import org.ad.model.Player;

/**
 * Отрисовывает игровое поле и все его компоненты.
 */
public class Render {

  private static final int TILE_SIZE = Game.TILE_SIZE;
  private static final int TEXT_HEIGHT = 30;

  public static void drawCurrentPlayer(Graphics g, Player currentPlayer, int textHeight,
                                       GameLogic gameLogic) {
    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 20));
    String text = "Ходит: " + currentPlayer.getColor();
    int textX = 10;
    int textY = gameLogic.getBoard().getHeight() * TILE_SIZE + textHeight;
    g.drawString(text, textX, textY);
  }

  public static Piece findKing(PieceColor color, Board board) {
    for (Piece piece : board.getPieces()) {
      if (piece.getType() == PieceType.KING && piece.getColor() == color) {
        return piece;
      }
    }
    return null;
  }

  public static void render(Graphics g, GameLogic gameLogic, Game game) {
    int boardHeight = gameLogic.getBoard().getHeight() * TILE_SIZE;
    int boardWidth = gameLogic.getBoard().getWidth() * TILE_SIZE;
    int textHeight = 30;

    g.setColor(Color.BLACK);
    g.fillRect(0, 0, boardWidth, boardHeight + textHeight * 2);
    gameLogic.getBoard().draw(g, TILE_SIZE);

    drawCurrentPlayer(g, gameLogic.getCurrentPlayer(), textHeight, gameLogic);
    PieceColor color = gameLogic.getCurrentPlayer().getColor();
    Piece king = findKing(color, gameLogic.getBoard());
    if (gameLogic.isKingUnderCheck(color)) {
      if (king != null) {
        g.setColor(new Color(255, 0, 0, 100));
        g.fillRect(king.getX() * TILE_SIZE, king.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
      }
      List<Piece> checkers = CheckmateChecker.getCheckers(color, gameLogic);
      for (Piece checker : checkers) {
        g.setColor(new Color(255, 165, 0, 100));
        g.fillRect(checker.getX() * TILE_SIZE, checker.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
      }
    }
    int textX = 10;
    String currentPlayerText = "Ходит: " + gameLogic.getCurrentPlayer().getColor();
    int textWidth = g.getFontMetrics().stringWidth(currentPlayerText);
    if (gameLogic.isCheckmate(color)) {
      String text = "Мат! Побеждает " + (color == PieceColor.WHITE ? "Черный" : "Белый") + "!";
      g.setColor(Color.WHITE);
      g.setFont(new Font("Arial", Font.BOLD, 25));
      g.drawString(text, textX, boardHeight + textHeight + 25);
    } else if (gameLogic.isKingUnderCheck(color)) {
      String text = "Шах!";
      g.setColor(Color.WHITE);
      g.setFont(new Font("Arial", Font.BOLD, 25));
      g.drawString(text, textX, boardHeight + textHeight + 25);
    }
    if (game.isGameOver()) {
      String winnerText = "Победитель: " + game.getWinner() + "!";
      g.setColor(Color.WHITE);
      g.setFont(new Font("Arial", Font.BOLD, 25));
      g.drawString(winnerText, 10, boardHeight + textHeight + 50); // или другое подходящее место
    }
    if (game.getSelectedX() != -1 && game.getSelectedY() != -1) {
      g.setColor(new Color(88, 252, 230, 206));
      g.fillRect(game.getSelectedX() * TILE_SIZE, game.getSelectedY() * TILE_SIZE, TILE_SIZE,
          TILE_SIZE);
    }
  }
}