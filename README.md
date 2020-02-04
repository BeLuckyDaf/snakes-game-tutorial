# snakes_game
Snakes Game framework

Implement Bot interface to construct bot
```
public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple);
```
### Description of classes
+ Direction is a class with predefined move directions. Bot's task is to return one of 4 constants: Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT 

Example:
```
return Direction.UP
```
