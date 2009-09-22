/*
 * @(#)DeflaterOutputStream.java	1.36 06/03/13
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.dromard.common.zip;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipException;

/**
 * This class implements a stream filter for uncompressing data in the "deflate" compression format. It is also used as the basis for other decompression filters, such as GZIPInputStream.
 * @see Inflater
 * @version 1.40, 04/07/06
 * @author David Connelly
 */
public class InflaterInputStream extends FilterInputStream {
    /**
     * Decompressor for this stream.
     */
    private final Inflater inf;

    /**
     * Input buffer for decompression.
     */
    private final byte[] buf;

    /**
     * Length of input buffer.
     */
    private int len;

    /** The closed. */
    private boolean closed = false;
    // this flag is set to true after EOF has reached
    /** The reachEOF. */
    private boolean reachEOF = false;
    /** The usesDefaultInflater. */
    private boolean usesDefaultInflater = false;
    /** The singleByteBuf. */
    private final byte[] singleByteBuf = new byte[1];
    /** The b. */
    private final byte[] b = new byte[512];

    /**
     * Check to make sure that this stream has not been closed.
     */
    private void ensureOpen() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
    }

    /**
     * Creates a new input stream with the specified decompressor and buffer size.
     * @param in the input stream
     * @param inf the decompressor ("inflater")
     * @param size the input buffer size
     * @exception IllegalArgumentException if size is <= 0
     */
    public InflaterInputStream(final InputStream in, final Inflater inf, final int size) {
        super(in);
        if (in == null || inf == null) {
            throw new NullPointerException();
        } else if (size <= 0) {
            throw new IllegalArgumentException("buffer size <= 0");
        }
        this.inf = inf;
        buf = new byte[size];
    }

    /**
     * Creates a new input stream with the specified decompressor and a default buffer size.
     * @param in the input stream
     * @param inf the decompressor ("inflater")
     */
    public InflaterInputStream(final InputStream in, final Inflater inf) {
        this(in, inf, 512);
    }


    /**
     * Creates a new input stream with a default decompressor and buffer size.
     * @param in the input stream
     */
    public InflaterInputStream(final InputStream in) {
        this(in, new Inflater());
        usesDefaultInflater = true;
    }


    /**
     * Reads a byte of uncompressed data. This method will block until enough input is available for decompression.
     * @return the byte read, or -1 if end of compressed input is reached
     * @exception IOException if an I/O error has occurred
     */
    @Override
    public int read() throws IOException {
        ensureOpen();
        return read(singleByteBuf, 0, 1) == -1 ? -1 : singleByteBuf[0] & 0xff;
    }

    /**
     * Reads uncompressed data into an array of bytes. If <code>len</code> is not zero, the method will block until some input can be decompressed; otherwise, no bytes are read and <code>0</code> is returned.
     * @param pB the buffer into which the data is read
     * @param off the start offset in the destination array <code>b</code>
     * @param pLen the maximum number of bytes read
     * @return the actual number of bytes read, or -1 if the end of the compressed input is reached or a preset dictionary is needed
     * @exception NullPointerException If <code>b</code> is <code>null</code>.
     * @exception IndexOutOfBoundsException If <code>off</code> is negative, <code>len</code> is negative, or <code>len</code> is greater than <code>b.length - off</code>
     * @exception ZipException if a ZIP format error has occurred
     * @exception IOException if an I/O error has occurred
     */
    @Override
    public int read(final byte[] pB, final int off, final int pLen) throws IOException {
        ensureOpen();
        if (pB == null) {
            throw new NullPointerException();
        } else if (off < 0 || pLen < 0 || pLen > pB.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (pLen == 0) {
            return 0;
        }
        try {
            int n;
            while ((n = getInf().inflate(pB, off, pLen)) == 0) {
                if (getInf().finished() || getInf().needsDictionary()) {
                    reachEOF = true;
                    return -1;
                }
                if (getInf().needsInput()) {
                    fill();
                }
            }
            return n;
        } catch (DataFormatException e) {
            String s = e.getMessage();
            throw new ZipException(s != null ? s : "Invalid ZLIB data format");
        }
    }

    /**
     * Returns 0 after EOF has been reached, otherwise always return 1.
     * <p>
     * Programs should not count on this method to return the actual number of bytes that could be read without blocking.
     * @return 1 before EOF and 0 after EOF.
     * @exception IOException if an I/O error occurs.
     */
    @Override
    public int available() throws IOException {
        ensureOpen();
        if (reachEOF) {
            return 0;
        } else {
            return 1;
        }
    }


    /**
     * Skips specified number of bytes of uncompressed data.
     * @param n the number of bytes to skip
     * @return the actual number of bytes skipped.
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
            int localLen = max - total;
            if (localLen > b.length) {
                localLen = b.length;
            }
            localLen = read(b, 0, localLen);
            if (localLen == -1) {
                reachEOF = true;
                break;
            }
            total += localLen;
        }
        return total;
    }

    /**
     * Closes this input stream and releases any system resources associated with the stream.
     * @exception IOException if an I/O error has occurred
     */
    @Override
    public void close() throws IOException {
        if (!closed) {
            if (usesDefaultInflater) {
                getInf().end();
            }
            in.close();
            closed = true;
        }
    }

    /**
     * Fills input buffer with more data to decompress.
     * @exception IOException if an I/O error has occurred
     */
    protected void fill() throws IOException {
        ensureOpen();
        len = in.read(buf, 0, buf.length);
        if (len == -1) {
            throw new EOFException("Unexpected end of ZLIB input stream");
        }
        getInf().setInput(buf, 0, len);
    }

    /**
     * @return the inf
     */
    public Inflater getInf() {
        return inf;
    }

    /**
     * @return the buf
     */
    public byte[] getBuf() {
        return buf;
    }

    /**
     * @param len the len to set
     */
    public void setLen(final int len) {
        this.len = len;
    }

    /**
     * @return the len
     */
    public int getLen() {
        return len;
    }

    /**
     * @param usesDefaultInflater the usesDefaultInflater to set
     */
    public void setUsesDefaultInflater(final boolean usesDefaultInflater) {
        this.usesDefaultInflater = usesDefaultInflater;
    }

    /**
     * @return the usesDefaultInflater
     */
    public boolean isUsesDefaultInflater() {
        return usesDefaultInflater;
    }
}
