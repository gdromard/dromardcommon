/**
 * 	File : ThreadDispatcher.java 12 juin 08
 */

package net.dromard.common.httpbench;

import java.util.ArrayList;
import java.util.List;

import net.dromard.common.util.TimeWatch;

/**
 * This is a process runner that run a process into several thread so as to test threaded processes.
 * <br>
 * @author Gabriel Dromard
 */
public final class ThreadDispatcher {

    /** The threads. */
    private final ArrayList threads = new ArrayList();

    /**
     * Constructor.
     * @param processes The processes to be ran.
     * @param nbTimes   The number of times the entire process has to be executed !
     */
    public ThreadDispatcher(final List processes, final int nbTimes) {
        for (int i = 0; i < processes.size(); ++i) {
            final HttpScenariiTestRunner httpScenariiTestRunner = (HttpScenariiTestRunner) processes.get(i);
            threads.add(new Thread() {
                public void run() {
                    for (int r = 0; r < nbTimes; ++r) {
                        httpScenariiTestRunner.run();
                    }
                }
            });
        }
    }

    /**
     * Add Time watch in process.
     * @param process The process to be watched
     * @param threadNumber The process number
     * @return The resulting thread
     */
    private Thread addTimeWatch(final Runnable process, final int threadNumber) {
        return new Thread(new Runnable() {
            public void run() {
                TimeWatch tw = TimeWatch.start("Thread " + threadNumber);
                process.run();
                tw.stop().record();
            }
        });
    }

    /**
     * Constructor.
     * @param process The process to be ran.
     * @param nbThread The number of thread to instantiate.
     */
    public ThreadDispatcher(final Runnable process, final int nbThread) {
        for (int i = 0; i < nbThread; ++i) {
            threads.add(addTimeWatch(process, i));
        }
    }

    /**
     * Start all thread (running given process).
     * @param delay The delay between two thread.
     * @throws InterruptedException If a thread is interrupted
     */
    public void start(final int delay) throws InterruptedException {
        start(delay, 0);
    }

    /**
     * Start all thread (running given process).
     * @param delay The delay between two thread.
     * @param delayAfterFirst The delay just after the first process (can be used to load cache)
     * @throws InterruptedException If a thread is interrupted
     */
    public void start(final int delay, final int delayAfterFirst) throws InterruptedException {
        for (int i = 0; i < threads.size(); ++i) {
            Thread th = (Thread) threads.get(i);
            th.start();
            if (delayAfterFirst > 0 && i == 0) {
                Thread.sleep(delayAfterFirst);
            } else {
                if (delay > 0) {
                    Thread.sleep(delay);
                }
            }
        }
    }

    /**
     * Start first thread than start all in parallel.
     * @param delay The delay between two thread.
     * @throws InterruptedException If a thread is interrupted
     */
    public void runFirstAloneAndStart(final int delay) throws InterruptedException {
        if (threads.size() > 0) {
            ((Thread) threads.get(0)).run();
            start(delay);
        }
    }

    /**
     * Wait until the threads has finished.
     * @throws InterruptedException If a thread is interrupted
     */
    public void join() throws InterruptedException {
        for (int i = 0; i < threads.size(); ++i) {
            Thread th = (Thread) threads.get(i);
            th.join();
        }
    }
}

