import GameWindow.GameWindow;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Objects;

public  class Game implements Runnable {
    private  GameWindow       wnd ;
    private boolean         runState;
    private Thread          gameThread;
    private BufferStrategy bs;
    private Graphics g;
    private static  Game instance;
    private  Game(String title, int width, int height)
    {
        wnd = new GameWindow(title, width, height);
        runState = false;
    }
    public static Game createGame(String title, int width, int height)
    {
        return Objects.requireNonNullElseGet(instance, () -> new Game(title, width, height));
    }
    private void InitGame()
    {
        /// Este construita fereastra grafica.
        wnd.BuildGameWindow();
        ///Personajele
        /// Se incarca toate elementele grafice
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
        g.setColor(Color.BLACK);
        bs.show();
        g.dispose();
    }
}