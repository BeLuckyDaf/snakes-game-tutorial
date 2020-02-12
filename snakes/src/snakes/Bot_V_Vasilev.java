// bot made by Vyacheslav Vasilev

package snakes;

import java.util.*;

public class Bot_V_Vasilev implements Bot {
    private static final Direction down = Direction.UP;
    private static final Direction up = Direction.DOWN;
    private static final Direction left = Direction.LEFT;
    private static final Direction right = Direction.RIGHT;
    private static final Direction[] directions = new Direction[]{up, down, left, right};
    private static long start;

    class Pair {
        Direction direction;
        int steps;

        Pair(Direction direction, int steps) {
            this.direction = direction;
            this.steps = steps;
        }
    }

    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        start = System.nanoTime();

        Pair result = pathfind_BFS(snake, opponent, mazeSize, apple);

        //if no path found, go to the center
        if (result == null)
            return to_center(snake, opponent, mazeSize, apple);

        Direction myMove = result.direction;
        int mySteps = result.steps;

        result = pathfind_BFS(opponent, snake, mazeSize, apple);
        //if no opponent path found, go for the apple
        if (result == null)
            return myMove;

        Direction opponentMove = result.direction;
        int opponentSteps = result.steps;

        //if I will catch faster, go for the apple
        if (mySteps < opponentSteps)
            return myMove;
        //if opponent catch faster, go to the center
        if (mySteps > opponentSteps)
            return to_center(snake, opponent, mazeSize, apple);

        //go to the apple if speed is the same & my snake is not shorter
        if (snake.elements.size() >= opponent.elements.size())
            return myMove;
        //else go to the center
        return to_center(snake, opponent, mazeSize, apple);
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

    private Direction to_center(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        Pair result = pathfind_BFS(snake, opponent, mazeSize, new Coordinate(mazeSize.x / 2, mazeSize.y / 2));
        if (result != null) return result.direction;

        Coordinate head = snake.getHead();
        Direction[] safeMoves = safeDirections(snake, opponent, mazeSize, apple);
        Direction[] bestMoves = new Direction[4];
        short i = 0;

        //if can't go to the center, just move towards
        if (Math.abs(head.x - mazeSize.x / 2) > Math.abs(head.y - mazeSize.y / 2)) {
            if (head.x < mazeSize.x / 2) {
                bestMoves[i] = right;
                i++;
            }
            else {
                bestMoves[i] = left;
                i++;
            }
        }
        if (head.y < mazeSize.y / 2)
            bestMoves[i] = down;
        else
            bestMoves[i] = up;

        //return best move if will not die
        if (arr_contains(safeMoves, bestMoves[0]))
            return bestMoves[0];
        //return second best move if will not die
        if (arr_contains(safeMoves, bestMoves[1]))
            return bestMoves[0];
        //return any move that will not kill me, null if it doesn't exist
        if (safeMoves[0] == null)
            return up;
        return safeMoves[0];
    }

    private boolean arr_contains(Direction[] arr, Direction dir) {
        for (Direction elem : arr) {
            if (dir == elem)
                return true;
        }
        return false;
    }

