package snakes;

/*
The class of the snake. It contains the coordinates of the snake body.
 */

import java.util.ArrayList;

public class Snake {
    boolean alive = true; // to indicate if the snake is dead
    ArrayList<Coordinate> positions = new ArrayList<Coordinate>(); /* this arraylist holds the coordinates of the every body cell of the snake
    the first cell is the head, and the cells are supposed to be ordered. The program will treat the order of the cells in this array list
    as the order of the body of the snake*/
    public Snake(int row, int column) { /* constructor of the snake, its size will be 3 at the beginning. first all of the body will be in the
         same spot then when the snake will more the body will spread out and it will never again reach a state where there is more than one body
         cell in the same place (except when the snake eats an apple, in that case there will be 2 cells with the same coordinates in the end of the
         array list)*/
        alive = true;
        Coordinate x = new Coordinate(row, column);
        positions.add(x);
        positions.add(x);
        positions.add(x);
    }
    public Coordinate getHead() { // gives back the place of the head
        Coordinate x = new Coordinate(positions.get(0).row, positions.get(0).column);
        return x;
    }
}
