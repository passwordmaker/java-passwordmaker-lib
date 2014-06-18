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

import static org.daveware.passwordmaker.test.TestUtils.saToString;
import static org.junit.Assert.*;

public class AccountManagerTest {
    AccountManager manager;

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
        Account account =  manager.getDefaultAccount();
        PasswordMaker pwm = manager.getPwm();
        assertEquals("HRdgNiyh", saToString(pwm.makePassword(new SecureCharArray("happy"), account, "google.com")));
    }

    @Test
    public void testAccountManagerUsesDefaultAccountForNonMatchingUrl() {
        assertEquals("HRdgNiyh", saToString((SecureCharArray)manager.generatePassword("happy", "google.com")));
    }

    @Test
    public void testAccountManagerMatchingUrl() {
        Account redditAccount =  manager.getAccountForInputText("http://reddit.com/");
        assertNotNull(redditAccount);
        assertEquals("Reddit", redditAccount.getName());
    }

    @Test
    public void testAccountManagerEmptyUrl() {
        Account defaultAccount =  manager.getAccountForInputText("");
        assertNotNull(defaultAccount);
        assertTrue("Default Account name: " + defaultAccount.getName(), defaultAccount.isDefault());
        assertEquals("default", defaultAccount.getName());
    }

    @Test
    public void testAccountManagerNonMatchingNonEmptyUrl() {
        Account defaultAccount =  manager.getAccountForInputText("http://fake.com");
        assertNotNull(defaultAccount);
        assertTrue("Default Account name: " + defaultAccount.getName(), defaultAccount.isDefault());
        assertEquals("default", defaultAccount.getName());
    }

}