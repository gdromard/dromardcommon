package net.dromard.common.widget.zipviewer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * TODO Comment here.
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public abstract class TreeSelectionHandler extends MouseAdapter implements KeyListener {

    /* ----------- MouseAdapter ----------- */

    @Override
    public void mouseClicked(final MouseEvent e) {
        super.mouseClicked(e);
        if (e.getClickCount() > 1) {
            selectionDone();
        }
    }

    /* ----------- KeyListener ----------- */


    public void keyPressed(final KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            selectionDone();
        }
    }

    public void keyReleased(final KeyEvent e) {
    }

    public void keyTyped(final KeyEvent e) {
    }

    /* ----------- Selection ----------- */

    public abstract void selectionDone();
}