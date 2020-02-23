package snakes;

/**
 * This enum class implements a direction vector
 * that shows staring coordinate and direction of movement
 * Size of step for movement is restricted to adjacent cells
 */
public enum Direction {
    UP(0, 1),
    DOWN(0, -1),
    RIGHT(1, 0),
    LEFT(-1, 0);

    public final int dx, dy;
    public final Coordinate v;
    /**
     * Construct the direction of movement
     *
     * @param dx row coordinate
     * @param dy column coordinate
     */
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
        this.v = new Coordinate(dx, dy);
    }
}
