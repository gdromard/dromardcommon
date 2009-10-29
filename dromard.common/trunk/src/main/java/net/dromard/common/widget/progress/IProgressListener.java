package net.dromard.common.widget.progress;

/**
 * A progress listener.
 * @author Gabriel Dromard
 * 29 oct. 2009
 */
public interface IProgressListener {
    /**
     * Fired when starting a length's determinate progression.
     * @param length The length of the current process listened (if == 0 -> indeterminate length).
     */
    void start(int length, String message);

    /**
     * Fired when the process grow.
     * @param value   The current value of the progression.
     * @param details The details of current step.
     */
    void setValue(int value, String details);

    /**
     * Fired when a process is finished.
     */
    void endProgress();
}
