package net.dromard.common.widget.progress;

/**
 * Comment here.
 * <br><br>
 * Project name : NDTKit eads  <br><br>
 * Class created by : Kameneff Ivan <br>
 * Creation date : 24 juil. 2009
 */
public interface IProgressAware {

    /**
     * Add a listener.
     * @param listener
     */
    void addListener(IProgressListener listener);

    /**
     * Remove a listener.
     * @param listener
     */
    void removeListener(IProgressListener listener);

    /**
     * Clear listeners.
     */
    void clearListeners();
}