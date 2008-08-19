/**
 * File : TreeGridTagTest.java 12 juin 07
 */
package net.dromard.common.httpbench;

import java.util.ArrayList;

import net.dromard.common.util.TimeWatch;

/**
 * Testing AssemblyTreeGrid generation.
 */
public final class HttpBencher {
    /** Private constructor. */
    private HttpBencher() {
    }

    /**
     * Allows profiling from outside IDE. First command line argument to indicate test method.
     * @param args call arguments.
     */
    public static void main(final String[] args) {
        int thread = 20;// 1 20
        int nb = 20;
        int delay = 0;
        /**/
        System.out.println("All Pages Test (nbThread: " + thread + ", delay: " + delay + ")");
        bench(thread, delay, nb, OrkestraHttpScenariiTestsExample.ALL_PAGES_ACTIONS);
        /**
        System.out.println("Assembly View Test (nbThread: " + thread + ", delay: " + delay + ")");
        bench(thread, delay, nb, HttpScenariiTests.ASSEMBLY_VIEW_ACTIONS);
        /**
        System.out.println("Lead View Test (nbThread: " + thread + ", delay: " + delay + ")");
        bench(thread, delay, nb, HttpScenariiTests.LEAD_VIEW_ACTIONS);
        /**
        System.out.println("Assembly Tree Test (nbThread: " + thread + ", delay: " + delay + ")");
        bench(thread, delay, nb, HttpScenariiTests.ASSEMBLY_TREE_ACTIONS);
        /**
        System.out.println("Lead List Test (nbThread: " + thread + ", delay: " + delay + ")");
        bench(thread, delay, nb, HttpScenariiTests.LEADS_GRID_ACTIONS);
        /**/
        TimeWatch.printStats();
        TimeWatch.resetStats();
    }

    /**
     * Run a test on local server to AssemblyTree XML generation.
     * @param thread the number of thread to be ran
     * @param delay the delay between two thread start
     * @param nbTimes the number of times the scenario has to be executed
     * @param actions the set of actions to be ran
     */
    public static void bench(final int thread, final int delay, final int nbTimes, final int actions) {
        try {
            ArrayList processes = new ArrayList();
            for (int i = 0; i < thread; i++) {
                processes.add(new HttpScenariiTestRunner("Thread " + i, OrkestraHttpScenariiTestsExample.getActions(actions)));
            }
            ThreadDispatcher td = new ThreadDispatcher(processes, nbTimes);
            td.start(delay, 0);
            td.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
