package snakes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SnakeGame {
    private static final String LOG_FILE = "log.txt";
    public final Snake snake0, snake1;
    private final Bot bot0, bot1;
    public final Coordinate mazeSize;
    private final Random rnd = new Random();
    public Coordinate appleCoordinate;
    public String gameResult = "0 - 0";
    public int appleEaten0 = 0;
    public int appleEaten1 = 0;
    private int snakeSize;

    /* Constructs SnakeGame class */
    public SnakeGame(Coordinate mazeSize, Coordinate head0, Direction tailDir0, Coordinate head1, Direction tailDir1, int size,
                     Bot bot0, Bot bot1) {
        snakeSize = size;
        this.mazeSize = mazeSize;
        this.snake0 = new Snake(head0, tailDir0, size, mazeSize);
        this.snake1 = new Snake(head1, tailDir1, size, mazeSize);
        this.bot0 = bot0;
        this.bot1 = bot1;

        appleCoordinate = randomNonOccupiedCell();
    }

    /* Converts game to string representation */
    public String toString() {
        char[][] cc = new char[mazeSize.x][mazeSize.y];
        for (int x = 0; x < mazeSize.x; x++)
            for (int y = 0; y < mazeSize.y; y++)
                cc[x][y] = '.';

        var h0 = snake0.getHead();
        var h1 = snake1.getHead();
        cc[h0.x][h0.y] = 'h';
        cc[h1.x][h1.y] = 'H';

        var it = snake0.body.stream().skip(1).iterator();
        while (it.hasNext()) {
            var bp = it.next();
            cc[bp.x][bp.y] = 'b';
        }

        it = snake1.body.stream().skip(1).iterator();
        while (it.hasNext()) {
            var bp = it.next();
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

    /* Outputs text to stdout and file */
    private void output(String text) {
        System.out.println(text);
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

    /* run one game step, return whether to continue the game */
    public boolean runOneStep() {
        output(toString());

        var d0 = bot0.chooseDirection(snake0, snake1, mazeSize, appleCoordinate);
        var d1 = bot1.chooseDirection(snake1, snake0, mazeSize, appleCoordinate);

        output("snake0->" + d0 + ", snake1->" + d1);

        //var grow = move % 3 == 2;
        var grow0 = snake0.getHead().moveTo(d0).equals(appleCoordinate);
        var grow1 = snake1.getHead().moveTo(d1).equals(appleCoordinate);

        var wasGrow = grow0 || grow1;

        var s0dead = !snake0.moveTo(d0, grow0);
        var s1dead = !snake1.moveTo(d1, grow1);

        if (wasGrow || appleCoordinate == null) {
            appleEaten0 = snake0.body.size() - snakeSize;
            appleEaten1 = snake1.body.size() - snakeSize;
            appleCoordinate = randomNonOccupiedCell();
        }
        s0dead |= snake0.headCollidesWith(snake1);
        s1dead |= snake1.headCollidesWith(snake0);

        var cont = !(s0dead || s1dead);

        if (!cont) {
            gameResult = "";
            var result = "0 - 0";
            if (s0dead ^ s1dead)
                result = (s0dead ? 0 : 1) + " - " + (s1dead ? 0 : 1);
            else if (s0dead && s1dead)
                result = (appleEaten0 > appleEaten1 ? 1 : 0) + " - " + (appleEaten1 > appleEaten0 ? 1 : 0);
            gameResult += result;
        }
        return cont;
    }

    /* run the game */
    public void run() {
        while (runOneStep())
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }

        output(gameResult);
    }

    /* selects random non-occupied cell of maze */
    private Coordinate randomNonOccupiedCell() {
        while (true) {
            var c = new Coordinate(rnd.nextInt(mazeSize.x), rnd.nextInt(mazeSize.y));
            if (snake0.elements.contains(c))
                continue;
            if (snake1.elements.contains(c))
                continue;

            return c;
        }
    }
}
