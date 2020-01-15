package snakes_game;

/*
Interface from moodle and enum Direction from moodle
 */

enum Direction {
    NORTH, SOUTH, WEST, EAST;
}

public interface Bot {
    public abstract Direction chooseDirection(final Snake mySnake, final Snake otherSnake, final int[][] maze);
}