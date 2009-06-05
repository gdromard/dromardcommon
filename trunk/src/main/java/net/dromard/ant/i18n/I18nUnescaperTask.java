package net.dromard.ant.i18n;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import net.dromard.common.io.StreamHelper;

import org.apache.tools.ant.Task;

/**
 * A properties duplicated value finder class.
 * @author Gabriel Dromard
 */
public class I18nUnescaperTask extends Task {
    /** The resourcesFolder. */
    private File resourcesFolder;

    /**
     * @param resourcesFolder the sourceFolder to set
     */
    public final void setResourcesFolder(final String resourcesFolder) {
        this.resourcesFolder = new File(resourcesFolder);
    }

    @Override
    public void execute() {
        for (String f : resourcesFolder.list()) {
            if (f.toLowerCase().endsWith("properties")) {
                File file = new File(resourcesFolder.getAbsolutePath() + File.separator + f);
                try {
                    String content = StreamHelper.getStreamContent(new FileInputStream(file), "ISO-8859-1");
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    writer.write(I18nUnescaperTask.escapeProperties(content));
                    writer.close();
                } catch (FileNotFoundException e) {
                    super.log(e, 0);
                } catch (IOException e) {
                    super.log(e, 0);
                }
            }
        }
    }

    private static String escapeProperties(final String toConvert) {
        char[] in = toConvert.toCharArray();
        int end = in.length;
        int off = 0;
        char aChar;
        char[] out = new char[end];
        int outLen = 0;

        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\' && in[off] == 'u') {
                aChar = in[off++];
                // Read the xxxx
                int value = 0;
                for (int i = 0; i < 4; i++) {
                    aChar = in[off++];
                    switch (aChar) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        value = (value << 4) + aChar - '0';
                        break;
                    case 'a':
                    case 'b':
                    case 'c':
                    case 'd':
                    case 'e':
                    case 'f':
                        value = (value << 4) + 10 + aChar - 'a';
                        break;
                    case 'A':
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'E':
                    case 'F':
                        value = (value << 4) + 10 + aChar - 'A';
                        break;
                    default:
                        throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                    }
                }
                out[outLen++] = (char) value;
            } else {
                out[outLen++] = aChar;
            }
        }
        return new String(out, 0, outLen);
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            args = new String[] { "../NDTKit trunk/src/resources" };
        }
        I18nUnescaperTask cleaner = new I18nUnescaperTask();
        cleaner.setResourcesFolder(args[0]);
        cleaner.execute();
    }
}
