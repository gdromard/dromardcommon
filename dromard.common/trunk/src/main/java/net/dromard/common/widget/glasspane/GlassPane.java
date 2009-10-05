package net.dromard.common.widget.glasspane;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GlassPane {
    private final RenderingHints hints;
    private final Color backgroundColor = new Color(255, 255, 255, 150);
    private final GlassPanePanel glasspane = new GlassPanePanel();
    private final MouseListener mouseListener = new MouseAdapter() {
    };

    public GlassPane() {
        hints = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    public void installGlassPane(final JFrame frame) {
        frame.setGlassPane(glasspane);
    }

    public void setVisible(final boolean visible) {
        glasspane.setVisible(visible);
        if (visible) {
            glasspane.addMouseListener(mouseListener);
        } else {
            glasspane.removeMouseListener(mouseListener);
        }
    }

    public void setLayout(final LayoutManager layourManager) {
        glasspane.setLayout(layourManager);
    }

    public void add(final JComponent component, final Object constraint) {
        glasspane.add(component, constraint);
    }

    private class GlassPanePanel extends JPanel {
        /** The serialVersionUID. */
        private static final long serialVersionUID = -552913844411692387L;

        private GlassPanePanel() {
            setOpaque(false);
        }

        @Override
        public void paintComponent(final Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(hints);
            if (backgroundColor != null) {
                g2.setColor(backgroundColor);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            super.paintComponents(g);
        }
    }
}
