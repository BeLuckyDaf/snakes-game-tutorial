package snakes;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.LinkedList;

public class Bot_user_control implements Bot {
    private final long TIME_FOR_TURN = 0; // time to press the button in millis (to slow down the game if needed)

    public class IsKeyPressed implements Runnable {
        public Direction get_direction() {
            synchronized (IsKeyPressed.class) {
                return last;
            }
        }

        public void run() {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent ke) {
                    synchronized (IsKeyPressed.class) {
                        switch (ke.getID()) {
                            case KeyEvent.KEY_PRESSED:
                                if (ke.getKeyCode() == KeyEvent.VK_UP) {
                                    if (last != Direction.UP)
                                        last = Direction.DOWN;
                                }
                                else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                                    if (last != Direction.DOWN)
                                        last = Direction.UP;
                                }
                                else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                                    if (last != Direction.RIGHT)
                                        last = Direction.LEFT;
                                }
                                else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                                    if (last != Direction.LEFT)
                                        last = Direction.RIGHT;
                                }
                                break;
                        }
                        return false;
                    }
                }
            });
        }
    }

    private Direction last;
    private Thread t = null;

    @Override
    public Direction chooseDirection(Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        long startTime = System.currentTimeMillis();

        if (last == null) {
            LinkedList<Coordinate> newBody = new LinkedList<>(snake.body);
            newBody.removeFirst();
            Coordinate neck = newBody.getFirst();
            for (Direction d : Direction.values()) {
                if (neck.moveTo(d).equals(snake.getHead())) {
                    last = d;
                }
            }
        }

        if (t == null) {
            IsKeyPressed ikp = new IsKeyPressed();
            t = new Thread(ikp);
            t.start();
        }

        IsKeyPressed ik = new IsKeyPressed();
        while (System.currentTimeMillis() - startTime <= TIME_FOR_TURN);
        //System.out.println(last);
        return ik.get_direction();
    }
}
