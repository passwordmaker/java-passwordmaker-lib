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
package org.daveware.passwordmaker.util;

import java.util.Iterator;

public class Joiner {
    String sep;

    private Joiner(String sep) {
        this.sep = sep;
    }

    public static Joiner on(String sep) {
        return new Joiner(sep);
    }

    public String join(Iterable<String> items) {
        return join(items.iterator());
    }

    public String join(Iterator<String> items) {
        return appendTo(new StringBuilder(), items).toString();
    }

    public StringBuilder appendTo(StringBuilder buffer, Iterable<String> items) {
        return appendTo(buffer, items.iterator());
    }

    public StringBuilder appendTo(StringBuilder buffer, Iterator<String> items) {
        if (items.hasNext()){
            buffer.append(items.next());
            while (items.hasNext()) {
                buffer.append(sep);
                buffer.append(items.next());
            }
        }

        return buffer;
    }
}
