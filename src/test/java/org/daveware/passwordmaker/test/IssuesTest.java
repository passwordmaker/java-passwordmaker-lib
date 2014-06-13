/*
 * PasswordMaker Java Edition - One Password To Rule Them All
 * Copyright (C) 2014 James Stapleton
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.daveware.passwordmaker.test;

import org.daveware.passwordmaker.*;
import org.junit.Test;

import static org.daveware.passwordmaker.test.TestUtils.saToString;
import static org.junit.Assert.assertEquals;

public class IssuesTest {
    @Test
    public void testIssue10() throws Exception {
        PasswordMaker pm = new PasswordMaker();
        Account account = new Account();
        account.setAlgorithm(AlgorithmType.MD5);
        account.setLength(8);
        account.setCharacterSet(CharacterSets.ALPHANUMERIC);
        account.clearUrlComponents();
        account.addUrlComponent(Account.UrlComponents.Domain);
        final SecureCharArray masterPassword = new SecureCharArray("happy");
        saToString(pm.makePassword(masterPassword, account, "google.com"));
        assertEquals("HRdgNiyh", saToString(pm.makePassword(masterPassword, account, "google.com")));
        assertEquals("DsTpW36p", saToString(pm.makePassword(masterPassword, account, "cnn.com")));
        assertEquals("Fn4n23hm", saToString(pm.makePassword(masterPassword, account, "google.co.uk")));
        assertEquals("Fn4n23hm", saToString(pm.makePassword(masterPassword, account, "www.google.co.uk")));
        assertEquals("G78kh9hf", saToString(pm.makePassword(masterPassword, account, "news.bbc.co.uk")));
        assertEquals("BZmxeqtq", saToString(pm.makePassword(masterPassword, account, "random.co.uk")));
    }
}
