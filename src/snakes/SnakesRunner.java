package snakes;

/**
 *
 */
public class SnakesRunner implements Runnable {
    private Bot bot;
    private Snake snake;
    private Snake opponent;
    private Coordinate mazeSize;
    public Coordinate apple;
    public Direction choosen_direction;

    /**
     *
     * @param bot
     * @param snake
     * @param opponent
     * @param mazeSize
     * @param apple
     */
    public SnakesRunner(Bot bot, Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        this.bot = bot;
        this.snake = snake;
        this.opponent = opponent;
        this.mazeSize = mazeSize;
        this.apple = apple;
    }

    /**
     *
     */
    @Override
    public void run() {
        choosen_direction = bot.chooseDirection(snake, opponent, mazeSize, apple);
    }
}