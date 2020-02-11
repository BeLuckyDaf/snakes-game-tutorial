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
        // by Nikita Strygin
        // n.strygin@innopolis.university

        ArrayList<Bot> bots = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            bots.add(new BotVS());
        }
        for (int i = 0; i < 5; i++) {
            bots.add(new Bot_D_Kabirov());
        }

        Scanner sc = new Scanner(System.in);
        boolean continue_;
        do {

            start_round_robin_tournament(bots);

            String c;
            do {
                System.out.println("Again? (yes/no)");
                c = sc.next();
            } while (!c.equalsIgnoreCase("yes") && !c.equalsIgnoreCase("no"));
            continue_ = c.equalsIgnoreCase("yes");
        } while (continue_);
    }

    // by Danil Kabirov
    public static void start_round_robin_tournament(ArrayList<Bot> bots) {
        // init game settings
        Coordinate mazeSize = new Coordinate(14, 14);
        Coordinate head0 = new Coordinate(2, 2);
        Direction tailDirection0 = Direction.DOWN;
        Coordinate head1 = new Coordinate(5, 5);
        Direction tailDirection1 = Direction.UP;
        int snakeSize = 3;

        // a number assiciated to each player in bots ArrayList
        ArrayList<Integer> playerNumber = new ArrayList<>();

        // points earned by each player
        ArrayList<Integer> points = new ArrayList<>();


        // If there are an odd number of players - add the dummy player
        if (bots.size() % 2 == 1)
            bots.add(null);

        for (int i = 0; i < bots.size(); i++) {
            playerNumber.add(i);
            points.add(0);
        }

        for (int k = 0; k < bots.size() - 1; k++) {
            // play N / 2 rounds
            for (int i = 0; i < bots.size() / 2; i++) {
                // start the game between ith and N-i-1 bots
                Bot bot0 = bots.get(i);
                Bot bot1 = bots.get(bots.size() - i - 1);
                if (bot0 == null || bot1 == null) continue;
                SnakeGame game = new SnakeGame(mazeSize, head0, tailDirection0, head1, tailDirection1, snakeSize, bot0, bot1);
                game.run();

                // add the result of the game to total points
                points.set(playerNumber.get(i), points.get(playerNumber.get(i)) + Integer.parseInt(game.gameResult.substring(0, 1)));
                points.set(playerNumber.get(bots.size() - i - 1), points.get(playerNumber.get(bots.size() - i - 1)) + Integer.parseInt(game.gameResult.substring(game.gameResult.length() - 1)));
            }

            // shuffle players in special way
            Bot buffer_player = bots.get(1);
            int buffer_player_number = playerNumber.get(1);
            for (int i = 2; i < bots.size(); i++) {
                // swap elements
                Bot t = buffer_player;
                int t_number = buffer_player_number;

                buffer_player = bots.get(i);
                buffer_player_number = playerNumber.get(i);

                bots.set(i, t);
                playerNumber.set(i, t_number);
            }
            // set 1-st player by last player(stored in the buffer)
            bots.set(1, buffer_player);
            playerNumber.set(1, buffer_player_number);
        }

        // get and print the results
        for (int i = 0; i < bots.size(); i++) {
            if (bots.get(i) == null) continue;
            System.out.println(playerNumber.get(i) + "th player earned: " + points.get(playerNumber.get(i)).toString());
        }
    }
}

