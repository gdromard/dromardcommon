package net.dromard.common.swing.mouseadapter;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JWindow;

public class MoveAndResizeMouseListener extends MouseAdapter {
    private final JWindow window;
    private Point relative = null;
    private Point absolute = null;
    private Dimension size = null;

    public MoveAndResizeMouseListener(final JWindow clockFrame) {
        this.window = clockFrame;
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        if (!e.isConsumed()) {
            e.consume();
            relative = e.getPoint();
            absolute = e.getLocationOnScreen();
            size = window.getSize();
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (!e.isConsumed() && relative != null) {
            e.consume();
            switch (window.getCursor().getType()) {
            // In -> Move
            case Cursor.DEFAULT_CURSOR:
                window.setLocation(e.getLocationOnScreen().x - relative.x, e.getLocationOnScreen().y - relative.y);
                break;
            // Corners -> resize
            case Cursor.NW_RESIZE_CURSOR:
                window.setLocation(e.getLocationOnScreen());
                window.setSize(size.width + (absolute.x - e.getLocationOnScreen().x), size.height + (absolute.y) - e.getLocationOnScreen().y);
                break;
            case Cursor.SE_RESIZE_CURSOR:
                window.setSize(size.width + (e.getPoint().x - relative.x), size.height + (e.getPoint().y - relative.y));
                break;
            case Cursor.SW_RESIZE_CURSOR:
                window.setLocation(e.getLocationOnScreen().x, window.getLocationOnScreen().y);
                window.setSize(size.width + (absolute.x - e.getLocationOnScreen().x), size.height + (e.getLocationOnScreen().y - absolute.y));
                break;
            case Cursor.NE_RESIZE_CURSOR:
                window.setLocation(window.getLocationOnScreen().x, e.getLocationOnScreen().y);
                window.setSize(size.width + (e.getLocationOnScreen().x - absolute.x), size.height + (absolute.y - e.getLocationOnScreen().y));
                break;
            // Borders -> resize
            case Cursor.N_RESIZE_CURSOR:
                window.setLocation(window.getLocationOnScreen().x, e.getLocationOnScreen().y);
                window.setSize(size.width, size.height + (absolute.y - e.getLocationOnScreen().y));
                break;
            case Cursor.S_RESIZE_CURSOR:
                window.setSize(size.width, size.height + (e.getLocationOnScreen().y - absolute.y));
                break;
            case Cursor.E_RESIZE_CURSOR:
                window.setSize(size.width + (e.getLocationOnScreen().x - absolute.x), size.height);
                break;
            case Cursor.W_RESIZE_CURSOR:
                window.setLocation(e.getLocationOnScreen().x, window.getLocationOnScreen().y);
                window.setSize(size.width + (absolute.x - e.getLocationOnScreen().x), size.height);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        if (!e.isConsumed()) {
            e.consume();
            relative = null;
        }
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        int cursor = Cursor.DEFAULT_CURSOR;
        if (e.getPoint().getX() <= 3 && e.getPoint().getY() <= 3) {
            // Top/Left
            cursor = Cursor.NW_RESIZE_CURSOR;
        } else if (e.getPoint().getX() >= window.getWidth() - 3 && e.getPoint().getY() >= window.getHeight() - 3) {
            // Bottom/Right
            cursor = Cursor.SE_RESIZE_CURSOR;
        } else if (e.getPoint().getX() <= 3 && e.getPoint().getY() >= window.getHeight() - 3) {
            // Bottom/Left
            cursor = Cursor.SW_RESIZE_CURSOR;
        } else if (e.getPoint().getX() >= window.getWidth() - 3 && e.getPoint().getY() <= 3) {
            // Top/Right
            cursor = Cursor.NE_RESIZE_CURSOR;
        } else if (e.getPoint().getX() <= 3) {
            // Left border
            cursor = Cursor.W_RESIZE_CURSOR;
        } else if (e.getPoint().getX() >= window.getWidth() - 3) {
            // Right border
            cursor = Cursor.E_RESIZE_CURSOR;
        } else if (e.getPoint().getY() <= 3) {
            // Top border
            cursor = Cursor.N_RESIZE_CURSOR;
        } else if (e.getPoint().getY() >= window.getHeight() - 3) {
            // Bottom border
            cursor = Cursor.S_RESIZE_CURSOR;
        }
        if (window.getCursor().getType() != cursor) {
            window.setCursor(Cursor.getPredefinedCursor(cursor));
        }
    }
}
