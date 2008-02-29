/*
 * File Name    : ResourceFileBundle.java
 * Creation Date: 19/03/2004
 *    
 *   +---------+
 *   | History |
 *   +---------+
 * 
 * 27/07/2004 : 
 *  - Add methods getInputStream()
 *  - Remove methods load(File) & laod(URL)
 *  - Change reload() to apply precedent changes 
 */
package net.dromard.common.util;

// Java
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Load a property file from disk (files or resources inner packages).
 * This takes care of locale.
 * This is able to store data to disk even if data was load from a resource,
 * data will be saved into a file in current folder.
 * @see java.util.ResourceBundle
 * @see java.util.Properties
 * @author Gabriel Dromard
 */
public class ResourceFileBundle extends Properties {
	protected String filename = null;
	protected String filepath = "";
	protected String extention = null;
	protected Class requester = null;
	protected boolean autoCommit = true;
	
	/**
	 * Initialise the ResourFileBundle
	 * @see #getResourceFileBundle
	 */
	protected ResourceFileBundle() { super(); }
	
	/**
	 * If the resource is in the current directory ... take it !
	 * If the resource is not in the current directory check in the requester package.
	 * @throws SourcingException Thrown if the file can not be found or if an error occured while loading it or closing it.
	 */
	public static ResourceFileBundle getResourceFileBundle(Class requester, String resourceName) throws FileNotFoundException, IOException {
		ResourceFileBundle toReturn = new ResourceFileBundle();
		toReturn.filename = resourceName.substring(0, resourceName.lastIndexOf("."));
		toReturn.extention = resourceName.substring(resourceName.lastIndexOf("."), resourceName.length());
		toReturn.requester = requester;
		toReturn.reload();
		return toReturn;
	}
	
	/**
	 * If the resource is in the current directory ... take it !
	 * If the resource is not in the current directory check in the requester package.
	 * @throws SourcingException Thrown if the file can not be found or if an error occured while loading it or closing it.
	 */
	public static ResourceFileBundle getResourceFileBundle(File file) throws FileNotFoundException, IOException {
	    if(!file.exists()) throw new FileNotFoundException();
		ResourceFileBundle toReturn = new ResourceFileBundle();
		toReturn.filename = file.getName().substring(0, file.getName().lastIndexOf("."));
		toReturn.filepath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(toReturn.filename));
		toReturn.extention = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length());
		toReturn.reload();
		return toReturn;
	}
	
	/**
	 * Get auto commit status
	 * @return a boolean saying if auto commit is true or false
	 */
	public boolean isAutoCommitEnabled() {
		return autoCommit;
	}
	
	/**
	 * Set auto commit to true/false. 
	 * If false you have to call method save.
	 * If true methods save is call each time you call the setProperty one
	 * @param autoCommit A boolean to set if auto commit is enable or not.
	 */
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}
	
	/**
	 * @param key The key to be placed into this property list.
     * @param value The value corresponding to key. 
     * @return the previous value of the specified key in this property list, or null if it did not have one.
	 */
	public Object setProperty(String key, String value) {
		Object o = super.setProperty(key, value);
		try {
			if(isAutoCommitEnabled()) save();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return o;
	}
	
	/**
	 * Get fileName if exists
	 * @return filename if exists
	 */
	public String getFileName() {
		return filepath+filename+"."+extention;
	}
	
	/**
	 * Saving data to disk, can be done a call to <code>setProperty(key, value);</code>
	 * @throws SourcingException Can be thrown if the property file is not found, or if storing failed
	 */
	public void save() throws FileNotFoundException, IOException {
		File file = getFileByLocale();
		FileOutputStream oStream = null;
		// file does not exist create it !
		if(!file.exists()) file.createNewFile();
		// Initialisation du flux de sortie
		oStream = new FileOutputStream(file);
		// Enregistrement des données
		store(oStream, file.getName());
		// Fermeture du flux de sortie
		oStream.close();
	}
	
	/**
	 * This load or reload data from property.
	 * If <code>setProperty(key, value);</code> was called, all changes will be lost.
	 * If you want to make data persistant call <code>save()</code>
	 * @throws SourcingException Can be thrown if the property file is not found, or if loading failed
	 */
	public void reload() throws FileNotFoundException, IOException {
	    InputStream in = getInputStream();
	    load(in);
	    in.close();
	}
	
	/**
	 * Get the correct input stream.
	 * @throws SourcingException Can be thrown if the property file is not found.
	 */
	public InputStream getInputStream() throws FileNotFoundException, IOException {
		// Build file name
		File file = getFileByLocale();
		// File exists
		if(file.exists()) return new FileInputStream(file);
		
		// Try to found in package if there is something !
		URL url = getResourceByLocale();
		// Yes resource found in package
		if(url != null) return url.openStream();

		// If we are here it is not normal ...
		throw new FileNotFoundException(file.getAbsolutePath());
	}
	
	/**
	 * This will load the property file from an URL
	 * @param url the URL representing the resource
	 * @throws SourcingException Can be thrown if the property file is not found, or if storing failed
	protected void load(URL url) throws FileNotFoundException, IOException {
		load(url.openStream());
	}
	
	/**
	 * This will load the property file from a file
	 * @param file The property File
	 * @throws SourcingException Can be thrown if the property file is not found, or if storing failed
	protected void load(File file) throws FileNotFoundException, IOException {
		if(!file.exists()) file.createNewFile();
		load(new FileInputStream(file));
	}
	 */
	
	/**
	 * Get the file of one property File.
	 * A locale check is done:
	 * If there are files like <ode>name_EN.properties, name_FR.properties, name.properties</code>
	 * The one with the current locale is taken. If it does not exist it takes the <code>name.properties</code> one.
	 * @return The URL of the resource or null if requester is null or if resource does not exist
	 */
	protected File getFileByLocale() {
		// File name in current locale
		File f = new File(filepath+filename+"_"+Locale.getDefault().getLanguage()+extention);
		if(f.exists()) return f;
		// File name in no locale
		return new File(filepath+filename+extention);
	}
	
	/**
	 * Get the URL of one property resource in the requester's package.
	 * A locale check is done:
	 * If there are properties like <ode>name_EN.properties, name_FR.properties, name.properties</code>
	 * The one with the current locale is taken. If it does not exist it takes the <code>name.properties</code> one.
	 * @return The URL of the resource or null if requester is null or if resource does not exist
	 */
	protected URL getResourceByLocale() {
		if(requester == null) return null;
		// File resource in current locale
		URL url = requester.getResource(filepath+filename+"_"+Locale.getDefault().getLanguage()+extention);
		if(url != null) return url;
		
		// File resource in any locale
		return requester.getResource(filepath+filename+extention);
	}
}