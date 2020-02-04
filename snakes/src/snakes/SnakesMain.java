package snakes;

import java.util.*;

/**
 * This class implements entry point of the Snake game
 */
public class SnakesMain {
    /**
     * Entry point of the application
     * @param args system arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean continue_;
        do {
            SnakeGame game = new SnakeGame(
                    new Coordinate(8, 8), // mazeSize
                    new Coordinate(2, 2), // head0
                    Direction.DOWN,            // tailDirection0
                    new Coordinate(5, 5), // head1
                    Direction.UP,              // tailDirection1
                    3,  // size
                    new Bot_n_strygin(),       // bot0
                    new Bot_n_strygin()        // bot1
            );
            game.run();
            String c;
            do {
                System.out.println("Again? (yes/no)");
                c = sc.next();
            } while (!c.equalsIgnoreCase("yes") && !c.equalsIgnoreCase("no"));
            continue_ = c.equalsIgnoreCase("yes");
        } while (continue_);
    }
}

