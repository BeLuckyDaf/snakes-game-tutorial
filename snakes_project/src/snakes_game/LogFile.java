package snakes_game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


/**
 * This class contains methods to print certain information to a file.
 * You should create a LogFile instance to create a file.
 * In this way the program creates a file or erases information if this file already exists.
 * You must call the "closeFile()" method when you will no longer write information to a file.
 */
public class LogFile {
    private PrintWriter file;

    public LogFile(String fileName) {
        try {
            file = new PrintWriter(new File(fileName), StandardCharsets.US_ASCII.toString());
        } catch (FileNotFoundException e) {
            System.out.println("Non Existing directory for a file");
        } catch (UnsupportedEncodingException e) {
            System.out.println("The charset is not supported");
        }
    }

    /**
     * Call this method when you will no longer write information to a file.
     */
    public void closeFile() {
        file.close();
    }

    /**
     * Prints information about snake size, head position and apple position
     */
    public void logBoardStatus() {
        // Prints in this way:
        /* 'Size of snake 0' TAB 'Size of snake 1' TAB 'Head coordinates of snake 0' TAB
            'Head coordinates of snake 1' TAB 'Apple coordinates'
         */

        StringBuffer stringToPrint = new StringBuffer(); // we can append any information to this variable

        stringToPrint.append(Main.snake0.positions.size()); // append the size of snake1
        stringToPrint.append("\t" + Main.snake1.positions.size()); // append the size of snake2
        stringToPrint.append("\t(" + Main.snake0.getHead().row + "; " + Main.snake0.getHead().column + ")"); // append the snake0 head coordinates
        stringToPrint.append("\t(" + Main.snake1.getHead().row + "; " + Main.snake1.getHead().column + ")"); // append the snake1 head coordinates

        Coordinate apple = getAppleCoordinates(Main.maze); // get an apple coordinates
        if (apple != null) {
            stringToPrint.append("\t(" + apple.row + "; " + apple.column + ")"); // append the apple coordinates if it exists
        }

        file.println(stringToPrint.toString()); // prints information to a file
    }

    /**
     * Prints to a file in which direction snakes are going to move
     */
    public void logMoves(Direction[] moves) {
        file.println(moves[0] + " " + moves[1]);
    }

    /**
     * Prints the maze to a file
     */
    public void logMaze() {

        // I copied and pasted it from moh.showmaze() method
        for (int r = 0; r < Main.N; r++) {
            for (int c = 0; c < Main.M; c++) {
                if (Main.maze[r][c] == 0) {
                    file.print(".");  // empty cell
                } else {
                    if (Main.snake0.getHead().row == r && Main.snake0.getHead().column == c && !Main.snake0.alive) {
                        // draw a collision in the place of the head of the first snake if it is dead
                        file.print("*"); // "*" is for collision.
                    } else if (Main.snake1.getHead().row == r && Main.snake1.getHead().column == c && !Main.snake1.alive) {
                        // same but for the second snake
                        file.print("*");
                    } else {
                        if (Main.maze[r][c] == 10) {
                            file.print("#"); // print a "#" for an apple
                        } else {
                            file.print(Main.maze[r][c]);
                        }
                    }
                }
            }
            file.println();
        }
        file.println();
    }

    /**
     * Prints any text to a file
     */
    public void logText(String text) {
        file.println(text);
    }

    /**
     * Find the apple coordinates according to the maze.
     * I'm not sure that this method should be placed in this class. It's better to replace it somewhere.
     *
     * @return apple coordinates
     */
    private Coordinate getAppleCoordinates(int[][] maze) {
        Coordinate apple = null;
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 10) {
                    apple = new Coordinate(i, j);
                    break;
                }
            }
        }

        return apple;
    }
}
