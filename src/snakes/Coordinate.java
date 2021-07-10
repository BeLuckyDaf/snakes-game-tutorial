package snakes;

import java.util.Objects;

/**
 * Implements coordinate of a cell on the game boar
 */
public class Coordinate implements Comparable<Coordinate> {
    public final int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Add two coordinates of two points
     *
     * @param a first coordinate
     * @param b second coordinate
     * @return result of summation of coordinates
     */
    public static Coordinate add(Coordinate a, Coordinate b) {
        return new Coordinate(a.x + b.x, a.y + b.y);
    }


    /**
     * Move coordinate in direction
     *
     * @param d the direction in which coordinate should be moved
     * @return a moved coordinate
     */
    public Coordinate moveTo(Direction d) {
        return add(this, d.v);
    }

    /**
     * Get direction from current point to other point
     * @param other point to move
     * @return direction
     */
    public Direction getDirection(Coordinate other) {
        final Coordinate vector = new Coordinate(other.x - this.x, other.y - this.y);
        for (Direction direction : Direction.values())
            if (direction.dx == vector.x && direction.dy == vector.y)
                return direction;
        return null;
    }

    /**
     * Check whether coordinate is in bounds of board
     *
     * @param mazeSize size of the game board
     * @return True - if coordinate in bounds
     */
    public boolean inBounds(Coordinate mazeSize) {
        return x >= 0 && y >= 0 && x < mazeSize.x && y < mazeSize.y;
    }


    /**
     * Check whether objects are equal - coordinates in particular
     *
     * @param o an object that should be compared
     * @return True - if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate coordinate = (Coordinate) o;
        return x == coordinate.x &&
                y == coordinate.y;
    }


    /**
     * Compute hash code for coordinates
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }


    /**
     * Compare to other coordinate
     *
     * @param o coordinate
     * @return comparison result
     */
    @Override
    public int compareTo(Coordinate o) {
        int dx = Integer.compare(x, o.x);
        int dy = Integer.compare(y, o.y);
        if (dx == 0)
            return dy;
        else
            return dx;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

}
