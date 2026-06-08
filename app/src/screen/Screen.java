package screen;

import gamewindow.GameWindow;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

public interface Screen {
    void draw(Graphics g, GameWindow wnd);
    default void handleClick(MouseEvent e) {}
}
