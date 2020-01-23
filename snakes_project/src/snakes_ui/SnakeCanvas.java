package snakes_ui;

import snakes.Coordinate;
import snakes.SnakeGame;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class SnakeCanvas extends Canvas {
    private static final int CELL_SIZE = 40;
    private static final int PAD = 2;
    private static final int SMALLER_PAD = 6;
    private static final int SMALL_PAD = 6;
    private static final int ApplePad = 10;
    private static final Color color0 = new Color(92, 192, 255);
    private static final Color color1 = new Color(255, 255, 255);
    private static final Color bodyColor0 = new Color(92, 192, 255);
    private static final Color bodyColor1 = new Color(255,255, 255);
    private static final Color backgroundColor = new Color(0, 0, 0);
    private static final Color borderColor = new Color(22, 50, 76);
    private static final Color appleColor = Color.red;

    Dimension renderSize;
    BufferStrategy bufferStrategy;
    private SnakeGame game;

    /* construct snake canvas */
    public SnakeCanvas(SnakeGame game) {
        this.game = game;

        renderSize = new Dimension((game.mazeSize.x + 2) * CELL_SIZE, (game.mazeSize.y + 2) * CELL_SIZE);
    }

    /* fill cell with some padding */
    private void fillCellWithPad(Graphics2D g, Coordinate cell, Color color, int pad) {
        g.setColor(color);
        g.fillRect((cell.x + 1) * CELL_SIZE + pad, (cell.y + 1) * CELL_SIZE + pad, CELL_SIZE - 2 * pad, CELL_SIZE - 2 * pad);
    }

    /* fill cell */
    private void fillCell(Graphics2D g, Coordinate cell, Color color) {
        fillCellWithPad(g, cell, color, PAD);
    }

    /* fill smaller cell */
    private void fillSmallerCell(Graphics2D g, Coordinate cell, Color color) {
        fillCellWithPad(g, cell, color, SMALLER_PAD);
    }

    /* fill small cell */
    private void fillSmallCell(Graphics2D g, Coordinate cell, Color color) {
        fillCellWithPad(g, cell, color, SMALL_PAD);
    }

    /* render the game */
    private void render(Graphics2D g){

        g.setColor(borderColor);
        g.fillRect(0,0, renderSize.width, renderSize.height);
        g.setColor(backgroundColor);
        g.fillRect(CELL_SIZE, CELL_SIZE, renderSize.width - 2 * CELL_SIZE, renderSize.height - 2 * CELL_SIZE);

        fillCellWithPad(g, game.appleCoordinate, appleColor, ApplePad);

        var it = game.snake0.body.stream().iterator();
        while (it.hasNext()) {
            var bp = it.next();
            fillCell(g, bp, color0);
            fillSmallerCell(g, bp, bodyColor0);
        }

        it = game.snake1.body.stream().iterator();
        while (it.hasNext()) {
            var bp = it.next();
            fillCell(g, bp, color1);
            fillSmallerCell(g, bp, bodyColor1);
        }

        fillSmallCell(g, game.snake1.getHead(), new Color(0, 0, 0));
        fillSmallCell(g, game.snake0.getHead(), new Color(0, 0, 0));

        //Print the score
        g.setFont(new Font("Droid Serif", Font.PLAIN, 28));
        g.setColor(new Color(228, 255, 224));
        g.drawString(game.gameResult, (float) (renderSize.width - 5*CELL_SIZE) -17, (float) (renderSize.height - 10));
        g.setColor(new Color(92, 192, 255));
        g.fillRect(renderSize.width - 6*CELL_SIZE -2,renderSize.height - CELL_SIZE + 11,CELL_SIZE-18,CELL_SIZE -18);
        g.setColor(new Color(92, 192, 255));
        g.fillRect(renderSize.width - 6*CELL_SIZE +2,renderSize.height - CELL_SIZE + 15,CELL_SIZE-26,CELL_SIZE -26);

        g.setColor(new Color(255, 255, 255));
        g.fillRect(renderSize.width - 4*CELL_SIZE +3,renderSize.height - CELL_SIZE + 11,CELL_SIZE-18,CELL_SIZE -18);
        g.setColor(new Color(255, 255, 255));
        g.fillRect(renderSize.width - 4*CELL_SIZE +7,renderSize.height - CELL_SIZE + 15,CELL_SIZE-26,CELL_SIZE -26);

    }

    /* repaint the control */
    @Override
    public void paint(Graphics g) {
        Graphics2D gg = (Graphics2D) g; //bufferStrategy.getDrawGraphics();
        gg.clearRect(0, 0, renderSize.width, renderSize.height);
        render(gg);

        //bufferStrategy.show();
    }
}
