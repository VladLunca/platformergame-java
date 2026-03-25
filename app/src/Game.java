import gamewindow.GameWindow;
import graphics.assets.Assets;
import levelmanager.LevelManager;
import map.MapManager;
import utils.LevelMaps;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public  class Game implements Runnable {
    private  GameWindow       wnd ;
    private boolean         runState;
    private Thread          gameThread;
    private BufferStrategy bs;
    private Graphics g;
    private static  Game instance;
    private static MapManager mapManager;
    private static LevelManager levelManager;
    private  Game(String title, int width, int height) throws IOException {
        wnd = new GameWindow(title, width, height);
        runState = false;
        mapManager= MapManager.createMapManager("/textures/maps/maps.json");
        levelManager = LevelManager.createLevelManager(mapManager);
    }
    public static Game createGame(String title, int width, int height)
    {
        return Objects.requireNonNullElseGet(instance, () -> {
            try {
                return new Game(title, width, height);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void InitGame()
    {
        /// Este construita fereastra grafica.
        wnd.BuildGameWindow();

        Assets.Init();
        levelManager.loadLevel();
    }
    public synchronized void StartGame()
    {
        if(!runState)
        {
            runState = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }
    public synchronized void StopGame()
    {
        if(runState)
        {
            runState = false;
            try
            {
                gameThread.join();
            }
            catch(InterruptedException ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            return;
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
        double timePerFrame = (double) 2000000000 / fps_limit;

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
    private void update()
    {
        levelManager.update();
    }
    private void Draw()
    {
        bs = wnd.GetCanvas().getBufferStrategy();
        if(bs == null)
        {
            try
            {
                wnd.GetCanvas().createBufferStrategy(3);
                return;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());
        Color mycolor=new Color(0,140,220);
        g.setColor( mycolor);
        g.fillRect(0,0,wnd.GetWndWidth(), wnd.GetWndHeight());
        levelManager.draw(g,wnd);
        bs.show();
        g.dispose();
    }
}