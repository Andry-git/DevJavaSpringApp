package org.ad;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import org.ad.engine.Game;

/**
 * Главный класс приложения, запускающий игру "Змейка".
 */
public class Main
{
    public static void main(String[] args) {

        JFrame frame = new JFrame("Zmeika");
        Game game = new Game();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (game.isGameOver() && e.getKeyCode() == KeyEvent.VK_R) {
                    game.restart();
                } else{
                    game.handleKeyPress(e);
                }
            }
        });
    }
}
