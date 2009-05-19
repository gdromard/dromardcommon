package net.dromard.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
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
public final class FileHelper {
    /**
     * Private empty constructor. So as be a valid util class.
     */
    private FileHelper() {
    }

    /**
     * Write string output.
     * @param output   The Output stream, where to write data
     * @param content  The content string to be written
     * @param override Set it to true if you want to override the file if it already exists
     * @throws IOException While creating the file - if override is true - or while
     */
    public static void writeToFile(final File output, final String content, final boolean override) throws IOException {
        if (!output.exists()) {
            output.createNewFile();
        } else if (!override) {
            throw new IOException("File " + output.getAbsolutePath() + " already exists");
        }
        PrintStream printter = new PrintStream(new FileOutputStream(output));
        printter.print(content);
        printter.close();
    }

    /**
     * Save one file
     * @param fileName    File name to be saved
     * @param fileContent File content
     * @throws FileNotFoundException 
     */
    public static final void saveToFile(final String fileName, final String fileContent) throws FileNotFoundException, IOException {
        FileHelper.saveToFile(new File(fileName), fileContent);
    }

    public static final void saveToFile(final File file, final String fileContent) throws FileNotFoundException, IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();
    }

    public static final String getFileContent(final File file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuffer buf = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null)
            buf.append(line + "\n");
        return buf.toString();
    }

    /**
     * Add String at end of file
     * @param fileName    File to append
     * @param fileContent String content to insert at end of file
     * @throws FileNotFoundException, IOException 
     */
    public static void append2File(final String fileName, final String fileContent) throws FileNotFoundException, IOException {
        FileWriter fw = new FileWriter(fileName, true);
        fw.write(fileContent);
        fw.close();
    }

    /**
     * Retrieve extension file (ex: for 'file.txt' return 'txt')
     * @param file File name
     * @return File extension
     */
    public static String getExtension(final String file) {
        if (file != null) {
            int extensionIndex = file.lastIndexOf('.');
            if (extensionIndex < 0)
                return "";
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
    public static String getExtension(final File file) {
        String name = file.getName();
        return FileHelper.getExtension(name);
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
                boolean success = FileHelper.delete(new File(file, children[i]));
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
     * @throws IOException 
     */
    public static void copy(final File source, final File destination) throws IOException {
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        try {
            // Create channel on the source
            srcChannel = new FileInputStream(source).getChannel();

            // Create channel on the destination
            dstChannel = new FileOutputStream(destination).getChannel();

            // Copy file contents from source to destination
            srcChannel.transferTo(0, srcChannel.size(), dstChannel);
        } finally {
            // Close the channels
            if (srcChannel != null)
                srcChannel.close();
            if (dstChannel != null)
                dstChannel.close();
        }
    }

    public static boolean fileContentEquals(final InputStream textFile1, final InputStream textFile2) throws IOException {
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(textFile1));
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(textFile2));
        try {
            String line1;
            String line2;
            int lineCount = 0;
            do {
                line1 = reader1.readLine();
                line2 = reader2.readLine();
                ++lineCount;
                if (line1 != null && !line1.equals(line2)) {
                    if (line1.length() != line2.length()) {
                        System.out.println("Lines at " + lineCount + " differ in size: " + line1.length() + " != " + line2.length());
                    } else {
                        /**/
                        String begin = null;
                        for (int i = 0; i < line1.length(); ++i) {
                            begin = line1.substring(0, i);
                            if (!line2.startsWith(begin)) {
                                continue;
                            }
                        }
                        System.out.println("Character at " + begin.length() + " are different.");
                        System.out.println("\tLine1 (Characters from " + begin.length() + " to the end " + line1.length() + "): " + line1.substring(begin.length()));
                        System.out.println("\tLine2 (Characters from " + begin.length() + " to the end " + line2.length() + "): " + line2.substring(begin.length()));
                    }
                    return false;
                }
            } while (line1 != null && line2 != null);
            return (line1 == null && line2 == null);
        } finally {
            reader1.close();
            reader2.close();
        }
    }
}