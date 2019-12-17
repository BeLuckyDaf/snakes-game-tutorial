/*
So this is the main class where everything starts
 */

package snakes_game;

import java.util.Timer;

public class Main {
    // some decelerations
    public static int N = 8, M = 8; // the dimensions for the maze where the snakes play
    public static int[][] maze = new int[N][M]; // the actual maze where we will play
    public static Snake snake0 = new Snake(2, 2); // first snake creation (go to class Snake to see how the snakes are implemented). I have put the first snake in cell (2, 2)
    public static Snake snake1 = new Snake(5, 5); // second snake creation. I have put the second snake in cell (5, 5)

    /*
    note: what is written on the output and what is present in the array maze is different.
    If a cell has a number 0 in it, it means that this cell is empty
    If a cell has a number 10 in it, it means that there's an apple in this cell
    If a cell has a number 2 in it, it means that the head of the first snake is here
    If a cell has a number 1 in it, it means that the body (one cell of it) of the first snake is here
    If a cell has a number 4 in it, it means that the head of the second snake is here
    If a cell has a number 3 in it, it means that the body (one cell of it) of the second snake is here
    */


    public static void main(String args[]) {
        maze[2][2] = 2; // the number 2 is the head of the first snake, this line of code marks the place where the first snake will start
        maze[5][5] = 4; // the same for the second snake, but using the number 4 for the head
        Timer timer = new Timer(); // creating a timer so I can put a delay between every output iteration (Not 100% sure what this line is doing to be honest I saw on the internet)
        Apples.create(); // calling the method "create" in class "Apples" to create the an apple in the game (go to the class to see its implementation)
        int period = 1000;
        timer.schedule(new moh(), 0, period); // I got this on the internet. I know that this function will call the method "moh" every "period" millisecond
    }
}