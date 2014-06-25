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
