package net.dromard.common.zip;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;

/**
 * A zip reader class.
 * @author Gabriel Dromard
 * 30 juil. 2009
 */
public class ZipReader {
    /** The zipFile. */
    private final File zipFile;
    /** The ZipFile object. */
    private ZipFile zip;
    /** The inputStream. */
    private ZipInputStream inputStream;
    /** The opened. */
    private boolean opened = false;
    /** The zipFileMode. */
    private boolean zipFileMode = true;

    public ZipReader(final File zipFile) throws IOException {
        this.zipFile = zipFile;
        reset();
    }

    public ZipReader(final String zipFile) throws IOException {
        this(new File(zipFile));
    }

    public List<String> getEntries() throws IOException {
        final List<String> entries = new ArrayList<String>();
        checkValidity();
        if (zipFileMode) {
            for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
                entries.add(ZipReader.getZipEntryName(e.nextElement().getName()));
            }
            if (entries.size() == 0) {
                close();
                zipFileMode = false;
                reset();
            }
        }
        if (!zipFileMode) {
            ZipEntry entry = null;
            while ((entry = inputStream.getNextEntry()) != null) {
                entries.add(entry.getName());
                inputStream.closeEntry();
            }
        }
        close();
        return entries;
    }

    public void readAll(final ZipEntryReader reader) throws IOException {
        checkValidity();
        ZipEntry entry = null;
        if (zipFileMode) {
            for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
                entry = e.nextElement();
                InputStream entryInputStream = zip.getInputStream(entry);
                if (entryInputStream == null) {
                    close();
                    zipFileMode = false;
                    reset();
                    break;
                }
                reader.read(entryInputStream, ZipReader.getZipEntryName(entry.getName()), entry.isDirectory());
            }
        }
        if (!zipFileMode) {
            while ((entry = inputStream.getNextEntry()) != null) {
                reader.read(inputStream, entry.getName(), entry.isDirectory());
                inputStream.closeEntry();
            }
        }
        close();
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
        if (zipFileMode) {
            zip = new ZipFile(zipFile);
            inputStream = null;
        } else {
            inputStream = new ZipInputStream(new FileInputStream(zipFile));
            zip = null;
        }
        opened = true;
    }

    public void close() throws IOException {
        if (opened) {
            opened = false;
            if (zipFileMode) {
                zip.close();
            } else {
                inputStream.close();
            }
        }
    }

    /**
     * Static method that show the content of a ZIP.
     * @param zipFilePath
     * @throws IOException
     */
    public static final void showZipContent(final String zipFilePath) throws IOException {
        ZipReader zip = new ZipReader(new File(zipFilePath));
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
        ZipReader zip = new ZipReader(new File(zipFilePath));
        zip.readAll(new ZipEntryExtractor(target, entries));
        zip.close();
    }

    public static void main(final String[] args) throws IOException {
        String zipPath = "C:/Documents and Settings/45505230/My Documents/NDTKit/Cartos/ISQUS/test_3D_Erfassung_3.zip";
        //String zipPath = "C:/Documents and Settings/45505230/My Documents/NDTKit/Cartos/M2K - M2M - Multi2000/étalon-test-pasdif2.m2k.zip";
        ZipReader.extract(zipPath, "tmp");
        //ZipReader.showZipContent(zipPath);
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
        public void read(final InputStream input, final String name, final boolean directory) {
            try {
                if (!directory) {
                    if (entries == null || entries.size() == 0) {
                        addExtractedFile(extractTo(getTarget() + File.separator + name, input));
                        //System.out.println(name + " extracted");
                    } else {
                        for (String entry : entries) {
                            if (name.matches(entry.replaceAll("\\*", ".*").replaceAll("[.][.]", "."))) {
                                getExtractedFiles().add(extractTo(getTarget() + File.separator + name, input));
                                //System.out.println(name + " extracted");
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected void addExtractedFile(final File extractedFile) {
            extractedFiles.add(extractedFile);
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
     * As the ZIP entry name is in cp850 encoding, this method convert the string in valid string.
     * @param entryName The ZipEntry name.
     * @return The entry name converted
     */
    public static String getZipEntryName(final String entryName) {
        try {
            char[] characters = entryName.toCharArray();
            byte[] bytes = new byte[characters.length];
            for (int i = 0; i < characters.length; ++i) {
                bytes[i] = (byte) characters[i];
            }
            return new String(bytes, "cp850");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return entryName;
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
        public abstract void read(final InputStream input, final String name, final boolean directory);

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