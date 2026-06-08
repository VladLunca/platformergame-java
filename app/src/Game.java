import entity.EntityFactory;
import entity.enamy.Dragon;
import entity.enamy.Snake;
import gamewindow.GameWindow;
import graphics.assets.Assets;
import handle.KeyHandler;
import handle.MouseHandler;
import levelmanager.LevelManager;
import map.MapManager;
import screen.*;
import utils.GameStates;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.Objects;
import static gamewindow.GameWindow.createGameWindow;

public class Game implements Runnable {
    private GameWindow     wnd;
    private boolean        runState;
    private Thread         gameThread;
    private BufferStrategy bs;
    private Graphics       g;
    private static volatile Game instance;
    private static MapManager    mapManager;
    private static LevelManager  levelManager;

    private MenuScreen     menuScreen;
    private PlayingScreen  playingScreen;
    private PauseScreen    pauseScreen;
    private GameOverScreen gameOverScreen;
    private LevelWonScreen levelWonScreen;

    private Game(String title, int width, int height) throws IOException {
        wnd = createGameWindow(title, width, height);
        runState = false;
        EntityFactory.register("snake",  (x, y, h, p) -> new Snake(x, y, h, p));
        EntityFactory.register("dragon", (x, y, h, p) -> new Dragon(x, y, h));
        mapManager   = MapManager.createMapManager("/textures/maps/maps.json");
        levelManager = LevelManager.createLevelManager(mapManager);

        playingScreen  = new PlayingScreen(levelManager);
        menuScreen     = new MenuScreen(levelManager);
        pauseScreen    = new PauseScreen(playingScreen);
        gameOverScreen = new GameOverScreen(playingScreen, levelManager);
        levelWonScreen = new LevelWonScreen(playingScreen, levelManager);
    }

    public static Game createGame(String title, int width, int height) {
        return Objects.requireNonNullElseGet(instance, () -> {
            try {
                synchronized (Game.class) {
                    if (instance == null) {
                        instance = new Game(title, width, height);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return instance;
        });
    }

    private void InitGame() {
        wnd.BuildGameWindow();
        Assets.Init();
        MouseHandler.register(GameStates.MENU,      menuScreen::handleClick);
        MouseHandler.register(GameStates.PAUSE,     pauseScreen::handleClick);
        MouseHandler.register(GameStates.GAME_OVER, gameOverScreen::handleClick);
        MouseHandler.register(GameStates.LEVEL_WON, levelWonScreen::handleClick);
    }

    public synchronized void StartGame() {
        if (!runState) {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public synchronized void StopGame() {
        if (runState) {
            runState = false;
            try {
                gameThread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        InitGame();

        long oldTime = System.nanoTime();
        long currentTime;
        double delta = 0;

        final int ups_frame = 60;
        final int fps_limit = 60;
        double timePerUpdate = (double) 1000000000 / ups_frame;
        double timePerFrame  = (double) 2000000000 / fps_limit;

        long lastDrawTime = System.nanoTime();

        while (runState) {
            currentTime = System.nanoTime();
            delta += (currentTime - oldTime) / timePerUpdate;
            oldTime = currentTime;

            while (delta >= 1) {
                update();
                delta--;
            }

            if (System.nanoTime() - lastDrawTime >= timePerFrame) {
                Draw();
                lastDrawTime = System.nanoTime();
            }
        }
    }

    private void update() {
        switch (GameStates.current) {
            case PLAYING -> {
                if (KeyHandler.isEscape()) {
                    KeyHandler.resetEscape();
                    GameStates.current = GameStates.PAUSE;
                    break;
                }
                levelManager.update();
                if (levelManager.isPlayerDead()) {
                    GameStates.current = GameStates.GAME_OVER;
                } else if (levelManager.isLevelWon()) {
                    GameStates.current = GameStates.LEVEL_WON;
                }
            }
            case PAUSE -> {
                if (KeyHandler.isEscape()) {
                    KeyHandler.resetEscape();
                    GameStates.current = GameStates.PLAYING;
                }
            }
            default -> {}
        }
    }

    private void Draw() {
        bs = wnd.GetCanvas().getBufferStrategy();
        if (bs == null) {
            try {
                wnd.GetCanvas().createBufferStrategy(3);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        switch (GameStates.current) {
            case MENU      -> menuScreen.draw(g, wnd);
            case PLAYING   -> playingScreen.draw(g, wnd);
            case PAUSE     -> pauseScreen.draw(g, wnd);
            case GAME_OVER -> gameOverScreen.draw(g, wnd);
            case LEVEL_WON -> levelWonScreen.draw(g, wnd);
            default        -> {}
        }

        bs.show();
        g.dispose();
    }
}
