package net.dromard.common.zip;


import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipError;
import java.util.zip.ZipException;

/**
 * This class is used to read entries from a zip file.
 * <p> Unless otherwise noted, passing a <tt>null</tt> argument to a constructor or method
 * in this class will cause a {@link NullPointerException} to be thrown.</p>
 * @author Gabriel Dromard
 * 30 sept. 2009
 */
public class ZipFile implements ZipConstants {
    /** The jzfile. */
    private long jzfile; // address of jzfile data
    /** The name. */
    private final String name; // zip file name
    /** The total. */
    private final int total; // total number of entries
    /** The closeRequested. */
    private boolean closeRequested;
    /** The jzentryByName. */
    private final Map<String, Long> jzentryByName = new HashMap<String, Long>();
    /** The inflaters. */
    private final Vector<Inflater> inflaters = new Vector<Inflater>();

    /** The STORED. */
    private static final int STORED = ZipEntry.STORED;
    /** The DEFLATED. */
    private static final int DEFLATED = ZipEntry.DEFLATED;

    /**
     * Mode flag to open a zip file for reading.
     */
    public static final int OPEN_READ = 0x1;

    /**
     * Mode flag to open a zip file and mark it for deletion.  The file will be
     * deleted some time between the moment that it is opened and the moment
     * that it is closed, but its contents will remain accessible via the
     * <tt>ZipFile</tt> object until either the close method is invoked or the
     * virtual machine exits.
     */
    public static final int OPEN_DELETE = 0x4;

    static {
        /* Zip library is loaded from System.initializeSystemClass */
        ZipFile.initIDs();
    }

    /**
     * Opens a zip file for reading.
     *
     * <p>First, if there is a security
     * manager, its <code>checkRead</code> method
     * is called with the <code>name</code> argument
     * as its argument to ensure the read is allowed.
     *
     * @param name the name of the zip file
     * @throws ZipException if a ZIP format error has occurred
     * @throws IOException if an I/O error has occurred
     * @throws SecurityException if a security manager exists and its
     *         <code>checkRead</code> method doesn't allow read access to the file.
     * @see SecurityManager#checkRead(java.lang.String)
     */
    public ZipFile(final String name) throws IOException {
        this(new File(name), ZipFile.OPEN_READ);
    }

    /**
     * Opens a new <code>ZipFile</code> to read from the specified
     * <code>File</code> object in the specified mode.  The mode argument
     * must be either <tt>OPEN_READ</tt> or <tt>OPEN_READ | OPEN_DELETE</tt>.
     *
     * <p>First, if there is a security manager, its <code>checkRead</code>
     * method is called with the <code>name</code> argument as its argument to
     * ensure the read is allowed.
     *
     * @param file the ZIP file to be opened for reading
     * @param mode the mode in which the file is to be opened
     * @throws ZipException if a ZIP format error has occurred
     * @throws IOException if an I/O error has occurred
     * @throws SecurityException if a security manager exists and
     *         its <code>checkRead</code> method
     *         doesn't allow read access to the file,
     *         or its <code>checkDelete</code> method doesn't allow deleting
     *         the file when the <tt>OPEN_DELETE</tt> flag is set.
     * @throws IllegalArgumentException if the <tt>mode</tt> argument is invalid
     * @see SecurityManager#checkRead(java.lang.String)
     * @since 1.3
     */
    public ZipFile(final File file, final int mode) throws IOException {
        if (((mode & ZipFile.OPEN_READ) == 0) || ((mode & ~(ZipFile.OPEN_READ | ZipFile.OPEN_DELETE)) != 0)) {
            throw new IllegalArgumentException("Illegal mode: 0x" + Integer.toHexString(mode));
        }
        String fileName = file.getPath();
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            sm.checkRead(fileName);
            if ((mode & ZipFile.OPEN_DELETE) != 0) {
                sm.checkDelete(fileName);
            }
        }
        jzfile = ZipFile.open(fileName, mode, file.lastModified());

