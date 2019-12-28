package snakes_ui;

import snakes.*;

public class SnakesUIMain {
    /* UI Entry point */
    public static void main(String[] args) {

        var game = new SnakeGame(
                new Coordinate(8, 8), // mazeSize

                new Coordinate(2, 2), // head0
                Direction.DOWN,         // tailDirection2
                new Coordinate(5, 5), // head1
                Direction.UP,           // tailDirection1
                3,                 // initial snake size
                new Bot_n_strygin(),      // bot0
                new Bot_n_strygin()       // bot1
        );

        var window = new SnakesWindow(game);
        new Thread(window).start();
    }
}
