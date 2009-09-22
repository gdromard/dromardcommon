package net.dromard.common.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO Comment here.
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public class ZipReader {
    /** The zipFile. */
    private File zipFile;
    /** The inputStream. */
    private ZipInputStream inputStream;
    /** The opened. */
    private boolean opened = false;

    public ZipReader(final File zipFile) throws IOException {
        this.zipFile = zipFile;
        reset();
    }

    public ZipReader(final String zipFile) throws IOException {
        this(new File(zipFile));
    }

    public ZipReader(final InputStream inputStream) throws IOException {
        this.inputStream = new ZipInputStream(inputStream);
        opened = true;
    }

    public void readAll(final ZipEntryReader reader) throws IOException {
        checkValidity();
        ZipEntry entry = null;
        while ((entry = inputStream.getNextEntry()) != null) {
            reader.read(inputStream, entry.getName());
        }
        close();
    }

    public List<String> getEntries() throws IOException {
        final List<String> entries = new ArrayList<String>();
        checkValidity();
        ZipEntry entry = null;
        while ((entry = inputStream.getNextEntry()) != null) {
            entries.add(entry.getName());
            inputStream.closeEntry();
        }
        close();
        return entries;
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

    private void checkValidity() throws IOException {
        if (!opened) {
            throw new IOException("Inputstream is closed.");
        }
    }

    private void reset() throws IOException {
        if (zipFile == null) {
            throw new IOException("Can not reset inputstream (file undefined).");
        }
        inputStream = new ZipInputStream(new FileInputStream(zipFile));
        opened = true;
    }

    public void close() throws IOException {
        if (opened) {
            opened = false;
            inputStream.close();
        }
    }

    /**
     * Static method that show the content of a ZIP.
     * @param zipFilePath
     * @throws IOException
     */
    public static final void showZipContent(final String zipFilePath) throws IOException {
        ZipReader zip = new ZipReader(new FileInputStream(new File(zipFilePath)));
        for (String entry : zip.getEntries()) {
            System.out.println(entry);
        }
        zip.close();
    }

    /**
     * Static method that show the content of a ZIP.
     * @param zipFilePath
     * @throws IOException
     */
    public static final void extract(final String zipFilePath, final String target, final String... entries) throws IOException {
        ZipReader zip = new ZipReader(new FileInputStream(new File(zipFilePath)));
        zip.readAll(new ZipEntryExtractor(target, entries));
        zip.close();
    }

    public static void main(final String[] args) throws IOException {
        String zipPath = "test.zip";
        ZipReader.extract(zipPath, "temp");
        //showZipContent(zipPath);
    }

    /**
     * A ZipEntryReader that extract all entries into a targeted folder.
     * {@inheritDoc}
     */
    public static class ZipEntryExtractor extends ZipEntryReader {
        /** The target. */
        private final String target;
        /** The entries. */
        private List<String> entries = null;
        /** The extractedFiles. */
        private final Set<File> extractedFiles = new HashSet<File>();

        public ZipEntryExtractor(final String target, final String... entries) {
            this(target, (entries == null ? null : Arrays.asList(entries)));
        }

        public ZipEntryExtractor(final String target, final List<String> entries) {
            this.target = target;
            this.entries = entries;
        }

        /** {@inheritDoc} */
        @Override
        public void read(final InputStream input, final String name) {
            try {
                if (entries == null || entries.size() == 0) {
                    getExtractedFiles().add(extractTo(getTarget() + File.separator + name, input));
                    System.out.println(name + " extracted");
                } else {
                    for (String entry : entries) {
                        if (name.matches(entry.replaceAll("\\*", ".*").replaceAll("[.][.]", "."))) {
                            getExtractedFiles().add(extractTo(getTarget() + File.separator + name, input));
                            System.out.println(name + " extracted");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public List<File> getExtractedFiles() {
            return new ArrayList<File>(extractedFiles);
        }

        /**
         * @param entries the entries to set
         */
        public void setEntries(final List<String> entries) {
            this.entries = entries;
        }

        /**
         * @return the entries
         */
        public List<String> getEntries() {
            return entries;
        }

        /**
         * @return the target
         */
        public String getTarget() {
            return target;
        }
    }

    /**
     * A Zip Entry reader abstract class.
     * @see Zip
     * @author Gabriel Dromard
     */
    public abstract static class ZipEntryReader {
        /**
         * This method is called while reading entries, when using the new {@link Zip#Zip(InputStream, ZipEntryReader)}.
         * @param input     The entry input stream
         * @param entryName The entry name
         */
        public abstract void read(final InputStream input, final String name);

        /**
         * Extract an entry.
         * @param name The targeted name.
         * @param input The inputstream of ZIP
         * @return The extracted file.
         */
        public File extractTo(final String name, final InputStream input) throws IOException {
            File file = new File(name);
            file.getParentFile().mkdirs();
            OutputStream out = new FileOutputStream(file);
            int len;
            byte[] b = new byte[1024];
            while ((len = input.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.close();
            return file;
        }
    }
}