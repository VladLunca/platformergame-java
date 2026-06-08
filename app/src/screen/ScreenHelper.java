package screen;

import java.awt.*;

class ScreenHelper {

    static final int BTN_X = 520;
    static final int BTN_W = 240;
    static final int BTN_H = 52;
    static final int UI_X  = 190;
    static final int UI_Y  = 80;
    static final int UI_W  = 900;
    static final int UI_H  = 560;

    static void drawButton(Graphics g, Rectangle btn, String label) {
        g.setColor(new Color(50, 100, 180));
        g.fillRoundRect(btn.x, btn.y, btn.width, btn.height, 14, 14);
        g.setColor(Color.WHITE);
        g.drawRoundRect(btn.x, btn.y, btn.width, btn.height, 14, 14);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(label,
                btn.x + (btn.width  - fm.stringWidth(label)) / 2,
                btn.y + btn.height / 2 + fm.getAscent() / 2 - 2);
    }
}
