package net.dromard.common.widget.orderable;

import java.awt.Dimension;

import javax.swing.JButton;

/**
 * TODO Comment here.
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public abstract class JArrowButton extends JButton {
    /** The serialVersionUID. */
    private static final long serialVersionUID = 6097564256618702746L;

    public JArrowButton() {
        setPreferredSize(new Dimension(16, 16));
    }
}
