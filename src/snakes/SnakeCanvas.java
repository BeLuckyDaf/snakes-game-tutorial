package snakes;
//Graphical interface extended from Strygin's version by Truong Nguyen

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

/**
 * This class is responsible for the snakes canvas in GUI
 */
public class SnakeCanvas extends Canvas {
    private static final int CELL_SIZE = 40;
    private static final int PAD = 2;
    private static final int SMALLER_PAD = 6;
    private static final int SMALL_PAD = 6;
    private static final int ApplePad = 10;
    private static final Color color0 = new Color(92, 192, 255);
    private static final Color color1 = new Color(255, 255, 255);
    private static final Color bodyColor0 = new Color(92, 192, 255);
    private static final Color bodyColor1 = new Color(255, 255, 255);
    private static final Color backgroundColor = new Color(0, 0, 0);
    private static final Color borderColor = new Color(22, 50, 76);
    private static final Color appleColor = Color.red;
    private static ImageIcon apple;
    Dimension renderSize;
    private SnakeGame game;

    /**
     * Constructs snake canvas
     * @param game main game flow with all its states within
     */
    public SnakeCanvas(SnakeGame game) {
        this.game = game;
        java.net.URL imageURL = getClass().getResource("images/apple.png");
        if (imageURL != null) {
            Image appleImage = new ImageIcon(imageURL).getImage().getScaledInstance(CELL_SIZE - 10, CELL_SIZE - 10, Image.SCALE_SMOOTH);
            apple = new ImageIcon(appleImage);
        }
        renderSize = new Dimension((game.mazeSize.x + 2) * CELL_SIZE, (game.mazeSize.y + 2) * CELL_SIZE);
    }

    /**
     * Fills cell with color and pads by pad
     * @param g     game field
     * @param cell  cell to fill
     * @param color color to fill
     * @param pad   cell padding
     */
    private void fillCellWithPad(Graphics2D g, Coordinate cell, Color color, int pad) {
        g.setColor(color);
        g.fillRect((cell.x + 1) * CELL_SIZE + pad, (cell.y + 1) * CELL_SIZE + pad, CELL_SIZE - 2 * pad, CELL_SIZE - 2 * pad);
    }

    /**
     * Fills cell with color and pads by constant PAD
     * @param g     game field
     * @param cell  cell to fill
     * @param color color to fill
     */
    private void fillCell(Graphics2D g, Coordinate cell, Color color) {
        fillCellWithPad(g, cell, color, PAD);
    }

    /**
     * Fills cell with color and padding by constant SMALLER_PAD
     * @param g     game field
     * @param cell  cell to fill
     * @param color color to fill
     */
    private void fillSmallerCell(Graphics2D g, Coordinate cell, Color color) {
        fillCellWithPad(g, cell, color, SMALLER_PAD);
    }

    /**
     * Fills cell with color and padding by constant SMALL_PAD
     * @param g     game field
     * @param cell  cell to fill
     * @param color color to fill
     */
    private void fillSmallCell(Graphics2D g, Coordinate cell, Color color) {
        fillCellWithPad(g, cell, color, SMALL_PAD);
    }

