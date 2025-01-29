package org.ad.engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.ad.model.Point;
import org.ad.model.Snake;

/**
 * Класс, реализующий игровую логику и отображение игры "Змейка".
 */
public class Game extends JPanel implements ActionListener {
  private final int TILE_SIZE = 20;
  private final int WIDTH = 20;
  private final int HEIGHT = 20;
  private boolean gameOver = false;
  private Timer timer;
  private Snake snake;
  private Point food;
  private int score = 0;
  private Direction direction = Direction.RIGHT; // Начальное направление змейки

  //Перечисление для обозначения направления змеи
  private enum Direction {
    UP, DOWN, LEFT, RIGHT
  }

  /**
   * Конструктор класса Game. Инициализирует игру.
   */
  public Game() {
    setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
    setBackground(Color.BLACK);
    setFocusable(true);
    snake = new Snake();
    generateFood();
    timer = new Timer(350, this); // Уменьшили интервал для более плавной игры
    timer.start();
  }

  /**
   * Генерирует координаты еды в случайном месте, не внутри змейки.
   */
  private void generateFood() {
    Random random = new Random();
    int x, y;
    do {
      x = random.nextInt(WIDTH);
      y = random.nextInt(HEIGHT);
    } while (snake.body.contains(
        new Point(x, y)));  // Проверяем, что еда не спавнится внутри змейки

    food = new Point(x, y);
  }

  /**
   * Обрабатывает нажатие клавиш для управления направлением змейки.
   *
   * @param e KeyEvent, содержащий информацию о нажатой клавише.
   */
  public void handleKeyPress(KeyEvent e) {
    int keyCode = e.getKeyCode();
    switch (keyCode) {
      case KeyEvent.VK_UP:
        if (direction != Direction.DOWN) {
          direction = Direction.UP;
        }
        break;
      case KeyEvent.VK_DOWN:
        if (direction != Direction.UP) {
          direction = Direction.DOWN;
        }
        break;
      case KeyEvent.VK_LEFT:
        if (direction != Direction.RIGHT) {
          direction = Direction.LEFT;
        }
        break;
      case KeyEvent.VK_RIGHT:
        if (direction != Direction.LEFT) {
          direction = Direction.RIGHT;
        }
        break;
    }
  }

  /**
   * Вызывается таймером для обновления состояния игры.
   *
   * @param e ActionEvent, содержащий информацию о событии таймера.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (!gameOver) {
      moveSnake();
      checkCollisions();
    }
    repaint();
  }

  /**
   * Перемещает змейку в текущем направлении.
   */
  private void moveSnake() {
    Point head = snake.body.get(0);
    Point newHead = null;
    switch (direction) {
      case UP:
        newHead = new Point(head.x, head.y - 1);
        break;
      case DOWN:
        newHead = new Point(head.x, head.y + 1);
        break;
      case LEFT:
        newHead = new Point(head.x - 1, head.y);
        break;
      case RIGHT:
        newHead = new Point(head.x + 1, head.y);
        break;
    }
    snake.body.add(0, newHead);

    if (newHead.equals(food)) {
      score++;
      generateFood();
    } else {
      snake.body.remove(snake.body.size() - 1);
    }
  }

  /**
   * Проверяет столкновения змейки со стенами или с самой собой.
   */
  private void checkCollisions() {
    Point head = snake.body.get(0);

    // Проверка столкновения со стенами
    if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
      gameOver = true;
      timer.stop();
      return;
    }

    // Проверка столкновения с самой собой
    for (int i = 1; i < snake.body.size(); i++) {
      if (head.equals(snake.body.get(i))) {
        gameOver = true;
        timer.stop();
        return;
      }
    }
  }

  /**
   * Отрисовывает игровое поле, змейку, еду и счет.
   *
   * @param g Graphics, контекст для рисования.
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g); // Отрисовка фона

    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Отрисовка змейки
    g2d.setColor(Color.GREEN);
    for (Point point : snake.body) {
      g2d.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    // Отрисовка еды
    g2d.setColor(Color.RED);
    g2d.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

    // Отрисовка счета
    g2d.setColor(Color.WHITE);
    g2d.setFont(new Font("Arial", Font.BOLD, 20));
    g2d.drawString("Score: " + score, 10, 30);

    // Отрисовка сообщения "Game Over"
    if (gameOver) {
      g2d.setColor(Color.WHITE);
      g2d.setFont(new Font("Arial", Font.BOLD, 30));
      String gameOverText = "Game Over!";
      String restartText = "Press R to restart";

      int gameOverTextWidth = g2d.getFontMetrics().stringWidth(gameOverText);
      int restartTextWidth = g2d.getFontMetrics().stringWidth(restartText);
      int gameOverX = (getWidth() - gameOverTextWidth) / 2;
      int restartX = (getWidth() - restartTextWidth) / 2;
      int gameOverY = getHeight() / 2 - 20;
      int restartY = getHeight() / 2 + 30;

      g2d.drawString(gameOverText, gameOverX, gameOverY);
      g2d.drawString(restartText, restartX, restartY);
    }
  }

  /**
   * Перезапускает игру.
   */
  public void restart() {
    gameOver = false;
    snake = new Snake();
    direction = Direction.RIGHT;
    score = 0;
    generateFood();
    timer.start();
    repaint();
  }

  /**
   * @return Возвращает true, если игра окончена.
   */
  public boolean isGameOver() {
    return gameOver;
  }
}