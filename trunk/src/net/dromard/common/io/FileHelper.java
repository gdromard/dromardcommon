package net.dromard.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * File Helper Class.
 * Helper Class for file management.
 * 
 * <br/>
 * <pre>
 *    +---------+
 *    | History |
 *    +---------+
 *
 *  [02/09/2006] by Gabriel Dromard
 *     - Add copy methods.
 *     
 * </pre>
 * <br/>
 * @author Gabriel Dromard
 * @version 1.0
 */
public class FileHelper {

    
    /**
     * Save one file
     * @param fileName    File name to be saved
     * @param fileContent File content
     * @throws FileNotFoundException 
     */
    public static final void saveToFile(String fileName, String fileContent) throws FileNotFoundException, IOException {
        saveToFile(new File(fileName), fileContent);
    }
    
    public static final void saveToFile(File file, String fileContent) throws FileNotFoundException, IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();
    }
    
    public static final String getFileContent(File file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuffer buf = new StringBuffer();
        String line;
        while((line = reader.readLine()) != null) buf.append(line+"\n");
        return buf.toString();
    }

	/**
	 * Add String at end of file
	 * @param fileName    File to append
	 * @param fileContent String content to insert at end of file
	 * @throws FileNotFoundException, IOException 
	 */
	public static void append2File(String fileName, String fileContent) throws FileNotFoundException, IOException {
	    FileWriter fw = new FileWriter(fileName, true);
	    fw.write(fileContent);
	    fw.close();
	}
	
	/**
	 * Retrieve extension file (ex: for 'file.txt' return 'txt')
	 * @param file File name
	 * @return File extension
	 */
	public static String getExtension(String file) {
		if(file != null) {
			int extensionIndex = file.lastIndexOf('.');
			if (extensionIndex < 0) return "";
			String extension = file.substring(extensionIndex + 1).toLowerCase();
			if (extension.indexOf("/") > -1 || extension.indexOf("\\") > -1) {
				return "";
			}
			return extension;
		}
		return null;
	}

	/**
	 * Retrieve extension file (ex: for 'file.txt' return 'txt')
	 * @param file File object
	 * @return File extension
	 */
	public static String getExtension(File file) {
		String name = file.getName();
		return getExtension(name);
	}

    /**
     * Delete a file or a directory (deleting recursivly all its content)
     * Returns true if all deletions were successful.
     * If a deletion fails, the method stops attempting to delete and returns false.
     */
    public static boolean delete(final File file) {
        if (file.isDirectory()) {
            String[] children = file.list();
            for (int i = 0; i < children.length; ++i) {
                boolean success = delete(new File(file, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return file.delete();
    }

	/**
	 * Copy a file from <code>source</code> to <code>destination</code>.
	 * Not that no checks has been done concerning file existance ...
	 * 
	 * @param source      The source File object
	 * @param destination The destination File object
	 * @param true if the copy succeed
	 */
    public static boolean copy(File source, File destination) {
    	try {
            // Create channel on the source
            FileChannel srcChannel = new FileInputStream(source).getChannel();
        
            // Create channel on the destination
            FileChannel dstChannel = new FileOutputStream(destination).getChannel();
        
            // Copy file contents from source to destination
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        
            // Close the channels
            srcChannel.close();
            dstChannel.close();
            return true;
        } catch (IOException e) {
        	e.printStackTrace();
        	return false;
        }
    }
}