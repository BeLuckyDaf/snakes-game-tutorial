package snakes_ui;

import snakes.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SnakesUIMain {
    private static final String RESULTS_LOG_FILE = "results.txt";
    private static FileWriter results_fw;
    /* UI Entry point */
    public static void main(String[] args) throws InterruptedException, IOException {
//        ArrayList<Bot> bots = new ArrayList<>();
//        bots.add(new BotVS());
//        bots.add(new Bot_D_Kabirov());
//        bots.add(new Bot_V_Vasilev());

        results_fw = new FileWriter(RESULTS_LOG_FILE, false);

//        start_round_robin_tournament(bots);

        results_fw.close();
        SnakeGame game = new SnakeGame(
                new Coordinate(14, 14), // mazeSize


                new Coordinate(4, 6), // head0
                Direction.DOWN,         // tailDirection2
                new Coordinate(7, 7), // head1
                Direction.UP,           // tailDirection1
                3,                 // initial snake size
                new Bot_D_Kabirov(),      // bot0
                new Bot_V_Vasilev()       // bot1
        );

        SnakesWindow window = new SnakesWindow(game);
        new Thread(window).start();
    }

    public static void start_round_robin_tournament(ArrayList<Bot> bots) throws InterruptedException, IOException {
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
        ArrayList<String> bots_names = new ArrayList<>();


        // If there are an odd number of players - add the dummy player
        if (bots.size() % 2 == 1)
            bots.add(null);

        for (int i = 0; i < bots.size(); i++) {
            playerNumber.add(i);
            points.add(0);
            if (bots.get(i) != null)
                bots_names.add(bots.get(i).getClass().getSimpleName());
        }

        for (int k = 0; k < bots.size() - 1; k++) {
            // play N / 2 rounds
            for (int i = 0; i < bots.size() / 2; i++) {
                // start the game between ith and N-i-1 bots
                Bot bot0 = bots.get(i);
                Bot bot1 = bots.get(bots.size() - i - 1);
                if (bot0 == null || bot1 == null) continue;
                SnakeGame game = new SnakeGame(mazeSize, head0, tailDirection0, head1, tailDirection1, snakeSize, bot0, bot1);
                SnakesWindow window = new SnakesWindow(game);
                Thread t = new Thread(window);
                t.start();
                t.join();

                Thread.sleep(1000);
                window.closeWindow();

                results_fw.write(game.name0 + " vs " + game.name1 + " : " + game.gameResult + "\n");
                System.out.println(game.name0 + " vs " + game.name1 + " : " + game.gameResult + "\n");

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

        results_fw.write("\n\n-------------------------------------------\n");
        // get and print the results
        for (int i = 0; i < bots.size(); i++) {
            if (bots.get(i) == null) continue;
            System.out.println(bots_names.get(playerNumber.get(i)) + " earned: " + points.get(playerNumber.get(i)).toString());
            results_fw.write(bots_names.get(playerNumber.get(i)) + " earned: " + points.get(playerNumber.get(i)).toString() + "\n");
        }
    }
}
