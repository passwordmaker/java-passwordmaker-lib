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
package org.daveware.passwordmaker.xmlwrappers;

import java.io.IOException;

public class XmlIOException extends IOException {
    public XmlIOException() {
        super();
    }

    public XmlIOException(String s) {
        super(s);
    }

    public XmlIOException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public XmlIOException(Throwable throwable) {
        super(throwable);
    }
}
