package snakes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Implements tournament of the snake game with several rounds
 */
public class SnakesUIMain {
    private static final String LOG_DIRECTORY_PATH = "logs";
    private static FileWriter results_fw;
    private static int[][] total_results_table;

    /**
     * UI Entry point
     * @param args Two classes implementing the Bot interface
     * @throws InterruptedException Threads handler
     * @throws IOException  FileWriter handler
     */
    public static void main(String[] args) throws InterruptedException, IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        if (args.length < 2) {
            System.err.println("You must provide two classes implementing the Bot interface.");
            System.exit(1);
        }

        ArrayList<Class<? extends Bot>> bots = new ArrayList<>();
        BotLoader loader = new BotLoader();

        bots.add(loader.getBotClass(args[0]));
        bots.add(loader.getBotClass(args[1]));

        start_tournament_n_times(5, bots);
    }

    /**
     * Launch several rounds of snake game between bots
     * @param n Number of rounds
     * @param bots Competitive bots
     * @throws IOException FileWriter handler
     * @throws InterruptedException Threads handler
     */
    public static void start_tournament_n_times(int n, ArrayList<Class<? extends Bot>> bots) throws IOException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        total_results_table = new int[bots.size() + 1][bots.size() + 1];
        File dir = new File(LOG_DIRECTORY_PATH);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Cannot create log directory.");
        }
        for (int i = 0; i < n; i++) {
            System.out.println("\nTournament iteration number " + i + "\n");
            results_fw = new FileWriter(String.format("%s\\iteration_%d.txt", LOG_DIRECTORY_PATH, i), false);
            start_round_robin_tournament(bots);
            results_fw.close();
        }

        results_fw = new FileWriter(String.format("%s\\total.txt", LOG_DIRECTORY_PATH), false);
        for (int i = 0; i < bots.size(); i++)
            for (int j = i + 1; j < bots.size(); j++) {
                if (bots.get(i) == null || bots.get(j) == null) continue;
                System.out.println("\n" + bots.get(i).getSimpleName() + " vs. " + bots.get(j).getSimpleName() + ": " + total_results_table[i][j] + " - " + total_results_table[j][i]);
                results_fw.write(bots.get(i).getSimpleName() + " vs. " + bots.get(j).getSimpleName() + ": " + total_results_table[i][j] + " - " + total_results_table[j][i] + "\n");
            }
        results_fw.close();
    }

    /**
     * Start tournament between bots
     * @param bots Competitive bots
     * @throws InterruptedException Threads handler
     * @throws IOException FileWriter handler
     */
    public static void start_round_robin_tournament(ArrayList<Class<? extends Bot>> bots) throws InterruptedException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // init game settings
        Coordinate mazeSize = new Coordinate(14, 14);
        Coordinate head0 = new Coordinate(2, 2);
        Direction tailDirection0 = Direction.DOWN;
        Coordinate head1 = new Coordinate(11, 11);
        Direction tailDirection1 = Direction.UP;
        int snakeSize = 3;

        // a number associated to each player in bots ArrayList
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
                bots_names.add(bots.get(i).getSimpleName());
        }

        for (int k = 0; k < bots.size() - 1; k++) {
            // play N / 2 rounds
            for (int i = 0; i < bots.size() / 2; i++) {
                // start the game between ith and N-i-1 bots
                Bot bot0 = bots.get(i).getConstructor().newInstance();
                Bot bot1 = bots.get(bots.size() - i - 1).getConstructor().newInstance();
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
            Class<? extends Bot> buffer_player = bots.get(1);
            int buffer_player_number = playerNumber.get(1);
            for (int i = 2; i < bots.size(); i++) {
                // swap elements
                Class<? extends Bot> t = buffer_player;
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

