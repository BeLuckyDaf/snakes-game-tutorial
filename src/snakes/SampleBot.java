package snakes;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Stream;

public class SampleBot implements Bot {
    private static final Direction[] DIRECTIONS = new Direction[] {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

    @Override
    /* choose the direction (stupidly) */
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        Coordinate head = snake.getHead();

        /* Get the coordinate of the second element of the snake's body
         * to prevent going backwards */
        Coordinate afterHeadNotFinal = null;
        if (snake.body.size() >= 2) {
            Iterator<Coordinate> it = snake.body.iterator();
            it.next();
            afterHeadNotFinal = it.next();
        }

        final Coordinate afterHead = afterHeadNotFinal;

        /* The only illegal move is going backwards. Here we are checking for not doing it */
        Direction[] validMoves = Arrays.stream(DIRECTIONS)
                .filter(d -> !head.moveTo(d).equals(afterHead)) // Filter out the backwards move
                .sorted()
                .toArray(Direction[]::new);

        /* Just naïve greedy algorithm that tries not to die at each moment in time */
        Direction[] notLosing = Arrays.stream(validMoves)
                .filter(d -> head.moveTo(d).inBounds(mazeSize))             // Don't leave maze
                .filter(d -> !opponent.elements.contains(head.moveTo(d)))   // Don't collide with opponent...
                .filter(d -> !snake.elements.contains(head.moveTo(d)))      // and yourself
                .sorted()
                .toArray(Direction[]::new);

        if (notLosing.length > 0) return notLosing[0];
        else return validMoves[0];
        /* ^^^ Cannot avoid losing here */
    }
}
