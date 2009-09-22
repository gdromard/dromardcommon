/**
 * 	File : AssemblyTreePerformanceTest.java 17 juin 08
 */
package net.dromard.common.httpbench;

import java.util.List;

import net.dromard.common.util.HTTPSession;

/**
 * The HttpScenariiTest Runner.
 * <br>
 * @author Gabriel Dromard
 */
public class HttpScenariiTestRunner implements Runnable {

    /** The current process name. */
    private String processName;
    /** The current list of actions to be executed. */
    private List actions;
    /** The Session object. */
    private HTTPSession session;

    /**
     * Constructor.
     * @param processName The processor name
     * @param actions     The action list
     */
    public HttpScenariiTestRunner(final String processName, final List actions) {
        this.processName = processName;
        this.actions = actions;
        session = new HTTPSession("jsessionid");
    }

    /** @return the current process name. */
    public final String getProcessName() {
        return processName;
    }

    /**
     * Execute the actions.
     */
    public final void run() {
        try {
            for (int i = 0; i < actions.size(); ++i) {
                HttpRequestAction action = (HttpRequestAction) actions.get(i);
                if (action.isEnabled()) {
                    action.run(session, getProcessName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
