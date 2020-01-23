package snakes_ui;

import snakes.Coordinate;
import snakes.SnakeGame;

import javax.swing.*;
import java.awt.*;

public class SnakesWindow implements Runnable{
    private JFrame frame;
    private SnakeCanvas canvas;
    private SnakeGame game;

    private boolean running = false;

    /* construct main window */
    public SnakesWindow(SnakeGame game){
        frame = new JFrame("Snake Game");
        this.game = game;
        canvas = new SnakeCanvas(game);
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(canvas.renderSize);
        panel.setLayout(new GridLayout());

        //canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(false);

        panel.add(canvas);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        //canvas.createBufferStrategy(2);

        //canvas.createBufferStrategy(2);
        canvas.requestFocus();
        centreWindow(frame);
        //canvas.bufferStrategy = canvas.getBufferStrategy();
    }

    /* run the UI */
    public void run(){
        running = true;
        canvas.repaint();
        while(running) {
            var t = System.currentTimeMillis();

            running = game.runOneStep();
            canvas.repaint();

            var elapsed = System.currentTimeMillis() - t;

            try {
                Thread.sleep(Math.max(200 - elapsed, 0));
            } catch (InterruptedException e) {
                if (game.gameResult != null)
                    game.gameResult = "interrupted";
                break;
            }
        }

        JOptionPane.showMessageDialog(null, game.gameResult, "Game results", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}

