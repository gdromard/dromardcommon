package net.dromard.common.io;

// Java
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Stream Helper class.
 * @author Gabriel Dromard
 * @version 1.0
 */
public final class StreamHelper {
    /**
     * Private empty constructor. So as be a valid util class.
     */
    private StreamHelper() {
        /* Private empty constructor. So as be a valid util class. */
    }

    /** Buffer size. */
    private static final int BUFFER_SIZE = 1024;
    /**
     * This static method copy the input stream into the output stream.
     * @param in The InputStream, where to read data
     * @param out The Output stream, where to write data
     * @throws IOException Occured if you did a mistake in the given parameters ...
     */
    public static void streamCopier(final InputStream in, final OutputStream out) throws IOException {
        int len;
        byte[] b = new byte[BUFFER_SIZE];
        while ((len = in.read(b)) != -1) {
            out.write(b, 0, len);
        }
    }

    /**
     * This static method get the content from an input stream.
     * @param in The InputStream, where to read data.
     * @return The String representation of the stream.
     * @throws IOException Occured if you did a mistake in the given parameters ...
     */
    public static String getStreamContent(final InputStream in) throws IOException {
        StringBuffer content = new StringBuffer();
        int len;
        byte[] b = new byte[BUFFER_SIZE];
        while ((len = in.read(b)) != -1) {
            content.append(new String(b, 0, len));
        }
        return content.toString();
    }
}
