/*
 * @(#)DeflaterOutputStream.java	1.36 06/03/13
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.dromard.common.zip;

import java.util.Date;

/**
 * This class is used to represent a ZIP file entry.
 *
 * @version	1.40, 11/17/05
 * @author	David Connelly
 */
public class ZipEntry implements ZipConstants, Cloneable {
    /** The entry name. */
    private String name;
    /** The modification time (in DOS time). */
    private long time = -1;
    /** crc-32 of entry data. */
    private long crc = -1;
    /** The uncompressed size of entry data. */
    private long size = -1;
    /** The compressed size of entry data. */
    private long csize = -1;
    /** compression method. */
    private int method = -1;
    /** The optional extra field data for entry. */
    private byte[] extra;
    /** The optional comment string for entry. */
    private String comment;

    /**
     * Compression method for uncompressed entries.
     */
    public static final int STORED = 0;

    /**
     * Compression method for compressed (deflated) entries.
     */
    public static final int DEFLATED = 8;

    /**
     * Creates a new zip entry with the specified name.
     *
     * @param name the entry name
     * @exception NullPointerException if the entry name is null
     * @exception IllegalArgumentException if the entry name is longer than
     *		  0xFFFF bytes
     */
    public ZipEntry(final String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        if (name.length() > 0xFFFF) {
            throw new IllegalArgumentException("entry name too long");
        }
        this.name = name;
    }

    /**
     * Creates a new zip entry with fields taken from the specified
     * zip entry.
     * @param e a zip Entry object
     */
    public ZipEntry(final ZipEntry e) {
        name = e.name;
        time = e.time;
        crc = e.crc;
        size = e.size;
        csize = e.csize;
        method = e.method;
        extra = e.extra;
        comment = e.comment;
    }

    /*
     * Creates a new zip entry for the given name with fields initialized
     * from the specified jzentry data.
     */
    ZipEntry(final String name, final long jzentry) {
        this.name = name;
        initFields(jzentry);
    }

    private native void initFields(final long jzentry);

    /*
     * Creates a new zip entry with fields initialized from the specified
     * jzentry data.
     */
    ZipEntry(final long jzentry) {
        initFields(jzentry);
    }

    /**
     * Returns the name of the entry.
     * @return the name of the entry
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the modification time of the entry.
     * @param time the entry modification time in number of milliseconds
     *		   since the epoch
     * @see #getTime()
     */
    public void setTime(final long time) {
        this.time = ZipEntry.javaToDosTime(time);
    }

    /**
     * Returns the modification time of the entry, or -1 if not specified.
     * @return the modification time of the entry, or -1 if not specified
     * @see #setTime(long)
     */
    public long getTime() {
        return time != -1 ? ZipEntry.dosToJavaTime(time) : -1;
    }

    /**
     * Sets the uncompressed size of the entry data.
     * @param size the uncompressed size in bytes
     * @exception IllegalArgumentException if the specified size is less
     *		  than 0 or greater than 0xFFFFFFFF bytes
     * @see #getSize()
     */
    public void setSize(final long size) {
        if (size < 0 || size > 0xFFFFFFFFL) {
            throw new IllegalArgumentException("invalid entry size");
        }
        this.size = size;
    }

    /**
     * Returns the uncompressed size of the entry data, or -1 if not known.
     * @return the uncompressed size of the entry data, or -1 if not known
     * @see #setSize(long)
     */
    public long getSize() {
        return size;
    }

    /**
     * Returns the size of the compressed entry data, or -1 if not known.
     * In the case of a stored entry, the compressed size will be the same
     * as the uncompressed size of the entry.
     * @return the size of the compressed entry data, or -1 if not known
     * @see #setCompressedSize(long)
     */
    public long getCompressedSize() {
        return csize;
    }

    /**
     * Sets the size of the compressed entry data.
     * @param pCsize the compressed size to set to
     * @see #getCompressedSize()
     */
    public void setCompressedSize(final long pCsize) {
        csize = pCsize;
    }

    /**
     * Sets the CRC-32 checksum of the uncompressed entry data.
     * @param crc the CRC-32 value
     * @exception IllegalArgumentException if the specified CRC-32 value is
     *		  less than 0 or greater than 0xFFFFFFFF
     * @see #getCrc()
     */
    public void setCrc(final long crc) {
        if (crc < 0 || crc > 0xFFFFFFFFL) {
            throw new IllegalArgumentException("invalid entry crc-32");
        }
        this.crc = crc;
    }

