package snakes;

/**
 * This interface provides functions that should be implemented
 * to create smart snake bot for the game
 */
public interface Bot {
    /**
     * Smart snake bot (brain of your snake) should choose step (direction where to go)
     * on each game step until the end of game
     *
     * @param snake    Your snake's body with coordinates for each segment
     * @param opponent Opponent snake's body with coordinates for each segme
     * @param mazeSize Size of the board
     * @param apple    Coordinate of an apple
     * @return Direction in which snake should crawl next game step
     */
    Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple);
}