    private Pair pathfind_BFS(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        //create a matrix of cells. 0 is free, -1 is apple, n is the number of turns after which it will be free.
        int[][] maze = new int[mazeSize.x][mazeSize.y];
        maze[apple.x][apple.y] = -1;
        //create a matrix of flags. true is checked, false is not.
        Direction[][] maze_from = new Direction[mazeSize.x][mazeSize.y];

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
        maze_from[cur.x][cur.y] = up;

        LinkedList<Coordinate> queue = new LinkedList<>();

        if (cur.y != 0 && maze[cur.x][cur.y - 1] < 2) {
            if (maze[cur.x][cur.y - 1] == -1)
                return new Pair(up, 1);
            queue.addLast(new Coordinate(cur.x, cur.y - 1));
            maze_from[cur.x][cur.y - 1] = up;
        }
        if (cur.x != mazeSize.x - 1 && maze[cur.x + 1][cur.y] < 2) {
            if (maze[cur.x + 1][cur.y] == -1)
                return new Pair(right, 1);
            queue.addLast(new Coordinate(cur.x + 1, cur.y));
            maze_from[cur.x + 1][cur.y] = right;
        }
        if (cur.y != mazeSize.y - 1 && maze[cur.x][cur.y + 1] < 2) {
            if (maze[cur.x][cur.y + 1] == -1)
                return new Pair(down, 1);
            queue.addLast(new Coordinate(cur.x, cur.y + 1));
            maze_from[cur.x][cur.y + 1] = down;
        }
        if (cur.x != 0 && maze[cur.x - 1][cur.y] < 2) {
            if (maze[cur.x - 1][cur.y] == -1)
                return new Pair(left, 1);
            queue.addLast(new Coordinate(cur.x - 1, cur.y));
            maze_from[cur.x - 1][cur.y] = left;
        }
        while (!queue.isEmpty()) {
            cur = queue.getFirst();
            queue.removeFirst();
            i = manhattan_distance(cur, snake.getHead());

//            print(maze_from, maze);

//            if too long calculations, return null
            if (System.nanoTime() - start > 400000000L) return null;

             if (cur.y != 0 && maze_from[cur.x][cur.y - 1] == null && maze[cur.x][cur.y - 1] - i < 2 ) {
                if (maze[cur.x][cur.y - 1] == -1)
                    return new Pair(maze_from[cur.x][cur.y], i + 1);
                maze_from[cur.x][cur.y - 1] = maze_from[cur.x][cur.y];
                queue.addLast(new Coordinate(cur.x, cur.y - 1));
            }
            if (cur.x != mazeSize.x - 1 && maze_from[cur.x + 1][cur.y] == null && maze[cur.x + 1][cur.y] - i < 2 ) {
                if (maze[cur.x + 1][cur.y] == -1)
                    return new Pair(maze_from[cur.x][cur.y], i + 1);
                maze_from[cur.x + 1][cur.y] = maze_from[cur.x][cur.y];
                queue.addLast(new Coordinate(cur.x + 1, cur.y));
            }
            if (cur.y != mazeSize.y - 1 && maze_from[cur.x][cur.y + 1] == null && maze[cur.x][cur.y + 1] - i < 2 ) {
                if (maze[cur.x][cur.y + 1] == -1)
                    return new Pair(maze_from[cur.x][cur.y], i + 1);
                maze_from[cur.x][cur.y + 1] = maze_from[cur.x][cur.y];
                queue.addLast(new Coordinate(cur.x, cur.y + 1));
            }
            if (cur.x != 0 && maze_from[cur.x - 1][cur.y] == null && maze[cur.x - 1][cur.y] - i < 2 ) {
                if (maze[cur.x - 1][cur.y] == -1)
                    return new Pair(maze_from[cur.x][cur.y], i + 1);
                maze_from[cur.x - 1][cur.y] = maze_from[cur.x][cur.y];
                queue.addLast(new Coordinate(cur.x - 1, cur.y));
            }
        }

        //if path is not found, return null
        return null;
    }

    private void print (Direction[][] maze_from, int[][] maze) {
        System.out.print("\n");
        for (int i = 0; i < maze_from.length; i++) {
            for (int k = 0; k < maze_from[i].length; k++) {
                if (maze_from[k][i] == up)
                    System.out.print("[u]");
                else if (maze_from[k][i] == down)
                    System.out.print("[d]");
                else if (maze_from[k][i] == left)
                    System.out.print("[l]");
                else if (maze_from[k][i] == right)
                    System.out.print("[r]");
                else if (maze[k][i] == -1)
                    System.out.print("[a]");
                else
                    System.out.print("[ ]");
            }
            System.out.print("   ");
            for (int k = 0; k < maze[i].length; k++) {
                if (maze[k][i] == -1) {
                    System.out.print("[a]");
                    continue;
                }
                System.out.print("[" + maze[k][i] + "]");
            }

            System.out.print("\n");
        }
        System.out.print("\n");
    }

    private Direction[] safeDirections(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        Snake mySnake = snake.clone();
        Snake opponentSnake = opponent.clone();
        Coordinate head = mySnake.getHead();

        if (manhattan_distance(opponentSnake.getHead(), apple) != 1)
            opponentSnake.body.removeLast();
        mySnake.body.removeLast();

        return Arrays.stream(directions)
                .filter(direction -> head.moveTo(direction).inBounds(mazeSize))
                .filter(direction -> !opponentSnake.body.contains(head.moveTo(direction)))
                .filter(direction -> !mySnake.body.contains(head.moveTo(direction)))
                .toArray(Direction[]::new);
    }

    private int manhattan_distance(Coordinate a, Coordinate b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
}
