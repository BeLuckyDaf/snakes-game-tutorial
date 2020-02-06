package snakes_ui;

import snakes.*;

public class SnakesUIMain {
    /* UI Entry point */
    public static void main(String[] args) {

<<<<<<< HEAD
        var game = new SnakeGame(
                new Coordinate(14, 14), // mazeSize
=======
        SnakeGame game = new SnakeGame(
                new Coordinate(8, 8), // mazeSize
>>>>>>> 4a7c4120d8b90a51b4e7850c714881197aa6c765

                new Coordinate(4, 6), // head0
                Direction.DOWN,         // tailDirection2
                new Coordinate(7, 7), // head1
                Direction.UP,           // tailDirection1
                3,                 // initial snake size
                new Bot_n_strygin(),      // bot0
                new BotVS()       // bot1
        );

        SnakesWindow window = new SnakesWindow(game);
        new Thread(window).start();
    }
}
