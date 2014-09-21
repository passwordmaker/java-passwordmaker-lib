package org.daveware.passwordmaker;

import static org.daveware.passwordmaker.StringEncodingUtils.stringAsUTF8ByteArray;

public class SecureUTF8String extends SecureCharArray {
    public SecureUTF8String() {
        super();
    }

    public SecureUTF8String(int size) {
        super(size);
    }

    protected SecureUTF8String(byte[] bytes) {
        super(bytes, true);
    }

    public SecureUTF8String(SecureUTF8String copy) {
        super(copy);
    }

    public SecureUTF8String(String str) {
        this(stringAsUTF8ByteArray(str));
    }
}
