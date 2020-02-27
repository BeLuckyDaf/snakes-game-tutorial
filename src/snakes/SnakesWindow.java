package snakes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * This class is responsible for the game's GUI window
 */
public class SnakesWindow implements Runnable {
    private JFrame frame;
    private SnakeCanvas canvas;
    private SnakeGame game;
    private final static int TIME_LIMIT_PER_GAME = 3 * 60 * 1000; // time limit in mills
    private final static int TIME_LIMIT_PER_STEP = 1000; // time limit for one step in mills

    private boolean running = false;

    /**
     * Creates and set ups the window
     * @param game main game flow with all its states within
     */
    public SnakesWindow(SnakeGame game) {
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

    /**
     * Centers the window
     * @param frame game's window
     */
    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    /**
     * Runs the UI
     */
    public void run() {
        running = true;
        canvas.repaint();
        long startTime = System.currentTimeMillis();
        while(running) {
            long t = System.currentTimeMillis();

            try {
                running = game.runOneStep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            canvas.repaint();

            long elapsed = System.currentTimeMillis() - t;

            try {
                Thread.sleep(Math.max(200 - elapsed, 0));
            } catch (InterruptedException e) {
                if (game.gameResult != null)
                    game.gameResult = "interrupted";
                break;
            }

            // check for time limit
            if (System.currentTimeMillis() - startTime >= TIME_LIMIT_PER_GAME) {
                int snake0_size = game.snake0.body.size();
                int snake1_size = game.snake1.body.size();
                game.gameResult = (snake0_size > snake1_size ? 1 : 0) + " - " + (snake1_size > snake0_size ? 1 : 0);
                running = false;
                System.out.println("Round time left (" + (TIME_LIMIT_PER_GAME / 1000) + "seconds) \n");
            }
        }

        //JOptionPane.showMessageDialog(null, game.gameResult, "Game results", JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * Closes the frame
     */
    public void closeWindow() {
        frame.setVisible(false);
        frame.dispose();
    }
}

