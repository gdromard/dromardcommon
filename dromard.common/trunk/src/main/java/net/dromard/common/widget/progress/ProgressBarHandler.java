package net.dromard.common.widget.progress;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import net.dromard.common.widget.infinityprogress.InfiniteProgressGlassPane;

/**
 * A progress bar handler.
 * @author Gabriel Dromard
 * 29 oct. 2009
 */
public final class ProgressBarHandler {
    private final List<Progressor> progressorPool = new ArrayList<Progressor>();
    private final InfiniteProgressGlassPane progress;

    /**
     * @param application The application to be progress aware.
     */
    public ProgressBarHandler(final JFrame application) {
        progress = new InfiniteProgressGlassPane(application);
    }

    /**
     * @return A new progress Listener.
     */
    public IProgressListener getProgressListener() {
        return new Progressor();
    }

    /**
     * Internal progress listener that handle several progressor in parallel.
     * @author Gabriel Dromard
     * 29 oct. 2009
     */
    private class Progressor implements IProgressListener {
        private int length = 0;

        @Override
        public void start(final int length, final String message) {
            synchronized (progress) {
                progressorPool.add(this);
                System.out.println("Starting progress");
                progress.start();
                this.length = length;
                if (length > 0) {
                    progress.setText(message, 0);
                } else {
                    progress.setText(message);
                }
            }
        }

        @Override
        public void endProgress() {
            synchronized (progress) {
                System.out.println("Stopping progress");
                progress.stop();
                progressorPool.remove(this);
            }
        }

        @Override
        public void setValue(final int value, final String details) {
            progress.start();
            progress.setInfo(details);
            if (length > 0) {
                progress.setPourcentage(value / length * 100);
            }
        }
    }
}
