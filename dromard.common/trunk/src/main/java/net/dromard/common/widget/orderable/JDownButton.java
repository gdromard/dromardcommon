package net.dromard.common.widget.orderable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * TODO Comment here.
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public class JDownButton extends JArrowButton {
    /** The serialVersionUID. */
    private static final long serialVersionUID = 3352339565208194637L;

    public JDownButton() {
        super();
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        Polygon arrow = new Polygon(new int[] {0, 6, 3}, new int[] {0, 0, 3}, 3);
        int x = (getWidth() - arrow.getBounds().width) / 2;
        int y = (getHeight() - arrow.getBounds().height) / 2;
        g.translate(x, y);
        g.fillPolygon(arrow);
        g.translate(-x, -y);
    }
}
