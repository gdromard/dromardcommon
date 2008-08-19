/**
 * 	File : Action.java 25 juin 08
 */
package net.dromard.common.httpbench;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import net.dromard.common.util.HTTPSession;
import net.dromard.common.util.StringHelper;
import net.dromard.common.util.TimeWatch;

/**
 * An HttpRequest Action handler.
 * <br>
 * @author Gabriel Dromard
 */
public class HttpRequestAction {
    /** The action name. */
    private String name = null;
    /** The action URL. */
    private String url = null;
    /** The action result size. */
    private int size = -1;
    /** Can the action be executed more than one times ? */
    private boolean multipleExecutionEnabled = true;
    /** Is this action enabled ? */
    private boolean enabled = true;

    /**
     * Constructor.
     * @param urlContext The context of the URL action.
     * @param url The URL action.
     */
    public HttpRequestAction(final String urlContext, final String url) {
        this.name = url;
        if (name.indexOf('?') > -1) {
            name = name.substring(0, name.indexOf('?'));
        }
        if (url.indexOf("method=") > -1) {
            name += "(" + StringHelper.subStringBeetwen(url, "method=", "&") + ")";
        }
        this.url = urlContext + url;
    }

    /**
     * Constructor.
     * @param urlContext The context of the URL action.
     * @param url The URL action.
     * @param multipleExecutionEnabled Can the action be executed more than one times ?
     */
    public HttpRequestAction(final String urlContext, final String url, final boolean multipleExecutionEnabled) {
        this(urlContext, url);
        this.multipleExecutionEnabled = multipleExecutionEnabled;
    }

    /**
     * @return Can the action be executed more than one times ?
     */
    public final boolean isEnabled() {
        return enabled;
    }

    /**
     * Run the action.
     * @param session The HTTPSession in which to execute the action.
     * @param processName The Process name that run the action.
     * @throws Exception If an exception occur during action
     */
    public final void run(final HTTPSession session, final String processName) throws Exception {
        if (!enabled) {
            throw new Exception("This action is not enabled, and can not be ran !");
        }
        ByteArrayOutputStream is = new ByteArrayOutputStream();
        TimeWatch w = TimeWatch.start(name);
        session.request(url, is);
        w.stop().record();
        is.flush();
        String fileNamePrefix = "TEST/AllPages";
        if (new String(is.toByteArray()).indexOf("unexpected error") > -1) {
            fileNamePrefix += ".ERROR";
        } else {
            if (size == -1) {
                size = is.size();
            } else if (size != is.size()) {
                fileNamePrefix += ".SIZE diff " + size + "-" + is.size();
            }
        }
        FileOutputStream os = new FileOutputStream(fileNamePrefix + "." + name + ".[" + w.duration() + "]." + processName + ".[" + w.started() + "].txt");
        is.writeTo(os);
        os.flush();
        os.close();
        if (!multipleExecutionEnabled) {
            enabled = false;
        }
        is.close();
    }
}

