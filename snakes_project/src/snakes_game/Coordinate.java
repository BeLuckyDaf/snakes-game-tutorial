package snakes_game;

/*
This class is the same from the lab but I have made the compareTo function work randomly so that every game will be different.
 */

import java.util.concurrent.ThreadLocalRandom;

public class Coordinate implements Comparable<Coordinate>{
    int row;
    int column;
    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }
    @Override
    public int compareTo(Coordinate other) {
        return ThreadLocalRandom.current().nextInt(-1, 2);
    }
}
