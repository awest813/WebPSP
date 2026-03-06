package jpcsp.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class FileUtilTest {
    @Test
    public void testGetExtension() {
        Assert.assertEquals("txt", FileUtil.getExtension(new File("test/test.txt")));
        Assert.assertEquals("demo", FileUtil.getExtension(new File("test/test.DEMO")));
        Assert.assertEquals("", FileUtil.getExtension(new File("test/test.")));
        Assert.assertEquals("", FileUtil.getExtension(new File("test/test")));
        Assert.assertEquals("", FileUtil.getExtension(null));
    }

    @Test
    public void testReadAllReadsRequestedBytes() throws Exception {
        byte[] buffer = new byte[6];
        FileUtil.readAll(new ByteArrayInputStream(new byte[]{1, 2, 3, 4}), buffer, 1, 4);

        Assert.assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 0}, buffer);
    }

    @Test
    public void testReadAllThrowsOnEndOfStream() {
        byte[] buffer = new byte[4];

        IOException exception = Assert.assertThrows(IOException.class,
                () -> FileUtil.readAll(new ByteArrayInputStream(new byte[]{1, 2}), buffer, 0, 4));

        Assert.assertTrue(exception.getMessage().contains("End of stream reached"));
    }

    @Test
    public void testReadAllThrowsOnInvalidBounds() {
        Assert.assertThrows(IndexOutOfBoundsException.class,
                () -> FileUtil.readAll(new ByteArrayInputStream(new byte[]{1, 2}), new byte[2], 1, 2));
    }

    @Test
    public void testListFilesRecursivelyHandlesInvalidDirectory() {
        Assert.assertEquals(0, FileUtil.listFilesRecursively(null, null).length);
        Assert.assertEquals(0, FileUtil.listFilesRecursively(new File("does-not-exist"), null).length);
        Assert.assertEquals(0, FileUtil.listFilesRecursively(new File("README.md"), null).length);
    }
}
