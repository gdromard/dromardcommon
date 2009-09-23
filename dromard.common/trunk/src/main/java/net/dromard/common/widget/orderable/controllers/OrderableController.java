package net.dromard.common.widget.orderable.controllers;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;

import net.dromard.common.widget.orderable.listeners.OrderListener;
import net.dromard.common.widget.orderable.models.Editable;
import net.dromard.common.widget.orderable.models.Orderable;
import net.dromard.common.widget.orderable.models.OrderableModel;
import net.dromard.common.widget.orderable.renderer.JOrderableComponent;
import net.dromard.common.widget.orderable.renderer.JOrderableLabel;
import net.dromard.common.widget.orderable.renderer.JOrderableTextField;


/**
 * @author Steven Ortiz
 */
public class OrderableController {
    /** The model. */
    private final OrderableModel model;
    /** The listeners. */
    private List<OrderListener> listeners;
    /** The renderers. */
    private final ArrayList<JOrderableComponent> renderers = new ArrayList<JOrderableComponent>();

    public OrderableController(final OrderableModel model) {
        this.model = model;
    }

    protected void updateRenderer(final JOrderableComponent renderer, final Orderable orderable) {
        renderer.getUpButton().setVisible(isEditable(model.getPreviousOrderable(orderable)));
        renderer.getDownButton().setVisible(isEditable(model.getNextOrderable(orderable)));
    }

    public void updateRenderers() {
        for (JOrderableComponent renderer : renderers) {
            Orderable orderable = model.getOrderable(renderer.getOrder());
            updateRenderer(renderer, orderable);
        }
    }

    protected JOrderableComponent getRendererFor(final Orderable orderable) {
        JOrderableComponent renderer;
        if (orderable instanceof Editable && ((Editable) orderable).isEditable()) {
            renderer = new JOrderableTextField(this, orderable);
        } else {
            renderer = new JOrderableLabel(this, orderable);
        }
        return renderer;
    }

    public List<JOrderableComponent> getRenderers() {
        renderers.clear();
        for (Orderable orderable : model.getSortedList()) {
            JOrderableComponent renderer = getRendererFor(orderable);
            if (isEditable(orderable)) {
                renderer.getUpButton().setAction(new UpAction(renderer));
                renderer.getDownButton().setAction(new DownAction(renderer));
            }
            renderers.add(renderer);
        }
        updateRenderers();
        return renderers;
    }

    protected OrderableModel getModel() {
        return model;
    }

    public boolean isEditable(final Orderable orderable) {
        return orderable != null && (!(orderable instanceof Editable) || ((Editable) orderable).isEditable());
    }

    protected void swapOrder(final Orderable oldOrder, final Orderable newOrder) {
        if (oldOrder == null || newOrder == null) {
            return;
        }
        boolean oldEditable = !(oldOrder instanceof Editable) || ((Editable) oldOrder).isEditable();
        boolean newEditable = !(newOrder instanceof Editable) || ((Editable) newOrder).isEditable();

        if (oldEditable && newEditable) {
            int tmp = oldOrder.getOrder();
            oldOrder.setOrder(newOrder.getOrder());
            newOrder.setOrder(tmp);

            // Fire order changed
            fireOrderChangedEvent(oldOrder, newOrder);
        }
        // update renderers
        updateRenderers();
    }

    /**
     * @param orderable The orderable that will be removed
     */
    public void remove(final Orderable orderable) {
        getModel().remove(orderable);
    }

    protected void fireOrderChangedEvent(final Orderable changedOrderable, final Orderable impactedOrderable) {
        for (OrderListener l : listeners) {
            l.orderChanged(changedOrderable, impactedOrderable);
        }
    }

    public List<OrderListener> getListeners() {
        return listeners;
    }

    public void addListener(final OrderListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<OrderListener>();
        }
        listeners.add(listener);
    }

    public void removeListener(final OrderListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    /* ---------------------------------------------------------------------- */

    /** The ORDERABLE_COMPARATOR. */
    public static final Comparator<Orderable> ORDERABLE_COMPARATOR = new Comparator<Orderable>() {
        public int compare(final Orderable o1, final Orderable o2) {
            return new Integer(o1.getOrder()).compareTo(new Integer(o2.getOrder()));
        }
    };

    /** The COMPONENT_ORDERABLE_COMPARATOR. */
    public static final Comparator<Component> COMPONENT_ORDERABLE_COMPARATOR = new Comparator<Component>() {
        public int compare(final Component o1, final Component o2) {
            if (o1 instanceof Orderable && o2 instanceof Orderable) {
                return compare((Orderable) o1, (Orderable) o2);
            }
            return 0;
        }

        public int compare(final Orderable o1, final Orderable o2) {
            return new Integer(o1.getOrder()).compareTo(new Integer(o2.getOrder()));
        }
    };

    /**
     * TODO Comment here.
     * @author Gabriel Dromard
     * 30 juil. 2009
     */
    private class DownAction extends AbstractAction {
        /** The serialVersionUID. */
        private static final long serialVersionUID = -8338145099874153231L;

        /** The orderable. */
        private final JOrderableComponent orderable;

        public DownAction(final JOrderableComponent orderable) {
            this.orderable = orderable;
        }

        public void actionPerformed(final ActionEvent e) {
            swapOrder(orderable, model.getNextOrderable(orderable));
        }
    }

    /**
     * TODO Comment here.
     * @author Gabriel Dromard
     * 30 juil. 2009
     */
    private class UpAction extends AbstractAction {
        /** The serialVersionUID. */
        private static final long serialVersionUID = -2753894783062195147L;

        /** The orderable. */
        private final JOrderableComponent orderable;

        public UpAction(final JOrderableComponent orderable) {
            this.orderable = orderable;
        }

        public void actionPerformed(final ActionEvent e) {
            swapOrder(orderable, model.getPreviousOrderable(orderable));
        }
    }
}