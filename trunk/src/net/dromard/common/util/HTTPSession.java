/**
 * 	File : HTTPSession.java 17 juin 08
 */
package net.dromard.common.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.dromard.common.io.StreamHelper;

/**
 * A HTTP Session helper class that is able to call request throw a server maintaining the session.
 * TODO Remove the dependency with the sessionIdIdentifier by handling the sessionId (who is in URL) using cookie.
 * <br>
 * @author Gabriel Dromard
 */
public class HTTPSession {
    /** The session id identifier to be put in URL request so as to handle session with server. */
    private String sessionIdIdentifier = null;
    /** The session id of the current session. (one session per instance) */
    private String sessionId = null;
    /** Do we handle the session id as a cookie ? */
    private boolean cookie = false;

    /**
     * Create a new session.
     * @param sessionIdIdentifier The session id identifier. Generally for JAVA web application its 'jsessionid'.
     */
    public HTTPSession(final String sessionIdIdentifier) {
        this.sessionIdIdentifier = sessionIdIdentifier;
    }

    /**
     * @return The session id of the current session.
     */
    public final String getSessionId() {
        return sessionId;
    }

    /**
     * Request an URL to the server.
     * @param url The home page URL of the server
     * @param outputStream If you want to store the result given by the server into a file.
     * @return The URLConnection object
     * @exception Exception If an error occurred with the server or while storing the response.
     */
    public final HttpURLConnection request(final String url, final OutputStream outputStream) throws Exception {
        return request(url, outputStream, true);
    }

    /**
     * Request an URL to the server.
     * @param url The home page URL of the server
     * @param outputStream If you want to store the result given by the server into a file.
     * @param followRedirects Does the connection has to follow redirects ?
     * @return The URLConnection object
     * @exception Exception If an error occurred with the server or while storing the response.
     */
    public final HttpURLConnection request(final String url, final OutputStream outputStream, final boolean followRedirects) throws Exception {
        String sessionUrl = url;
        // Construct URL by including session ID
        if (!cookie && sessionId != null) {
            // Verify prerequisites
            if (sessionIdIdentifier == null) {
                throw new Exception("Session ID identifier has to be set properly");
            }
            if (sessionUrl.indexOf("?") > -1) {
                sessionUrl = StringHelper.replaceFirst(sessionUrl, "?", ";" + sessionIdIdentifier + "=" + sessionId + "?");
            } else {
                sessionUrl += ";" + sessionIdIdentifier + "=" + sessionId;
            }
        }
        // Open connection
        HttpURLConnection conn = (HttpURLConnection) new URL(sessionUrl).openConnection();
        HttpURLConnection.setFollowRedirects(followRedirects);
        if (cookie) {
            conn.setRequestProperty("Cookie", sessionId);
        }
        // Ask response to server
        if (outputStream != null) {
            StreamHelper.streamCopier(conn.getInputStream(), outputStream);
        } else {
            conn.getInputStream();
        }
        // Retrieve session ID if not yet done !conn.getContentLength()
        if (sessionId == null) {
            if (conn.getURL().toString().indexOf(sessionIdIdentifier) > -1) {
                sessionId = StringHelper.subStringBeetwen(conn.getURL().toString(), ";" + sessionIdIdentifier + "=", "?");
                cookie = false;
            } else if (conn.getHeaderField("Set-Cookie") != null) {
                sessionId = conn.getHeaderField("Set-Cookie").substring(0, conn.getHeaderField("Set-Cookie").indexOf(";"));
                cookie = true;
            }
        }
        // Return connection for deeper information
        return conn;
    }

    /**
     * Usage example of HTTPSession class.
     * @param args not used.
     */
    public static void main(final String[] args) {
        String baseUrl = "http://localhost:8080/myApp/";
        String loginUrl = "http://localhost:8080/myApp/login.action?username=user&password=*****";
        String actionUrl = "http://localhost:8080/myApp/my.action";

        try {
            HTTPSession session = new HTTPSession("jsessionid");
            // Don't need to trace the result (its why we put null)
            session.request(baseUrl, null);
            // Don't need to trace the result (its why we put null)
            session.request(loginUrl, null);
            // If you need to retrieve the result in a file
            HttpURLConnection connection = session.request(actionUrl, new FileOutputStream("myActionResult.html"));
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Server response is valid ... now you just have to verify that its functionaly valid !");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


