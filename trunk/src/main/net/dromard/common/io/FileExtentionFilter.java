package net.dromard.common.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;


/**
 * This Class is an implementation of FilenameFilter interface.
 * 
 * <br/>
 * 
 * <pre>
 *     +---------+
 *     | History |
 *     +---------+
 * 
 *   [14 mai 07] by Gabriel Dromard
 *      - Creation.
 * </pre>
 * 
 * <br/>
 * @author Gabriel Dromard
 */
public class FileExtentionFilter implements FilenameFilter {
    /** Possible extensions. The filtered extentsions. */
    List<String> possibleExt;

    /**
     * Construct an instance of FileExtentionFilter with a set of extension to filter.
     * @param extensions The extensions list. (Without the dot character)
     */
    public FileExtentionFilter(List<String> extensions) {
        possibleExt = extensions;
    }


    /**
     * Construct an instance of FileExtentionFilter with an empty set of extension to filter.
     * You have to use the addExtention() method.
     * @see #addExtention(String)
     */
    public FileExtentionFilter() {
        possibleExt = new ArrayList<String>();
    }
    
    /**
     * Add an extension to the filter instance.
     * @param extension The extension to be added to the filter.
     */
    public void addExtention(String extension) {
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
