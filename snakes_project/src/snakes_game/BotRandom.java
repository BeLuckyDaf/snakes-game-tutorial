package snakes_game;

import java.util.ArrayList;
import java.util.Random;

public class BotRandom implements Bot {

    public Direction chooseDirection(final Snake mySnake, final Snake otherSnake, final int[][] maze){
        Coordinate x = mySnake.getHead();
        ArrayList<Coordinate> Arr = new ArrayList<Coordinate>();
        if(x.row > 0 && (mySnake.positions.get(1).row + 1 != x.row || mySnake.positions.get(1).column != x.column)){
            Arr.add(new Coordinate(x.row - 1, x.column));
        }
        if(x.row < Main.N - 1 && (mySnake.positions.get(1).row - 1 != x.row || mySnake.positions.get(1).column != x.column)){
            Arr.add(new Coordinate(x.row + 1, x.column));
        }
        if(x.column > 0 && (mySnake.positions.get(1).row != x.row || mySnake.positions.get(1).column + 1 != x.column)){
            Arr.add(new Coordinate(x.row, x.column - 1));
        }
        if(x.column < Main.M - 1 && (mySnake.positions.get(1).row != x.row || mySnake.positions.get(1).column - 1 != x.column)) {
            Arr.add(new Coordinate(x.row, x.column + 1));
        }

        Random r = new Random();

        Coordinate temp = Arr.get(r.nextInt(Arr.size()));
        if(temp.row + 1 == x.row) return Direction.NORTH;
        if(temp.row - 1 == x.row) return Direction.SOUTH;
        if(temp.column + 1 == x.column) return Direction.WEST;
        if(temp.column - 1 == x.column) return Direction.EAST;
        return Direction.NORTH;
    }
}
