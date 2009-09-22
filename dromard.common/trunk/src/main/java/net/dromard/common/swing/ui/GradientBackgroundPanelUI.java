package net.dromard.common.swing.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JComponent;
import javax.swing.plaf.PanelUI;

/**
 * A gradiant for background.
 * @author Gabriel Dromard
 * 9 sept. 2009
 */
public class GradientBackgroundPanelUI extends PanelUI {
    /** The topColor. */
    private final Color topColor;
    /** The bottomColor. */
    private final Color bottomColor;

    public GradientBackgroundPanelUI(final Color topColor, final Color bottomColor) {
        this.topColor = topColor;
        this.bottomColor = bottomColor;
    }

    @Override
    public void update(final Graphics g, final JComponent c) {
        if (c.isOpaque()) {
            g.setColor(c.getBackground());
            Graphics2D g2d = (Graphics2D) g;
            Paint oldPaint = g2d.getPaint();
            GradientPaint paint = new GradientPaint(0, 0, topColor, 0, c.getHeight(), bottomColor);
            g2d.setPaint(paint);
            g2d.fillRect(0, 0, c.getWidth(), c.getHeight());
            g2d.setPaint(oldPaint);
        }
        paint(g, c);
    }
}
