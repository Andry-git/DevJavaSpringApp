package org.ad.engine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import lombok.Getter;

/**
 * Основная панель для пользовательского интерфейса шахматной игры.
 */
public class Game extends JPanel implements ActionListener {
  public static final int TILE_SIZE = 64;
  private final GameLogic gameLogic;
  private final int WIDTH = 8;
  private final int HEIGHT = 8;
  private int selectedX = -1;
  private int selectedY = -1;
  private String winner;

  private boolean gameOver = false;

  public void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public void setWinner(String winner) {
    this.winner = winner;
  }

  public String getWinner() {
    return winner;
  }

  public Game() {
    this.gameLogic = new GameLogic(this);
    int textHeight = 30;
    setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE + textHeight * 2));
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int x = e.getX() / TILE_SIZE;
        int y = e.getY() / TILE_SIZE;
        if (selectedX == -1 && selectedY == -1) {
          if (gameLogic.getBoard().getPieceAt(x, y) != null) {
            selectedX = x;
            selectedY = y;
          }
        } else {
          if (gameLogic.movePiece(selectedX, selectedY, x, y)) {
            selectedX = -1;
            selectedY = -1;
          } else {
            selectedX = x;
            selectedY = y;
          }
        }
        repaint();
      }
    });

  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Render.render(g, gameLogic, this);
  }

  public int getSelectedX() {
    return selectedX;
  }

  public int getSelectedY() {
    return selectedY;
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("Chess Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Game game = new Game();
    frame.add(game);
    frame.pack();
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

  }
}