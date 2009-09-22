package net.dromard.common.util;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.util.*;

/**
 * @author Gabriel Dromard
 * @version 1.0
 */
public class Helper {
	/**
	 * Ask for resource file.<br>
	 * This will first try to get the resource file on current directory.<br>
	 * If no files found, it will ask the requester class to get the resource.
	 * In this case, the resource must located on the same package of the requester class.
	 * @param requester Class requester
	 * @param fileName Property file name (without the extention '.properties').
	 * @return An external resource if exists, an internal resource, if not.
	 * @throws MissingResourceException Thrown if the resource is not found.
	 */
	public static final ResourceBundle getResourceBundle(Class requester, String fileName) {
		// That means that the resource fileName does not exist.
		// System.out.println("e: "+e);
		try {
			//System.out.println("Search for resource file = "+fileName+"_"+Locale.getDefault().getLanguage()+".properties");
			File f = new File(fileName+"_"+Locale.getDefault().getLanguage()+".properties");
			if(!f.exists()) f = new File(fileName+".properties");
			if(f.exists()) {
				ResourceBundle r=new PropertyResourceBundle(f.toURI().toURL().openStream());
				if(r!=null) return r;
			}
		} catch(Exception ex) {
			// That means that the resource fileName does not exist.
			System.err.println("Can't find bundle for base name "+fileName+", locale "+Locale.getDefault().getLanguage());
			ex.printStackTrace();
		}
		try {
			return ResourceBundle.getBundle(requester.getPackage().getName()+"."+fileName);
		} catch(Exception ex) {
			// That means that the resource fileName does not exist.
			System.err.println("[Helper] <getResourceBundle> Use of requester.getPackage().getName() returned: "+requester.getPackage().getName());
			ex.printStackTrace();
		}
		try {
			String sPackage = requester.getName();
			sPackage = sPackage.substring(0, sPackage.lastIndexOf('.'));
			System.err.println("[Helper] <getResourceBundle> sPackage: "+sPackage);
			return ResourceBundle.getBundle(sPackage+"."+fileName);
		} catch(Exception ex) {
			// That means that the resource fileName does not exist.
			System.err.println("[Helper] <getResourceBundle> Use of requester.getName() returned: "+requester.getName());
			ex.printStackTrace();
		}
		return null;
	}

	public static final URL getResource(Class requester, String fileName) throws Exception {
		String filename = fileName.substring(0, fileName.lastIndexOf("."));
		String extention = fileName.substring(fileName.lastIndexOf("."), fileName.length());

		// File name in current locale
		File f = new File(filename+"_"+Locale.getDefault().getLanguage()+extention);
		if(f.exists()) return f.toURI().toURL();

		// File name in any locale
		f = new File(fileName);
		if(f.exists()) return f.toURI().toURL();

		// File resource in current locale
		URL url = requester.getResource(filename+"_"+Locale.getDefault().getLanguage()+extention);
		if(url != null) return url;

		// File resource in any locale
		return requester.getResource(fileName);
	}

	/**
	 * This static method get the content from an url.
	 * @param url The URL from which to read data.
	 * @throws IOException Thrown if un error occured while loading the URL content.
	 */
	public static final String getURLContent(URL url) throws IOException {
		return getStreamContent(url.openStream());
	}

	/**
	 * This static method get the content from an input stream
	 * @param in The InputStream, where to read data.
	 * @throws IOException Thrown  if un error occured while loading the stream content.
	 */
	public static final String getStreamContent(InputStream in) throws IOException {
		String content = "";
		int len;
		byte[] b=new byte[1024];
		while((len=in.read(b))!=-1) content += new String(b, 0, len);
		in.close();
		return content;
	}

	/**
	 * Get the current date into a comprehensive string
	 * @return Get the current date and time into a comprehensive string
	 */
	public static final String getCurrentDate() {
		return DateFormat.getDateInstance(DateFormat.SHORT).format(new java.util.Date(System.currentTimeMillis()));	
	}
	
	/**
	 * Get the current time into a comprehensive string
	 * @return Get the current date and time into a comprehensive string
	 */
	public static final String getCurrentTime() {
		return DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new java.util.Date(System.currentTimeMillis()));	
	}
	
	/**
	 * This process a template file to fill in the values. (replacing fields by values)
	 * @param templateURL The URL to the template file.
	 * @param fields      The fields in the template.
	 * @param values      The values of each fields, to fill in the template.
	 * @return The template filled with the given values
	 * @throws IOException Thrown if an error occured while loading the template.
	 */
	public static final String convertTemplate(URL templateURL, String[] fields, String[] values) throws IOException {
		return StringHelper.replace(getURLContent(templateURL), fields, values);
	}
}