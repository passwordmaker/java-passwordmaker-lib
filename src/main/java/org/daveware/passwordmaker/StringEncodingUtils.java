package org.daveware.passwordmaker;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class StringEncodingUtils {
    public static String ENCODING = "UTF-8";
    public static byte[] charArrayToBytesUTFNIO(char[] buffer) {
        CharBuffer charBuffer = CharBuffer.wrap(buffer);
        ByteBuffer byteBuffer = Charset.forName(ENCODING).encode(charBuffer);
        try {
            return byteBufferToBytes(byteBuffer);
        } finally {
            eraseByteBuffer(byteBuffer);
        }
    }

    /**
     * Always returns a copy of the data as a char[]
     * @param buffer buffer to decode into UTF-8 characters
     * @return UFT-8 characters
     */

    public static char[] bytesToCharArrayUTFNIO(byte[] buffer) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        CharBuffer charBuffer = Charset.forName(ENCODING).decode(byteBuffer);
        try {
            return charBufferToChars(charBuffer);
        } finally {
            eraseCharBuffer(charBuffer);
        }
    }

    /**
     * @param buffer the buffer to convert
     * @return the bytes of the buffer
     */
    public static byte[] byteBufferToBytes(ByteBuffer buffer) {
        final byte[] array = buffer.array();
        final int arrayOffset = buffer.arrayOffset();
        return Arrays.copyOfRange(array, arrayOffset + buffer.position(),
                arrayOffset + buffer.limit());
    }


    /**
     * @param buffer the buffer to convert
     * @return the char's of the buffer
     */
    public static char[] charBufferToChars(CharBuffer buffer) {
        final char[] array = buffer.array();
        final int arrayOffset = buffer.arrayOffset();
        return Arrays.copyOfRange(array, arrayOffset + buffer.position(), arrayOffset + buffer.limit());
    }


    public static void eraseCharBuffer(CharBuffer buffer) {
        char[] data = buffer.array();
        long limit = buffer.arrayOffset() + buffer.limit();
        for (int i = buffer.arrayOffset(); i < limit; i++)
            data[i] = 0xAA;
        for (int i = buffer.arrayOffset(); i < limit; i++)
            data[i] = 0x55;
        for (int i = buffer.arrayOffset(); i < limit; i++)
            data[i] = 0x00;
    }

    public static void eraseByteBuffer(ByteBuffer buffer) {
        byte[] data = buffer.array();
        long limit = buffer.arrayOffset() + buffer.limit();
        for (int i = buffer.arrayOffset(); i < limit; i++)
            data[i] = (byte)0xAA;
        for (int i = buffer.arrayOffset(); i < limit; i++)
            data[i] = 0x55;
        for (int i = buffer.arrayOffset(); i < limit; i++)
            data[i] = 0x00;
    }

    public static byte[] stringAsUTF8ByteArray(String str) {
        try {
            return str.getBytes(ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
