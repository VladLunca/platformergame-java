package handle;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private static boolean jump;
    private static boolean moveLeft;
    private static boolean moveRight;
    private static boolean attack;
    private static boolean escape;
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode =e.getKeyCode();
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            attack = true;
        }
        if(keyCode == KeyEvent.VK_ESCAPE)
        {
            escape = true;
        }

        if(keyCode == KeyEvent.VK_A)
        {
            moveLeft = true;
        }
        else if(keyCode == KeyEvent.VK_D)
        {
            moveRight = true;
        }else  if(keyCode == KeyEvent.VK_W)
        {
           jump = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode =e.getKeyCode();
        if(keyCode == KeyEvent.VK_A)
        {
            moveLeft = false;
        }
        else if(keyCode == KeyEvent.VK_D)
        {
            moveRight = false;
        }else  if(keyCode == KeyEvent.VK_W)
        {
            jump = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            attack = false;
        }
        if(keyCode == KeyEvent.VK_ESCAPE)
        {
            escape = false;
        }
    }

    public static boolean isJump() {
        return jump;
    }

    public static boolean isMoveLeft() {
        return moveLeft;
    }

    public static boolean isAttack() {
        return attack;
    }

    public static boolean isMoveRight() {
        return moveRight;
    }

    public static boolean isEscape() {
        return escape;
    }

    public static void resetEscape() {
        escape = false;
    }
}
