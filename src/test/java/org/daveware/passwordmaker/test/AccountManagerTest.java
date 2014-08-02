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
import org.daveware.passwordmaker.Account;
import org.daveware.passwordmaker.AccountManager;
import org.daveware.passwordmaker.PasswordMaker;
import org.daveware.passwordmaker.SecureCharArray;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import static org.daveware.passwordmaker.test.TestUtils.saToString;
import static org.junit.Assert.*;

public class AccountManagerTest {
    AccountManager manager;
    private static String SERIALIZED_FAVS = "<http://reddit.com/>,<http://realnews.com/>,<http://google.com/>," +
            "<http://goo.gl/>,<http://markerisred.com/>,<http://github.com/>,<http://www.stackoverflow.com/>," +
            "<https://www.facebook.com/>,<https://gmail.com/>";

    @BeforeClass
    public static void setUpClass() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Before
    public void setUp() throws Exception {
        manager = new AccountManager();
        AccountManagerSamples.addSamples(manager);
    }

    @Test
    public void testAccountManagerDefaultAccount() {
        assertTrue(manager.getDefaultAccount().isDefault());
    }

    @Test
    public void testAccountManagerDefaultAccountOnNonMatchingUrl() {
        assertSame(manager.getDefaultAccount(), manager.getAccountForInputText("google.com"));
    }

    @Test
    public void testDefaultAccountUseGivenUrl() throws Exception {
        Account account = manager.getDefaultAccount();
        PasswordMaker pwm = manager.getPwm();
        assertEquals("HRdgNiyh", saToString(pwm.makePassword(new SecureCharArray("happy"), account, "google.com")));
    }

    @Test
    public void testAccountManagerUsesDefaultAccountForNonMatchingUrl() {
        assertEquals("HRdgNiyh", saToString(manager.generatePassword("happy", "google.com")));
    }

    @Test
    public void testAccountManagerMatchingUrl() {
        Account redditAccount = manager.getAccountForInputText("http://reddit.com/");
        assertNotNull(redditAccount);
        assertEquals("Reddit", redditAccount.getName());
    }

    @Test
    public void testAccountManagerEmptyUrl() {
        Account defaultAccount = manager.getAccountForInputText("");
        assertNotNull(defaultAccount);
        assertTrue("Default Account name: " + defaultAccount.getName(), defaultAccount.isDefault());
        assertEquals("default", defaultAccount.getName());
    }

    @Test
    public void testAccountManagerNonMatchingNonEmptyUrl() {
        Account defaultAccount = manager.getAccountForInputText("http://fake.com");
        assertNotNull(defaultAccount);
        assertTrue("Default Account name: " + defaultAccount.getName(), defaultAccount.isDefault());
        assertEquals("default", defaultAccount.getName());
    }

    @Test
    public void testEncodingFavorites() {
        AccountManagerSamples.addFavorites(manager);
        assertEquals(SERIALIZED_FAVS, manager.encodeFavoriteUrls());
    }

    @Test
    public void testDecodingFavoritesNotClearing() {
        manager.addFavoriteUrl("http://doesnotexist.com/");
        manager.decodeFavoritesUrls(SERIALIZED_FAVS, false);
        List<String> expected = new ArrayList<String>();
        expected.add("http://doesnotexist.com/");
        expected.addAll(AccountManagerSamples.SAMPLE_URLS);
        assertEquals(expected, manager.getFavoriteUrls());
    }

    @Test
    public void testDecodingFavoritesClearingFirst() {
        manager.addFavoriteUrl("http://doesnotexist.com/");
        manager.decodeFavoritesUrls(SERIALIZED_FAVS, true);
        assertEquals(AccountManagerSamples.SAMPLE_URLS, manager.getFavoriteUrls());
    }

}