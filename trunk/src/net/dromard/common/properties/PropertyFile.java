package net.dromard.common.properties;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Load a property file from disk (files or resources inner packages). This takes care of locale.
 * @see java.util.ResourceBundle
 * @see java.util.Properties
 * @author  Gabriel Dromard
 */
public class PropertyFile extends Properties {
	protected String filename = null;
	protected String filepath = "";
	protected String extention = null;
	protected Class requester = null;
    protected long fileLastModified = 0;
    protected boolean automaticReload = false;
    
    /**
     * If the resource is in the current directory ... it will take it !
     * If the resource is not in the current directory check in the requester package.
     * @throws SourcingException Thrown if the file can not be found or if an error occured while loading it or closing it.
     */
    public PropertyFile(Class requester, String resourceName) throws FileNotFoundException, IOException {
        super(); 
        this.filename = resourceName.substring(0, resourceName.lastIndexOf("."));
        this.extention = resourceName.substring(resourceName.lastIndexOf("."), resourceName.length());
        this.requester = requester;
        this.reload();
    }
	
    /**
     * If the resource is in the current directory ... take it !
     * If the resource is not in the current directory check in the requester package.
     * @throws SourcingException Thrown if the file can not be found or if an error occured while loading it or closing it.
     */
    public PropertyFile(File file) throws FileNotFoundException, IOException {
        super(); 
        if(!file.exists()) throw new FileNotFoundException();
        filename = file.getName().substring(0, file.getName().lastIndexOf("."));
        filepath = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(filename));
        extention = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length());
        reload();
    }
	
	/**
	 * Get fileName if exists
	 * @return filename if exists
	 */
	public String getFileName() {
		return filepath+filename+"."+extention;
	}
	
	/**
	 * This load or reload data from property.
	 * If <code>setProperty(key, value);</code> was called, all changes will be lost.
	 * If you want to make data persistant call <code>save()</code>
	 * @throws SourcingException Can be thrown if the property file is not found, or if loading failed
	 */
	public void reload() throws FileNotFoundException, IOException {
        // Load the properties from file (or resource)
	    InputStream in = getInputStream();
	    load(in);
	    in.close();
	}

    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns
     * <code>null</code> if the property is not found.
     * 
     * If Automatic Reload is set to true, and the proerties are stored from a 
     * file then before getting property from object a check of the file last
     * modification date is done, and if the file has been modified by third 
     * party the properties will be reload from file, than the value will be 
     * returned.
     *
     * @param   key   the property key.
     * @return  the value in this property list with the specified key value.
     * @see     java.util.Properties#getProperty
     * @see     java.util.Properties#setProperty
     * @see     java.util.Properties#defaults
     */
    public String getProperty(String key) {
        try {
            if(automaticReload && fileLastModified < (new File(filepath+filename+extention)).lastModified()) reload();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return super.getProperty(key);
    }

    /**
     * By default automatic reload is disabled.
     * @return  true if automatic reload is enabled.
     * @uml.property  name="automaticReload"
     */
    public boolean isAutomaticReload() {
        return automaticReload;
    }

    /**
     * Enable / disable automatic reload. Automatic reload is done by checking difference beetween the modification  date of cached file and the current date.
     * @uml.property  name="automaticReload"
     */
    public void setAutomaticReload(boolean automaticReload) {
        if(automaticReload) 
        this.automaticReload = automaticReload;
    }

    /**
     * Get the correct input stream.
     * @throws SourcingException Can be thrown if the property file is not found.
     */
    protected InputStream getInputStream() throws FileNotFoundException, IOException {
        // Declare input stream
        InputStream in = null;
        
        // Build file name
        //File file = getFileByLocale();
        File file = new File(filepath+filename+extention);

        
        // Test if file exists
        if(file.exists()) {
            if(file.exists()) fileLastModified = file.lastModified();
            in = new FileInputStream(file);
        // Try to found in package if the resource exists !
        } else {
            //URL url = getResourceByLocale();
            URL url = requester.getResource(filepath+filename+extention);
            
            // Yes resource found in package
            if(url != null) {
                fileLastModified = new File(url.getFile()).lastModified();
                in = url.openStream();
            } else {
                // File or resource not found
                throw new FileNotFoundException(file.getAbsolutePath());
            }
        }
        return in;
    }
    
    /**
	 * Get the file by locale.
     * If you want to load file <code>name.extension</code> it will search for files like
     * <code>name_FR.extension</code> or <code>name_EN.extension</code>.
	 * The one with the current locale is taken. If it does not exist it takes the <code>name.properties</code> one.
	 * @return The localized property file, the non localized property file, (even if the file does not exists).
	protected File getFileByLocale() {
        // File name in current locale
        File f = new File(filepath+filename+"_"+Locale.getDefault().getLanguage()+extention);
        if(!f.exists()) {
            // File name in any locale
            f = new File(filepath+filename+extention);
        }
    
        return f;
    }
     */
	
	/**
	 * Get the URL of one property resource in the requester's package.
	 * A locale check is done:
	 * If there are properties like <code>name_EN.properties, name_FR.properties, name.properties</code>
	 * The one with the current locale is taken. If it does not exist it takes the <code>name.properties</code> one.
	 * @return The URL of the resource or null if requester is null or if resource does not exist.
    protected URL getResourceByLocale() {
        // Return null if the requester is not set
		if(requester == null) return null;
        
		// File resource in current locale
        URL url = requester.getResource(filepath+filename+"_"+Locale.getDefault().getLanguage()+extention);
		if(url != null) return url;
		
		// File resource in any locale
		return requester.getResource(filepath+filename+extention);
	}
     */
}