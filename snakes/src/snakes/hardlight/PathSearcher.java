package snakes.hardlight;

import snakes.Coordinate;
import snakes.Direction;
import snakes.Snake;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

public class PathSearcher {
    private final Coordinate mazeSize;
    private static PathSearcher instance = null;

    private PathSearcher(Coordinate mazeSize) {
        this.mazeSize = mazeSize;
    }

    public static PathSearcher getPathSearcher(Coordinate mazeSize) {
        if (instance == null)
            instance = new PathSearcher(mazeSize);
        return instance;
    }

    public LinkedList<Coordinate> findPath(Snake snake, Coordinate apple) {
        final Coordinate head = snake.getHead();
        final LinkedList<LinkedList<Coordinate>> paths = new LinkedList<>();
        final HashSet<Coordinate> visited = new HashSet<>();

        LinkedList<Coordinate> startPath = new LinkedList<>();
        startPath.add(head);
        paths.add(startPath);

        Coordinate previous = (Coordinate) snake.body.toArray()[1];
        Coordinate current;
        Direction direction;
        LinkedList<Coordinate> path;
        LinkedList<Coordinate> newPath;
        while (paths.size() != 0) {
            path = paths.removeFirst();
            current = path.getLast();
            if (!visited.contains(current) && (isValidMove(snake, null, current) || current.equals(head))) {
                if (current.equals(apple)) {
                    path.removeFirst();
                    return path;
                }
                visited.add(current);
                direction = current.getDirection(previous);
                for (Coordinate adjusted : getAdjustedCoordinates(current, direction)) {
                    newPath = new LinkedList<>(path);
                    newPath.add(adjusted);
                    paths.add(newPath);
                }
            }
        }
        return null;
    }

    public Direction getFreeDirection(Snake snake, Snake opponent) {
        final int depth = snake.body.size() - 1;
        final Coordinate head = snake.getHead();
        final HashSet<Coordinate> visited = new HashSet<>();
        final LinkedList<LinkedList<Coordinate>> paths = new LinkedList<>();

        LinkedList<Coordinate> startPath = new LinkedList<>();
        startPath.add(head);
        paths.add(startPath);

        visited.add(head);
        int maxDepth = 0;
        Direction bestDirection = getAnyDirection(snake, opponent);
        for (Direction direction : Direction.values()) {
            Coordinate current;
            while (paths.size() != 0) {
                LinkedList<Coordinate> path = paths.pop();
                current = path.getLast();
                if (!visited.contains(current) && (isValidMove(snake, opponent, current) || current.equals(head))) {
                    if (path.size() > maxDepth) {
                        maxDepth = path.size();
                        bestDirection = direction;
                    }
                    if (current.equals(snake.body.getLast()) || path.size() == depth)
                        return direction;
                    visited.add(current);
                    for (Coordinate adjusted : getAdjustedCoordinates(current, direction)) {
                        LinkedList<Coordinate> newPath = new LinkedList<>(path);
                        newPath.add(adjusted);
                        paths.add(newPath);
                    }
                }
            }
        }

        return bestDirection;
    }

    public Direction getAnyDirection(Snake snake, Snake opponent) {
        return Arrays.stream(Direction.values())
                .filter(direction -> isValidMove(snake, opponent, snake.getHead().moveTo(direction)))
                .findFirst()
                .orElse(Direction.DOWN);
    }

    private Stack<Coordinate> getAdjustedCoordinates(Coordinate coordinate, Direction tailDirection) {
        final Stack<Coordinate> adjusted = new Stack<>();
        Arrays.stream(Direction.values())
                .filter(direction -> direction != tailDirection)
                .map(coordinate::moveTo)
                .forEachOrdered(adjusted::push);

        return adjusted;
    }

    private boolean isValidMove(Snake snake, Snake opponent, Coordinate next) {
        boolean hasLessPoints = opponent != null && snake.body.size() < opponent.body.size();
        boolean closeToOpponentHead = opponent != null &&
                getAdjustedCoordinates(opponent.getHead(), null).stream()
                        .anyMatch(coordinate -> coordinate.equals(next));
        boolean mayDamaged = hasLessPoints && closeToOpponentHead;
        return next.inBounds(mazeSize) &&
                isNotSnake(snake, next) &&
                (opponent != null && isNotSnake(opponent, next));
    }

    private boolean isNotSnake(Snake snake, Coordinate coordinate) {
        return snake.body.stream().noneMatch(chunk -> chunk.equals(coordinate));
    }
}
