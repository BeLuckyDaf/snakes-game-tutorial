package snakes.hardlight;

import snakes.Bot;
import snakes.Coordinate;
import snakes.Direction;
import snakes.Snake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class BotHardlight implements Bot {

    private int getSize(Snake snake) {
        return snake.body.size();
    }

    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        try {
            SearchThread[] searchThreads = new SearchThread[]{
                    new SearchThread(snake, mazeSize, apple),
                    new SearchThread(opponent, mazeSize, apple)};

            Thread[] threads = Arrays.stream(searchThreads)
                    .map(Thread::new)
                    .peek(Thread::start)
                    .toArray(Thread[]::new);

            int WAITING_TIME = 900;
            for (Thread thread : threads)
                thread.join(WAITING_TIME);

            for (Thread thread : threads)
                if (thread.isAlive())
                    thread.interrupt();

            LinkedList<Coordinate> myPath = searchThreads[0].getPath();
            LinkedList<Coordinate> opponentPath = searchThreads[1].getPath();

            boolean iFaster = myPath.size() < opponentPath.size();
            boolean equalChances = myPath.size() == opponentPath.size();
            boolean iHaveNotLessPoints = getSize(snake) >= getSize(opponent);
            boolean goToApple = iFaster || equalChances && iHaveNotLessPoints;

            if (goToApple)
                return snake.getHead().getDirection(myPath.getFirst());
        } catch (InterruptedException | NullPointerException ignore) {
        }
        return PathSearcher.getPathSearcher(mazeSize).getFreeDirection(snake, opponent);
    }
}
