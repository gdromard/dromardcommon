package net.dromard.common.zip;
/*
 * @(#)DeflaterOutputStream.java	1.36 06/03/13
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


/**
 * This interface defines the constants that are used by the classes
 * which manipulate ZIP files.
 *
 * @version	1.19, 11/17/05
 * @author	David Connelly
 */
public interface ZipConstants {
    /*
     * Header signatures
     */
    /** "PK\003\004". */
    long LOCSIG = 0x04034b50L;
    /** "PK\007\008". */
    long EXTSIG = 0x08074b50L;
    /** "PK\001\002". */
    long CENSIG = 0x02014b50L;
    /** "PK\005\006". */
    long ENDSIG = 0x06054b50L;

    /*
     * Header sizes in bytes (including signatures)
     */
    /** The LOC header size. */
    int LOCHDR = 30;
    /** The EXT header size. */
    int EXTHDR = 16;
    /** The CEN header size. */
    int CENHDR = 46;
    /** The END header size. */
    int ENDHDR = 22;

    /*
     * Local file (LOC) header field offsets
     */
    /** The version needed to extract. */
    int LOCVER = 4;
    /** The general purpose bit flag. */
    int LOCFLG = 6;
    /** The compression method. */
    int LOCHOW = 8;
    /** The modification time. */
    int LOCTIM = 10;
    /** The uncompressed file crc-32 value. */
    int LOCCRC = 14;
    /** The compressed size. */
    int LOCSIZ = 18;
    /** The uncompressed size. */
    int LOCLEN = 22;
    /** The filename length. */
    int LOCNAM = 26;
    /** The extra field length. */
    int LOCEXT = 28;

    /*
     * Extra local (EXT) header field offsets
     */
    /** The uncompressed file crc-32 value. */
    int EXTCRC = 4;
    /** The compressed size. */
    int EXTSIZ = 8;
    /** The uncompressed size. */
    int EXTLEN = 12;

    /*
     * Central directory (CEN) header field offsets
     */
    /** The version made by. */
    int CENVEM = 4;
    /** The version needed to extract. */
    int CENVER = 6;
    /** The encrypt, decrypt flags. */
    int CENFLG = 8;
    /** The compression method. */
    int CENHOW = 10;
    /** The modification time. */
    int CENTIM = 12;
    /** The uncompressed file crc-32 value. */
    int CENCRC = 16;
    /** The compressed size. */
    int CENSIZ = 20;
    /** The uncompressed size. */
    int CENLEN = 24;
    /** The filename length. */
    int CENNAM = 28;
    /** The extra field length. */
    int CENEXT = 30;
    /** The comment length. */
    int CENCOM = 32;
    /** The disk number start. */
    int CENDSK = 34;
    /** The internal file attributes. */
    int CENATT = 36;
    /** The external file attributes. */
    int CENATX = 38;
    /** The LOC header offset. */
    int CENOFF = 42;

    /*
     * End of central directory (END) header field offsets
     */
    /** The number of entries on this disk. */
    int ENDSUB = 8;
    /** The total number of entries. */
    int ENDTOT = 10;
    /** The central directory size in bytes. */
    int ENDSIZ = 12;
    /** The offset of first CEN header. */
    int ENDOFF = 16;
    /** The zip file comment length. */
    int ENDCOM = 20;
}
