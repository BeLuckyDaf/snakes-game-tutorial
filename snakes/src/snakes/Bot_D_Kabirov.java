package snakes;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Bot_D_Kabirov implements Bot{
    private final int USED_TRESHOLD = 2;
    private ArrayList<ArrayList<HashSet<Integer>>> used;
    private int release[][];
    private static final Direction[] DIRECTIONS = new Direction[] {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
    private Coordinate mazeSize;
    private Coordinate apple;
    private Coordinate found = null;
    private LinkedList<Pair<Pair<Coordinate, Coordinate>, Pair<Integer, Coordinate>>> Q;

    private void bfs(Coordinate cur, Coordinate from, int travel_time, Coordinate start) {
        //System.out.println(cur.x + " " + cur.y);
        if (cur.equals(apple)) {
            found = start;
            return;
        }
        if (used.get(cur.x).get(cur.y).size() >= USED_TRESHOLD)
            return;

        used.get(cur.x).get(cur.y).add(travel_time);

        for (Direction d : DIRECTIONS) {
            Coordinate to = cur.moveTo(d);
//            System.out.println(to.inBounds(mazeSize));
//            System.out.println(!to.equals(from));
//            System.out.println(travel_time + 1 >= release[to.x][to.y]);
//            System.out.println(used.get(to.x).get(to.y).size() < USED_TRESHOLD);
            if (to.inBounds(mazeSize) &&
                    !to.equals(from) &&
                    travel_time + 1 >= release[to.x][to.y] &&
                    used.get(to.x).get(to.y).size() < USED_TRESHOLD) {
                Q.add(new Pair<>(new Pair<>(to, cur), new Pair<>(travel_time + 1, start)));
            }
        }
    }

    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        this.mazeSize = mazeSize;
        this.apple = apple;
        this.found = null;
        used = new ArrayList<>();
        release = new int[mazeSize.x + 5][mazeSize.y + 5];

        used.clear();

        for (int i = 0; i < mazeSize.x; i++) {
            used.add(new ArrayList<>());
            for (int j = 0; j < mazeSize.y; j++)
                used.get(i).add(new HashSet<>());
        }


        int time = snake.body.size() + 1; // + 1 should be removed!
        for (Coordinate c : snake.body) {
            release[c.x][c.y] = time;
            time -= 1;
        }


        int delt = 0;


        for (Direction d : DIRECTIONS) {
            Coordinate to = opponent.getHead().moveTo(d);
            if (to.inBounds(mazeSize)) {
                if (opponent.elements.size() >= snake.elements.size()) { // remove = if you agree on a draw
                    release[to.x][to.y] = opponent.body.size() + 1;
                }
                if (to.equals(apple)) // if the opponent will grow now
                    delt = 1;
            }
        }

        time = opponent.body.size() + delt;
        for (Coordinate c : opponent.body) {
            release[c.x][c.y] = time;
            time -= 1;
        }


        Q = new LinkedList<>();
        Coordinate head = snake.getHead();
        for (Direction d : DIRECTIONS) {
            Coordinate to = head.moveTo(d);
            if (to.inBounds(mazeSize) && release[to.x][to.y] <= 1) {
                Q.add(new Pair<>(new Pair<>(to, head), new Pair<>(1, to)));
            }
        }

        while (!Q.isEmpty() && found == null) {
            Pair<Pair<Coordinate, Coordinate>, Pair<Integer, Coordinate>> cur = Q.getFirst();
            Q.removeFirst();
            bfs(cur.getKey().getKey(), cur.getKey().getValue(), cur.getValue().getKey(), cur.getValue().getValue());
        }

        if (found != null) {
            for (Direction d : DIRECTIONS) {
                if (head.moveTo(d).equals(found)) {
                    return d;
                }
            }
            //System.out.println(found.x + " " + found.y);
        }
        else {
            //System.out.println("No path to apple");
            for (Direction d : DIRECTIONS) {
                Coordinate to = head.moveTo(d);
                if (to.inBounds(mazeSize) && release[to.x][to.y] <= 1) {
                    return d;
                }
            }

            for (Direction d : DIRECTIONS) {
                Coordinate to = head.moveTo(d);
                if (to.inBounds(mazeSize) && !snake.elements.contains(to) && !opponent.elements.contains(to)) {
                    return d;
                }
            }

            //System.out.println("Let's die");
            Random rnd = new Random();
            return DIRECTIONS[rnd.nextInt(DIRECTIONS.length)];
        }


//        for (int i = 0; i < mazeSize.y; i++) {
//            for (int j = 0; j < mazeSize.x; j++) {
//                System.out.print(release[j][mazeSize.y - i - 1]);
//                System.out.print(" ");
//            }
//            System.out.println();
//        }

       // System.out.println("Impossible move");

        return Direction.LEFT;
    }
}
