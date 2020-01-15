package snakes_game;

import java.util.ArrayList;
import java.util.Collections;

/*
This class chooses the next move for MySnake. It puts the possible moves in an arraylist and then sorts it randomly and chooses the first move.
 */

class Bot_m_darwish implements Bot{
    public Direction chooseDirection(final Snake mySnake, final Snake otherSnake, final int[][] maze){
        Coordinate x = mySnake.getHead();
        ArrayList<Coordinate> Arr = new ArrayList<Coordinate>();
        if(x.row > 0 && (mySnake.positions.get(1).row + 1 != x.row || mySnake.positions.get(1).column != x.column)){
            Arr.add(new Coordinate(x.row - 1, x.column));
        }
        if(x.row < Main.N && (mySnake.positions.get(1).row - 1 != x.row || mySnake.positions.get(1).column != x.column)){
            Arr.add(new Coordinate(x.row + 1, x.column));
        }
        if(x.column > 0 && (mySnake.positions.get(1).row != x.row || mySnake.positions.get(1).column + 1 != x.column)){
            Arr.add(new Coordinate(x.row, x.column - 1));
        }
        if(x.column < Main.M && (mySnake.positions.get(1).row != x.row || mySnake.positions.get(1).column - 1 != x.column)) {
            Arr.add(new Coordinate(x.row, x.column + 1));
        }
        Collections.sort(Arr);
        Coordinate temp = Arr.get(0);
        if(temp.row + 1 == x.row) return Direction.NORTH;
        if(temp.row - 1 == x.row) return Direction.SOUTH;
        if(temp.column + 1 == x.column) return Direction.WEST;
        if(temp.column - 1 == x.column) return Direction.EAST;
        return Direction.NORTH;
    }
}
