package snakes;

import javafx.util.Pair;

import java.util.*;

// Add TL
// collide with oppoonent's head if i'm winning
// implement hunt method
// add path to queue bfs class // DONE
// add order when put into the to queue according to Manhattan distance // DONE

public class Bot_D_Kabirov implements Bot {

//    private class queue_instance {
//        Coordinate cur;
//        //Coordinate from; // path is enough //
//        //int travel_time; // path is enough // size of path
//        Coordinate start;
//        HashSet<Coordinate> path;
//        public queue_instance(Coordinate cur, Coordinate start, HashSet<Coordinate> path) {
//            this.cur = cur;
//            this.start = start;
//            this.path = (HashSet<Coordinate>)(path.clone());
//        }
//    }


    private int HUNT_THRESHOLD = 10;
    private final int USED_THRESHOLD = 3;
    private HashSet[][] used;
    private int release[][];
    private Coordinate mazeSize;
    private Coordinate apple;
    private Coordinate found = null;
    private Snake snake;
    private Snake opponent;
    private LinkedList<ArrayList<Coordinate>> Q;

    private boolean dfs_used[][];
    private boolean bfs_used[][];
    private int dist[][];
    private int treshold_total = 0;
    private int hunt_used[][];
    private ArrayList<Coordinate> parent[][];
    private ArrayList<Coordinate> hunt_to[][];
    private LinkedList<Hunt_queue> hunt_Q;
    private LinkedList<Prep_hunt_queue> prep_hunt_Q;
    private Coordinate hunt_found = null;
    private long timeStart;

    class Prep_hunt_queue {
        Coordinate cur;
        int time_travel;
        Coordinate par; // delete??
        public Prep_hunt_queue(Coordinate cur, int time_travel, Coordinate par) {
            this.cur = cur;
            this.time_travel = time_travel;
            this.par = par;
        }
    }

    private void preparing_hunt_bfs(Coordinate cur, int time_travel, Coordinate par) {
        if (bfs_used[cur.x][cur.y]) {
                //if (dist[cur.x][cur.y] + 1 == dist[to.x][to.y]) {
            if (parent[cur.x][cur.y] == null)
                parent[cur.x][cur.y] = new ArrayList<>(); // no need actually
            parent[cur.x][cur.y].add(par);

            if (par != null) {
                if (hunt_to[par.x][par.y] == null)
                    hunt_to[par.x][par.y] = new ArrayList<>();
                hunt_to[par.x][par.y].add(cur);
            }

            return;
        }
        bfs_used[cur.x][cur.y] = true;
        dist[cur.x][cur.y] = time_travel;

        if (parent[cur.x][cur.y] == null)
            parent[cur.x][cur.y] = new ArrayList<>();
        parent[cur.x][cur.y].add(par);

        if (par != null) {
            if (hunt_to[par.x][par.y] == null)
                hunt_to[par.x][par.y] = new ArrayList<>();
            hunt_to[par.x][par.y].add(cur);
        }


        if (time_travel == HUNT_THRESHOLD) {
            treshold_total += 1;
            return;
        }

        for (Direction d : Direction.values()) {
            Coordinate to = cur.moveTo(d);
            if (to.inBounds(mazeSize) && release[to.x][to.y] <= time_travel + 1) {
                if (!bfs_used[to.x][to.y]) {
                    prep_hunt_Q.add(new Prep_hunt_queue(to, time_travel + 1, cur));
                }
            }
        }
    }

    private boolean path_to_root(Coordinate cur, HashSet<Coordinate> killed, Coordinate excl) {
        if (cur == null)
            return true;
        if (killed.contains(cur) || cur.equals(excl))
            return false;

        if (parent[cur.x][cur.y] != null) {
            for (Coordinate to : parent[cur.x][cur.y]) {
                if (path_to_root(to, killed, excl))
                    return true;
            }
        }
        return false;
    }

    private HashSet<Coordinate> tree_killed_used;

    private int tree_killed(Coordinate cur, HashSet<Coordinate> killed, Coordinate excl) {//can be improved, but difficult
        if (tree_killed_used.contains(cur))
            return 0;
        tree_killed_used.add(cur);
        if (killed.contains(cur))
            return 0;
        if (dist[cur.x][cur.y] == HUNT_THRESHOLD)
            return 1;
        int ans = 0;
        if (hunt_to[cur.x][cur.y] != null) {
            for (Coordinate to : hunt_to[cur.x][cur.y]) {
                if (!path_to_root(to, killed, excl))
                    ans += tree_killed(to, killed, excl);
            }
        }
        return ans;
    }

    class Hunt_queue {
        ArrayList<Coordinate> path;
        int tresholds_left;
        HashSet<Coordinate> killed;

        public Hunt_queue(ArrayList<Coordinate> path, int tresholds_left, HashSet<Coordinate> killed) {
            this.path = path;
            this.tresholds_left = tresholds_left;
            this.killed = killed;
        }
    }

