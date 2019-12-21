package snakes_game;

/*
The most important class in the game. It calls the Bot to choose the moves for the snakes and then moves the snakes. It has a helper function validate move to check if
a snake is dead after a move. It has a function called show maze to draw the maze after every move.
 */

import java.util.TimerTask;

public class moh extends TimerTask{
    Bot_m_darwish Mahmood = new Bot_m_darwish(); // creating the bot that will control the snakes
    public void run() { // this method will either end the game if a snake is dead or will move the snakes if they both are alive
        if (Main.snake0.alive && Main.snake1.alive) {
            showmaze(); // draw the maze for output
            boolean bol = false; // if this variable is true in the end of this function that means a snake has eaten an apple and we need to create another apple
            Direction moves[] = {Mahmood.chooseDirection(Main.snake0, Main.snake1, Main.maze), Mahmood.chooseDirection(Main.snake1, Main.snake0, Main.maze)};
            /*
            "Direction" is an enum present in the interface Bot, for now you can treat it as a string that can only take the values "North", "East", "West", and "South"
            the array moves will have 2 elements, the first one is the move for the first snake and the second is the move for the second snake.
            to understand how it is done we call the function "chooseDirection" in the bot Mahmood. Go to class Bot_m_darwish for implementation details
             */

            Main.file.logBoardStatus(); // Prints information about snakes, apple and etc.
            Main.file.logMoves(moves); // appends current moves to the file
            Main.file.logMaze(); // print the maze to a file

            boolean validMove1 = validateMove(moves[1], Main.snake1); // this boolean variable will be false if this move will kill snake1
            boolean validMove0 = validateMove(moves[0], Main.snake0); // same here for snake0
            if(!validMove0 || !validMove1) {
                // the rest of the code assumes that the snakes did a valid move. If they didn't then cancel this iteration and kill the snake that did a wrong move
                Main.snake0.alive = validMove0;
                Main.snake1.alive = validMove1;
                return;
            }
            Direction s = moves[0];
            Coordinate x = new Coordinate (Main.snake0.getHead().row, Main.snake0.getHead().column);
            // x is now the coordinate of the head of snake0 before the move
            if (s == Direction.NORTH) {
                x.row--;
            }
            if (s == Direction.SOUTH) {
                x.row++;
            }
            if (s == Direction.EAST) {
                x.column++;
            }
            if (s == Direction.WEST) {
                x.column--;
            }
            // x is now the coordinate of the head of snake0 after the move
            if(Main.maze[x.row][x.column] == 10) { // it means that snake0 ate an apple
                Main.snake0.positions.add(Main.snake0.positions.get(Main.snake0.positions.size() - 1));  /* add to the body of snake0
                the position of the tail. This will mean that currently there are two body cells that have the same coordinates but
                when the snake will move, one of them will stay in its place while the other will move. That will allow the snake to grow.
                */
                bol = true; // the apple is eaten create another one
            }
            else{
                Main.maze[Main.snake0.positions.get(Main.snake0.positions.size() - 1).row][Main.snake0.positions.get(Main.snake0.positions.size() - 1).column] = 0;
                // it means that the cell where the tail of snake0 was present is now clear
            }
            for(int i = Main.snake0.positions.size() - 1 ; i > 0 ; i--){
                Main.snake0.positions.set(i, Main.snake0.positions.get(i - 1));
                /*
                make every body cell of snake0 (except the head) equal to the cell in front of it.
                cell 4 will take the coordinate of cell 3. Cell 3 will take the coordinates of cell 2. etc.
                when this is done the first and second cells will have the same coordinate.
                 */
            }
            Main.snake0.positions.set(0, x); // now we change the coordinates of the head to its new coordinates

            /********************************/
            /* the coordinates of the snakes are present in the arrayLists in their classes but not present on the maze.
            in this part of the code I put the body and head of the first snake in the maze. You don't really need to know how it works.
            you just need to know what it does.
             */
            int tem = 2;
            Coordinate bef = new Coordinate(-1, -1);
            for (Coordinate i : Main.snake0.positions) {
                if (i.row == bef.row && i.column == bef.column) break;
                Main.maze[i.row][i.column] = tem;
                if(tem == 2) tem--;
                bef.row = i.row;
                bef.column = i.column;
            }
            /*******************************/


            if (!validMove0) {
                Main.snake0.alive = false;
            }


            /********************************/
            // in this part of the code I do the same things I did for the first snake. But here I do it for the second snake
            s = moves[1];
            x = new Coordinate (Main.snake1.getHead().row, Main.snake1.getHead().column);
            if (s == Direction.NORTH) {
                x.row--;
            }
            if (s == Direction.SOUTH) {
                x.row++;
            }
            if (s == Direction.EAST) {
                x.column++;
            }
            if (s == Direction.WEST) {
                x.column--;
            }
            if(Main.maze[x.row][x.column] == 10) {
                Main.snake1.positions.add(Main.snake1.positions.get(Main.snake1.positions.size() - 1));
                bol = true;
            }
            else{
                Main.maze[Main.snake1.positions.get(Main.snake1.positions.size() - 1).row][Main.snake1.positions.get(Main.snake1.positions.size() - 1).column] = 0;
            }
            for(int i = Main.snake1.positions.size() - 1 ; i > 0 ; i--){
                Main.snake1.positions.set(i, Main.snake1.positions.get(i - 1));
            }
            Main.snake1.positions.set(0, x);
            tem = 4;
            bef = new Coordinate(-1, -1);
            for (Coordinate i : Main.snake1.positions) {
                if (i.row == bef.row && i.column == bef.column) break;
                Main.maze[i.row][i.column] = tem;
                if(tem == 4) tem--;
                bef.row = i.row;
                bef.column = i.column;
            }
            if (!validMove1) {
                Main.snake1.alive = false;
            }
            /**************************/


            if (Main.snake0.getHead().row == Main.snake1.getHead().row && Main.snake0.getHead().column == Main.snake1.getHead().column) {
                // the draw case. Both heads are now in the same cell.
                Main.snake0.alive = false;
                Main.snake1.alive = false;
            }
            if(bol){
                Apples.create();
            }
        }
        else {
            // if a snake at least is dead then draw the maze and give the result
            showmaze();
            //Main.file.logMaze(); // print the maze to a file

            if (!Main.snake0.alive && !Main.snake1.alive) {
                System.out.println("0-0");
                Main.file.logText("0-0");
            }
            else if (Main.snake0.alive && !Main.snake1.alive) {
                System.out.println("1-0");
                Main.file.logText("1-0");
            }
            else if (!Main.snake0.alive && Main.snake1.alive) {
                System.out.println("0-1");
                Main.file.logText("0-1");
            }
            Main.file.closeFile(); // close the file
            System.exit(0); // turn the program off
        }
    }

