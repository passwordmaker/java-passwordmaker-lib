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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Splitter {
    private String sep;
    private boolean mOmmitEmptyStrings;

    private Splitter(String sep) {
        this.sep = sep;
    }

    public static Splitter on(String sep) {
        return new Splitter(sep);
    }

    public Splitter omitEmptyStrings() {
        mOmmitEmptyStrings = true;
        return this;
    }

    public Iterable<String> split(final CharSequence sequence) {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                if ( mOmmitEmptyStrings ) {
                    return filterOutEmptyStrings(splittingIterator(sequence));
                }
                return splittingIterator(sequence);
            }

            @Override
            public String toString() {
                return Joiner.on(", ")
                        .appendTo(new StringBuilder().append('['), this)
                        .append(']')
                        .toString();
            }
        };
    }

    /**
     * Its easier to understand if the logic for omit empty sequences is a filtering iterator
     *
     * @param splitted - the results of a splittingIterator
     *
     * @return iterator with no empty values
     */
    private Iterator<String> filterOutEmptyStrings(final Iterator<String> splitted) {
        return new Iterator<String>() {
            String next = getNext();
            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public String next() {
                if ( !hasNext() ) throw new NoSuchElementException();
                String result = next;
                next = getNext();
                return result;
            }

            public String getNext() {
                while (splitted.hasNext()) {
                    String value = splitted.next();
                    if ( value != null && !value.isEmpty() ) {
                        return value;
                    }
                }
                return null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private Iterator<String> splittingIterator(final CharSequence toSplit) {
        if (toSplit.length() == 0) {
            return new ArrayList<String>().iterator();
        }
        return new Iterator<String>() {
            int currentPos = 0;
            int nextPos = separatorStart(0);

            @Override
            public boolean hasNext() {
                return currentPos >= 0;
            }

            @Override
            public String next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                int ps = nextPos >= 0 ? nextPos : toSplit.length();
                final String result;
                // this means we found a sep at the end of the string (nothing after it)
                if ( currentPos == toSplit.length() )
                    result = "";
                else
                    result = toSplit.subSequence(currentPos, ps).toString();
                // update currentPos, if nextPos was already past the end, now we are done
                currentPos = nextPos < 0 ? -1 : separatorEnd(ps);
                // update the nextPos, if we didn't find one, we mark it with -1
                nextPos = nextPos >= 0 ? separatorStart(currentPos) : -1;
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            public int separatorStart(int start) {
                if (start < 0) return start;
                int separatorLength = sep.length();

                positions:
                for (int p = start, last = toSplit.length() - separatorLength;
                     p <= last; p++) {
                    for (int i = 0; i < separatorLength; i++) {
                        if (toSplit.charAt(i + p) != sep.charAt(i)) {
                            continue positions;
                        }
                    }
                    return p;
                }
                return -1;
            }

            public int separatorEnd(int separatorPosition) {
                if (separatorPosition < 0) return separatorPosition;
                int val = separatorPosition + sep.length();
                if ( val > toSplit.length() ) val = -1;
                return val;
            }
        };
    }

}
