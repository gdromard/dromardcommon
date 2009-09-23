package net.dromard.common.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * This is a CustomFileChannelWriter class that handle an internal FileChannel, and make it's use simpler.
 * It is an abstraction of a FileChannel object.
 * It add the unsigned byte, short, int reading.
 * @author Gabriel Dromard
 */
public class FileChannelHandler {

    /** The INT_SIZE. */
    public static final int INT_SIZE = 4;
    /** The SHORT_SIZE. */
    public static final int SHORT_SIZE = 2;
    /** Field name : channel. */
    private final FileChannel channel;
    /** Field name : byteOrder. */
    private ByteOrder byteOrder = null;
    /** Field name : buffer1. */
    private ByteBuffer buffer1 = null;
    /** Field name : buffer2. */
    private ByteBuffer buffer2 = null;
    /** Field name : buffer4. */
    private ByteBuffer buffer4 = null;
    /** Field name : buffer8. */
    private ByteBuffer buffer8 = null;

    /**
     * <p>
     * In order to use this handler for read/write use:<br>
     * <code>new AdvanceFileChannel(new RandomAccessFile(file, "rw").getChannel());</code>
     * <br>
     * In order to use this handler for read only use:<br>
     * <code>new AdvanceFileChannel(new FileInputStream(file).getChannel());</code>
     * </p>
     * @param channel The channel to handle.
     */
    public FileChannelHandler(final FileChannel channel) {
        this.channel = channel;
        setByteOrder(ByteOrder.nativeOrder());
    }

    public final void setByteOrder(final ByteOrder byteOrder) {
        if (byteOrder.equals(this.byteOrder)) {
            return;
        }
        buffer1 = ByteBuffer.allocate(1).order(byteOrder);
        buffer2 = ByteBuffer.allocate(2).order(byteOrder);
        buffer4 = ByteBuffer.allocate(4).order(byteOrder);
        buffer8 = ByteBuffer.allocate(8).order(byteOrder);
        this.byteOrder = byteOrder;
    }

    private ByteBuffer getBuffer1() {
        buffer1.clear();
        return buffer1;
    }

    private ByteBuffer getBuffer2() {
        buffer2.clear();
        return buffer2;
    }

    private ByteBuffer getBuffer4() {
        buffer4.clear();
        return buffer4;
    }

    private ByteBuffer getBuffer8() {
        buffer8.clear();
        return buffer8;
    }

    /* ---------------------------------------------------------------------- */

    public final void writeString(final int length, final String encoding, final String toBeWritten) throws IOException {
        Charset charset = Charset.forName(encoding);
        CharsetEncoder encoder = charset.newEncoder();
        ByteBuffer buf = encoder.encode(CharBuffer.wrap(toBeWritten));
        channel.write(buf);
        buf.rewind();
    }

    /* ---------------------------------------------------------------------- */

    public final void writeByte(final byte b) throws IOException {
        ByteBuffer buf = getBuffer1();
        buf.put(b);
        buf.rewind();
        channel.write(buf);
    }

    public final void writeShort(final short s) throws IOException {
        ByteBuffer buf = getBuffer2();
        buf.asShortBuffer().put(s);
        buf.rewind();
        channel.write(buf);

    }

    public final void writeChar(final char c) throws IOException {
        ByteBuffer buf = getBuffer2();
        buf.asCharBuffer().put(c);
        buf.rewind();
        channel.write(buf);
    }

    public final void writeInt(final int i) throws IOException {
        ByteBuffer buf = getBuffer4();
        buf.asIntBuffer().put(i);
        buf.rewind();
        channel.write(buf);
    }

    public final void writeFloat(final float f) throws IOException {
        ByteBuffer buf = getBuffer4();
        buf.asFloatBuffer().put(f);
        buf.rewind();
        channel.write(buf);
    }

    public final void writeLong(final long l) throws IOException {
        ByteBuffer buf = getBuffer8();
        buf.asLongBuffer().put(l);
        buf.rewind();
        channel.write(buf);
    }

    public final void writeDouble(final double d) throws IOException {
        ByteBuffer buf = getBuffer8();
        buf.asDoubleBuffer().put(d);
        buf.rewind();
        channel.write(buf);
    }

