package org.ad.model;

import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;

import java.util.ArrayList;

/**
 * Класс для хранения тела змейки.
 */
public class Snake {
  public ArrayList<Point> body;

  public Snake() {
    body = new ArrayList<>();
    body.add(new Point(WIDTH / 2, HEIGHT / 2)); // Начальное положение змейки
  }
}
