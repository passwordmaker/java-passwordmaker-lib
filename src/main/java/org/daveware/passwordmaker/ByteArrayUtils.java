package org.daveware.passwordmaker;

public final class ByteArrayUtils {
    private ByteArrayUtils(){}
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes)
        {
            builder.append(Character.forDigit(b/16, 16));
            builder.append(Character.forDigit(b % 16, 16));
        }
        return builder.toString();
    }
}
