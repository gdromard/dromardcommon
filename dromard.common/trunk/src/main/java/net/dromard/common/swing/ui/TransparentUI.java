package net.dromard.common.swing.ui;

import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.PanelUI;

public class TransparentUI extends PanelUI {
    /** The serialVersionUID. */
    protected Image background;
    private long lastupdate = 0;
    public boolean refreshRequested = true;
    private Window window = null;

    @Override
    public void paint(final Graphics g, final JComponent c) {
        if (window == null) {
            window = SwingUtilities.getWindowAncestor(c);
            WindowRepainter repainter = new WindowRepainter(window);
            window.addComponentListener(repainter);
            window.addWindowFocusListener(repainter);
            quickRefresh();
            //updateBackground();
        }
        Point pos = c.getLocationOnScreen();
        Point offset = new Point(-pos.x, -pos.y);
        g.drawImage(background, offset.x, offset.y, null);
    }

    public void updateBackground() {
        try {
            Robot rbt = new Robot();
            background = rbt.createScreenCapture(getTotalScreenBounds());
        } catch (Exception ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
    }

    private Rectangle getTotalScreenBounds() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        int w = 0, h = 0;
        for (int i = 0; i < ge.getScreenDevices().length; i++) {
            w += ge.getMaximumWindowBounds().width;
        }
        h = ge.getMaximumWindowBounds().height;
        return new Rectangle(0, 0, w, h);
    }

    public void quickRefresh() {
        try {
            System.out.println("quick refresh");
            long now = new Date().getTime();
            if ((now - lastupdate) > 10) {
                if (window.isVisible()) {
                    Point location = window.getLocation();
                    window.setVisible(false);
                    updateBackground();
                    window.setVisible(true);
                    window.setLocation(location);
                }
                lastupdate = now;
                refreshRequested = false;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
    }

    public void refresh() {
        if (window.isVisible()) {
            window.repaint();
            refreshRequested = true;
            lastupdate = new Date().getTime();
        }
    }

    class WindowRepainter implements ComponentListener, WindowFocusListener {
        private final Window window;

        public WindowRepainter(final Window window) {
            this.window = window;
        }

        public void componentShown(final ComponentEvent evt) {
            System.out.println("componentShown");
            refresh();
            window.repaint();
        }

        public void componentResized(final ComponentEvent evt) {
            System.out.println("componentResized");
            window.repaint();
        }

        public void componentMoved(final ComponentEvent evt) {
            System.out.println("componentMoved");
            window.repaint();
        }

        public void componentHidden(final ComponentEvent evt) {
            System.out.println("componentHidden");
        }

        public void windowGainedFocus(final WindowEvent evt) {
            System.out.println("windowGainFocus");
            quickRefresh();
        }

        public void windowLostFocus(final WindowEvent evt) {
            System.out.println("windowLostFocus");
            refresh();
        }
    }
}