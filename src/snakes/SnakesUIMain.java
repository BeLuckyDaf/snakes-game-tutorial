package snakes;

public class SnakesUIMain {
    /* UI Entry point */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("You must provide two classes implementing the Bot interface.");
            System.exit(1);
        }

        BotLoader loader = new BotLoader();
        Bot firstBot = loader.getBotClass(args[0]);
        Bot secondBot = loader.getBotClass(args[1]);

        SnakeGame game = new SnakeGame(
                new Coordinate(14, 14), // mazeSize
                new Coordinate(4, 6), // head0
                Direction.DOWN,         // tailDirection2
                new Coordinate(7, 7), // head1
                Direction.UP,           // tailDirection1
                3,                 // initial snake size
                firstBot,      // bot0
                secondBot       // bot1
        );

        SnakesWindow window = new SnakesWindow(game);
        new Thread(window).start();
    }
}

