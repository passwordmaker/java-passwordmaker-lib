package org.daveware.passwordmaker.test;

import org.daveware.passwordmaker.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.daveware.passwordmaker.test.TestUtils.readFully;
import static org.junit.Assert.*;

public class AndroidIssue5Test {

    public static final String GROUP_1_ID = "rdf:#$pehSJ2";
    public static final String GROUP_2_ID = "rdf:#$TZRYl3";
    public static final String GROUP_3_ID = "rdf:#$AG62u2";
    private static Database db;
    private static Account rootAccount;

    @BeforeClass
    public static void setupClass() throws Exception {
        Logger configure_logger = Logger.getLogger("org.daveware.passwordmaker");
        configure_logger.setLevel(Level.FINE);
        TestUtils.addBCProvider();

        byte[] xml = readResourceAsByteArray("sample3.xml");
        RDFDatabaseReader reader = new RDFDatabaseReader();
        reader.setBuggyAlgoUseAction(DatabaseReader.BuggyAlgoAction.CONVERT);
        db = reader.read(new ByteArrayInputStream(xml));
        assertNotNull(db);
        rootAccount = db.getRootAccount();
        assertNotNull(rootAccount);
    }

    @Test
    public void rootShouldBeARoot() {
        assertTrue(db.getRootAccount().isRoot());
        assertEquals(4, rootAccount.getChildren().size());
    }

    @Test
    public void testDefaultAccount() {
        Account actualDefaultAccount =  db.findAccountById(Account.DEFAULT_ACCOUNT_URI);
        assertNotNull(actualDefaultAccount);
        assertEquals(AlgorithmType.SHA1, actualDefaultAccount.getAlgorithm());
        assertTrue(actualDefaultAccount.isHmac());
        assertEquals(CharacterSets.BASE_93_SET, actualDefaultAccount.getCharacterSet());
        assertEquals(12, actualDefaultAccount.getLength());
        assertEquals(LeetType.NONE, actualDefaultAccount.getLeetType());
        assertTrue(actualDefaultAccount.getUrlComponents().contains(Account.UrlComponents.Protocol));
        assertTrue(actualDefaultAccount.getUrlComponents().contains(Account.UrlComponents.Domain));
        assertTrue(actualDefaultAccount.getUrlComponents().contains(Account.UrlComponents.Subdomain));
        assertTrue(actualDefaultAccount.getUrlComponents().contains(Account.UrlComponents.PortPathAnchorQuery));
        assertEquals("Default settings for URLs not elsewhere in this list", actualDefaultAccount.getDesc());
        assertTrue(actualDefaultAccount.isDefault());
    }

    @Test
    public void group1HasChildren() {
        Account actualAccount =  db.findAccountById(GROUP_1_ID);
        assertEquals("Group1", actualAccount.getName());
        assertTrue(actualAccount.isFolder());
        assertEquals(5, actualAccount.getChildren().size());
    }

    @Test
    public void group1Account1() {
        Account actualAccount =  db.findAccountById(GROUP_1_ID);
        Account actualChild = childById(actualAccount, "rdf:#$ETgST3");
        testAccount(actualChild, "Acc1", CharacterSets.BASE_93_SET, "username", "https://domain1.net/account1/",
                12, AlgorithmType.MD4, false, "", "https://domain1.*", AccountPatternType.WILDCARD, true);
    }

    @Test
    public void group1Account2() {
        Account actualAccount =  db.findAccountById(GROUP_1_ID);
        Account actualChild = childById(actualAccount, "rdf:#$qrhr73");
        testAccount(actualChild, "Acc2", CharacterSets.BASE_93_SET, "username", "https://domain2.net/account2/",
                12, AlgorithmType.MD4, true, "", "https://domain2.*", AccountPatternType.WILDCARD, true);
    }

    @Test
    public void group1Account3() {
        Account actualAccount =  db.findAccountById(GROUP_1_ID);
        Account actualChild = childById(actualAccount, "rdf:#$gvFEB");
        testAccount(actualChild, "Acc3", CharacterSets.BASE_93_SET, "username", "https://domain3.net/account3/",
                12, AlgorithmType.RIPEMD160, false, "", "https://domain3.*", AccountPatternType.WILDCARD, true);
    }

    @Test
    public void group1Account4() {
        Account actualAccount =  db.findAccountById(GROUP_1_ID);
        Account actualChild = childById(actualAccount, "rdf:#$b2dBg1");
        String customChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        testAccount(actualChild, "Acc4", customChar, "username", "https://domain4.net/account4/",
                12, AlgorithmType.SHA256, true, "", "https://domain4.*", AccountPatternType.WILDCARD, true);
    }

