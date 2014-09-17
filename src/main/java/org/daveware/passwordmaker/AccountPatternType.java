/*
 * This file is part of Passwordmaker-je-lib.
 * Copyright (C) 2011 Dave Marotti, James Stapleton
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
package org.daveware.passwordmaker;


/**
 * Represents the type of pattern to use when auto-matching accounts.
 *
 * @author Dave Marotti
 */
public class AccountPatternType implements Comparable<AccountPatternType> {

    private final static String NAMES[] = {
            "Wildcard", "Regular Expression"
    };
    public static AccountPatternType WILDCARD = new AccountPatternType(0);
    public static AccountPatternType REGEX = new AccountPatternType(1);
    private int type;

    private AccountPatternType() {
        type = 0;
    }

    private AccountPatternType(int i) {
        type = i;
    }

    public static AccountPatternType fromString(String str)
            throws Exception {
        if (str.length() == 0)
            return WILDCARD;
        if (str.equalsIgnoreCase("wildcard"))
            return WILDCARD;
        if (str.equalsIgnoreCase("regex") || str.equalsIgnoreCase(NAMES[1]))
            return REGEX;

        throw new Exception(String.format("Invalid AccountPatternType '%1s'", str));
    }

    public int compareTo(AccountPatternType o) {
        if (type < o.type)
            return -1;
        if (type > o.type)
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return NAMES[type];
    }
}
