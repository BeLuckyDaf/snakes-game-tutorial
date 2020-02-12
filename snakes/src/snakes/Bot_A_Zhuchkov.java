package snakes;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Stream;

public class Bot_A_Zhuchkov implements Bot {
    private final Random rnd = new Random();
    private static final Direction[] DIRECTIONS = new Direction[]{Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

    @Override
    /* choose the direction (stupidly) */
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        Coordinate head = snake.getHead();
        Coordinate headOpponent = opponent.getHead();

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

        Coordinate afterHeadNotFinalOp = null;
        if (opponent.body.size() >= 2) {
            Iterator<Coordinate> it = opponent.body.iterator();
            it.next();
            afterHeadNotFinalOp = it.next();
        }

        final Coordinate afterHeadOp = afterHeadNotFinalOp;
        Direction[] validMovesOp = Arrays.stream(DIRECTIONS)
                .filter(d -> !headOpponent.moveTo(d).equals(afterHeadOp))
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
            double shortestDistanceToApple = Math.max(mazeSize.x, mazeSize.y) + 1;
            Direction shortestDirectionToApple = null;

            for (Direction dir : notLosing) {
                double dist = calculateManhattanDistance(head.moveTo(dir), apple);

                Snake new_snake = snake.clone();
                new_snake.moveTo(dir, false);

                boolean result = true;
                for (Direction dOp : validMovesOp) {
                    Snake new_opponent = opponent.clone();
                    new_opponent.moveTo(dOp, false);

                    result = result & !new_opponent.elements.contains(new_snake.getHead());
                }

                if (dist < shortestDistanceToApple && result) {
                    shortestDistanceToApple = dist;
                    shortestDirectionToApple = dir;
                }
            }

            double shortestDistanceToAppleOpponent = calculateManhattanDistance(headOpponent, apple);

            if (shortestDistanceToAppleOpponent > shortestDistanceToApple)
                return shortestDirectionToApple;
            else
                return notLosing[rnd.nextInt(notLosing.length)];
        } else
            return validMoves[rnd.nextInt(validMoves.length)];
    }

    private double calculateManhattanDistance(Coordinate a, Coordinate b) {
        return Math.sqrt(Math.abs(a.x - b.x) + Math.abs(a.y - b.y));
    }
}
