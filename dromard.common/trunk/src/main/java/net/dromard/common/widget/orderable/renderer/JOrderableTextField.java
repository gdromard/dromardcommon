package net.dromard.common.widget.orderable.renderer;

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import net.dromard.common.widget.orderable.controllers.OrderableController;
import net.dromard.common.widget.orderable.models.Editable;
import net.dromard.common.widget.orderable.models.Orderable;


/**
 * TODO Comment here.
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public class JOrderableTextField extends JAbstractOrderable {
    /** The serialVersionUID. */
    private static final long serialVersionUID = 1281760921038786790L;

    public JOrderableTextField(final OrderableController controller, final Orderable orderable) {
        super(controller, orderable);
    }

    @Override
    protected void init() {
        super.init();
        final JTextField txtFld = new JTextField(getOrderable().toString());
        txtFld.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(4, 0, 4, 0, getBackground()), txtFld.getBorder()));
        final Editable editable = ((Editable) getOrderable());
        if (editable.isEditable()) {
            txtFld.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(final FocusEvent e) {
                    super.focusLost(e);
                    editable.setName(txtFld.getText());
                }
            });
        } else {
            txtFld.setEnabled(false);
        }
        add(txtFld, BorderLayout.CENTER);
    }
}
