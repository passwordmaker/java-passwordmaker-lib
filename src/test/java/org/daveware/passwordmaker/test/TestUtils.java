/*
 * This file is part of Passwordmaker-je-lib.
 * Copyright (C) 2014 James Stapleton
 *
 * Passwordmaker-je-lib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Passwordmaker-je-lib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Passwordmaker-je-lib.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.daveware.passwordmaker.test;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.daveware.passwordmaker.SecureCharArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;

public class TestUtils {
    public static final String nonAsciiPassword = "PasswordMaker\u00A9\u20AC\uD852\uDF62";

    public static String saToString(SecureCharArray arr) {
        return new String(arr.getData());
    }


    public static void addBCProvider() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] readFully(InputStream input) throws IOException {
        ByteArrayOutputStream retVal = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int read = 0;
        while(read != -1) {
            read = input.read(buffer);
            if(read > 0) {
                retVal.write(buffer, 0, read);
            }
        }

        return retVal.toByteArray();
    }
}