    public final void writeBytes(final byte[] bytes) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(bytes.length).order(byteOrder);
        buf.put(bytes);
        buf.rewind();
        channel.write(buf);
    }

    public final void writeShorts(final short[] shorts) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(shorts.length * 2).order(byteOrder);
        buf.asShortBuffer().put(shorts);
        buf.rewind();
        channel.write(buf);
    }

    public final void writeFloats(final float[] floats) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(floats.length * 4).order(byteOrder);
        buf.asFloatBuffer().put(floats);
        buf.rewind();
        channel.write(buf);
    }

    public final void writeInts(final int[] ints) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(ints.length * 4).order(byteOrder);
        buf.asIntBuffer().put(ints);
        buf.rewind();
        channel.write(buf);
    }

    public final void writeUnsignedByte(final short b) throws IOException {
        ByteBuffer buf = getBuffer1();
        buf.put((byte) b);
        buf.rewind();
        channel.write(buf);
    }

    public final void writeUnsignedByte(final short[] bytes) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(bytes.length).order(byteOrder);
        for (short b : bytes) {
            buf.put((byte) b);
        }
        buf.rewind();
        channel.write(buf);
    }

    public final void writeUnsignedShort(final int s) throws IOException {
        ByteBuffer buf = getBuffer2();
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            buf.put((byte) (s >> 8));
            buf.put((byte) (s));
        } else {
            buf.put((byte) (s));
            buf.put((byte) (s >> 8));
        }
        buf.rewind();
        channel.write(buf);
    }

    public final void writeUnsignedShort(final int[] uShorts) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(uShorts.length * 2).order(byteOrder);
        for (int uShort : uShorts) {
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                buf.put((byte) (uShort >> 8));
                buf.put((byte) (uShort));
            } else {
                buf.put((byte) (uShort));
                buf.put((byte) (uShort >> 8));
            }
        }
        buf.rewind();
        channel.write(buf);
    }

    public final void writeUnsignedInt(final long i) throws IOException {
        ByteBuffer buf = getBuffer4();
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            buf.put((byte) (i >> 24));
            buf.put((byte) (i >> 16));
            buf.put((byte) (i >> 8));
            buf.put((byte) i);
        } else {
            buf.put((byte) i);
            buf.put((byte) (i >> 8));
            buf.put((byte) (i >> 16));
            buf.put((byte) (i >> 24));
        }
        buf.rewind();
        channel.write(buf);
    }

    public final void writeUnsignedInt(final int[] uInts) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(uInts.length * 4).order(byteOrder);
        for (int i : uInts) {
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                buf.put((byte) (i >> 24));
                buf.put((byte) (i >> 16));
                buf.put((byte) (i >> 8));
                buf.put((byte) i);
            } else {
                buf.put((byte) i);
                buf.put((byte) (i >> 8));
                buf.put((byte) (i >> 16));
                buf.put((byte) (i >> 24));
            }
        }
        buf.rewind();
        channel.write(buf);
    }

    /* ---------------------------------------------------------------------- */

    public final String readString(final int length, final String encoding) throws IOException {
        Charset charset = Charset.forName(encoding);
        ByteBuffer buf = ByteBuffer.allocate(length).order(byteOrder);
        channel.read(buf);
        buf.rewind();
        return charset.decode(buf).toString();
    }

    /* ---------------------------------------------------------------------- */

    public final byte readByte() throws IOException {
        ByteBuffer buf = getBuffer1();
        channel.read(buf);
        buf.rewind();
        return buf.get();
    }

    public final short readShort() throws IOException {
        ByteBuffer buf = getBuffer2();
        channel.read(buf);
        buf.rewind();
        return buf.getShort();
    }

    public final char readChar() throws IOException {
        ByteBuffer buf = getBuffer2();
        channel.read(buf);
        buf.rewind();
        return buf.getChar();
    }

    public final int readInt() throws IOException {
        ByteBuffer buf = getBuffer4();
        channel.read(buf);
        buf.rewind();
        return buf.getInt();
    }

    public final float readFloat() throws IOException {
        ByteBuffer buf = getBuffer4();
        channel.read(buf);
        buf.rewind();
        return buf.getFloat();
    }

    public final long readLong() throws IOException {
        ByteBuffer buf = getBuffer8();
        channel.read(buf);
        buf.rewind();
        return buf.getLong();
    }

    public final double readDouble() throws IOException {
        ByteBuffer buf = getBuffer8();
        channel.read(buf);
        buf.rewind();
        return buf.getDouble();
    }

    public final byte[] readBytes(final byte[] bytes) throws IOException {
        if (bytes.length != 0) {
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length).order(byteOrder);
            channel.read(buffer);
            buffer.rewind();
            buffer.get(bytes);
        }
        return bytes;
    }

    public final short[] readShorts(final short[] shorts) throws IOException {
        if (shorts.length != 0) {
            ByteBuffer buffer = ByteBuffer.allocate(shorts.length * 2).order(byteOrder);
            channel.read(buffer);
            buffer.rewind();
            buffer.asShortBuffer().get(shorts);
        }
        return shorts;
    }

    public final float[] readFloats(final float[] floats) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(floats.length * 4).order(byteOrder);
        channel.read(buf);
        buf.rewind();
        buf.asFloatBuffer().get(floats);
        return floats;
    }

    public final short readUnsignedByte() throws IOException {
        return (short) (readByte() & 0xFF);
    }

    public final int readUnsignedShort() throws IOException {
        int b1 = readUnsignedByte();
        int b2 = readUnsignedByte();
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return ((b1 << 8) + (b2 << 0));
        }
        return (b2 << 8) + (b1 << 0);
    }

    public final short[] readUnsignedBytes(final short[] unsignedBytes) throws IOException {
        byte[] bytes = readBytes(new byte[unsignedBytes.length]);
        for (int i = 0; i < unsignedBytes.length; ++i) {
            unsignedBytes[i] = (short) (bytes[i] & 0xFF);
        }
        return unsignedBytes;
    }

    public final int[] readUnsignedShorts(final int[] unsignedShort) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(unsignedShort.length * 2).order(byteOrder);
        channel.read(buf);
        buf.rewind();
        for (int i = 0; i < unsignedShort.length; ++i) {
            int b1 = buf.get() & 0xFF;
            int b2 = buf.get() & 0xFF;
            if (byteOrder == ByteOrder.BIG_ENDIAN) {
                unsignedShort[i] = (b1 << 8) + (b2 << 0);
            }
            unsignedShort[i] = (b2 << 8) + (b1 << 0);
        }
        return unsignedShort;
    }

    public final int readUnsignedInt() throws IOException {
        int b1 = readUnsignedByte();
        int b2 = readUnsignedByte();
        int b3 = readUnsignedByte();
        int b4 = readUnsignedByte();
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            return (b1 << 24) + (b2 << 16) + (b3 << 8) + b4;
        }
        return (b4 << 24) + (b3 << 16) + (b2 << 8) + b1;
    }

    /* ---------------------------------------------------------------------- */

    public final void close() throws IOException {
        channel.close();
    }

    public final void seek(final long position) throws IOException {
        channel.position(position);
    }

    public final long getFilePointer() throws IOException {
        return channel.position();
    }

    public final long getSize() throws IOException {
        return channel.size();
    }

    /**
     * Test of unsigned byte conversions.
     * @param args ..
     */
    public static void main(final String[] args) {
        int i = 64000;
        System.out.println("before being converted: " + i);
        System.out.println("After convertion into signed byte " + (short) i);
        byte b4 = (byte) (i >> 24);
        byte b3 = (byte) (i >> 16);
        byte b2 = (byte) (i >> 8);
        byte b1 = (byte) (i);
        System.out.println("After convertion 4 (" + b4 + ") " + Integer.toBinaryString(b4));
        System.out.println("After convertion 3 (" + b3 + ") " + Integer.toBinaryString(b3));
        System.out.println("After convertion 2 (" + b2 + ") " + Integer.toBinaryString(b2));
        System.out.println("After convertion 1 (" + b1 + ") " + Integer.toBinaryString(b1));

        int j = (b4 << 24) + (b3 << 16) + (b2 << 8) + (b1);
        System.out.println("After reconvertion (signed) " + j);
        j = ((b4 & 0xFF) << 24) + ((b3 & 0xFF) << 16) + ((b2 & 0xFF) << 8) + (b1 & 0xFF);
        System.out.println("After reconvertion (unsigned) " + j);
        System.out.println((byte) 0xF1);
    }
}
