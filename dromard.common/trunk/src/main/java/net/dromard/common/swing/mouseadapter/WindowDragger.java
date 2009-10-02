package net.dromard.common.swing.mouseadapter;

import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.SwingUtilities;

/**
 * A class that makes a window draggable.
 * This implementation use Event.isConsumed() so as to know if it has to consume the event or not !
 * @author Gabriel Dromard
 * 2 oct. 2009
 */
public class WindowDragger {

    private final Window fWindow;

    private final Component fComponent;

    private int dX;

    private int dY;

    /**
     * @param window    The window that you want to be dragged
     * @param component The component on witch the window will be draggable (ex: frame top bar)
     */
    public WindowDragger(final Window window, final Component component) {

        fWindow = window;
        fComponent = component;

        fComponent.addMouseListener(createMouseListener());
        fComponent.addMouseMotionListener(createMouseMotionListener());
    }

    private MouseListener createMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                Point clickPoint = new Point(e.getPoint());
                SwingUtilities.convertPointToScreen(clickPoint, fComponent);

                dX = clickPoint.x - fWindow.getX();
                dY = clickPoint.y - fWindow.getY();
            }
        };
    }

    private MouseMotionAdapter createMouseMotionListener() {
        return new MouseMotionAdapter() {
            @Override
            public void mouseDragged(final MouseEvent e) {
                if (!e.isConsumed()) {
                    Point dragPoint = new Point(e.getPoint());
                    SwingUtilities.convertPointToScreen(dragPoint, fComponent);
                    fWindow.setLocation(dragPoint.x - dX, dragPoint.y - dY);
                    e.consume();
                }
            }
        };
    }

}
