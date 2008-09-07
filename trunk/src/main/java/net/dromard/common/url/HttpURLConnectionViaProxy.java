package net.dromard.common.url;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Date;

import net.dromard.common.io.StreamHelper;

import sun.net.www.http.HttpClient;
import sun.net.www.protocol.http.HttpURLConnection;

/**
 * URL connection Class, that use proxy connection
 * @author Gabriel Dromard
 * @version 1.0
 */
public class HttpURLConnectionViaProxy extends HttpURLConnection {
	String myProxyHost=null;
	int myProxyPort=-1;

	/**
	 * Constructor for the HttpURLConnectionViaProxy, this one does not use proxy.
	 * @param url       The URL where you whant to connect
	 * @param proxyHost The proxy host
	 * @param proxyPort The proxy port number
	 * @throws IOException
	 */
	public HttpURLConnectionViaProxy(URL url, String proxyHost, int proxyPort) throws IOException {
		super(url, proxyHost, proxyPort);
		if (proxyHost != null) {
			myProxyHost=proxyHost;
			myProxyPort=proxyPort;
		}
	}

	/**
	 * Constructor for the HttpURLConnectionViaProxy, this one does not use proxy.
	 * @param url The URL where you whant to connect
	 * @throws IOException
	 */
	public HttpURLConnectionViaProxy(URL url) throws IOException {
		super(url, null, -1);
	}

	/**
	 * Connection to the URL
	 * @throws IOException
	 */
	public void connect() throws IOException {
		try {
			super.connect();
		} catch (java.net.UnknownHostException ex) {
			ex.printStackTrace();
		}
		if (super.connected) return;
		http = new HttpClient(super.url, myProxyHost, myProxyPort);
		ps = (PrintStream) http.getOutputStream();
		super.connected = true;
	}

	/**
	 * Download the URL Connection to a file
	 * @param file The file where to download the URL
	 * @throws IOException
	 */
	public void download(File file) throws IOException {
		StreamHelper.streamCopier(getInputStream(), new FileOutputStream(file));
	}

	/**
	 * A class that displays information about a URL.
	 * Use the URLConnection class to get info about the URL
	 **/
	public void printinfo() throws IOException {
		// Display some information about the URL contents
		System.out.println("  Content Type:     "+getContentType());
		System.out.println("  Content Encoding: "+getContentEncoding());
		System.out.println("  Content Length:   "+getContentLength());
		System.out.println("  Date:             "+new Date(getDate()));
		System.out.println("  Last Modified:    "+new Date(getLastModified()));
		System.out.println("  Expiration:       "+new Date(getExpiration()));

		// If it is an HTTP connection, display some additional information.
		System.out.println("  Request Method:   "  +getRequestMethod());
		System.out.println("  Response Message: "+getResponseMessage());
		System.out.println("  Response Code:    "   +getResponseCode());
	}
}