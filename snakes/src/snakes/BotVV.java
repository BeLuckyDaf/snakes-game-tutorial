// bot made by Vyacheslav Vasilev

package snakes;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class BotVV implements Bot {
    private static final Direction down = Direction.UP;
    private static final Direction up = Direction.DOWN;
    private static final Direction left = Direction.LEFT;
    private static final Direction right = Direction.RIGHT;
    private long start;

    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        start = System.nanoTime();

        Pair<Direction, Integer> myPath = pathfindBFS(snake, opponent, mazeSize, apple);
        Pair<Direction, Integer> opponentPath = pathfindBFS(opponent, snake, mazeSize, apple);

        //if no path found, go to the center
        if (myPath == null)
            return toCenter(snake, opponent, mazeSize, apple, opponentPath);

        //if no opponent path found, go for the apple
        if (opponentPath == null)
            return myPath.getKey();

        //if I will catch faster, go for the apple
        if (myPath.getValue() < opponentPath.getValue())
            return myPath.getKey();

        //if opponent catch faster, go to the center
        if (myPath.getValue() > opponentPath.getValue())
            return toCenter(snake, opponent, mazeSize, apple, opponentPath);

        //go to the apple if speed is the same & my snake is not shorter
        if (snake.elements.size() >= opponent.elements.size())
            return myPath.getKey();

        //else go to the center
        return toCenter(snake, opponent, mazeSize, apple, opponentPath);
    }

    private boolean wontDie(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple, Direction to) {
        Snake newSnake = snake.clone();
        if (apple.x == snake.getHead().x + to.dx && apple.y == snake.getHead().y + to.dy)
            newSnake.moveTo(to, true);
        else
            newSnake.moveTo(to, false);

        Snake newOp = opponent.clone();
        Coordinate head = newOp.getHead();
        Coordinate taken = newOp.body.pollFirst();
        Coordinate neck = newOp.body.peekFirst();
        newOp.body.addFirst(taken);

        if (apple.x == head.x + (head.x - neck.x) && apple.y == head.y + (head.y - neck.y))
            newOp.moveTo(to, true);
        else
            newOp.moveTo(to, false);

        return pathfindBFS(newSnake, newOp, mazeSize, snake.getHead()) != null;
    }

    private Direction toCenter(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple, Pair<Direction, Integer> opponentPath) {
        Pair<Direction, Integer> result = pathfindBFS(snake, opponent, mazeSize, new Coordinate(mazeSize.x / 2, mazeSize.y / 2));

        //if found a path to the center, go
        if (result != null && wontDie(snake, opponent, mazeSize, apple, result.getKey()))
            return result.getKey();

        Coordinate head = snake.getHead();
        Direction[] safeMoves = safeDirections(snake, opponent, mazeSize, apple);
        ArrayList<Direction> safestMoves = new ArrayList<>(safeMoves.length);

        for (Direction move: safeMoves) {
            if (wontDie(snake, opponent, mazeSize, apple, move))
                safestMoves.add(move);
        }
        if (safestMoves.size() == 0)
            safestMoves = new ArrayList<>(Arrays.asList(safeMoves));

        //if everywhere is death, commit suicide
        if (safestMoves.size() == 0)
            return up;

        //if already at the center, go random, but don't die
        if (head.x == mazeSize.x / 2 && head.y == mazeSize.y / 2)
            return safestMoves.get(0);

        Queue<Direction> bestMoves = new LinkedList<>();

        //if can't go to the center, just move towards
        if (Math.abs(head.x - mazeSize.x / 2) > Math.abs(head.y - mazeSize.y / 2)) {
            if (head.x < mazeSize.x / 2)
                bestMoves.add(right);
            else
                bestMoves.add(left);
        }
        if (head.y < mazeSize.y / 2)
            bestMoves.add(down);
        else
            bestMoves.add(up);

        //return best move if will not die
        if (arrContains(safestMoves, bestMoves.peek()))
            return bestMoves.peek();
        bestMoves.remove();

        //return second best move if will not die
        if (bestMoves.peek() != null && arrContains(safestMoves, bestMoves.peek()))
            return bestMoves.peek();

        //return move that will not kill me
        if (opponentPath.getValue() < Math.abs(head.y - mazeSize.y / 2) && safestMoves.size() > 1)
            return safestMoves.get(1);
        return safestMoves.get(0);
    }

    private boolean arrContains(ArrayList<Direction> arr, Direction dir) {
        for (Direction elem : arr) {
            if (dir == elem)
                return true;
        }
        return false;
    }

    private Pair<Direction, Integer> pathfindBFS(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        //create a matrix of cells. 0 is free, -1 is apple, n is the number of turns after which it will be free.
        int[][] maze = new int[mazeSize.x][mazeSize.y];
        maze[apple.x][apple.y] = -1;
        //create a matrix of flags. true is checked, false is not.
        Direction[][] mazeFrom = new Direction[mazeSize.x][mazeSize.y];

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
            i++;
        }

        Coordinate cur = snake.getHead();
        mazeFrom[cur.x][cur.y] = up;

        Queue<Coordinate> queue = new LinkedList<>();

        if (cur.y != 0 && maze[cur.x][cur.y - 1] < 2) {
            if (maze[cur.x][cur.y - 1] == -1)
                return new Pair<>(up, 1);
            queue.add(new Coordinate(cur.x, cur.y - 1));
            mazeFrom[cur.x][cur.y - 1] = up;
        }
        if (cur.x != mazeSize.x - 1 && maze[cur.x + 1][cur.y] < 2) {
            if (maze[cur.x + 1][cur.y] == -1)
                return new Pair<>(right, 1);
            queue.add(new Coordinate(cur.x + 1, cur.y));
            mazeFrom[cur.x + 1][cur.y] = right;
        }
        if (cur.y != mazeSize.y - 1 && maze[cur.x][cur.y + 1] < 2) {
            if (maze[cur.x][cur.y + 1] == -1)
                return new Pair<>(down, 1);
            queue.add(new Coordinate(cur.x, cur.y + 1));
            mazeFrom[cur.x][cur.y + 1] = down;
        }
        if (cur.x != 0 && maze[cur.x - 1][cur.y] < 2) {
            if (maze[cur.x - 1][cur.y] == -1)
                return new Pair<>(left, 1);
            queue.add(new Coordinate(cur.x - 1, cur.y));
            mazeFrom[cur.x - 1][cur.y] = left;
        }
        while (!queue.isEmpty()) {
            cur = queue.poll();
            i = manhattanDistance(cur, snake.getHead());

            //if too long calculations, return null
            if (System.nanoTime() - start > 800000000L) return null;

             if (cur.y != 0 && mazeFrom[cur.x][cur.y - 1] == null && maze[cur.x][cur.y - 1] - i < 2 ) {
                if (maze[cur.x][cur.y - 1] == -1)
                    return new Pair<>(mazeFrom[cur.x][cur.y], i + 1);
                mazeFrom[cur.x][cur.y - 1] = mazeFrom[cur.x][cur.y];
                queue.add(new Coordinate(cur.x, cur.y - 1));
            }
            if (cur.x != mazeSize.x - 1 && mazeFrom[cur.x + 1][cur.y] == null && maze[cur.x + 1][cur.y] - i < 2 ) {
                if (maze[cur.x + 1][cur.y] == -1)
                    return new Pair<>(mazeFrom[cur.x][cur.y], i + 1);
                mazeFrom[cur.x + 1][cur.y] = mazeFrom[cur.x][cur.y];
                queue.add(new Coordinate(cur.x + 1, cur.y));
            }
            if (cur.y != mazeSize.y - 1 && mazeFrom[cur.x][cur.y + 1] == null && maze[cur.x][cur.y + 1] - i < 2 ) {
                if (maze[cur.x][cur.y + 1] == -1)
                    return new Pair<>(mazeFrom[cur.x][cur.y], i + 1);
                mazeFrom[cur.x][cur.y + 1] = mazeFrom[cur.x][cur.y];
                queue.add(new Coordinate(cur.x, cur.y + 1));
            }
            if (cur.x != 0 && mazeFrom[cur.x - 1][cur.y] == null && maze[cur.x - 1][cur.y] - i < 2 ) {
                if (maze[cur.x - 1][cur.y] == -1)
                    return new Pair<>(mazeFrom[cur.x][cur.y], i + 1);
                mazeFrom[cur.x - 1][cur.y] = mazeFrom[cur.x][cur.y];
                queue.add(new Coordinate(cur.x - 1, cur.y));
            }
        }

        return null;
    }
    private Direction[] safeDirections(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        Snake mySnake = snake.clone();
        Snake opponentSnake = opponent.clone();
        Coordinate head = mySnake.getHead();

        if (manhattanDistance(opponentSnake.getHead(), apple) != 1)
            opponentSnake.body.removeLast();
        mySnake.body.removeLast();

        return Arrays.stream(Direction.values())
                .filter(direction -> head.moveTo(direction).inBounds(mazeSize))
                .filter(direction -> !opponentSnake.body.contains(head.moveTo(direction)))
                .filter(direction -> !mySnake.body.contains(head.moveTo(direction)))
                .toArray(Direction[]::new);
    }

    private int manhattanDistance(Coordinate a, Coordinate b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    //dank algorithm that looks like a dog hunting its tail
    /*private Direction tailSearch(Snake snake) {
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
    }*/
}
