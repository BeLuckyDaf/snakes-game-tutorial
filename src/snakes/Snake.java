package snakes;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * This class implements snake body (the brain is your bot) that
 * determines the place of head and body, length of body, etc on the game board
 */
public class Snake implements Cloneable {
    public final HashSet<Coordinate> elements; // To quickly check intersections
    public final Deque<Coordinate> body; // Instead of ArrayList; this is more efficient and logical
    // Those two contain same values, but for different purposes

    public final Coordinate mazeSize;

    /**
     * Construct a snake with the body that defined in elements HashSet
     *
     * @param mazeSize size of the board
     * @param elements Coordinates that define body elements of the snake
     * @param body     Coordinates that define body elements of the snake
     */
    private Snake(Coordinate mazeSize, HashSet<Coordinate> elements, Deque<Coordinate> body) {
        this.mazeSize = mazeSize;
        this.elements = elements;
        this.body = body;
    }


    /**
     * Initialize snake with length 1
     *
     * @param initialHead coordinate of initial position of snake
     * @param mazeSize    size of the board
     */
    public Snake(Coordinate initialHead, Coordinate mazeSize) {
        this(mazeSize, new HashSet<Coordinate>(), new LinkedList<Coordinate>());

        body.addFirst(initialHead);
        elements.add(initialHead);
    }


    /**
     * Construct snake with predefined length
     *
     * @param head          An initial coordinate of snake's head
     * @param tailDirection An initial direction of the snake's tail in which direction snake should grow
     * @param size          A size of snake after expansion
     * @param mazeSize      size of the board
     */
    public Snake(Coordinate head, Direction tailDirection, int size, Coordinate mazeSize) {
        this(head, mazeSize);

        Coordinate p = head.moveTo(tailDirection);
        for (int i = 0; i < size - 1; i++) {
            body.addLast(p);
            elements.add(p);
            p = p.moveTo(tailDirection);
        }
    }

    /**
     * Get head position
     *
     * @return Coordinates with place of snake's head
     */
    public Coordinate getHead() {
        return body.getFirst();
    }


    /**
     * Move snake in direction
     *
     * @param d    direction where should snake crawl
     * @param grow True - if snake eat an apple
     * @return False - if collides with itself or maze bounds
     */
    public boolean moveTo(Direction d, boolean grow) {
        Coordinate newHead = getHead().moveTo(d);

        if (!newHead.inBounds(mazeSize))
            return false; // Left maze

        if (elements.contains(newHead))
            return false; // Collided with itself

        if (!grow)
            elements.remove(body.removeLast());
        body.addFirst(newHead);
        elements.add(newHead);

        return true;
    }

    /**
     * Check whether head collides with another snake
     *
     * @param other Snake body of opponent's snake
     * @return True - if collides with another snake
     */
    public boolean headCollidesWith(Snake other) {
        return other.elements.contains(getHead());
    }

    /**
     * Clone snake
     *
     * @return copy of current snake
     */
    @Override
    public Snake clone() {
        HashSet<Coordinate> newElements = new HashSet<>(elements);
        LinkedList<Coordinate> newBody = new LinkedList<>(body);
        return new Snake(mazeSize, newElements, newBody);
    }
}