    private static boolean validateMove(Direction move, Snake snake) {
        /*
        if we use the first parameter as the direction used on the snake in the second parameter then
        this function will return true if the snake will live or false if it will die. It will ignore the case where the snakes both move to
        the same cell and then they both die because their heads collided.
         */

        // the next 4 if statements will check if the snake has fallen out of the maze
        if (move == Direction.NORTH && snake.getHead().row == 0)
            return false;
        if (move == Direction.SOUTH && snake.getHead().row == Main.N - 1)
            return false;
        if (move == Direction.WEST && snake.getHead().column == 0)
            return false;
        if (move == Direction.EAST && snake.getHead().column == Main.M - 1)
            return false;

        Coordinate x = snake.getHead();
        // after the next 4 if statements "x" will be the coordinates of the snake's head after it does the move
        if (move == Direction.NORTH) {
            x.row--;
        }
        if (move == Direction.SOUTH) {
            x.row++;
        }
        if (move == Direction.EAST) {
            x.column++;
        }
        if (move == Direction.WEST) {
            x.column--;
        }

        if (Main.maze[x.row][x.column] == 0 || Main.maze[x.row][x.column] == 10) // if the cell is empty or has an apple then the snake won't die
            return true;
        if (x == Main.snake1.positions.get(Main.snake1.positions.size() - 1) || x == Main.snake0.positions.get(Main.snake0.positions.size() - 1))
            /*
                if the head of this snake will take the place of the tail of this snake or the other snake then it is ok, because
                when the the head of our snake moves both snakes will move and that will clear the place where the tails of our
                snake and the other snake are.
            */
            return true;
        return false;
    }

    private static void showmaze() {
        // just draw the maze
        for (int r = 0; r < Main.N; r++) {
            for (int c = 0; c < Main.M ; c++) {
                if (Main.maze[r][c] == 0)
                    System.out.print("."); // empty cell
                else {
                    if (Main.snake0.getHead().row == r && Main.snake0.getHead().column == c && !Main.snake0.alive) {
                        // draw a collision in the place of the head of the first snake if it is dead
                        System.out.print("*"); // "*" is for collision.
                    }
                    else if (Main.snake1.getHead().row == r && Main.snake1.getHead().column == c && !Main.snake1.alive) {
                        // same but for the second snake
                        System.out.print("*");
                    }
                    else{
                        if (Main.maze[r][c] == 10) System.out.print("#"); // print a "#" for an apple
                        else System.out.print(Main.maze[r][c]);
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
