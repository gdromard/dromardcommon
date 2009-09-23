package net.dromard.common.widget.orderable.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.dromard.common.widget.orderable.controllers.OrderableController;


/**
 * The model of orderable components.
 * @author Gabriel Dromard
 */
public class OrderableModel {
    /** The orderables. */
    private List<Orderable> orderables = new ArrayList<Orderable>();

    public void add(final Orderable orderable) {
        int order = orderable.getOrder();
        while (getOrderable(order) != null) {
            ++order;
        }
        orderable.setOrder(order);
        orderables.add(orderable);
    }

    public void remove(final Orderable orderable) {
        orderables.remove(orderable);
    }

    public void setOrderables(final List<Orderable> models) {
        orderables = models;
    }

    public List<Orderable> getOrderables() {
        return orderables;
    }

    public List<Orderable> getSortedList() {
        Collections.sort(orderables, OrderableController.ORDERABLE_COMPARATOR);
        return orderables;
    }

    public Orderable getOrderable(final int order) {
        for (Orderable o : orderables) {
            if (o.getOrder() == order) {
                return o;
            }
        }
        System.out.println("[DEBUG] Ooops it was not found !! (orderable)");
        return null;
    }

    public Orderable getNextOrderable(final Orderable orderable) {
        Orderable previous = null;
        for (Orderable o : getSortedList()) {
            if (previous != null && previous.getOrder() == orderable.getOrder()) {
                return o;
            }
            previous = o;
        }
        return null;
    }

    public Orderable getPreviousOrderable(final Orderable orderable) {
        Orderable previous = null;
        for (Orderable o : getSortedList()) {
            if (previous != null && o.getOrder() == orderable.getOrder()) {
                return previous;
            }
            previous = o;
        }
        return null;
    }
}