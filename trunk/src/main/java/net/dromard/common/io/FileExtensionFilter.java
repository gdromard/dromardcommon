package net.dromard.common.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;


/**
 * This Class is an implementation of FilenameFilter interface.
 * <br/>
 * @author Gabriel Dromard
 */
public class FileExtensionFilter implements FilenameFilter {
    /** Possible extensions. The filtered extensions. */
    List<String> possibleExt;


    /**
     * Construct an instance of FileExtensionFilter with an empty set of extension to filter.
     * You have to use the addExtension() method.
     * @see #addExtension(String)
     */
    public FileExtensionFilter() {
        possibleExt = new ArrayList<String>();
    }
    
    /**
     * Construct an instance of FileExtensionFilter with a set of extension to filter.
     * @param extensions The extensions list. (Without the dot character)
     */
    public FileExtensionFilter(String... extensions) {
    	this();
    	for (String extension: extensions) {
    		addExtension(extension);
    	}
    }
    
    /**
     * Construct an instance of FileExtensionFilter with a set of extension to filter.
     * @param extensions The extensions list. (Without the dot character)
     */
    public FileExtensionFilter(List<String> extensions) {
    	possibleExt = extensions;
    }
    
    /**
     * Add an extension to the filter instance.
     * @param extension The extension to be added to the filter.
     */
    public void addExtension(String extension) {
        possibleExt.add(extension);
    }

    /**
     * Implementation of the FileNameExtensionFilter.accept(File) method. 
     * @see FileNameExtensionFilter#accept(File)
     * @param file The file to test
     * @param fileName The file name
     */
    public boolean accept(File file, String fileName) {
        if (new File(file, fileName).isDirectory())
            return false;
        String ext = fileName.toLowerCase().substring(fileName.lastIndexOf(".") + 1);
        return possibleExt.contains(ext.toLowerCase());
    }
}
