package snakes;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;

public class Snake implements Cloneable {
    public final HashSet<Coordinate> elements; // To quickly check intersections
    public final Deque<Coordinate> body; // Instead of ArrayList; this is more efficient and logical
    // Those two contain same values, but for different purposes

    public final Coordinate mazeSize;

    /* Construct emptySnake */
    private Snake(Coordinate mazeSize, HashSet<Coordinate> elements, Deque<Coordinate> body) {
        this.mazeSize = mazeSize;
        this.elements = elements;
        this.body = body;
    }

    /* construct snake */
    public Snake(Coordinate initialHead, Coordinate mazeSize) {
        this(mazeSize, new HashSet<Coordinate>(), new LinkedList<Coordinate>());

        body.addFirst(initialHead);
        elements.add(initialHead);
    }

    /* construct snake */
    public Snake(Coordinate head, Direction tailDirection, int size, Coordinate mazeSize) {
        this(head, mazeSize);

        var p = head.moveTo(tailDirection);
        for (int i = 0; i < size - 1; i++) {
            body.addLast(p);
            elements.add(p);
            p = p.moveTo(tailDirection);
        }
    }

    /* get head position */
    public Coordinate getHead() {
        return body.getFirst();
    }

    /* move in direction */
    public boolean moveTo(Direction d, boolean grow) {
        var newHead = getHead().moveTo(d);

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

    /* check whether head collides with other snake */
    public boolean headCollidesWith(Snake other) {
        return other.elements.contains(getHead());
    }

    /* clone snake */
    @Override
    public Snake clone()
    {
        var newElements = new HashSet<>(elements);
        var newBody = new LinkedList<>(body);
        return new Snake(mazeSize, newElements, newBody);
    }
}
