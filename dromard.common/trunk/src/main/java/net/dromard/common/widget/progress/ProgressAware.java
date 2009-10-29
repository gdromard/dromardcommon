package net.dromard.common.widget.progress;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that handle progression.
 * @author Gabriel Dromard
 * 29 oct. 2009
 */
public class ProgressAware implements IProgressAware {

    /** The listeners. */
    private List<IProgressListener> listeners = null;

    /**
     * @return the listeners
     */
    public final List<IProgressListener> getListeners() {
        return listeners;
    }

    /**
     * @param listeners The listeners to be set.
     */
    public final void setListeners(final List<IProgressListener> listeners) {
        this.listeners = listeners;
    }

    /* (non-Javadoc)
     * @see eads.ndtkit.core.ui.defaultimplementations.IProgressAware#addListener(eads.ndtkit.core.ui.listeners.IProgressListener)
     */
    @Override
    public final void clearListeners() {
        if (listeners != null) {
            listeners.clear();
        }
    }

    /* (non-Javadoc)
     * @see eads.ndtkit.core.ui.defaultimplementations.IProgressAware#addListener(eads.ndtkit.core.ui.listeners.IProgressListener)
     */
    @Override
    public final void addListener(final IProgressListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<IProgressListener>();
        }
        listeners.add(listener);
    }

    /* (non-Javadoc)
     * @see eads.ndtkit.core.ui.defaultimplementations.IProgressAware#removeListener(eads.ndtkit.core.ui.listeners.IProgressListener)
     */
    @Override
    public final void removeListener(final IProgressListener listener) {
        listeners.remove(listener);
    }

    /**
     * Fire progress start event.
     * @param length  The length of progress (or 0 if undeterminate)
     * @param message The progress message
     */
    public final void fireProgressStart(final int length, final String message) {
        if (listeners != null) {
            for (IProgressListener listener : listeners) {
                listener.start(length, message);
            }
        }
    }

    /**
     * Fire progress end event.
     */
    public final void fireProgressEnd() {
        if (listeners != null) {
            for (IProgressListener listener : listeners) {
                listener.endProgress();
            }
        }
    }

    /**
     * Fire progress event.
     * @param value   The value of progress (or 0 if undeterminate)
     * @param details The step details
     */
    public final void fireProgressValue(final int value, final String details) {
        if (listeners != null) {
            for (IProgressListener listener : listeners) {
                listener.setValue(value, details);
            }
        }
    }
}
