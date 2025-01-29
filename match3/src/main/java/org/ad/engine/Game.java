package org.ad.engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.ad.model.Element;

/**
 * Класс, представляющий основную игровую логику и интерфейс игры "три в ряд".
 */
public class Game extends JPanel implements ActionListener {
  private final int TILE_SIZE = 40;
  private final int WIDTH = 8;
  private final int HEIGHT = 8;
  private Element[][] board;
  private boolean gameOver = false;
  private Timer timer;
  private int selectedRow = -1;
  private int selectedCol = -1;
  private int score = 0;
  // Высота области для счета
  private final int SCORE_HEIGHT = 40;

  /**
   * Конструктор класса Game. Инициализирует игровое поле, устанавливает обработчики событий и запускает таймер.
   */
  public Game() {
    setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE + SCORE_HEIGHT));
    setBackground(Color.BLACK);
    setFocusable(true);

    board = new Element[HEIGHT][WIDTH];
    do {
      fillBoard();
    } while (hasMatches());

    //Обработчик события клика мыши
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        handleClick(e);
      }
    });

    timer = new Timer(100, this);
    timer.start();
  }

  /**
   * Обрабатывает клик мыши для выбора или обмена элементов.
   *
   * @param e Событие мыши.
   */
  private void handleClick(MouseEvent e) {
    int row = e.getY() / TILE_SIZE;
    int col = e.getX() / TILE_SIZE;

    if (row >= 0 && row < HEIGHT) {
      if (selectedRow == -1) {
        selectedRow = row;
        selectedCol = col;
      } else {
        if (isValidSwap(row, col)) {
          swapElements(row, col);
          processMatches();
          selectedRow = -1;
          selectedCol = -1;
        } else {
          selectedRow = row;
          selectedCol = col;
        }
      }
      repaint();
    }
  }

  /**
   * Обменивает местами два соседних элемента на игровом поле.
   *
   * @param row Строка второго элемента.
   * @param col Столбец второго элемента.
   */
  private void swapElements(int row, int col) {
    Element temp = board[selectedRow][selectedCol];
    board[selectedRow][selectedCol] = board[row][col];
    board[row][col] = temp;
  }

  /**
   * Проверяет, является ли обмен элементов допустимым.
   * Обмен допустим, если элементы соседние.
   *
   * @param row Строка элемента для проверки.
   * @param col Столбец элемента для проверки.
   * @return true если обмен возможен, иначе false
   */
  private boolean isValidSwap(int row, int col) {
    return Math.abs(row - selectedRow) + Math.abs(col - selectedCol) == 1;
  }

  /**
   * Обрабатывает совпадения на игровом поле.
   * Находит и удаляет совпадения, применяет гравитацию и заполняет пустые ячейки.
   */
  private void processMatches() {
    boolean matchesFound = true;
    while (matchesFound) {
      matchesFound = false;
      // Накапливаем совпадения в Set
      Set<Point> matches = new HashSet<>();

      //поиск по горизонтали
      for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH - 2; j++) {
          if (board[i][j] != null &&
              board[i][j] == board[i][j + 1] &&
              board[i][j] == board[i][j + 2]) {
            int start = j;
            int end = j;
            while (end < WIDTH && board[i][start] == board[i][end]) {
              matches.add(new Point(i, end));
              end++;
            }
            score += 10 * (end - start); // Собираем количество, будем умножать при удалении
            j = end - 1;
            matchesFound = true;
          }
        }
      }
      //поиск по вертикали
      for (int j = 0; j < WIDTH; j++) {
        for (int i = 0; i < HEIGHT - 2; i++) {
          if (board[i][j] != null &&
              board[i][j] == board[i + 1][j] &&
              board[i][j] == board[i + 2][j]) {
            int start = i;
            int end = i;
            while (end < HEIGHT && board[start][j] == board[end][j]) {
              matches.add(new Point(end, j));
              end++;
            }
            score += 10 * (end - start); // Собираем количество, будем умножать при удалении
            i = end - 1;
            matchesFound = true;
          }
        }
      }

      // Теперь удаляем все накопленные совпадения
      for (Point p : matches) {
        board[p.x][p.y] = null;
      }
      if (matchesFound) {
        applyGravity();
        fillEmptyCells();
      }
    }
    // Проверяем на отсутствие ходов после обработки совпадений
    gameOver = !hasMoves();
  }

  /**
   * Применяет гравитацию к игровому полю, перемещая элементы вниз.
   */
  private void applyGravity() {
    for (int col = 0; col < WIDTH; col++) {
      int emptyPos = HEIGHT - 1;
      for (int row = HEIGHT - 1; row >= 0; row--) {
        if (board[row][col] != null) {
          board[emptyPos][col] = board[row][col];
          if (row != emptyPos) {
            board[row][col] = null;
          }
          emptyPos--;
        }
      }
    }
  }

  /**
   * Заполняет пустые ячейки игрового поля новыми случайными элементами.
   */
  private void fillEmptyCells() {
    Random random = new Random();
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        if (board[i][j] == null) {
          board[i][j] = Element.values()[random.nextInt(Element.values().length)];
        }
      }
    }
  }

  /**
   * Заполняет игровое поле начальными случайными элементами.
   */
  private void fillBoard() {
    Random random = new Random();
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        board[i][j] = Element.values()[random.nextInt(Element.values().length)];
      }
    }
  }

  /**
   * Отрисовывает компоненты игрового поля.
   *
   * @param g Графический контекст для отрисовки.
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Отрисовываем элементы игрового поля
    for (int row = 0; row < HEIGHT; row++) {
      for (int col = 0; col < WIDTH; col++) {
        if (board[row][col] != null) {
          drawElement(g, row, col, board[row][col]);
        }
        if (row == selectedRow && col == selectedCol) {
          g.setColor(Color.WHITE);
          g.drawRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
      }
    }

    // Рисуем область для счета
    g.setColor(Color.BLACK);
    g.fillRect(0, HEIGHT * TILE_SIZE, getWidth(), SCORE_HEIGHT);

    // Выводим счет
    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.BOLD, 20));
    g.drawString("Score: " + score, 10, HEIGHT * TILE_SIZE + SCORE_HEIGHT - 10);

    if (gameOver) {
      g.setColor(Color.RED);
      g.setFont(new Font("Arial", Font.BOLD, 30));
      String gameOverText = "Game Over!";
      FontMetrics fm = g.getFontMetrics();
      int textWidth = fm.stringWidth(gameOverText);
      int textHeight = fm.getHeight();
      int x = (getWidth() - textWidth) / 2;
      int y = (getHeight() - textHeight) / 2 + textHeight / 2; // Центрирование по вертикали
      g.drawString(gameOverText, x, y);
    }
  }

  /**
   * Отрисовывает элемент на игровом поле в виде шарика.
   *
   * @param g       Графический контекст для отрисовки.
   * @param row     Строка элемента.
   * @param col     Столбец элемента.
   * @param element Элемент для отрисовки.
   */
  private void drawElement(Graphics g, int row, int col, Element element) {
    int padding = 5;
    g.setColor(element.getColor());
    g.fillOval(col * TILE_SIZE + padding, row * TILE_SIZE + padding, TILE_SIZE - 2 * padding,
        TILE_SIZE - 2 * padding);
  }

  /**
   * Проверяет наличие совпадений на игровом поле.
   *
   * @return true, если совпадения есть, иначе false
   */
  private boolean hasMatches() {
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        if (checkMatch(i, j)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Проверяет наличие совпадений на игровом поле после виртуального обмена местами двух элементов.
   *
   * @param row1 Строка первого элемента.
   * @param col1 Столбец первого элемента.
   * @param row2 Строка второго элемента.
   * @param col2 Столбец второго элемента.
   * @return true, если после обмена есть совпадения, иначе false.
   */
  private boolean checkMatchesAfterSwap(int row1, int col1, int row2, int col2) {
    Element[][] tempBoard = copyBoard(); // Создаем копию доски
    Element temp = tempBoard[row1][col1];
    tempBoard[row1][col1] = tempBoard[row2][col2];
    tempBoard[row2][col2] = temp;
    return checkForMatches(tempBoard);
  }

  /**
   * Проверяет наличие совпадений для элемента на заданных координатах.
   *
   * @param row Номер строки.
   * @param col Номер столбца.
   *
   * @return true, если совпадения есть, иначе false.
   */
  private boolean checkMatch(int row, int col) {
    if (board[row][col] == null) {
      return false;
    }
    // Проверка по горизонтали
    if (col < WIDTH - 2 && board[row][col] == board[row][col + 1] &&
        board[row][col] == board[row][col + 2]) {
      return true;
    }
    // Проверка по вертикали
    if (row < HEIGHT - 2 && board[row][col] == board[row + 1][col] &&
        board[row][col] == board[row + 2][col]) {
      return true;
    }
    return false;
  }

  /**
   * Проверяет наличие совпадений на переданной доске.
   *
   * @param board Доска для проверки.
   * @return true, если совпадения есть, иначе false
   */
  private boolean checkForMatches(Element[][] board) {
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        if (checkMatch(board, i, j)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Проверяет наличие совпадений для элемента на заданных координатах на переданной доске.
   *
   * @param board Доска для проверки.
   * @param row   Номер строки.
   * @param col   Номер столбца.
   * @return true, если совпадения есть, иначе false.
   */
  private boolean checkMatch(Element[][] board, int row, int col) {
    if (board[row][col] == null) {
      return false;
    }
    // Проверка по горизонтали
    if (col < WIDTH - 2 && board[row][col] == board[row][col + 1] &&
        board[row][col] == board[row][col + 2]) {
      return true;
    }
    // Проверка по вертикали
    if (row < HEIGHT - 2 && board[row][col] == board[row + 1][col] &&
        board[row][col] == board[row + 2][col]) {
      return true;
    }
    return false;
  }

  /**
   * Создает копию игрового поля.
   *
   * @return Копия игрового поля.
   */
  private Element[][] copyBoard() {
    Element[][] copy = new Element[HEIGHT][WIDTH];
    for (int i = 0; i < HEIGHT; i++) {
      System.arraycopy(board[i], 0, copy[i], 0, WIDTH);
    }
    return copy;
  }

  /**
   * Проверяет, есть ли на игровом поле возможные ходы, которые приведут к образованию комбинации.
   *
   * @return true, если есть хотя бы один возможный ход, иначе false.
   */
  private boolean hasMoves() {
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        // Проверяем обмен с соседями справа и снизу
        if (j < WIDTH - 1 && checkMatchesAfterSwap(i, j, i, j + 1)) {
          return true;
        }
        if (i < HEIGHT - 1 && checkMatchesAfterSwap(i, j, i + 1, j)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Выполняет действия таймера, обновляя отрисовку игрового поля.
   *
   * @param e Событие таймера.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();
  }

  /**
   * Перезапускает игру.
   */
  public void restart() {
    gameOver = false;
    timer.start();
    fillBoard();
    repaint();
  }

  /**
   * Проверяет, завершена ли игра.
   *
   * @return true если игра завершена, иначе false
   */
  public boolean isGameOver() {
    return gameOver;
  }
}
