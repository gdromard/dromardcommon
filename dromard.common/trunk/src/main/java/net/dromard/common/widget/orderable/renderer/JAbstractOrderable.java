package net.dromard.common.widget.orderable.renderer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import net.dromard.common.widget.orderable.JDownButton;
import net.dromard.common.widget.orderable.JUpButton;
import net.dromard.common.widget.orderable.controllers.OrderableController;
import net.dromard.common.widget.orderable.models.Orderable;

/**
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public abstract class JAbstractOrderable extends JOrderableComponent {
    /** The serialVersionUID. */
    protected static final long serialVersionUID = 1281760921038786790L;
    /** The orderable. */
    private final Orderable orderable;
    /** The controller. */
    private final OrderableController controller;
    /** The upBtn. */
    private JButton upBtn;
    /** The downBtn. */
    private JButton downBtn;
    /** The removeBtn. */
    private JButton removeBtn;

    public JAbstractOrderable(final OrderableController controller, final Orderable orderable) {
        super(new BorderLayout());
        this.controller = controller;
        this.orderable = orderable;
        init();
    }

    protected void init() {
        JPanel btnPanel = new JPanel(new GridLayout(2, 1));

        // Up / down buttons
        upBtn = new JUpButton();
        downBtn = new JDownButton();
        btnPanel.add(upBtn);
        btnPanel.add(downBtn);
        upBtn.setVisible(false);
        downBtn.setVisible(false);

        // Remove Button
        final JAbstractOrderable zis = this;
        removeBtn = new JButton(new AbstractAction() {
            /** The serialVersionUID. */
            private static final long serialVersionUID = -373549363554734064L;

            public void actionPerformed(final ActionEvent e) {
                (controller).remove(orderable);
                SwingUtilities.updateComponentTreeUI(zis.getParent());
            }
        });
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("images/suppr.png"));
        removeBtn.setIcon(icon);
        removeBtn.setVisible(false);
        removeBtn.setPreferredSize(new Dimension(20, 20));
        removeBtn.setVerticalAlignment(SwingConstants.CENTER);
        removeBtn.setBorder(BorderFactory.createEmptyBorder());
        JPanel removeBtnPanel = new JPanel();
        removeBtnPanel.add(removeBtn);

        // GUI association
        add(removeBtnPanel, BorderLayout.WEST);
        add(btnPanel, BorderLayout.EAST);
    }

    @Override
    public JButton getUpButton() {
        return upBtn;
    }

    @Override
    public JButton getDownButton() {
        return downBtn;
    }

    public JButton getRemoveBtn() {
        return removeBtn;
    }

    public int getOrder() {
        return orderable.getOrder();
    }

    public void setOrder(final int order) {
        if (order > orderable.getOrder()) {
            downBtn.requestFocusInWindow();
        } else {
            upBtn.requestFocusInWindow();
        }
        orderable.setOrder(order);
    }

    /**
     * @return the orderable
     */
    public Orderable getOrderable() {
        return orderable;
    }
}
