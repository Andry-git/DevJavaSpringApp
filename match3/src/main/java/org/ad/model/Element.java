package org.ad.model;

import java.awt.Color;

/**
 * Enum, представляющий типы элементов на игровом поле.
 * Каждый элемент имеет свой цвет.
 */
public enum Element {
  RED(Color.RED),
  GREEN(Color.GREEN),
  BLUE(Color.BLUE),
  YELLOW(Color.YELLOW),
  PURPLE(new Color(128, 0, 128)),
  CYAN(Color.CYAN),
  MAGENTA(Color.MAGENTA),
  PINK(Color.PINK),
  GRAY(Color.GRAY);


  private final Color color;

  Element(Color color) {
    this.color = color;
  }

  public Color getColor() {
    return color;
  }
}