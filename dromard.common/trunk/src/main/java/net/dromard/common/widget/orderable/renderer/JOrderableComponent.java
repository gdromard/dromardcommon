package net.dromard.common.widget.orderable.renderer;

import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.dromard.common.widget.orderable.models.Orderable;


/**
 * TODO Comment here.
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public abstract class JOrderableComponent extends JPanel implements Orderable {
    /** The serialVersionUID. */
    private static final long serialVersionUID = 6401153192562040306L;

    public JOrderableComponent(final LayoutManager layout) {
        super(layout);
    }

    public abstract JButton getUpButton();

    public abstract JButton getDownButton();
}
