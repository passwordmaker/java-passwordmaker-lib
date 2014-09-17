package org.daveware.passwordmaker.test;

import org.daveware.passwordmaker.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.daveware.passwordmaker.test.TestUtils.addBCProvider;
import static org.daveware.passwordmaker.test.TestUtils.saToString;
import static org.junit.Assert.assertEquals;

public class Md5V6Test {


    @BeforeClass
    public static void setupClass() {
        addBCProvider();
    }

    @Test
    public void testV6() throws Exception {
        PasswordMaker pm = new PasswordMaker();
        Account account = new Account();
        account.setAlgorithm(AlgorithmType.MD5);
        account.setTrim(false);
        account.setLength(4);
        account.setCharacterSet(CharacterSets.NUMERIC);
        account.clearUrlComponents();
        account.addUrlComponent(Account.UrlComponents.Domain);
        final SecureCharArray masterPassword = new SecureCharArray("happy");
        assertEquals("0617", saToString(pm.makePassword(masterPassword, account, "random.co.uk")));
    }

    @Test
    public void testNonV6() throws Exception {
        PasswordMaker pm = new PasswordMaker();
        Account account = new Account();
        account.setAlgorithm(AlgorithmType.MD5);
        account.setTrim(true);
        account.setLength(4);
        account.setCharacterSet(CharacterSets.NUMERIC);
        account.clearUrlComponents();
        account.addUrlComponent(Account.UrlComponents.Domain);
        final SecureCharArray masterPassword = new SecureCharArray("happy");
        assertEquals("6172", saToString(pm.makePassword(masterPassword, account, "random.co.uk")));
    }
}
