// bot made by Vyacheslav Vasilev

package snakes;

import java.util.Arrays;
import java.util.Iterator;

public class Bot_V_Vasilev implements Bot {
    private static final Direction up = Direction.UP;
    private static final Direction down = Direction.DOWN;
    private static final Direction left = Direction.LEFT;
    private static final Direction right = Direction.RIGHT;
    private static final Direction[] directions = new Direction[]{up, down, left, right};

    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
//        Direction[] safe = safeDirections(snake, opponent, mazeSize);

        return tail_search(snake);
//        return greedy(snake, opponent, mazeSize, apple);
    }

    private Direction tail_search(Snake snake) {
        Iterator<Coordinate> iterator = snake.body.iterator();
        Coordinate head = iterator.next();
        Coordinate neck = iterator.next();
        if (head.x - neck.x == 0) {
            if (head.y - neck.y == 1)
                return right;
            return left;
        }
        if (head.x - neck.x == 1)
            return down;
        return up;
    }

    private Direction greedy(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        //create a matrix of cells. 0 is free, -1 is visited, -2 is apple, n is the number of turns after which it will be free.
        int[][] maze = new int[mazeSize.x][mazeSize.y];
        maze[apple.x][apple.y] = -1;

        int size = snake.body.size();
        int i = 0;
        for (Coordinate c : snake.body) {
            maze[c.x][c.y] = size - i;
            i++;
        }

        size = opponent.body.size();
        i = 0;
        for (Coordinate c : opponent.body) {
            maze[c.x][c.y] = size - i;
        }

        Coordinate cur = snake.getHead();
        i = 0;
//        if (maze[cur.x][cur.y - 1] == -1)

        return up;
    }

    private Direction[] safeDirections(Snake snake, Snake opponent, Coordinate mazeSize) {
        opponent.body.removeLast();
        snake.body.removeLast();

        return Arrays.stream(directions)
                .filter(direction -> snake.getHead().moveTo(direction).inBounds(mazeSize))
                .filter(direction -> !opponent.body.contains(snake.getHead().moveTo(direction)))
                .filter(direction -> !snake.body.contains(snake.getHead().moveTo(direction)))
                .toArray(Direction[]::new);
    }
}
