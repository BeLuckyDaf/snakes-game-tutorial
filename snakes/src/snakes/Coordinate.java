package snakes;

import java.util.Objects;

public class Coordinate implements Comparable<Coordinate> {
    public final int x, y;
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /* add two coordinates */
    public static Coordinate add(Coordinate a, Coordinate b) {
        return new Coordinate(a.x + b.x, a.y + b.y);
    }

    /* move in direction */
    public Coordinate moveTo(Direction d) {
        return add(this, d.v);
    }

    /* check whether coordinate is in bounds */
    public boolean inBounds(Coordinate mazeSize) {
        return x >= 0 && y >= 0 && x < mazeSize.x && y < mazeSize.y;
    }

    /* check whether objects are equal */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate coordinate = (Coordinate) o;
        return x == coordinate.x &&
                y == coordinate.y;
    }

    /* compute hash code */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /* compare to other coordinate */
    @Override
    public int compareTo(Coordinate o) {
        int dx = Integer.compare(x, o.x);
        int dy = Integer.compare(y, o.y);
        if (dx == 0)
            return dy;
        else
            return dx;
    }
}
