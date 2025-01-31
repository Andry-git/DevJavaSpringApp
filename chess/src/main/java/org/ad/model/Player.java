package org.ad.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Представляет игрока в шахматной игре.
 */
@Data
@AllArgsConstructor
public class Player {

  private PieceColor color;
}
