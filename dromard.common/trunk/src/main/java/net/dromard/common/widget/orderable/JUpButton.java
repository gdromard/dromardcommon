package net.dromard.common.widget.orderable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public class JUpButton extends JArrowButton {
    /** The serialVersionUID. */
    private static final long serialVersionUID = -5058903061866852816L;

    public JUpButton() {
        super();
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        Polygon arrow = new Polygon(new int[]{0, 8, 4}, new int[]{4, 4, 0}, 3);
        int x = (getWidth() - arrow.getBounds().width) / 2;
        int y = (getHeight() - arrow.getBounds().height) / 2;
        g.translate(x, y);
        g.fillPolygon(arrow);
        g.translate(-x, -y);
    }
}