    /**
     * Renders the game
     * @param g game field
     */
    private void render(Graphics2D g) {

        g.setColor(borderColor);
        g.fillRect(0, 0, renderSize.width, renderSize.height);
        g.setColor(backgroundColor);
        g.fillRect(CELL_SIZE, CELL_SIZE, renderSize.width - 2 * CELL_SIZE, renderSize.height - 2 * CELL_SIZE);

        fillCellWithPad(g, game.appleCoordinate, appleColor, ApplePad);

        Iterator<Coordinate> it = game.snake0.body.stream().iterator();
        while (it.hasNext()) {
            Coordinate bp = it.next();
            fillCell(g, bp, color0);
            fillSmallerCell(g, bp, bodyColor0); //print body
        }

        it = game.snake1.body.stream().iterator();
        while (it.hasNext()) {
            Coordinate bp = it.next();
            fillCell(g, bp, color1);
            fillSmallerCell(g, bp, bodyColor1); //print body
        }

        fillSmallCell(g, game.snake1.getHead(), new Color(0, 0, 0)); //head
        fillSmallCell(g, game.snake0.getHead(), new Color(0, 0, 0)); //head

        //Print the score on score board
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
        g.setColor(new Color(228, 255, 224));
        g.drawString(game.gameResult, renderSize.width / 2 - getFontMetrics(g.getFont()).stringWidth(game.gameResult) / 2, renderSize.height - 10); //game results

        //Print snakes symbol on score board
        g.setColor(new Color(92, 192, 255)); //outer
        g.fillRect(renderSize.width / 2 - 2 * CELL_SIZE + 14, renderSize.height - CELL_SIZE + 11, CELL_SIZE - 18, CELL_SIZE - 18);
        g.setColor(new Color(92, 192, 255)); //inner
        g.fillRect(renderSize.width / 2 - 2 * CELL_SIZE + 18, renderSize.height - CELL_SIZE + 15, CELL_SIZE - 26, CELL_SIZE - 26);
        g.setColor(new Color(255, 255, 255)); //outer
        fillRect(g, renderSize.width / 2 + 2 * CELL_SIZE - 14, renderSize.height - CELL_SIZE + 11, CELL_SIZE - 18, CELL_SIZE - 18);
        g.setColor(new Color(255, 255, 255)); //inner
        fillRect(g, renderSize.width / 2 + 2 * CELL_SIZE - 18, renderSize.height - CELL_SIZE + 15, CELL_SIZE - 26, CELL_SIZE - 26);

        //Print apple symbols on score board
        apple.paintIcon(this, g, renderSize.width / 2 - 3 * CELL_SIZE, renderSize.height - CELL_SIZE + 3);
        paintIcon(g, apple, renderSize.width / 2 + 3 * CELL_SIZE, renderSize.height - CELL_SIZE + 3);

        //Print apple counts on score board
        g.drawString(Integer.toString(game.appleEaten0), renderSize.width / 2 - 4 * CELL_SIZE, renderSize.height - 10);
        drawString(g, Integer.toString(game.appleEaten1), renderSize.width / 2 + 4 * CELL_SIZE, renderSize.height - 10);

        //Print bot names on score board
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
        g.drawString(game.name0, CELL_SIZE, renderSize.height - 10);
        drawString(g, game.name1, renderSize.width - CELL_SIZE, renderSize.height - 10);
    }

    /**
     * Repaints the control.
     * @param g game field
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D gg = (Graphics2D) g; //bufferStrategy.getDrawGraphics();
        gg.clearRect(0, 0, renderSize.width, renderSize.height);
        render(gg);

        //bufferStrategy.show();
    }

    /**
     * Right align drawing texts
     * @param g game field
     * @param s text to draw
     * @param x x-axis position of the first character
     * @param y y-axis position of the first character
     */
    public void drawString(Graphics2D g, String s, int x, int y) {
        g.drawString(s, x - getFontMetrics(g.getFont()).stringWidth(s), y);
    }

    /**
     * Right align drawing images
     * @param g game field
     * @param i image to draw
     * @param x x-axis position of the top-left corner
     * @param y y-axis position of the top-left corner
     */
    public void paintIcon(Graphics2D g, ImageIcon i, int x, int y) {
        i.paintIcon(this, g, x - i.getIconWidth(), y);
    }

    /**
     * Fills rectangle
     * @param g      game field to fill
     * @param x      x-axis position of the rectangle
     * @param y      y-axis position of the rectangle
     * @param width  width of the rectangle
     * @param height height of the rectangle
     */
    public void fillRect(Graphics2D g, int x, int y, int width, int height) {
        g.fillRect(x - width, y, width, height);
    }
}
