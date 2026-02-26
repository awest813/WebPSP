package jpcsp.crypto;

import libkirk.KirkEngine;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class KirkEncryptionTest {

    @Test
    public void testEncryptDecrypt() {
        // Initialize Kirk
        KirkEngine.kirk_init(0);

        // Prepare data
        int dataSize = 0x10; // Small size for test
        byte[] data = new byte[dataSize];
        for (int i = 0; i < dataSize; i++) {
            data[i] = (byte) i;
        }

        // Prepare input buffer for CMD0
        // Header (0x90) + Data
        int headerSize = 0x90;
        int totalSize = headerSize + dataSize;
        byte[] inBuff = new byte[totalSize];

        // Fill header
        // mode = KIRK_MODE_CMD1 (1)
        // data_size = dataSize
        // data_offset = 0

        // Set mode to 1 (little endian)
        // Offset 0x60
        inBuff[0x60] = 1;
        inBuff[0x61] = 0;
        inBuff[0x62] = 0;
        inBuff[0x63] = 0;

        // Set data_size
        // Offset 0x70
        inBuff[0x70] = (byte) (dataSize & 0xFF);
        inBuff[0x71] = (byte) ((dataSize >> 8) & 0xFF);
        inBuff[0x72] = (byte) ((dataSize >> 16) & 0xFF);
        inBuff[0x73] = (byte) ((dataSize >> 24) & 0xFF);

        // Set data_offset = 0
        // ... defaults to 0

        // Copy data to input buffer (after header)
        System.arraycopy(data, 0, inBuff, headerSize, dataSize);

        // Output buffer
        byte[] encBuff = new byte[totalSize];

        // CMD0 (ENCRYPT_PRIVATE) = 0
        int ret = KirkEngine.sceUtilsBufferCopyWithRange(encBuff, totalSize, inBuff, totalSize, KirkEngine.KIRK_CMD_ENCRYPT_PRIVATE);
        Assert.assertEquals("Encryption failed", KirkEngine.KIRK_OPERATION_SUCCESS, ret);

        // Now Decrypt using CMD1 (DECRYPT_PRIVATE) = 1
        byte[] decBuff = new byte[dataSize]; // Output of CMD1 is just the data

        ret = KirkEngine.sceUtilsBufferCopyWithRange(decBuff, dataSize, encBuff, totalSize, KirkEngine.KIRK_CMD_DECRYPT_PRIVATE);
        Assert.assertEquals("Decryption failed", KirkEngine.KIRK_OPERATION_SUCCESS, ret);

        // Verify data
        Assert.assertArrayEquals("Decrypted data does not match original", data, decBuff);
    }
}
