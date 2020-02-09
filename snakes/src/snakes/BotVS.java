package snakes;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

public class BotVS implements Bot {
    // by Vladislav Smirnov
    // Extends Bot_n_strygin by Nikita Strygin

    private final Random rnd = new Random();
    private static final Direction[] DIRECTIONS = new Direction[] {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

    @Override
    /* choose the direction (stupidly) */
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        Coordinate head = snake.getHead();

        Coordinate afterHeadNotFinal = null;
        if (snake.body.size() >= 2) {
            Iterator<Coordinate> it = snake.body.iterator();
            it.next();
            afterHeadNotFinal = it.next();
        }

        final Coordinate afterHead = afterHeadNotFinal;

        /* The only illegal move is going backwards. Here we are checking for not doing it */
        Direction[] validMoves = Arrays.stream(DIRECTIONS)
                .filter(d -> !head.moveTo(d).equals(afterHead))
                .sorted()
                .toArray(Direction[]::new);

        /* Just naïve greedy algorithm that tries not to die at each moment in time */
        Direction[] notLosing = Arrays.stream(validMoves)
                .filter(d -> head.moveTo(d).inBounds(mazeSize))             // Don't leave maze
                .filter(d -> !opponent.elements.contains(head.moveTo(d)))   // Don't collide with opponent...
                .filter(d -> !snake.elements.contains(head.moveTo(d)))      // and yourself
                .sorted()
                .toArray(Direction[]::new);
        
        if (notLosing.length > 0) {
            // Choose the shortest path to the apple
            Arrays.sort(notLosing, new SortByManhattanDistance(apple, head));
            if (Manhattan(opponent.getHead(), apple) > Manhattan(head, apple))
                return notLosing[notLosing.length-1];
            return notLosing[0];
        } else
            // We can't avoid losing here :shrug:
            return validMoves[0];
    }

    private static int Manhattan(Coordinate a, Coordinate b) {
        return Math.abs(b.x - a.x) + Math.abs(b.y - a.y);
    }

    private static class SortByManhattanDistance implements Comparator<Direction> {
        private Coordinate target;
        private Coordinate head;

        public SortByManhattanDistance(Coordinate target, Coordinate head) {
            this.target = target;
            this.head = head;
        }

        public int compare(Direction a, Direction b) {
            return Integer.compare(Manhattan(head.moveTo(a), target), Manhattan(head.moveTo(b), target));
        }
    }
}
