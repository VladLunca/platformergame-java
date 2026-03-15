import gamewindow.GameWindow;
import graphics.assets.Assets;
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
    private int level=1;
    private  Game(String title, int width, int height) throws IOException {
        wnd = new GameWindow(title, width, height);
        runState = false;
        mapManager= MapManager.createMapManager("/maps/maps.json");
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
        long prevTime = System.nanoTime();
        long currentTime;
        double delta = 0;


        final int framesPerSecond = 60; /*!< Constanta intreaga initializata cu numarul de frame-uri pe secunda.*/
        final int ups_frame = 60;
        final double timeFrame = (double) 1000000000 / framesPerSecond; /*!< Durata unui frame in nanosecunde.*/
        double timePerUpdate = (double) 1000000000 / ups_frame;
        double lastFrame = System.nanoTime();
        double updates = 0;
        double frames = 0;
        while (runState) {
            currentTime = System.nanoTime();
            delta = delta +(currentTime-oldTime)/ timePerUpdate;
            oldTime = currentTime;
            if(delta>=1)
            {
                Update();
                updates++;
                delta--;
            }
            if(currentTime-lastFrame>=timeFrame){
                lastFrame=currentTime;
                frames++;
                Draw();
            }
        }
    }
    private void Update()
    {
        return;
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
        LevelMaps mapName = switch (level) {
            case 2 -> LevelMaps.redMap;
            case 3 -> LevelMaps.purpleMap;
            default -> LevelMaps.greenMap;
        };
        mapManager.drawMap(mapName.name(), g);
        bs.show();
        g.dispose();
    }
}