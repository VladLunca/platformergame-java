import gamewindow.GameWindow;
import graphics.assets.Assets;
import handle.MouseHandler;
import levelmanager.LevelManager;
import map.MapManager;
import utils.GameStates;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.Objects;
import static gamewindow.GameWindow.createGameWindow;

public class Game implements Runnable {
    private GameWindow       wnd;
    private boolean          runState;
    private Thread           gameThread;
    private BufferStrategy   bs;
    private Graphics         g;
    private static volatile Game instance;
    private static MapManager    mapManager;
    private static LevelManager  levelManager;

    private Game(String title, int width, int height) throws IOException {
        wnd = createGameWindow(title, width, height);
        runState = false;
        mapManager   = MapManager.createMapManager("/textures/maps/maps.json");
        levelManager = LevelManager.createLevelManager(mapManager);
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
        MouseHandler.register(GameStates.MENU,      this::handleMenuClick);
        MouseHandler.register(GameStates.PAUSE,     this::handlePauseClick);
        MouseHandler.register(GameStates.GAME_OVER, this::handleGameOverClick);
        MouseHandler.register(GameStates.LEVEL_WON, this::handleLevelWonClick);
    }

    // ── mouse click handlers per state ──────────────────────────────────────

    private void handleMenuClick(java.awt.event.MouseEvent e) {
        java.awt.Point p = e.getPoint();
        if (playBtn().contains(p)) {
            levelManager.setLevel(1);
            levelManager.resetLives();
            levelManager.loadLevel();
            GameStates.current = GameStates.PLAYING;
        } else if (quitBtn().contains(p)) {
            System.exit(0);
        }
    }

    private void handlePauseClick(java.awt.event.MouseEvent e) {
        java.awt.Point p = e.getPoint();
        if (resumeBtn().contains(p)) {
            GameStates.current = GameStates.PLAYING;
        } else if (menuBtn().contains(p)) {
            GameStates.current = GameStates.MENU;
        }
    }

    private void handleGameOverClick(java.awt.event.MouseEvent e) {
        java.awt.Point p = e.getPoint();
        if (retryBtn().contains(p)) {
            levelManager.resetLives();
            levelManager.loadLevel();
            GameStates.current = GameStates.PLAYING;
        } else if (menuBtn().contains(p)) {
            GameStates.current = GameStates.MENU;
        }
    }

    private void handleLevelWonClick(java.awt.event.MouseEvent e) {
        java.awt.Point p = e.getPoint();
        boolean isLastLevel = levelManager.getLevel() >= 3;
        if (!isLastLevel && nextBtn().contains(p)) {
            levelManager.gainLife();
            levelManager.nextLevel();
            levelManager.loadLevel();
            GameStates.current = GameStates.PLAYING;
        } else if (menuBtn().contains(p)) {
            GameStates.current = GameStates.MENU;
        }
    }

    // ── button rectangles ────────────────────────────────────────────────────

    private static final int BTN_X = 520;
    private static final int BTN_W = 240;
    private static final int BTN_H = 52;

    private java.awt.Rectangle playBtn()   { return new java.awt.Rectangle(BTN_X, 415, BTN_W, BTN_H); }
    private java.awt.Rectangle quitBtn()   { return new java.awt.Rectangle(BTN_X, 485, BTN_W, BTN_H); }
    private java.awt.Rectangle resumeBtn() { return new java.awt.Rectangle(BTN_X, 415, BTN_W, BTN_H); }
    private java.awt.Rectangle menuBtn()   { return new java.awt.Rectangle(BTN_X, 485, BTN_W, BTN_H); }
    private java.awt.Rectangle retryBtn()  { return new java.awt.Rectangle(BTN_X, 415, BTN_W, BTN_H); }
    private java.awt.Rectangle nextBtn()   { return new java.awt.Rectangle(BTN_X, 415, BTN_W, BTN_H); }

    // ── game loop ────────────────────────────────────────────────────────────

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
                if (handle.KeyHandler.isEscape()) {
                    handle.KeyHandler.resetEscape();
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
                if (handle.KeyHandler.isEscape()) {
                    handle.KeyHandler.resetEscape();
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
            case MENU      -> drawMenu(g);
            case PLAYING   -> drawPlaying(g);
            case PAUSE     -> drawPause(g);
            case GAME_OVER -> drawGameOver(g);
            case LEVEL_WON -> drawLevelWon(g);
            default        -> {}
        }

        bs.show();
        g.dispose();
    }

    // ── draw methods per state ───────────────────────────────────────────────

    private static final int UI_X = 190;
    private static final int UI_Y = 80;
    private static final int UI_W = 900;
    private static final int UI_H = 560;

    private void drawMenu(Graphics g) {
        int w = wnd.GetWndWidth();
        int h = wnd.GetWndHeight();
        g.setColor(new Color(15, 20, 45));
        g.fillRect(0, 0, w, h);
        g.drawImage(Assets.get("uiMenu")[0], UI_X, UI_Y, UI_W, UI_H, null);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 52));
        FontMetrics fm = g.getFontMetrics();
        g.setColor(new Color(80, 40, 10));
        String title = "PLATFORMER";
        g.drawString(title, (w - fm.stringWidth(title)) / 2, 235);
        drawButton(g, playBtn(), "PLAY");
        drawButton(g, quitBtn(), "QUIT");
    }

    private void drawPlaying(Graphics g) {
        g.setColor(new Color(0, 140, 220));
        g.fillRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());
        levelManager.draw(g, wnd, handle.KeyHandler.isDebug());
    }

    private void drawPause(Graphics g) {
        drawPlaying(g);
        g.drawImage(Assets.get("uiPause")[0], UI_X, UI_Y, UI_W, UI_H, null);
        drawButton(g, resumeBtn(), "RESUME");
        drawButton(g, menuBtn(),   "MAIN MENU");
    }

    private void drawGameOver(Graphics g) {
        drawPlaying(g);
        g.drawImage(Assets.get("uiGameOver")[0], UI_X, UI_Y, UI_W, UI_H, null);
        drawButton(g, retryBtn(), "RETRY");
        drawButton(g, menuBtn(),  "MAIN MENU");
    }

    private void drawLevelWon(Graphics g) {
        drawPlaying(g);
        boolean isLastLevel = levelManager.getLevel() >= 3;
        String key = isLastLevel ? "uiGameWon" : "uiLevelComplete";
        g.drawImage(Assets.get(key)[0], UI_X, UI_Y, UI_W, UI_H, null);
        if (!isLastLevel) drawButton(g, nextBtn(), "NEXT LEVEL");
        drawButton(g, menuBtn(), "MAIN MENU");
    }

    // ── shared drawing helpers ───────────────────────────────────────────────

    private void drawButton(Graphics g, java.awt.Rectangle btn, String label) {
        g.setColor(new Color(50, 100, 180));
        g.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 14, 14);
        g.setColor(Color.WHITE);
        g.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 14, 14);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 22));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(label,
                btn.x + (btn.width  - fm.stringWidth(label)) / 2,
                btn.y + btn.height / 2 + fm.getAscent() / 2 - 2);
    }

    private void drawOverlay(Graphics g, float alpha) {
        Graphics2D g2 = (Graphics2D) g;
        Composite old = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());
        g2.setComposite(old);
    }
}
