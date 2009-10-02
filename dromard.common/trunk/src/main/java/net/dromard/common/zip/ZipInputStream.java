package net.dromard.common.zip;


import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.util.zip.CRC32;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

/**
 * This class implements an input stream filter for reading files in the ZIP file format.
 * Includes support for both compressed and uncompressed entries.
 * @author Gabriel Dromard
 * 30 sept. 2009
 */
public class ZipInputStream extends InflaterInputStream implements ZipConstants {
    /** The entry. */
    private ZipEntry entry;
    /** The flag. */
    private int flag;
    /** The crc. */
    private final CRC32 crc = new CRC32();
    /** The remaining. */
    private long remaining;
    /** The tmpbuf. */
    private final byte[] tmpbuf = new byte[2048];

    /** The buffer. */
    private byte[] b = new byte[512];

    /** The STORED. */
    private static final int STORED = ZipEntry.STORED;
    /** The DEFLATED. */
    private static final int DEFLATED = ZipEntry.DEFLATED;

    /** The closed. */
    private boolean closed = false;
    /** this flag is set to true after EOF has reached for one entry. */
    private boolean entryEOF = false;

    /**
     * Check to make sure that this stream has not been closed.
     */
    private void ensureOpen() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
    }

    /**
     * Creates a new ZIP input stream.
     * @param in the actual input stream
     */
    public ZipInputStream(final InputStream in) {
        super(new PushbackInputStream(in, 512), new Inflater(true), 512);
        //usesDefaultInflater = true;
        Field usesDefaultInflaterField;
        try {
            usesDefaultInflaterField = InflaterInputStream.class.getDeclaredField("usesDefaultInflater");
            usesDefaultInflaterField.setAccessible(true);
            usesDefaultInflaterField.setBoolean(this, true);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (in == null) {
            throw new NullPointerException("in is null");
        }
    }

    /**
     * Reads the next ZIP file entry and positions the stream at the beginning of the entry data.
     * @return the next ZIP file entry, or null if there are no more entries
     * @exception ZipException if a ZIP file error has occurred
     * @exception IOException if an I/O error has occurred
     */
    public ZipEntry getNextEntry() throws IOException {
        ensureOpen();
        if (entry != null) {
            closeEntry();
        }
        crc.reset();
        inf.reset();
        entry = readLOC();
        if (entry == null) {
            return null;
        }
        if (entry.getMethod() == ZipInputStream.STORED) {
            remaining = entry.getSize();
        }
        entryEOF = false;
        return entry;
    }

    /**
     * Closes the current ZIP entry and positions the stream for reading the next entry.
     * @exception IOException if an I/O error has occurred
     */
    public void closeEntry() throws IOException {
        ensureOpen();
        while (read(tmpbuf, 0, tmpbuf.length) != -1) {
        }
        entryEOF = true;
    }

    /**
     * Returns 0 after EOF has reached for the current entry data, otherwise always return 1.
     * <p>
     * Programs should not count on this method to return the actual number of bytes that could be read without blocking.
     * @return 1 before EOF and 0 after EOF has reached for current entry.
     * @exception IOException if an I/O error occurs.
     */
    @Override
    public int available() throws IOException {
        ensureOpen();
        return (entryEOF) ? 0 : 1;
    }

    /**
     * Reads from the current ZIP entry into an array of bytes. If <code>len</code> is not zero, the method blocks until some input is available; otherwise, no bytes are read and <code>0</code> is returned.
     * @param buffer the buffer into which the data is read
     * @param off the start offset in the destination array <code>b</code>
     * @param len the maximum number of bytes read
     * @return the actual number of bytes read, or -1 if the end of the entry is reached
     * @exception IOException if an I/O error has occurred
     */
    @Override
    public int read(final byte[] bytes, final int off, final int length) throws IOException {
        int len = length;
        ensureOpen();
        if (off < 0 || len < 0 || off > bytes.length - len) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        if (entry == null) {
            return -1;
        }
        switch (entry.getMethod()) {
        case DEFLATED:
            len = super.read(bytes, off, len);
            if (len == -1) {
                readEnd(entry);
                entryEOF = true;
                entry = null;
            } else {
                crc.update(bytes, off, len);
            }
            return len;
        case STORED:
            if (remaining <= 0) {
                entryEOF = true;
                entry = null;
                return -1;
            }
            if (len > remaining) {
                len = (int) remaining;
            }
            len = in.read(bytes, off, len);
            if (len == -1) {
                throw new ZipException("unexpected EOF");
            }
            crc.update(bytes, off, len);
            remaining -= len;
            if (remaining == 0 && entry.getCrc() != crc.getValue()) {
                throw new ZipException("invalid entry CRC (expected 0x" + Long.toHexString(entry.getCrc()) + " but got 0x" + Long.toHexString(crc.getValue()) + ")");
            }
            return len;
        default:
            throw new ZipException("invalid compression method");
        }
    }

    /**
     * Skips specified number of bytes in the current ZIP entry.
     * @param n the number of bytes to skip
     * @return the actual number of bytes skipped
     * @exception ZipException if a ZIP file error has occurred
     * @exception IOException if an I/O error has occurred
     * @exception IllegalArgumentException if n < 0
     */
    @Override
    public long skip(final long n) throws IOException {
        if (n < 0) {
            throw new IllegalArgumentException("negative skip length");
        }
        ensureOpen();
        int max = (int) Math.min(n, Integer.MAX_VALUE);
        int total = 0;
        while (total < max) {
            int len = max - total;
            if (len > tmpbuf.length) {
                len = tmpbuf.length;
            }
            len = read(tmpbuf, 0, len);
            if (len == -1) {
                entryEOF = true;
                break;
            }
            total += len;
        }
        return total;
    }

    /**
     * Closes this input stream and releases any system resources associated with the stream.
     * @exception IOException if an I/O error has occurred
     */
    @Override
    public void close() throws IOException {
        closeEntry();
        if (entryEOF) {
            if (!closed) {
                super.close();
                closed = true;
            }
        }
    }

    /*
     * Reads local file (LOC) header for next entry.
     */
    private ZipEntry readLOC() throws IOException {
        try {
            readFully(tmpbuf, 0, ZipConstants.LOCHDR);
        } catch (EOFException e) {
            return null;
        }
        if (ZipInputStream.get32(tmpbuf, 0) != ZipConstants.LOCSIG) {
            return null;
        }
        // get the entry name and create the ZipEntry first
        int len = ZipInputStream.get16(tmpbuf, ZipConstants.LOCNAM);
        int blen = b.length;
        if (len > blen) {
            do {
                blen = blen * 2;
            } while (len > blen);
            b = new byte[blen];
        }
        readFully(b, 0, len);
        // ZipEntry e = createZipEntry(getUTF8String(b, 0, len));
        // Old Code:
        // ZipEntry e = createZipEntry(getUTF8String(b,0,len));
        // String fileName = null;
        // try{
        // fileName = getUTF8String(b, 0, len);
        // }catch (Exception ex){
        String fileName = (new String(b, "IBM437")).substring(0, len);
        // }
        ZipEntry e = createZipEntry(fileName);

        // now get the remaining fields for the entry
        flag = ZipInputStream.get16(tmpbuf, ZipConstants.LOCFLG);
        if ((flag & 1) == 1) {
            throw new ZipException("encrypted ZIP entry not supported");
        }
        int method = ZipInputStream.get16(tmpbuf, ZipConstants.LOCHOW);
        e.setMethod(method);
        e.setTime(ZipInputStream.get32(tmpbuf, ZipConstants.LOCTIM));
        if ((flag & 8) == 8) {
            /* "Data Descriptor" present */
            if (method != ZipInputStream.DEFLATED) {
                throw new ZipException("only DEFLATED entries can have EXT descriptor");
            }
        } else {
            e.setCrc(ZipInputStream.get32(tmpbuf, ZipConstants.LOCCRC));
            e.setCompressedSize(ZipInputStream.get32(tmpbuf, ZipConstants.LOCSIZ));
            e.setSize(ZipInputStream.get32(tmpbuf, ZipConstants.LOCLEN));
        }
        len = ZipInputStream.get16(tmpbuf, ZipConstants.LOCEXT);
        if (len > 0) {
            byte[] bb = new byte[len];
            readFully(bb, 0, len);
            e.setExtra(bb);
        }
        return e;
    }

    /**
     * Creates a new <code>ZipEntry</code> object for the specified entry name.
     * @param name the ZIP file entry name
     * @return the ZipEntry just created
     */
    protected ZipEntry createZipEntry(final String name) {
        return new ZipEntry(name);
    }

    /*
     * Reads end of deflated entry as well as EXT descriptor if present.
     */
    private void readEnd(final ZipEntry e) throws IOException {
        int n = inf.getRemaining();
        if (n > 0) {
            ((PushbackInputStream) in).unread(buf, len - n, n);
        }
        if ((flag & 8) == 8) {
            /* "Data Descriptor" present */
            readFully(tmpbuf, 0, ZipConstants.EXTHDR);
            long sig = ZipInputStream.get32(tmpbuf, 0);
            if (sig != ZipConstants.EXTSIG) { // no EXTSIG present
                e.setCrc(sig);
                e.setCompressedSize(ZipInputStream.get32(tmpbuf, ZipConstants.EXTSIZ - ZipConstants.EXTCRC));
                e.setSize(ZipInputStream.get32(tmpbuf, ZipConstants.EXTLEN - ZipConstants.EXTCRC));
                ((PushbackInputStream) in).unread(tmpbuf, ZipConstants.EXTHDR - ZipConstants.EXTCRC - 1, ZipConstants.EXTCRC);
            } else {
                e.setCrc(ZipInputStream.get32(tmpbuf, ZipConstants.EXTCRC));
                e.setCompressedSize(ZipInputStream.get32(tmpbuf, ZipConstants.EXTSIZ));
                e.setSize(ZipInputStream.get32(tmpbuf, ZipConstants.EXTLEN));
            }
        }
        if (e.getSize() != inf.getBytesWritten()) {
            throw new ZipException("invalid entry size (expected " + e.getSize() + " but got " + inf.getBytesWritten() + " bytes)");
        }
        if (e.getCompressedSize() != inf.getBytesRead()) {
            throw new ZipException("invalid entry compressed size (expected " + e.getCompressedSize() + " but got " + inf.getBytesRead() + " bytes)");
        }
        if (e.getCrc() != crc.getValue()) {
            throw new ZipException("invalid entry CRC (expected 0x" + Long.toHexString(e.getCrc()) + " but got 0x" + Long.toHexString(crc.getValue()) + ")");
        }
    }

    /*
     * Reads bytes, blocking until all bytes are read.
     */
    private void readFully(final byte[] b0, final int off, final int len) throws IOException {
        int k;
        int localOff = off;
        int localLen = len;
        for (; localLen > 0; localLen -= k) {
            k = in.read(b0, localOff, localLen);
            if (k == -1) {
                throw new EOFException();
            }
            localOff += k;
        }
    }

    /*
    private void readFully(byte[] b, int off, int len) throws IOException {
        while (len > 0) {
            int n = in.read(b, off, len);
            if (n == -1) {
                throw new EOFException();
            }
            off += n;
            len -= n;
        }
    }
     */

    /*
     * Fetches unsigned 16-bit value from byte array at specified offset. The bytes are assumed to be in Intel (little-endian) byte order.
     */
    private static int get16(final byte[] b, final int off) {
        return (b[off] & 0xff) | ((b[off + 1] & 0xff) << 8);
    }

    /*
     * Fetches unsigned 32-bit value from byte array at specified offset. The bytes are assumed to be in Intel (little-endian) byte order.
     */
    private static long get32(final byte[] b, final int off) {
        return ZipInputStream.get16(b, off) | ((long) ZipInputStream.get16(b, off + 2) << 16);
    }
}