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

import junit.framework.Assert;
import org.daveware.passwordmaker.Account;
import org.daveware.passwordmaker.Account.UrlComponents;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class AccountTest {

    @Test
    public void testCopySettings() {
        // Verify that the set of UrlComponents created during Account.copySettings()
        // is actually a copy.
        Account orig = new Account();
        Account copy = new Account();

        orig.addUrlComponent(UrlComponents.Domain);
        copy.copySettings(orig);
        copy.clearUrlComponents();
        Assert.assertFalse(orig.getUrlComponents().isEmpty());

        // Verify that copySettings works when the set of components is empty.
        copy.addUrlComponent(UrlComponents.Protocol);
        orig.clearUrlComponents();
        copy.copySettings(orig);
        assertTrue(copy.getUrlComponents().isEmpty());
    }

    @Test
    public void testDefaultAccount() {
        Account account =  Account.makeDefaultAccount();
        assertTrue(account.isDefault());
    }

}