    private void hunt_bfs(Coordinate cur, ArrayList<Coordinate> path, int left, HashSet<Coordinate> killed) {
        if (hunt_used[cur.x][cur.y] == 1)
            return;
        hunt_used[cur.x][cur.y] = 1;

        if (left <= 0) {
            //System.out.println("FOUND A WAY TO HUNT" + left);
            hunt_found = path.get(0);
            //System.out.println(hunt_found.x + " " + hunt_found.y);
            return;
        }

        ArrayList<Pair<Coordinate, Integer>> to_add = new ArrayList<Pair<Coordinate, Integer>>();
        int time_travel = path.size() + 1;
        for (Direction d : Direction.values()) {
            Coordinate to = cur.moveTo(d);
            if (to.inBounds(mazeSize) && release[to.x][to.y] <= time_travel) {
                int left_to = left;
                if (time_travel < dist[to.x][to.y] &&
                dist[to.x][to.y] - time_travel < snake.body.size() &&
                path_to_root(to, killed, null) && !killed.contains(to)) {
                    tree_killed_used.clear();
                    left_to -= tree_killed(to, killed, to);
                }
                if (hunt_used[to.x][to.y] == 0) {
                    to_add.add(new Pair<>(to, left_to));
                }
            }
        }

        to_add.sort((a, b) -> (a.getValue() - b.getValue()));
        for (int i = 0; i < to_add.size(); i++) {
            Coordinate to = to_add.get(i).getKey();
            int to_left = to_add.get(i).getValue();
            HashSet<Coordinate> to_killed = (HashSet<Coordinate>)(killed.clone());
            ArrayList<Coordinate> to_path = (ArrayList<Coordinate>)(path.clone());
            to_path.add(to);
            if (to_left < left) {// killed
                to_killed.add(to);
            }

            hunt_Q.add(new Hunt_queue(to_path, to_left, to_killed));
        }
    }

    // if dist==1 don't go
    private Direction hunt() { // boolean?
        //release should be set

        hunt_found = null;
        treshold_total = 0;


        prep_hunt_Q = new LinkedList<>();
        prep_hunt_Q.add(new Prep_hunt_queue(opponent.getHead(), 0, null));
        //fill the Q
        while(!prep_hunt_Q.isEmpty()) {
            Prep_hunt_queue cur = prep_hunt_Q.getFirst();
            prep_hunt_Q.removeFirst();
            preparing_hunt_bfs(cur.cur, cur.time_travel, cur.par);
        }


        if (treshold_total == 0) {
            // Follow the tail?
            //System.out.println("Now I should follow the tail");
            return null;
        }
        else {
            //System.out.println("total: " + treshold_total);

            for (int i = 0; i < mazeSize.x; i++)
                for(int j = 0; j < mazeSize.y; j++)
                    hunt_used[i][j] = 0; // infinity

            hunt_Q = new LinkedList<>();
            for (Direction d : Direction.values()) {
                Coordinate to = snake.getHead().moveTo(d);
                if (to.inBounds(mazeSize) && release[to.x][to.y] <= 1) {
                    ArrayList<Coordinate> to_list = new ArrayList<>();
                    to_list.add(to);
                    int cnt_killed = 0;
                    HashSet<Coordinate> killed = new HashSet<>();
                    // check if killed
                    if (1 < dist[to.x][to.y] &&
                    dist[to.x][to.y] - 1 < snake.body.size()) {
                        tree_killed_used.clear();
                        cnt_killed += tree_killed(to, killed, to);
                        //System.out.println(to.x + " " + to.y + " : " + cnt_killed);
                        killed.add(to);
                    }
                    hunt_Q.add(new Hunt_queue(to_list, treshold_total - cnt_killed, killed));
                }
            }

            while (!hunt_Q.isEmpty() && hunt_found == null) {
                Hunt_queue cur = hunt_Q.getFirst();
                hunt_Q.removeFirst();
                //System.out.println(cur.path.get(cur.path.size() - 1).x + " " + cur.path.get(cur.path.size() - 1).y + " " + cur.tresholds_left);
                hunt_bfs(cur.path.get(cur.path.size() - 1), cur.path, cur.tresholds_left, cur.killed);
            }

            if (hunt_found != null) {


//                System.out.println("release:");
//                for (int y = mazeSize.y - 1; y >= 0; y--) {
//                    for (int x = 0; x < mazeSize.x; x++) {
//                        System.out.print(release[x][y] + " ");
//                    }
//                    System.out.println();
//                }
//
//                System.out.println("\ndist:");
//                for (int y = mazeSize.y - 1; y >= 0; y--) {
//                    for (int x = 0; x < mazeSize.x; x++) {
//                        System.out.print(dist[x][y] + " ");
//                    }
//                    System.out.println();
//                }

                //System.out.println("HUNTING!!!");
                for (Direction d : Direction.values()) {
                    if (snake.getHead().moveTo(d).equals(hunt_found)) {
                        return d;
                    }
                }
                //System.out.println("Impossible stuff\n");
                return null;
            }
            else {
               // System.out.println("No hunting(");
                return null;
            }
        }
    }

