package snakes;

import java.util.*;

public class SnakesMain {
    /* entry point of the application */
    public static void main(String[] args) {
        // by Nikita Strygin
        // n.strygin@innopolis.university
        Scanner sc = new Scanner(System.in);
        boolean continue_;
        do {
            var game = new SnakeGame(
                    new Coordinate(8, 8), // mazeSize

                    new Coordinate(8, 4), // head0
                    Direction.DOWN,         // tailDirection2
                    new Coordinate(7, 5), // head1
                    Direction.LEFT,           // tailDirection1
                    3,  // size
                    new Bot_n_strygin(),    // bot0
                    new Bot_n_strygin()     // bot1
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

