# Snake Game AI -- Your First Bot

Snakes AI has got a very simple interface for bot creation. The minimum you need to implement in order to make a functioning bot is a class, that implements the Bot interface.

## Set up the workspace

### Requirements

* [JDK 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
* [IntelliJ Idea](https://www.jetbrains.com/idea/) (optional, recommended)

### Clone the repository

Clone the repo using Git: `git clone git@github.com:BeLuckyDaf/snakes-game-tutorial.git`

### Open the project

Open *snakes.iml* in IntelliJ Idea or any other IDE.

### Launch the game

The sample bot is already set up in the project, simply compile and launch the game.

## Example bot

### Create a package

First, let's create a new package for you. Name it however you want, for the sake of this tutorial, we'll call it *student*.

### Create a class

Our bot class must implement the Bot interface, otherwise, we won't be able to tell the game what it does.

```java
package student

public class MyBot implements Bot {

}
```

That also means, that we must implement the following method, specified in the interface.

```java
public Direction chooseDirection(Snake snake,
                                 Snake opponent, 
                                 Coordinate mazeSize, 
                                 Coordinate apple)
```

In fact, this is the only method that we are required to implement in order to make our bot work, so let's make a simple bot, the only function of which would be not dying.

So our code now looks like this:
```java
package student

public class MyBot implements Bot {
    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        return null;
    }
}
```

### The bare minimum

The code above is not going to work yet, so at least for it to make sense and for the sake of simplicity, we will now make the snake always go upwards.

```java
@Override
public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
    return Direction.UP;
}
```

You can try it out now, see how to [run your bot](#Run-your-bot).

### Pure randomness

Now to be able to make some randomness we'll also define a list of all directions that we could possible go in as an array.

```java
private static final Direction[] DIRECTIONS = new Direction[] {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
```

And pick one random direction.

```java
package student

import java.util.Random;

public class MyBot implements Bot {
    private static final Direction[] DIRECTIONS = new Direction[] {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        Random random = new Random();
        Direction randomDir = DIRECTIONS[random.nextInt(DIRECTIONS.length)]
        return randomDir;
    }
}
```

See how to [run your bot](#Run-your-bot).

### More advanced logic

Now we have a randomly moving bot, but it's not enough, it'll never achieve anything by just randomly moving in different directions, moreover, the snake can't move on all four directions, since there is no way it would go backwards, let's dig into this.

To achieve a more advanced AI, first take look at what information do we possess:
* Our own snake
* The opponent snake
* The size of the maze
* The coordinate of the apple

It would help us if we knew where our own snake's head is located, so let's add that.

```java
Coordinate head = snake.getHead();
```

We can move in any direction, except going backwards, so we should find the coordinate of "backwards". We will just take the second coordinate from our snake list of body parts.

```java
Coordinate afterHeadNotFinal = null;
if (snake.body.size() >= 2) {
    Iterator<Coordinate> it = snake.body.iterator();
    it.next();
    afterHeadNotFinal = it.next();
}

final Coordinate afterHead = afterHeadNotFinal;
```

Now remove the backwards direction from the list of our possible moves.

```java
Direction[] validMoves = Arrays.stream(DIRECTIONS)
    .filter(d -> !head.moveTo(d).equals(afterHead))
    .sorted()
    .toArray(Direction[]::new);
```

Since our bot doesn't want to die, filter out all directions which might cause that.

```java
Direction[] notLosing = Arrays.stream(validMoves)
    .filter(d -> head.moveTo(d).inBounds(mazeSize))           // maze bounds
    .filter(d -> !opponent.elements.contains(head.moveTo(d))) // opponent body
    .filter(d -> !snake.elements.contains(head.moveTo(d)))    // and yourself
    .sorted()
    .toArray(Direction[]::new);
```

Now choose which move to take. We could add some randomness here, but it is not important now. So if we can move without losing, do it, otherwise, take any valid move that is not backwards, since there is no way to not lose.

```java
if (notLosing.length > 0) return notLosing[0];
else return validMoves[0];
```

### Final Code

So here is what we came up with, this is included with the repository, you find it as *johndoe.SampleBot*.

```java
package student

public class MyBot implements Bot {
    private static final Direction[] DIRECTIONS = new Direction[] {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};

    @Override
    /* choose the direction (stupidly) */
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        Coordinate head = snake.getHead();

        /* Get the coordinate of the second element of the snake's body
         * to prevent going backwards */
        Coordinate afterHeadNotFinal = null;
        if (snake.body.size() >= 2) {
            Iterator<Coordinate> it = snake.body.iterator();
            it.next();
            afterHeadNotFinal = it.next();
        }

        final Coordinate afterHead = afterHeadNotFinal;

        /* The only illegal move is going backwards. Here we are checking for not doing it */
        Direction[] validMoves = Arrays.stream(DIRECTIONS)
                .filter(d -> !head.moveTo(d).equals(afterHead)) // Filter out the backwards move
                .sorted()
                .toArray(Direction[]::new);

        /* Just naïve greedy algorithm that tries not to die at each moment in time */
        Direction[] notLosing = Arrays.stream(validMoves)
                .filter(d -> head.moveTo(d).inBounds(mazeSize))             // Don't leave maze
                .filter(d -> !opponent.elements.contains(head.moveTo(d)))   // Don't collide with opponent...
                .filter(d -> !snake.elements.contains(head.moveTo(d)))      // and yourself
                .sorted()
                .toArray(Direction[]::new);

        if (notLosing.length > 0) return notLosing[0];
        else return validMoves[0];
        /* ^^^ Cannot avoid losing here */
    }
}
```

### Run your bot

In order to use your own bot, you must pass your package name and the name of the class as program arguments to the game.

You must pass two bots, in order for the game to work, those could be the same.

#### Example

Let's use your newly written bot with the one, provided by us. Even though, they actually are the same.

`java snakes.SnakesUIMain johndoe.SampleBot student.MyBot`

If you are using any IDE, you could add those program arguments to be added automatically whenever you want to run or debug the program.

## What's next?

Try to make the bot go towards the apple, it's basically the point of the game, but remember that you are not the only snake on the field.

## Be creative!

We're going to let you out on a journey of bot creation now, good luck and have fun!

