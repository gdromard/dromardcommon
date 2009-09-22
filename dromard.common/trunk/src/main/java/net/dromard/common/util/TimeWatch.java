/**
 * File : TimeWatch.java 11 juin 08
 */
package net.dromard.common.util;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * A tool to measure duration around piece of code.
 * </p>
 * <p>
 * Take a single measure, and print it :
 * </p>
 *
 * <pre>
 * TimeWatch t = TimeWatch.start(&quot;Choose a name, for example : Printing result&quot;);
 * // ... actual code to be measured
 * t.stop().print();
 * </pre>
 *
 * <p>
 * To cumulate the same measure accross different calls within current Thread :
 * </p>
 *
 * <pre>
 * TimeWatch t = TimeWatch.start(&quot;Choose a name, for example : Printing result&quot;);
 * // ... actual code to be measured
 * t.stop().recordLocally();
 *
 * // retrieve stats for a given thread :
 * TimeStats stats = (TimeStats) TimeWatch.localStats().get(&quot;The operation you want the stats for&quot;);
 *
 * // before a new bulk of measures :
 * TimeWatch.resetLocalStats();
 * </pre>
 *
 * <p>
 * To cumulate measures accross different calls / different threads :
 * </p>
 *
 * <pre>
 * TimeWatch t = TimeWatch.start(&quot;Choose a name, for example : Printing result&quot;);
 * // ... actual code to be measured
 * t.stop().record();
 *
 * // To browse the recorded stats :
 * TimeStats stats = (TimeStats) TimeWatch.stats().get(&quot;The operation you want the stats for&quot;);
 *
 * // before a new bulk of measures :
 * TimeWatch.resetStats();
 * </pre>
 *
 * <p>
 * Many methods that would usually be designed to return void, are implemented to return this, so that we can chain-call on a single line.
 * Introducting TimeRecorder into existing code, can take up alot of space, all is done here to reduce that impact. Also helps with comment/uncomment
 * the calls.
 * </p>
 *
 * <pre>
 * TimeRecorder r = TimeRecorder.start(&quot;MyOp&quot;);
 * myOp();
 * r.stop().record();
 * </pre>
 */
public class TimeWatch {

    // HELPER METHOD

    /**
     * Helper method to get a TimeWatch started.
     * @param op operation name. Used to aggregate statistics.
     * @return the timewatch instance created. To use to stop the count.
     */
    public static TimeWatch start(final String op) {
        return new TimeWatch(op).start();
    }

    // STAT RECORDING - CROSS-THREADS

    // for simplicity sake, we won't be double locking access to that table for initialation of stats per operation.
    // at worse we will be losing a few measures.
    // The easier is to run a single user once to insure all stat objects are created, and then run multiple users.
    /** Maps operation to a TimeStats, that summary all time measures for the whole application -(all threads). */
    private static Map<String, TimeStats> stats = new Hashtable<String, TimeStats>();
    private static Map<Thread, Map<String, TimeStats>> localstatsByTread = new Hashtable<Thread, Map<String, TimeStats>>();
    
    /**
     * Adds a record to the stats. Called from TimeWatch.record().
     * @param measure record to save.
     */
    private static void record(final TimeWatch measure) {
        // Get existing stat record for operation
        TimeStats s = (TimeStats) stats.get(measure.operation);

        // Lazy initialize stat record at the first record of an operation.
        // that is the part which is highly NOT thread-safe by choice !
        if (s == null) {
            s = new TimeStats();
            stats.put(measure.operation, s);
        }

        // that one is synchronized
        s.add(measure);
    }

    /**
     * Read recorded stats.
     * @return Mapping from Operation name (as given to start() method), to TimeStats data object.
     */
    public static Map<String, TimeStats> stats() {
        return Collections.unmodifiableMap(stats);
    }

    /**
     * Resets recorded times back to zero.
     */
    public static synchronized void resetStats() {
        stats = new Hashtable<String, TimeStats>();
    }

    // STAT RECORDING - PER-THREAD

    /**
     * Read locally recorded stats.
     * @return Mapping from Operation name (as given to start() method), to TimeStats data object.
     */
    private static Map<String, TimeStats> getLocalStats() {
    	Map<String, TimeStats> localstats = null;
    	if (localstatsByTread == null) {
    		localstats = localstatsByTread.get(Thread.currentThread());
    		if (localstats == null) {
    			localstats = new Hashtable<String, TimeStats>();
    			localstatsByTread.put(Thread.currentThread(), localstats);
    		}
        }
		return localstats;
    }


