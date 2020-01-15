package snakes_game;

/*
A class to create apples randomly in the maze whenever we call the method create()
 */

import java.util.Random;

class Apples {
    public static void create(){
        while(true) { // keep looping until you find an empty cell
            Random x = new Random();
            int ind1 = x.nextInt(Main.N);
            Random y = new Random();
            int ind2 = y.nextInt(Main.M);
            // ind1 and ind2 will be the X and Y for our apple
            if(Main.maze[ind1][ind2] == 0){
                // put the apple if the cell is empty. If the cell is not empty then the loop will repeat to get an empty cell
                Main.maze[ind1][ind2] = 10;
                break;
            }
        }
        return;
    }
}