        name = fileName;
        total = ZipFile.getTotal(jzfile);
    }

    /**
     * Opens a ZIP file for reading given the specified File object.
     * @param file the ZIP file to be opened for reading
     * @throws ZipException if a ZIP error has occurred
     * @throws IOException if an I/O error has occurred
     */
    public ZipFile(final File file) throws IOException {
        this(file, ZipFile.OPEN_READ);
    }

    /**
     * Returns the zip file entry for the specified name, or null
     * if not found.
     *
     * @param entryName the name of the entry
     * @return the zip file entry, or null if not found
     * @throws IllegalStateException if the zip file has been closed
     */
    public ZipEntry getEntry(final String entryName) {
        if (entryName == null) {
            throw new NullPointerException("name");
        }
        long jzentry = 0;
        synchronized (this) {
            ensureOpen();
            jzentry = ZipFile.getEntry(jzfile, entryName, true);
            if (jzentry != 0) {
                ZipEntry ze = instantiateZipEntry(entryName, jzentry);
                ZipFile.freeEntry(jzfile, jzentry);
                return ze;
            }
        }
        return null;
    }

    /**
     * Returns an input stream for reading the contents of the specified
     * zip file entry.
     *
     * <p> Closing this ZIP file will, in turn, close all input
     * streams that have been returned by invocations of this method.
     *
     * @param entry the zip file entry
     * @return the input stream for reading the contents of the specified
     * zip file entry.
     * @throws ZipException if a ZIP format error has occurred
     * @throws IOException if an I/O error has occurred
     * @throws IllegalStateException if the zip file has been closed
     */
    public InputStream getInputStream(final ZipEntry entry) throws IOException {
        return getInputStream(entry.getName());
    }

    /**
     * As the ZIP entry name is in cp850 encoding, this method convert the string in valid string.
     * @param entryName The ZipEntry name.
     * @return The entry name converted
     */
    public static byte[] getZipEntryNameInByte(final String entryName) {
        char[] characters = entryName.toCharArray();
        byte[] bytes = new byte[characters.length];
        for (int i = 0; i < characters.length; ++i) {
            bytes[i] = (byte) characters[i];
        }
        return bytes;
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
     * Returns an input stream for reading the contents of the specified
     * entry, or null if the entry was not found.
     */
    private InputStream getInputStream(final String entryName) throws IOException {
        if (entryName == null) {
            throw new NullPointerException("name");
        }
        long jzentry = 0;
        ZipFileInputStream in = null;
        synchronized (this) {
            ensureOpen();
            jzentry = ZipFile.getEntry(jzfile, entryName, false);
            if (jzentry == 0) {
                return null;
            }

            in = new ZipFileInputStream(jzentry);

        }
        final ZipFileInputStream zfin = in;
        switch (ZipFile.getMethod(jzentry)) {
        case STORED:
            return zfin;
        case DEFLATED:
            // MORE: Compute good size for inflater stream:
            long size = ZipFile.getSize(jzentry) + 2; // Inflater likes a bit of slack
            if (size > 65536) {
                size = 8192;
            }
            if (size <= 0) {
                size = 4096;
            }
            return new InflaterInputStream(zfin, getInflater(), (int) size) {
                private boolean isClosed = false;

                @Override
                public void close() throws IOException {
                    if (!isClosed) {
                        releaseInflater(inf);
                        in.close();
                        isClosed = true;
                    }
                }

                // Override fill() method to provide an extra "dummy" byte
                // at the end of the input stream. This is required when
                // using the "nowrap" Inflater option.
                @Override
                protected void fill() throws IOException {
                    if (eof) {
                        throw new EOFException("Unexpected end of ZLIB input stream");
                    }
                    len = in.read(buf, 0, buf.length);
                    if (len == -1) {
                        buf[0] = 0;
                        len = 1;
                        eof = true;
                    }
                    inf.setInput(buf, 0, len);
                }

                private boolean eof;

                @Override
                public int available() throws IOException {
                    if (isClosed) {
                        return 0;
                    }
                    long avail = zfin.size() - inf.getBytesWritten();
                    return avail > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) avail;
                }
            };
        default:
            throw new ZipException("invalid compression method");
        }
    }

    /*
     * Gets an inflater from the list of available inflaters or allocates
     * a new one.
     */
    private Inflater getInflater() {
        synchronized (inflaters) {
            int size = inflaters.size();
            if (size > 0) {
                Inflater inf = inflaters.remove(size - 1);
                return inf;
            } else {
                return new Inflater(true);
            }
        }
    }

    /*
     * Releases the specified inflater to the list of available inflaters.
     */
    private void releaseInflater(final Inflater inf) {
        synchronized (inf) {
            inf.reset();
            inflaters.add(inf);
        }
    }

    /**
     * Returns the path name of the ZIP file.
     * @return the path name of the ZIP file
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an enumeration of the ZIP file entries.
     * @return an enumeration of the ZIP file entries
     * @throws IllegalStateException if the zip file has been closed
     */
    public Enumeration<? extends ZipEntry> entries() {
        ensureOpen();
        return new Enumeration<ZipEntry>() {
            private int i = 0;

            public boolean hasMoreElements() {
                synchronized (ZipFile.this) {
                    ensureOpen();
                    return i < total;
                }
            }

            public ZipEntry nextElement() {
                synchronized (ZipFile.this) {
                    ensureOpen();
                    if (i >= total) {
                        throw new NoSuchElementException();
                    }
                    long jzentry = ZipFile.getNextEntry(jzfile, i++);
                    if (jzentry == 0) {
                        String message;
                        if (closeRequested) {
                            message = "ZipFile concurrently closed";
                        } else {
                            message = ZipFile.getZipMessage(jzfile);
                        }
                        throw new ZipError("jzentry == 0" + ",\n jzfile = " + jzfile + ",\n total = " + total + ",\n name = " + name + ",\n i = " + i + ",\n message = " + message);
                    }
                    ZipEntry ze = instantiateZipEntry(jzentry);
                    ZipFile.freeEntry(jzfile, jzentry);
                    //System.out.println("[DEBUG] <ZipFile.entries()> adding " + ze.getName() + " with " + jzentry);
                    jzentryByName.put(ze.getName(), jzentry);
                    return ze;
                }
            }
        };
    }

    private ZipEntry instantiateZipEntry(final String entryName, final long jzentry) {
        try {
            Constructor<ZipEntry> constructor = ZipEntry.class.getConstructor(new Class[]{String.class, long.class});
            constructor.setAccessible(true);
            return constructor.newInstance(entryName, jzentry);
        } catch (Exception e) {
            throw new ZipError("Can not instantiate ZipEntry !!");
        }
    }

    private ZipEntry instantiateZipEntry(final long jzentry) {
        try {
            Constructor<ZipEntry> constructor = ZipEntry.class.getDeclaredConstructor(new Class[]{long.class});
            constructor.setAccessible(true);
            return constructor.newInstance(jzentry);
        } catch (Exception e) {
            throw new ZipError("Can not instantiate ZipEntry !!");
        }
    }

    /**
     * Returns the number of entries in the ZIP file.
     * @return the number of entries in the ZIP file
     * @throws IllegalStateException if the zip file has been closed
     */
    public int size() {
        ensureOpen();
        return total;
    }

    /**
     * Closes the ZIP file.
     * <p> Closing this ZIP file will close all of the input streams
     * previously returned by invocations of the {@link #getInputStream
     * getInputStream} method.
     *
     * @throws IOException if an I/O error has occurred
     */
    public void close() throws IOException {
        synchronized (this) {
            closeRequested = true;

            if (jzfile != 0) {
                // Close the zip file
                long zf = jzfile;
                jzfile = 0;

                ZipFile.close(zf);

                // Release inflaters
                synchronized (inflaters) {
                    int size = inflaters.size();
                    for (int i = 0; i < size; i++) {
                        Inflater inf = inflaters.get(i);
                        inf.end();
                    }
                }
            }
        }
    }

    /**
     * Ensures that the <code>close</code> method of this ZIP file is
     * called when there are no more references to it.
     *
     * <p>
     * Since the time when GC would invoke this method is undetermined,
     * it is strongly recommended that applications invoke the <code>close</code>
     * method as soon they have finished accessing this <code>ZipFile</code>.
     * This will prevent holding up system resources for an undetermined
     * length of time.
     *
     * @throws IOException if an I/O error has occurred
     * @see    java.util.zip.ZipFile#close()
     */
    @Override
    protected void finalize() throws IOException {
        close();
    }

    private void ensureOpen() {
        if (closeRequested) {
            throw new IllegalStateException("zip file closed");
        }

        if (jzfile == 0) {
            throw new IllegalStateException("The object is not initialized.");
        }
    }

    private void ensureOpenOrZipException() throws IOException {
        if (closeRequested) {
            throw new ZipException("ZipFile closed");
        }
    }

    /**
     * Inner class implementing the input stream used to read a (possibly compressed) zip file entry.
     * @author Gabriel Dromard
     * 30 sept. 2009
     */
    private class ZipFileInputStream extends InputStream {
        /** address of jzentry data. */
        protected long jzentry;
        /** current position within entry data. */
        private long pos;
        /** number of remaining bytes within entry. */
        protected long rem;
        /** uncompressed size of this entry. */
        protected long size;

        ZipFileInputStream(final long jzentry) {
            pos = 0;
            rem = ZipFile.getCSize(jzentry);
            size = ZipFile.getSize(jzentry);
            this.jzentry = jzentry;
        }

        @Override
        public int read(final byte[] b, final int off, final int length) throws IOException {
            int len = length;
            if (rem == 0) {
                return -1;
            }
            if (len <= 0) {
                return 0;
            }
            if (len > rem) {
                len = (int) rem;
            }
            synchronized (ZipFile.this) {
                ensureOpenOrZipException();

                len = ZipFile.read(jzfile, jzentry, pos, b, off, len);
            }
            if (len > 0) {
                pos += len;
                rem -= len;
            }
            if (rem == 0) {
                close();
            }
            return len;
        }

        @Override
        public int read() throws IOException {
            byte[] b = new byte[1];
            if (read(b, 0, 1) == 1) {
                return b[0] & 0xff;
            } else {
                return -1;
            }
        }

        @Override
        public long skip(final long bytes) {
            long n = bytes;
            if (n > rem) {
                n = rem;
            }
            pos += n;
            rem -= n;
            if (rem == 0) {
                close();
            }
            return n;
        }

        @Override
        public int available() {
            return rem > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) rem;
        }

        public long size() {
            return size;
        }

        @Override
        public void close() {
            rem = 0;
            synchronized (ZipFile.this) {
                if (jzentry != 0 && jzfile != 0) {
                    ZipFile.freeEntry(jzfile, jzentry);
                    jzentry = 0;
                }
            }
        }

    }

    private static void initIDs() {
        try {
            Method m = java.util.zip.ZipFile.class.getDeclaredMethod("initIDs");
            m.setAccessible(true);
            m.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZipError(e.getMessage());
        }
    }

    private static long open(final String name, final int mode, final long lastModified) {
        try {
            Method m = java.util.zip.ZipFile.class.getDeclaredMethod("open", new Class[]{String.class, int.class, long.class});
            m.setAccessible(true);
            return (Long) m.invoke(null, name, mode, lastModified);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZipError(e.getMessage());
        }
    }

    private static int getTotal(final long jzfile) {
        try {
            Method m = java.util.zip.ZipFile.class.getDeclaredMethod("getTotal", new Class[]{long.class});
            m.setAccessible(true);
            return (Integer) m.invoke(null, jzfile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZipError(e.getMessage());
        }
    }

    private static long getEntry(final long jzfile, final String name, final boolean addSlash) {
        try {
            Method m = java.util.zip.ZipFile.class.getDeclaredMethod("getEntry", new Class[]{long.class, String.class, boolean.class});
            m.setAccessible(true);
            return (Long) m.invoke(null, jzfile, name, addSlash);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZipError(e.getMessage());
        }
    }

    // freeEntry releases the C jzentry struct.
    private static void freeEntry(final long jzfile, final long jzentry) {
        try {
            Method m = java.util.zip.ZipFile.class.getDeclaredMethod("freeEntry", new Class[]{long.class, long.class});
            m.setAccessible(true);
            m.invoke(null, jzfile, jzentry);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZipError(e.getMessage());
        }
    }

    private static int getMethod(final long jzentry) {
        try {
            Method m = java.util.zip.ZipFile.class.getDeclaredMethod("getMethod", new Class[]{long.class});
            m.setAccessible(true);
            return (Integer) m.invoke(null, jzentry);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZipError(e.getMessage());
        }
    }

    private static long getNextEntry(final long jzfile, final int i) {
        try {
            Method m = java.util.zip.ZipFile.class.getDeclaredMethod("getNextEntry", new Class[]{long.class, int.class});
            m.setAccessible(true);
            return (Long) m.invoke(null, jzfile, i);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZipError(e.getMessage());
        }
    }

    private static void close(final long jzfile) {
        try {
            Method m = java.util.zip.ZipFile.class.getDeclaredMethod("close", new Class[]{long.class});
            m.setAccessible(true);
            m.invoke(null, jzfile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZipError(e.getMessage());
        }
    }

    private static int read(final long jzfile, final long jzentry, final long pos, final byte[] b, final int off, final int len) {
        try {
            Method m = java.util.zip.ZipFile.class.getDeclaredMethod("read", new Class[]{long.class, long.class, long.class, byte[].class, int.class, int.class});
            m.setAccessible(true);
            return (Integer) m.invoke(null, jzfile, jzentry, pos, b, off, len);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZipError((e.getCause() != null) ? e.getCause().getMessage() : e.getMessage());
        }
    }

    private static long getCSize(final long jzentry) {
        try {
            Method m = java.util.zip.ZipFile.class.getDeclaredMethod("getCSize", new Class[]{long.class});
            m.setAccessible(true);
            return (Long) m.invoke(null, jzentry);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZipError(e.getMessage());
        }
    }

    private static long getSize(final long jzentry) {
        try {
            Method m = java.util.zip.ZipFile.class.getDeclaredMethod("getSize", new Class[]{long.class});
            m.setAccessible(true);
            return (Long) m.invoke(null, jzentry);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZipError(e.getMessage());
        }
    }

    // Temporary add on for bug troubleshooting
    private static String getZipMessage(final long jzfile) {
        try {
            Method m = java.util.zip.ZipFile.class.getDeclaredMethod("getZipMessage", new Class[]{long.class});
            m.setAccessible(true);
            return (String) m.invoke(null, jzfile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ZipError(e.getMessage());
        }
    }
}
