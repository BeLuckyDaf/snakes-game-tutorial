package snakes_ui;

import snakes.*;

public class SnakesUIMain {
    /* UI Entry point */
    public static void main(String[] args) {

        SnakeGame game = new SnakeGame(
                new Coordinate(14, 14), // mazeSize


                new Coordinate(4, 6), // head0
                Direction.DOWN,         // tailDirection2
                new Coordinate(7, 7), // head1
                Direction.UP,           // tailDirection1
                3,                 // initial snake size
                new BotVS(),      // bot0
                new BotVS()       // bot1
        );

        SnakesWindow window = new SnakesWindow(game);
        new Thread(window).start();
    }
}