    private void bfs(Coordinate cur, ArrayList<Coordinate> path) {
        if (System.currentTimeMillis() - this.timeStart >= 950) {
            return;
        }
        //System.out.println(cur.x + " " + cur.y);
        if (cur.equals(apple)) {
            found = path.get(0);
            return;
        }
        if (used[cur.x][cur.y].size() >= USED_THRESHOLD || used[cur.x][cur.y].contains(path.size()))
            return;

        used[cur.x][cur.y].add(path.size());

        for (Direction d : Direction.values()) {
            Coordinate to = cur.moveTo(d);
            ArrayList<Coordinate> to_add = new ArrayList<>();
            int indof = path.indexOf(to);
            if (to.inBounds(mazeSize) &&
                    (indof == -1 || snake.body.size() <= (path.size() - indof)) &&
                    path.size() + 1 >= release[to.x][to.y] &&
                    !used[to.x][to.y].contains(path.size()) &&
                    used[to.x][to.y].size() < USED_THRESHOLD) {
                to_add.add(to);
            }

            to_add.sort(Comparator.comparingInt(a -> Manhattan(a, apple)));
            for (int i = 0; i < to_add.size(); i++) {
                ArrayList<Coordinate> to_list = (ArrayList<Coordinate>)(path.clone());
                to_list.add(to_add.get(i));
                Q.add(to_list);
            }
        }
    }

    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        this.timeStart = System.currentTimeMillis();
        this.mazeSize = mazeSize;
        this.apple = apple;
        this.found = null;
        this.snake = snake;
        this.opponent = opponent;
        this.dfs_used = new boolean[mazeSize.x + 1][mazeSize.y + 1];
        this.bfs_used = new boolean[mazeSize.x + 1][mazeSize.y + 1];
        this.dist = new int[mazeSize.x + 1][mazeSize.y + 1];
      //  this.count = new int[mazeSize.x + 1][mazeSize.y + 1];
        this.parent = new ArrayList[mazeSize.x + 1][mazeSize.y + 1];
        this.hunt_to = new ArrayList[mazeSize.x + 1][mazeSize.y + 1];
        this.hunt_used = new int[mazeSize.x + 1][mazeSize.y + 1];
        this.HUNT_THRESHOLD = Math.min(opponent.body.size() + 1, 10);
        this.tree_killed_used = new HashSet<>();

        used = new HashSet[mazeSize.x][mazeSize.y];
        release = new int[mazeSize.x + 5][mazeSize.y + 5];

        for (int i = 0; i < mazeSize.x; i++)
            for (int j = 0; j < mazeSize.y; j++)
                used[i][j] = new HashSet();


        int time = snake.body.size();
        for (Coordinate c : snake.body) {
            release[c.x][c.y] = time;
            time -= 1;
        }

        int delt = 0;
        for (Direction d : Direction.values()) {
            Coordinate to = opponent.getHead().moveTo(d);
            if (to.equals(apple)) // if the opponent will grow now
                delt = 1;
        }

        time = opponent.body.size() + delt;
        for (Coordinate c : opponent.body) {
            release[c.x][c.y] = time;
            time -= 1;
        }

        Direction hunt_dir = hunt();

        if (hunt_dir != null) {
            return hunt_dir;
        }

        if (opponent.elements.size() >= snake.elements.size()) { // remove = if you agree on a draw
            for (Direction d : Direction.values()) {
                Coordinate to = opponent.getHead().moveTo(d);
                if (to.inBounds(mazeSize)) {
                    release[to.x][to.y] = opponent.body.size() + 1;
                }
            }
        }


        Q = new LinkedList<>();
        Coordinate head = snake.getHead();
        for (Direction d : Direction.values()) {
            Coordinate to = head.moveTo(d);
            if (to.inBounds(mazeSize) && release[to.x][to.y] <= 1) {
                ArrayList<Coordinate> to_list = new ArrayList<>();
                to_list.add(to);
                Q.add(to_list);
            }
        }

        while (!Q.isEmpty() && found == null) {
            ArrayList<Coordinate> cur_path = Q.getFirst();
            Q.removeFirst();
            bfs(cur_path.get(cur_path.size() - 1), cur_path);
        }

        if (found != null) {
            for (Direction d : Direction.values()) {
                if (head.moveTo(d).equals(found)) {
                    return d;
                }
            }
            //System.out.println(found.x + " " + found.y);
        }
        else { // CHECKED TILL NOW
            //System.out.println("No path to apple");
            for (Direction d : Direction.values()) {
                Coordinate to = head.moveTo(d);
                if (to.inBounds(mazeSize) && release[to.x][to.y] <= 1) {
                    return d;
                }
            }

            // can add here one more bfs without a fear of colliding

            // If no way, try to collide with other snake's head
            for (Direction d : Direction.values()) {
                Coordinate to = head.moveTo(d);
                if (to.inBounds(mazeSize) && !snake.elements.contains(to) && !opponent.elements.contains(to)) {
                    return d;
                }
            }

            //System.out.println("Let's die");
            Random rnd = new Random();
            return Direction.values()[rnd.nextInt(Direction.values().length)];
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



    private static int Manhattan(Coordinate a, Coordinate b) {
        return Math.abs(b.x - a.x) + Math.abs(b.y - a.y);
    }
}
