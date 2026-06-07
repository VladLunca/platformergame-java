package gamewindow;

import handle.KeyHandler;
import handle.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class GameWindow
{
    private static volatile GameWindow instance;
    private JFrame  wndFrame;
    private final String  wndTitle;
    private final int     wndWidth;
    private final int     wndHeight;
    private  Canvas  canvas;

    private GameWindow(String title, int width, int height){
        wndTitle    = title;
        wndWidth    = width;
        wndHeight   = height;
        wndFrame    = null;
        
    }
    public static GameWindow createGameWindow(String title, int width, int height){
        return Objects.requireNonNullElseGet(instance, () -> {
            synchronized (GameWindow.class) {
                if (instance == null) {
                    instance = new GameWindow(title, width, height);
                }
            }
            return instance;
        });
    }
    public void BuildGameWindow()
    {
        if(wndFrame != null)
        {
            return;
        }
        wndFrame = new JFrame(wndTitle);
        wndFrame.setSize(wndWidth, wndHeight);
        wndFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wndFrame.setResizable(false);
        wndFrame.setLocationRelativeTo(null);
        wndFrame.setVisible(true);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(wndWidth, wndHeight));
        canvas.setMaximumSize(new Dimension(wndWidth, wndHeight));
        canvas.setMinimumSize(new Dimension(wndWidth, wndHeight));
        canvas.setFocusable(false);
        wndFrame.add(canvas);
        wndFrame.setFocusable(true);
        wndFrame.pack();

        KeyHandler keyHandler = new KeyHandler();
        wndFrame.addKeyListener(keyHandler);

        MouseHandler mouseHandler = new MouseHandler();
        canvas.addMouseListener(mouseHandler);
        wndFrame.setFocusable(true);
        wndFrame.requestFocus();         // ← frame-ul primește focus automat

        wndFrame.setVisible(true);
    }

    public  int GetWndWidth()
    {
        return wndWidth;
    }

    public  int GetWndHeight()
    {
        return wndHeight;
    }

    public  Canvas GetCanvas() {
        return canvas;
    }
}
