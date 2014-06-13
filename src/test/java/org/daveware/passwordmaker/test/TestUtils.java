package org.daveware.passwordmaker.test;

import org.daveware.passwordmaker.SecureCharArray;

public class TestUtils {
    public static String saToString(SecureCharArray arr) {
        return new String(arr.getData());
    }
}
