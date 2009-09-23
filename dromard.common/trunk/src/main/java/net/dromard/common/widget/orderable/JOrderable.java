package net.dromard.common.widget.orderable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import net.dromard.common.swing.SwingComponent;
import net.dromard.common.widget.orderable.controllers.OrderableController;
import net.dromard.common.widget.orderable.listeners.OrderListener;
import net.dromard.common.widget.orderable.models.Orderable;
import net.dromard.common.widget.orderable.models.OrderableModel;
import net.dromard.common.widget.orderable.renderer.JOrderableComponent;

/**
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public class JOrderable implements SwingComponent<JComponent> {
    /** The orderables. */
    private final JPanel orderables;
    /** The component. */
    private final JComponent component;
    /** The controller. */
    private final OrderableController controller;
    /** The model. */
    private final OrderableModel model;
    /** The components. */
    private List<JOrderableComponent> components;

    public JOrderable(final OrderableModel model, final OrderableController controller) {
        this.model = model;
        this.controller = controller;
        orderables = new JOrderablePanel();
        JPanel top = new JPanel(new BorderLayout());
        top.add(orderables, BorderLayout.NORTH);
        component = new JScrollPane(top);
        component.setBorder(BorderFactory.createEmptyBorder());

        buildRenderers();
        JOrderableOrderListener listener = new JOrderableOrderListener();
        addListener(listener);
    }

    protected synchronized void buildRenderers() {
        orderables.removeAll();
        components = controller.getRenderers();
        orderables.setLayout(new GridLayout(components.size(), 1, 5, 5));
        for (JOrderableComponent c : components) {
            orderables.add(c);
        }
    }

    public void updateUI() {
        SwingUtilities.updateComponentTreeUI(orderables);
    }

    public JComponent getComponent() {
        return component;
    }

    public List<OrderListener> getListeners() {
        return controller.getListeners();
    }

    public void addListener(final OrderListener listener) {
        controller.addListener(listener);
    }

    public void removeListener(final OrderListener listener) {
        controller.removeListener(listener);
    }

    /**
     * @author Gabriel Dromard
     * 30 juil. 2009
     */
    class JOrderableOrderListener implements OrderListener {
        @Override
        public void orderChanged(final Orderable changed, final Orderable impacted) {
            updateUI();
        }
    }

    /**
     * @author Gabriel Dromard
     * 30 juil. 2009
     */
    class JOrderablePanel extends JPanel {
        /** The serialVersionUID. */
        private static final long serialVersionUID = 1017869107735331978L;

        @Override
        public void updateUI() {
            if (components != null) {
                // Rebuild renderers if needed
                if (model.getOrderables().size() != components.size()) {
                    buildRenderers();
                } else {
                    controller.updateRenderers();
                }
            }
            super.updateUI();
        }

        @Override
        public Component getComponent(final int index) {
            List<Component> lComponents = Arrays.asList(super.getComponents());
            Collections.sort(lComponents, OrderableController.COMPONENT_ORDERABLE_COMPARATOR);
            return lComponents.get(index);
        }

        @Override
        public Component[] getComponents() {
            List<Component> lComponents = Arrays.asList(super.getComponents());
            Collections.sort(lComponents, OrderableController.COMPONENT_ORDERABLE_COMPARATOR);
            return lComponents.toArray(new Component[0]);
        }
    }
}
