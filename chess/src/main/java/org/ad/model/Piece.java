package org.ad.model;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представляет шахматную фигуру.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Piece {

  private PieceType type;
  private PieceColor color;
  private int x;
  private int y;

  /**
   * Рисует фигуру на заданном графическом контексте.
   *
   * @param g        Графический контекст.
   * @param tileSize Размер одной клетки на доске.
   */
  public void draw(Graphics g, int tileSize) {
    String imageName =
        color.toString().toLowerCase() + "_" + type.toString().toLowerCase() + ".png";
    try {
      Image image = ImageIO.read(getClass().getResource("/" + imageName));
      g.drawImage(image, x * tileSize, y * tileSize, tileSize, tileSize, null);
    } catch (IOException e) {
      // В случае, если картинка не загрузилась, рисуем квадратик с текстом
      g.setColor(color == PieceColor.WHITE ? Color.WHITE : Color.BLACK);
      String text = type.toString().substring(0, 1);
      FontMetrics metrics = g.getFontMetrics();
      int x_text = x * tileSize + (tileSize - metrics.stringWidth(text)) / 2;
      int y_text = y * tileSize + ((tileSize - metrics.getHeight()) / 2) + metrics.getAscent();
      g.drawString(text, x_text, y_text);
    }
  }
}