package screen;

import gamewindow.GameWindow;
import graphics.assets.Assets;
import utils.GameStates;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PauseScreen implements Screen {

    private final Rectangle resumeBtn = new Rectangle(ScreenHelper.BTN_X, 415, ScreenHelper.BTN_W, ScreenHelper.BTN_H);
    private final Rectangle menuBtn   = new Rectangle(ScreenHelper.BTN_X, 485, ScreenHelper.BTN_W, ScreenHelper.BTN_H);

    private final PlayingScreen playingScreen;

    public PauseScreen(PlayingScreen playingScreen) {
        this.playingScreen = playingScreen;
    }

    @Override
    public void draw(Graphics g, GameWindow wnd) {
        playingScreen.draw(g, wnd);
        g.drawImage(Assets.get("uiPause")[0], ScreenHelper.UI_X, ScreenHelper.UI_Y, ScreenHelper.UI_W, ScreenHelper.UI_H, null);
        ScreenHelper.drawButton(g, resumeBtn, "RESUME");
        ScreenHelper.drawButton(g, menuBtn,   "MAIN MENU");
    }

    @Override
    public void handleClick(MouseEvent e) {
        Point p = e.getPoint();
        if (resumeBtn.contains(p)) {
            GameStates.current = GameStates.PLAYING;
        } else if (menuBtn.contains(p)) {
            GameStates.current = GameStates.MENU;
        }
    }
}
