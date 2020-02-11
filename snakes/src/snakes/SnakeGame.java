package snakes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

public class SnakeGame {
    private static final String LOG_FILE = "log.txt";
    // timeout threshold for taking a decision in seconds
    private static final long TIMEOUT_THRESHOLD = 1;
    public final Snake snake0, snake1;
    private final Bot bot0, bot1;
    public final Coordinate mazeSize;
    private final Random rnd = new Random();
    public Coordinate appleCoordinate;
    public String gameResult = "0 - 0";
    public int appleEaten0 = 0;
    public int appleEaten1 = 0;
    private int snakeSize;
    public String name0,name1;
    /**
     * Constructs SnakeGame class
     * @param mazeSize size of the game board
     * @param head0 initial coordinate of first snake's head
     * @param tailDir0 initial direction of first snake's tail
     * @param head1 initial coordinate of second snake's head
     * @param tailDir1 initial direction of first snake's tail
     * @param size initial length of snakes
     * @param bot0 first smart snake bot
     * @param bot1 second smart snake bot
     */
    public SnakeGame(Coordinate mazeSize, Coordinate head0, Direction tailDir0, Coordinate head1, Direction tailDir1, int size,
                     Bot bot0, Bot bot1) {
        snakeSize = size;
        this.mazeSize = mazeSize;
        this.snake0 = new Snake(head0, tailDir0, size, mazeSize);
        this.snake1 = new Snake(head1, tailDir1, size, mazeSize);
        this.bot0 = bot0;
        this.bot1 = bot1;
        this.name0 = bot0.getClass().getSimpleName();
        this.name1 = bot1.getClass().getSimpleName();

        appleCoordinate = randomNonOccupiedCell();
    }


    /**
     * Converts game to string representation
     * @return game state as a string
     */
    public String toString() {
        char[][] cc = new char[mazeSize.x][mazeSize.y];
        for (int x = 0; x < mazeSize.x; x++)
            for (int y = 0; y < mazeSize.y; y++)
                cc[x][y] = '.';

        // Coordinate of head of first snake on board
        Coordinate h0 = snake0.getHead();
        cc[h0.x][h0.y] = 'h';

        // Coordinate of head of second snake on board
        Coordinate h1 = snake1.getHead();
        cc[h1.x][h1.y] = 'H';

        Iterator<Coordinate> it = snake0.body.stream().skip(1).iterator();
        while (it.hasNext()) {
            Coordinate bp = it.next();
            cc[bp.x][bp.y] = 'b';
        }

        it = snake1.body.stream().skip(1).iterator();
        while (it.hasNext()) {
            Coordinate bp = it.next();
            cc[bp.x][bp.y] = 'B';
        }

        cc[appleCoordinate.x][appleCoordinate.y] = 'X';

        StringBuilder sb = new StringBuilder();
        for (int y = mazeSize.y - 1; y >= 0; y--) {
            for (int x = 0; x < mazeSize.x; x++)
                sb.append(cc[x][y]);
            if (y != 0)
                sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Outputs text to stdout and file
     * @param text text that should be displayed
     */
    private void output(String text) {
        //System.out.println(text);
        FileWriter fw;
        try {
            fw = new FileWriter(LOG_FILE, true);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            fw.write(text + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Run one game step, return whether to continue the game
     * @return whether to continue the game
     */
    public boolean runOneStep() {
        output(toString());

        // the first bot takes a decision of next move
        long startTime = System.currentTimeMillis();

        Direction d0 = bot0.chooseDirection(snake0, snake1, mazeSize, appleCoordinate);
        long endTime = System.currentTimeMillis();


        boolean s0timeout = checkTimeout(startTime, endTime);

        // the second bot takes a decision of next move
        startTime = System.currentTimeMillis();
        Direction d1 = bot1.chooseDirection(snake1, snake0, mazeSize, appleCoordinate);
        endTime = System.currentTimeMillis();

        boolean s1timeout = checkTimeout(startTime, endTime);

        output("snake0->" + d0 + ", snake1->" + d1);
        output("Apples eaten: " + appleEaten0 + " - " + appleEaten1);

        //var grow = move % 3 == 2;
        boolean grow0 = snake0.getHead().moveTo(d0).equals(appleCoordinate);
        boolean grow1 = snake1.getHead().moveTo(d1).equals(appleCoordinate);

        boolean wasGrow = grow0 || grow1;

        boolean s0dead = !snake0.moveTo(d0, grow0);
        boolean s1dead = !snake1.moveTo(d1, grow1);

        if (wasGrow || appleCoordinate == null) {
            appleEaten0 = snake0.body.size() - snakeSize;
            appleEaten1 = snake1.body.size() - snakeSize;
            appleCoordinate = randomNonOccupiedCell();
        }
        s0dead |= snake0.headCollidesWith(snake1);
        s1dead |= snake1.headCollidesWith(snake0);

        /* stopping game condition
            - one of snakes collides with something
            - one of the snakes decides what it's next move too long
        */
        boolean cont = !(s0dead || s1dead || s0timeout || s1timeout);

        if (!cont) {
            gameResult = "";
            String result = "0 - 0";
            if (s0dead ^ s1dead)
                result = (s0dead ? 0 : 1) + " - " + (s1dead ? 0 : 1);
            else if (s0dead && s1dead)
                result = (appleEaten0 > appleEaten1 ? 1 : 0) + " - " + (appleEaten1 > appleEaten0 ? 1 : 0);
            gameResult += result;
        }
        return cont;
    }

    /**
     * Run the game
     */
    public void run() {
        while (runOneStep())
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }

        output(gameResult);
    }

    /**
     * Check time spent by a snake for taking decision does not exceed the threshold
     * @param startTime starting time of deciding next move
     * @param endTime finish time of thinking
     * @return True - if exceed
     */
    private boolean checkTimeout(long startTime, long endTime){
        long duration = (endTime - startTime) / 1000;
        return duration > TIMEOUT_THRESHOLD;
    }

    /**
     * Selects random non-occupied cell of maze
     * @return random non-occupied coordinate of the game board
     */
    private Coordinate randomNonOccupiedCell() {
        while (true) {
            Coordinate c = new Coordinate(rnd.nextInt(mazeSize.x), rnd.nextInt(mazeSize.y));
            if (snake0.elements.contains(c))
                continue;
            if (snake1.elements.contains(c))
                continue;

            return c;
        }
    }
}
