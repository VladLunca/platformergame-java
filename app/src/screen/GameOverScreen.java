package screen;

import gamewindow.GameWindow;
import graphics.assets.Assets;
import levelmanager.LevelManager;
import utils.GameStates;

import java.awt.*;
import java.awt.event.MouseEvent;

public class GameOverScreen implements Screen {

    private final Rectangle retryBtn = new Rectangle(ScreenHelper.BTN_X, 415, ScreenHelper.BTN_W, ScreenHelper.BTN_H);
    private final Rectangle menuBtn  = new Rectangle(ScreenHelper.BTN_X, 485, ScreenHelper.BTN_W, ScreenHelper.BTN_H);

    private final PlayingScreen playingScreen;
    private final LevelManager  levelManager;

    public GameOverScreen(PlayingScreen playingScreen, LevelManager levelManager) {
        this.playingScreen = playingScreen;
        this.levelManager  = levelManager;
    }

    @Override
    public void draw(Graphics g, GameWindow wnd) {
        playingScreen.draw(g, wnd);
        g.drawImage(Assets.get("uiGameOver")[0], ScreenHelper.UI_X, ScreenHelper.UI_Y, ScreenHelper.UI_W, ScreenHelper.UI_H, null);
        ScreenHelper.drawButton(g, retryBtn, "RETRY");
        ScreenHelper.drawButton(g, menuBtn,  "MAIN MENU");
    }

    @Override
    public void handleClick(MouseEvent e) {
        Point p = e.getPoint();
        if (retryBtn.contains(p)) {
            levelManager.resetLives();
            levelManager.loadLevel();
            GameStates.current = GameStates.PLAYING;
        } else if (menuBtn.contains(p)) {
            GameStates.current = GameStates.MENU;
        }
    }
}