    /**
     * Adds a record to the local stats. Called from TimeWatch.record().
     * @param measure record to save.
     */
    private static void recordLocally(final TimeWatch measure) {
        // nothing threadsafe : we are only accessing ressource of current thread.
        Map<String, TimeStats> localStats = getLocalStats();

        TimeStats s = (TimeStats) localStats.get(measure.operation);

        // Lazy initialize stat record at the first record of an operation.
        if (s == null) {
            s = new TimeStats();
            localStats.put(measure.operation, s);
        }

        s.add(measure);
    }

    /**
     * Read recorded stats.
     * @return Mapping from Operation name (as given to start() method), to TimeStats data object.
     */
    public static Map<String, TimeStats> localStats() {
        return Collections.unmodifiableMap(getLocalStats());
    }

    /**
     * Resets recorded times back to zero.
     */
    public static synchronized void resetLocalStats() {
    	localstatsByTread.put(Thread.currentThread(), new Hashtable<String, TimeStats>());
    }

    /**
     * Print the stats.
     */
    public static final void printStats() {
        for (Iterator<String> it = TimeWatch.stats().keySet().iterator(); it.hasNext();) {
            String op = (String) it.next();
            System.out.println(op + "\t" + ((TimeStats) TimeWatch.stats().get(op)).toString());
        }
    }

    /**
     * Print the local stats.
     */
    public static final void printLocalStats() {
        for (Iterator<String> it = TimeWatch.localStats().keySet().iterator(); it.hasNext();) {
            String op = (String) it.next();
            System.out.println(op + "\t" + ((TimeStats) TimeWatch.stats().get(op)).toString());
        }
    }

    // INSTANCE
    /** Time when the Watch start. */
    private long started = 0;

    /** Time duration between a start and a stop. */
    private long duration = -1;

    /** The current operation name. */
    private final String operation;

    /**
     * Default constructor.
     * @param operation The current operation under watch !
     */
    public TimeWatch(final String operation) {
        this.operation = operation;
    }

    /**
     * @return this. (allow chain-call)
     */
    public final TimeWatch start() {
        started = System.currentTimeMillis();
        duration = -1;
        return this;
    }

    /**
     * @return this. (allow chain-call)
     */
    public final TimeWatch stop() {
        duration = System.currentTimeMillis() - started;
        return this;
    }

    /**
     * @return The operation name.
     */
    public final String operation() {
        return operation;
    }

    /**
     * @return The time when the watch was started.
     */
    public final long started() {
        return started;
    }

    /**
     * @return The time duration calculated between the start() and the stop() calls.
     */
    public final long duration() {
        return duration;
    }

    /**
     * Explicitly cancel last measure by setting duration to -2, when you release afterwards that what you measured was a failure.
     */
    public final void cancel() {
        duration = -2;
    }

    /**
     * @return this. (allow chain-call)
     */
    public final TimeWatch print() {
        System.out.println(toString());
        return this;
    }

    /**
     * @param prefix a prefix to be printed before the TimeWatch.
     * @return this. (allow chain-call)
     */
    public final TimeWatch print(final String prefix) {
        System.out.println(prefix + "\t" + toString());
        return this;
    }

    /**
     * Add the measure to statistics. See stats().
     * @return this. (allow chain-call)
     */
    public final TimeWatch record() {
        record(this);
        return this;
    }

    /**
     * Add the measure to statistics for current thread.
     * @return this. (allow chain-call)
     */
    public final TimeWatch recordLocally() {
        recordLocally(this);
        return this;
    }

    /**
     * @return The String representation of the time watch.
     */
    public final String toString() {
        return this.duration + "\t" + this.operation;
    }

    // STATS

    /**
     * Stat record for a single operation.
     */
    public static class TimeStats {
        private long started = -1;
        private int count = 0;

        private long min = Integer.MAX_VALUE; // max so that anything for sure will be smaller

        private long max = Integer.MIN_VALUE; // min so that anything will for sure be larger.

        private long total = 0;

        public final int count() {
            return count;
        }

        public final long max() {
            return max;
        }

        public final long min() {
            return min;
        }

        public final long total() {
            return total;
        }

        public final long average() {
            return total / count;
        }

        private synchronized void add(final TimeWatch w) {
            this.count++;
            this.max = Math.max(this.max, w.duration());
            this.min = Math.min(this.min, w.duration());
            this.total += w.duration();
            if (started == -1) {
                this.started = w.started();
            }
        }

        public String toString() {
            return started + "\t" + count() + "\t" + total() + "\t" + min() + "\t" + max() + "\t" + average();
        }
    }

}
