package screen;

import gamewindow.GameWindow;
import graphics.assets.Assets;
import levelmanager.LevelManager;
import utils.GameStates;

import java.awt.*;
import java.awt.event.MouseEvent;

public class LevelWonScreen implements Screen {

    private final Rectangle nextBtn = new Rectangle(ScreenHelper.BTN_X, 415, ScreenHelper.BTN_W, ScreenHelper.BTN_H);
    private final Rectangle menuBtn = new Rectangle(ScreenHelper.BTN_X, 485, ScreenHelper.BTN_W, ScreenHelper.BTN_H);

    private final PlayingScreen playingScreen;
    private final LevelManager  levelManager;

    public LevelWonScreen(PlayingScreen playingScreen, LevelManager levelManager) {
        this.playingScreen = playingScreen;
        this.levelManager  = levelManager;
    }

    @Override
    public void draw(Graphics g, GameWindow wnd) {
        playingScreen.draw(g, wnd);
        boolean isLastLevel = levelManager.getLevel() >= 3;
        String key = isLastLevel ? "uiGameWon" : "uiLevelComplete";
        g.drawImage(Assets.get(key)[0], ScreenHelper.UI_X, ScreenHelper.UI_Y, ScreenHelper.UI_W, ScreenHelper.UI_H, null);
        if (!isLastLevel) ScreenHelper.drawButton(g, nextBtn, "NEXT LEVEL");
        ScreenHelper.drawButton(g, menuBtn, "MAIN MENU");
    }

    @Override
    public void handleClick(MouseEvent e) {
        Point p = e.getPoint();
        boolean isLastLevel = levelManager.getLevel() >= 3;
        if (!isLastLevel && nextBtn.contains(p)) {
            levelManager.gainLife();
            levelManager.nextLevel();
            levelManager.loadLevel();
            GameStates.current = GameStates.PLAYING;
        } else if (menuBtn.contains(p)) {
            GameStates.current = GameStates.MENU;
        }
    }
}
