package net.dromard.common.widget.orderable.listeners;

import net.dromard.common.widget.orderable.models.Orderable;

/**
 * TODO Comment here.
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public interface OrderListener {
    void orderChanged(Orderable changed, Orderable impacted);
}
