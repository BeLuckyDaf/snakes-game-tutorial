package snakes_ui;

import snakes.*;
import snakes.hardlight.BotHardlight;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SnakesUIMain {
    private static final String RESULTS_FILE_PATH_PREFIX = "tournamentResultsLogs\\Iteration_";
    private static FileWriter results_fw;
    private static int[][] total_results_table;
    /* UI Entry point */
    public static void main(String[] args) throws InterruptedException, IOException {
        ArrayList<Bot> bots = new ArrayList<>();
//        bots.add(new BotVS());
//        bots.add(new Bot_D_Kabirov());
        bots.add(new Bot_user_control());
        bots.add(new BotVV());
//        bots.add(new Bot_n_strygin());
//        bots.add(new Bot_A_Zhuchkov());
//        bots.add(new BotHardlight());

        start_tournament_n_times(5, bots);
    }

    public static void start_tournament_n_times(int n, ArrayList<Bot> bots) throws IOException, InterruptedException {
        total_results_table = new int[bots.size() + 1][bots.size() + 1];
        for (int i = 0; i < n; i++) {
            System.out.println("\nTournament iteration number " + i + "\n");
            results_fw = new FileWriter("snakes\\" + RESULTS_FILE_PATH_PREFIX + i + ".txt", false);
            start_round_robin_tournament(bots);
            results_fw.close();
        }

        results_fw = new FileWriter("snakes\\tournamentResultsLogs\\Total_results.txt", false);
        for (int i = 0; i < bots.size(); i++)
            for (int j = i + 1; j < bots.size(); j++) {
                if (bots.get(i) == null || bots.get(j) == null) continue;
                System.out.println("\n" + bots.get(i).getClass().getSimpleName() + " vs. " + bots.get(j).getClass().getSimpleName() + ": " + total_results_table[i][j] + " - " + total_results_table[j][i]);
                results_fw.write(bots.get(i).getClass().getSimpleName() + " vs. " + bots.get(j).getClass().getSimpleName() + ": " + total_results_table[i][j] + " - " + total_results_table[j][i] + "\n");
            }
        results_fw.close();
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

                Thread.sleep(1000); // to allow users see the result
                window.closeWindow();

                float time_taken = (float)(System.currentTimeMillis() - game.startTime) / 1000;
                results_fw.write(game.name0 + " vs " + game.name1 + " : " + game.gameResult + "");
                results_fw.write(" (Time taken: " + time_taken + ")\n");
                System.out.print(game.name0 + " vs " + game.name1 + " : " + game.gameResult);
                System.out.println(" (Time taken: " + time_taken + ")");

                // add the result of the game to total points
                points.set(playerNumber.get(i), points.get(playerNumber.get(i)) + Integer.parseInt(game.gameResult.substring(0, 1)));
                points.set(playerNumber.get(bots.size() - i - 1), points.get(playerNumber.get(bots.size() - i - 1)) + Integer.parseInt(game.gameResult.substring(game.gameResult.length() - 1)));

                // add to the total results table
                total_results_table[playerNumber.get(i)][playerNumber.get(bots.size() - i - 1)] +=  Integer.parseInt(game.gameResult.substring(0, 1));
                total_results_table[playerNumber.get(bots.size() - i - 1)][playerNumber.get(i)] += Integer.parseInt(game.gameResult.substring(game.gameResult.length() - 1));
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

        results_fw.write("\n-------------------------------------------\n\n");
        // get and print the results
        for (int i = 0; i < bots.size(); i++) {
            if (bots.get(i) == null) continue;
            System.out.println(bots_names.get(playerNumber.get(i)) + " earned: " + points.get(playerNumber.get(i)).toString());
            results_fw.write(bots_names.get(playerNumber.get(i)) + " earned: " + points.get(playerNumber.get(i)).toString() + "\n");
        }
    }
}
