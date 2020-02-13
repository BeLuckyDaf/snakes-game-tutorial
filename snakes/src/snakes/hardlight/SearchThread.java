package snakes.hardlight;

import snakes.Coordinate;
import snakes.Snake;

import java.util.LinkedList;

public class SearchThread implements Runnable {
    private final Snake snake;
    private final Coordinate mazeSize;
    private final Coordinate apple;
    private volatile LinkedList<Coordinate> path = null;

    public SearchThread(Snake snake, Coordinate mazeSize, Coordinate apple) {
        this.snake = snake;
        this.mazeSize = mazeSize;
        this.apple = apple;
    }

    @Override
    public void run() {
        final PathSearcher searcher = PathSearcher.getPathSearcher(mazeSize);
        path = searcher.findPath(snake, apple);
    }

    public LinkedList<Coordinate> getPath() {
        return path;
    }
}