    /**
     * Returns the CRC-32 checksum of the uncompressed entry data, or -1 if
     * not known.
     * @return the CRC-32 checksum of the uncompressed entry data, or -1 if
     * not known
     * @see #setCrc(long)
     */
    public long getCrc() {
        return crc;
    }

    /**
     * Sets the compression method for the entry.
     * @param method the compression method, either STORED or DEFLATED
     * @exception IllegalArgumentException if the specified compression
     *		  method is invalid
     * @see #getMethod()
     */
    public void setMethod(final int method) {
        if (method != ZipEntry.STORED && method != ZipEntry.DEFLATED) {
            throw new IllegalArgumentException("invalid compression method");
        }
        this.method = method;
    }

    /**
     * Returns the compression method of the entry, or -1 if not specified.
     * @return the compression method of the entry, or -1 if not specified
     * @see #setMethod(int)
     */
    public int getMethod() {
        return method;
    }

    /**
     * Sets the optional extra field data for the entry.
     * @param extra the extra field data bytes
     * @exception IllegalArgumentException if the length of the specified
     *		  extra field data is greater than 0xFFFF bytes
     * @see #getExtra()
     */
    public void setExtra(final byte[] extra) {
        if (extra != null && extra.length > 0xFFFF) {
            throw new IllegalArgumentException("invalid extra field length");
        }
        this.extra = extra;
    }

    /**
     * Returns the extra field data for the entry, or null if none.
     * @return the extra field data for the entry, or null if none
     * @see #setExtra(byte[])
     */
    public byte[] getExtra() {
        return extra;
    }

    /**
     * Sets the optional comment string for the entry.
     * @param comment the comment string
     * @exception IllegalArgumentException if the length of the specified
     *		  comment string is greater than 0xFFFF bytes
     * @see #getComment()
     */
    public void setComment(final String comment) {
        if (comment != null && comment.length() > 0xffff / 3 && ZipOutputStream.getUTF8Length(comment) > 0xffff) {
            throw new IllegalArgumentException("invalid entry comment length");
        }
        this.comment = comment;
    }

    /**
     * Returns the comment string for the entry, or null if none.
     * @return the comment string for the entry, or null if none
     * @see #setComment(String)
     */
    public String getComment() {
        return comment;
    }

    /**
     * Returns true if this is a directory entry. A directory entry is
     * defined to be one whose name ends with a '/'.
     * @return true if this is a directory entry
     */
    public boolean isDirectory() {
        return name.endsWith("/");
    }

    /**
     * Returns a string representation of the ZIP entry.
     */
    @Override
    public String toString() {
        return getName();
    }

    /*
     * Converts DOS time to Java time (number of milliseconds since epoch).
     */
    private static long dosToJavaTime(final long dtime) {
        Date d = new Date((int) (((dtime >> 25) & 0x7f) + 80), (int) (((dtime >> 21) & 0x0f) - 1), (int) ((dtime >> 16) & 0x1f), (int) ((dtime >> 11) & 0x1f), (int) ((dtime >> 5) & 0x3f), (int) ((dtime << 1) & 0x3e));
        return d.getTime();
    }

    /*
     * Converts Java time to DOS time.
     */
    private static long javaToDosTime(final long time) {
        Date d = new Date(time);
        int year = d.getYear() + 1900;
        if (year < 1980) {
            return (1 << 21) | (1 << 16);
        }
        return (year - 1980) << 25 | (d.getMonth() + 1) << 21 | d.getDate() << 16 | d.getHours() << 11 | d.getMinutes() << 5 | d.getSeconds() >> 1;
    }

    /**
     * Returns the hash code value for this entry.
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Returns a copy of this entry.
     */
    @Override
    public Object clone() {
        try {
            ZipEntry e = (ZipEntry) super.clone();
            e.extra = (extra == null ? null : (byte[]) extra.clone());
            return e;
        } catch (CloneNotSupportedException e) {
            // This should never happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /**
     * @param csize the csize to set
     */
    public void setCsize(final long csize) {
        this.csize = csize;
    }

    /**
     * @return the csize
     */
    public long getCsize() {
        return csize;
    }
}
