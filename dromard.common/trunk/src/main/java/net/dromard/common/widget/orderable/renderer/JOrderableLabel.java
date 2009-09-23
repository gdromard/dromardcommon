package net.dromard.common.widget.orderable.renderer;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import net.dromard.common.widget.orderable.controllers.OrderableController;
import net.dromard.common.widget.orderable.models.Orderable;


/**
 * TODO Comment here.
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public class JOrderableLabel extends JAbstractOrderable {
    /** The serialVersionUID. */
    private static final long serialVersionUID = -744369847191679230L;

    public JOrderableLabel(final OrderableController controller, final Orderable orderable) {
        super(controller, orderable);
    }

    @Override
    protected void init() {
        super.init();
        add(new JLabel(getOrderable().toString()), BorderLayout.CENTER);
    }
}
