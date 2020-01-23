package snakes;

public interface Bot {
    /* Signature was tweaked to match my implementation of game. */
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple);
}