    @Test
    public void group1Account5() {
        Account actualAccount =  db.findAccountById(GROUP_1_ID);
        Account actualChild = childById(actualAccount, "rdf:#$rwJsu3");
        String customChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#$%&*()_-+=";
        testAccount(actualChild, "Acc5", customChar, "username", "https://domain5.net/account5/",
                12, AlgorithmType.RIPEMD160, true);
    }


    @Test
    public void group2HasChildren() {
        Account actualAccount =  db.findAccountById(GROUP_2_ID);
        assertEquals("Group2", actualAccount.getName());
        assertTrue(actualAccount.isFolder());
        assertEquals(1, actualAccount.getChildren().size());
    }

    @Test
    public void group2Account6() {
        Account actualAccount =  db.findAccountById(GROUP_2_ID);
        Account actualChild = childById(actualAccount, "rdf:#$aWLWp3");
        String customChar = CharacterSets.BASE_93_SET;
        testAccount(actualChild, "Acc6", customChar, "username", "https://domain6.net/account6/",
                12, AlgorithmType.RIPEMD160, true, "", "https://domain6.*", AccountPatternType.WILDCARD, true);
    }

    @Test
    public void group3HasChildren() {
        Account actualAccount =  db.findAccountById(GROUP_3_ID);
        assertEquals("Group3", actualAccount.getName());
        assertTrue(actualAccount.isFolder());
        assertEquals(1, actualAccount.getChildren().size());
    }

    @Test
    public void group3Account7() {
        Account actualAccount =  db.findAccountById(GROUP_3_ID);
        Account actualChild = childById(actualAccount, "rdf:#$z6zFm3");
        String customChar = CharacterSets.BASE_93_SET;
        testAccount(actualChild, "Acc7", customChar, "username", "https://domain7.net/account7/",
                12, AlgorithmType.SHA256, true, "", "http*://domain7.*", AccountPatternType.WILDCARD, true);
    }

    private void testAccount(Account actualChild, String name, String charSet, String username,
                             String url, int length, AlgorithmType algo, boolean isHmac) {
        testAccount(actualChild, name, charSet, username, url, length, algo, isHmac, new ArrayList<AccountPatternData>());
    }

    private void testAccount(Account actualChild, String name, String charSet, String username,
                             String url, int length, AlgorithmType algo, boolean isHmac, String patternDesc,
                             String pattern, AccountPatternType patternType, boolean patternEnabled) {
        ArrayList<AccountPatternData> patterns = new ArrayList<AccountPatternData>();
        AccountPatternData patternData = new AccountPatternData();
        patternData.setDesc(patternDesc);
        patternData.setPattern(pattern);
        patternData.setType(patternType);
        patternData.setEnabled(patternEnabled);
        patterns.add(patternData);
        testAccount(actualChild, name, charSet, username, url, length, algo, isHmac, patterns);
    }

    private void testAccount(Account actualChild, String name, String charSet, String username,
                             String url, int length, AlgorithmType algo, boolean isHmac,
                             List<AccountPatternData> patterns) {
        assertEquals(name, actualChild.getName());
        assertEquals(charSet, actualChild.getCharacterSet());
        assertEquals(username, actualChild.getUsername());
        assertEquals(length, actualChild.getLength());
        assertEquals(algo, actualChild.getAlgorithm());
        assertEquals(isHmac, actualChild.isHmac());
        assertEquals(url, actualChild.getUrl());

        assertEquals(patterns.size(), actualChild.getPatterns().size());
        for ( int i = 0; i < patterns.size(); ++i) {
            AccountPatternData expectedPattern = patterns.get(i);
            AccountPatternData actualPattern = actualChild.getPatterns().get(i);
            assertEquals(expectedPattern.getDesc(), actualPattern.getDesc());
            assertEquals(expectedPattern.getPattern(), actualPattern.getPattern());
            assertEquals(expectedPattern.getType(), actualPattern.getType());
            assertEquals(expectedPattern.isEnabled(), actualPattern.isEnabled());
        }
    }


    private static Account childById(Account account, String id) {
        for (Account child : account.getChildren()) {
            if ( child.getId().equals(id) )
                return child;
        }
        StringBuilder ids = new StringBuilder();
        for (Account child : account.getChildren()) {
            ids.append(child.getId());
            ids.append(",");
        }

        throw new NoSuchElementException(id + "{" + ids + "}");
    }

    private static byte[] readResourceAsByteArray(String resourceName) {
        InputStream inputStream = null;
        try {
            inputStream = AndroidIssue5Test.class.getResourceAsStream(resourceName);
            if ( inputStream == null ) {
                throw new FileNotFoundException(resourceName + " is not a found resource");
            }
            return readFully(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if ( inputStream != null ) {
                try { inputStream.close(); } catch (IOException ignored) {}
            }
        }
    }
}
