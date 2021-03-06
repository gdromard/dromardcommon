package net.dromard.common.properties;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Load a property file from disk (files or resources inner packages). This takes care of locale. This is able to store data to disk even if data was load from a resource, in this case data will be saved into a file in current folder.
 * @see java.util.ResourceBundle
 * @see java.util.Properties
 * @author  Gabriel Dromard
 */
public class WritablePropertyFile extends PropertyFile {
    /** The serialVersionUID. */
    private static final long serialVersionUID = -7532580736632170542L;
    /** The autoCommit. */
    protected boolean autoCommit = false;
    /** The encoding. */
    protected String encoding = "8859_1";

    /**
     * If the resource is in the current directory ... it will take it !
     * If the resource is not in the current directory check in the requester package.
     * @throws SourcingException Thrown if the file can not be found or if an error occurred while loading it or closing it.
     */
    public WritablePropertyFile(final Class requester, final String resourceName) throws FileNotFoundException, IOException {
        super(requester, resourceName);
    }

    /**
     * If the resource is in the current directory ... take it !
     * If the resource is not in the current directory check in the requester package.
     * @throws SourcingException Thrown if the file can not be found or if an error occurred while loading it or closing it.
     */
    public WritablePropertyFile(final File file) throws FileNotFoundException, IOException {
        super(file);
    }

    /**
     * If the resource is in the current directory ... take it !
     * If the resource is not in the current directory check in the requester package.
     * @throws SourcingException Thrown if the file can not be found or if an error occurred while loading it or closing it.
     */
    public WritablePropertyFile(final File file, final String encoding) throws FileNotFoundException, IOException {
        super(file);
        this.encoding = encoding;
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
    public void setAutoCommit(final boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    /**
     * @param key The key to be placed into this property list.
     * @param value The value corresponding to key. 
     * @return the previous value of the specified key in this property list, or null if it did not have one.
     */
    @Override
    public Object setProperty(final String key, final String value) {
        Object o = super.setProperty(key, value);
        try {
            if (isAutoCommitEnabled()) {
                save();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return o;
    }

    /**
     * @return The file of this properties
     */
    public final File getFile() {
        return new File(filepath + filename + extention);
    }

    /**
     * Saving data to disk, can be done a call to <code>setProperty(key, value);</code>
     * @throws SourcingException Can be thrown if the property file is not found, or if storing failed
     */
    public void save() throws FileNotFoundException, IOException {
        //File file = getFileByLocale();
        File file = getFile();
        FileOutputStream oStream = null;
        // file does not exist create it !
        if (!file.exists()) {
            file.createNewFile();
        }
        // Initialisation du flux de sortie
        oStream = new FileOutputStream(file);
        // Enregistrement des donn�es
        store(new BufferedWriter(new OutputStreamWriter(oStream, encoding)), file.getName());
        // Fermeture du flux de sortie
        oStream.close();
        // Update last modified attribute
        fileLastModified = file.lastModified();
    }

    /* (non-Javadoc)
     * @see java.util.Hashtable#keys()
     */
    @Override
    public synchronized Enumeration keys() {
        Vector<String> orderedKeys = new Vector<String>();
        for (Object key : keySet()) {
            orderedKeys.add(key.toString());
        }
        Collections.sort(orderedKeys);
        return orderedKeys.elements();
    }
}