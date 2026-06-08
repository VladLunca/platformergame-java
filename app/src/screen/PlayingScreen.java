package screen;

import gamewindow.GameWindow;
import handle.KeyHandler;
import levelmanager.LevelManager;

import java.awt.*;

public class PlayingScreen implements Screen {

    private final LevelManager levelManager;

    public PlayingScreen(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @Override
    public void draw(Graphics g, GameWindow wnd) {
        g.setColor(new Color(0, 140, 220));
        g.fillRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());
        levelManager.draw(g, wnd, KeyHandler.isDebug());
    }
}
