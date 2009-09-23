package net.dromard.common.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Test of FileChannelHandler library.
 * <br><br>
 * Project name : NDTKit EADS  <br><br>
 * @date 28 avr. 2009 18:54:45
 * @author Gabriel Dromard
 */
public class FileChannelHandlerTest extends TestCase {

    public final void testReadShorts() throws URISyntaxException, IOException {
        short[] shorts = new short[]{10234, 9876, 32767, -32767, -1234, -5045, 0};

        FileChannelHandler channel = new FileChannelHandler(new RandomAccessFile(new File(FileChannelHandlerTest.class.getResource("FileChannelHandler.bin").toURI()), "rw").getChannel());
        channel.writeShorts(shorts);
        channel.close();
        FileChannelHandler channelReader = new FileChannelHandler(new FileInputStream(new File(FileChannelHandlerTest.class.getResource("FileChannelHandler.bin").toURI())).getChannel());
        Assert.assertEquals(channelReader.readShort(), shorts[0]);
        Assert.assertEquals(channelReader.readShort(), shorts[1]);
        Assert.assertEquals(channelReader.readShort(), shorts[2]);
        Assert.assertEquals(channelReader.readShort(), shorts[3]);
        Assert.assertEquals(channelReader.readShort(), shorts[4]);
        Assert.assertEquals(channelReader.readShort(), shorts[5]);
        Assert.assertEquals(channelReader.readShort(), shorts[6]);
        channelReader.close();
        channelReader = new FileChannelHandler(new FileInputStream(new File(FileChannelHandlerTest.class.getResource("FileChannelHandler.bin").toURI())).getChannel());
        Assert.assertTrue(Arrays.equals(shorts, channelReader.readShorts(new short[7])));
        channelReader.close();
    }

    public final void testReadUnsignedShorts() throws URISyntaxException, IOException {
        int[] shorts = new int[]{10234, 9876, 32767, 65536, -1234, -5045, 0};
        int[] result = new int[]{10234, 9876, 32767, 0, 65536 - 1234, 65536 - 5045, 0};

        FileChannelHandler channel = new FileChannelHandler(new RandomAccessFile(new File(FileChannelHandlerTest.class.getResource("FileChannelHandler.bin").toURI()), "rw").getChannel());
        channel.writeUnsignedShort(shorts);
        channel.close();
        FileChannelHandler channelReader = new FileChannelHandler(new FileInputStream(new File(FileChannelHandlerTest.class.getResource("FileChannelHandler.bin").toURI())).getChannel());
        Assert.assertEquals(channelReader.readUnsignedShort(), result[0]);
        Assert.assertEquals(channelReader.readUnsignedShort(), result[1]);
        Assert.assertEquals(channelReader.readUnsignedShort(), result[2]);
        Assert.assertEquals(channelReader.readUnsignedShort(), result[3]);
        Assert.assertEquals(channelReader.readUnsignedShort(), result[4]);
        Assert.assertEquals(channelReader.readUnsignedShort(), result[5]);
        Assert.assertEquals(channelReader.readUnsignedShort(), result[6]);
        channelReader.close();
        channelReader = new FileChannelHandler(new FileInputStream(new File(FileChannelHandlerTest.class.getResource("FileChannelHandler.bin").toURI())).getChannel());
        Assert.assertTrue(Arrays.equals(result, channelReader.readUnsignedShorts(new int[7])));
        channelReader.close();
    }
}
