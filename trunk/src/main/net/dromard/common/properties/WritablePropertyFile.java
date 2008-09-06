package net.dromard.common.properties;


import java.io.*;

/**
 * Load a property file from disk (files or resources inner packages). This takes care of locale. This is able to store data to disk even if data was load from a resource, in this case data will be saved into a file in current folder.
 * @see java.util.ResourceBundle
 * @see java.util.Properties
 * @author  Gabriel Dromard
 */
public class WritablePropertyFile extends PropertyFile {
    protected boolean autoCommit = false;
	
    /**
     * If the resource is in the current directory ... it will take it !
     * If the resource is not in the current directory check in the requester package.
     * @throws SourcingException Thrown if the file can not be found or if an error occured while loading it or closing it.
     */
    public WritablePropertyFile(Class requester, String resourceName) throws FileNotFoundException, IOException {
        super(requester, resourceName);
    }
    
    /**
     * If the resource is in the current directory ... take it !
     * If the resource is not in the current directory check in the requester package.
     * @throws SourcingException Thrown if the file can not be found or if an error occured while loading it or closing it.
     */
    public WritablePropertyFile(File file) throws FileNotFoundException, IOException {
        super(file);
    }
	
	/**
	 * Get auto commit status
     * By default auto commit is disabled.
	 * @return a boolean saying if auto commit is true or false
	 */
	public boolean isAutoCommitEnabled() {
		return autoCommit;
	}
	
	/**
     * Set auto commit to true/false.  If false you have to call method save. If true methods save is call each time you call the setProperty one By default auto commit is disabled. BE WARE to not use auto commit AND auto reload ... In some cases you can loose data.
     * @param autoCommit  A boolean to set if auto commit is enable or not.
     * @uml.property  name="autoCommit"
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
	 * Saving data to disk, can be done a call to <code>setProperty(key, value);</code>
	 * @throws SourcingException Can be thrown if the property file is not found, or if storing failed
	 */
	public void save() throws FileNotFoundException, IOException {
		//File file = getFileByLocale();
        File file = new File(filepath+filename+extention);
		FileOutputStream oStream = null;
		// file does not exist create it !
		if(!file.exists()) file.createNewFile();
		// Initialisation du flux de sortie
		oStream = new FileOutputStream(file);
		// Enregistrement des données
		store(oStream, file.getName());
		// Fermeture du flux de sortie
		oStream.close();
        // Update last modified attribute
        fileLastModified = file.lastModified();
	}
}