package screen;

import gamewindow.GameWindow;
import graphics.assets.Assets;
import levelmanager.LevelManager;
import utils.GameStates;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MenuScreen implements Screen {

    private final Rectangle playBtn = new Rectangle(ScreenHelper.BTN_X, 415, ScreenHelper.BTN_W, ScreenHelper.BTN_H);
    private final Rectangle quitBtn = new Rectangle(ScreenHelper.BTN_X, 485, ScreenHelper.BTN_W, ScreenHelper.BTN_H);

    private final LevelManager levelManager;

    public MenuScreen(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    @Override
    public void draw(Graphics g, GameWindow wnd) {
        int w = wnd.GetWndWidth();
        int h = wnd.GetWndHeight();
        g.setColor(new Color(15, 20, 45));
        g.fillRect(0, 0, w, h);
        g.drawImage(Assets.get("uiMenu")[0], ScreenHelper.UI_X, ScreenHelper.UI_Y, ScreenHelper.UI_W, ScreenHelper.UI_H, null);
        g.setFont(new Font("Arial", Font.BOLD, 52));
        FontMetrics fm = g.getFontMetrics();
        g.setColor(new Color(80, 40, 10));
        String title = "PLATFORMER";
        g.drawString(title, (w - fm.stringWidth(title)) / 2, 235);
        ScreenHelper.drawButton(g, playBtn, "PLAY");
        ScreenHelper.drawButton(g, quitBtn, "QUIT");
    }

    @Override
    public void handleClick(MouseEvent e) {
        Point p = e.getPoint();
        if (playBtn.contains(p)) {
            levelManager.setLevel(1);
            levelManager.resetLives();
            levelManager.loadLevel();
            GameStates.current = GameStates.PLAYING;
        } else if (quitBtn.contains(p)) {
            System.exit(0);
        }
    }
}
