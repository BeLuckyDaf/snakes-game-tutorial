package snakes;

/**
 * This class is responsible for running bots in a separate threads
 */
public class SnakesRunner implements Runnable {
    private Bot bot;
    private Snake snake;
    private Snake opponent;
    private Coordinate mazeSize;
    public Coordinate apple;
    public Direction chosen_direction;

    /**
     * Construct SnakesRunner instance
     * @param bot running bot
     * @param snake snake that controlled by the current bot
     * @param opponent opponent's snake
     * @param mazeSize size of the board
     * @param apple apple's coordinate
     */
    public SnakesRunner(Bot bot, Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        this.bot = bot;
        this.snake = snake;
        this.opponent = opponent;
        this.mazeSize = mazeSize;
        this.apple = apple;
    }

    /**
     *  Execute chooseDirection method of the current bot and save chosen option in a field chosen_direction
     *  This method is running in a separate thread
     */
    @Override
    public void run() {
        chosen_direction = bot.chooseDirection(snake, opponent, mazeSize, apple);
    }
}