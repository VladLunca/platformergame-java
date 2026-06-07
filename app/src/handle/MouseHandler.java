package handle;

import utils.GameStates;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class MouseHandler implements MouseListener {
    private static final Map<GameStates, Consumer<MouseEvent>> handlers = new EnumMap<>(GameStates.class);

    public static void register(GameStates state, Consumer<MouseEvent> handler) {
        handlers.put(state, handler);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Consumer<MouseEvent> h = handlers.get(GameStates.current);
        if (h != null) h.accept(e);
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
