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

import org.daveware.passwordmaker.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static javax.xml.bind.DatatypeConverter.printHexBinary;
import static org.daveware.passwordmaker.StringEncodingUtils.bytesToCharArrayUTFNIO;
import static org.daveware.passwordmaker.StringEncodingUtils.charArrayToBytesUTFNIO;
import static org.daveware.passwordmaker.test.TestUtils.*;
import static org.junit.Assert.assertEquals;

public class IssuesTest {

    public static final String SAMPLE_2_RDF = "/org/daveware/passwordmaker/test/sample2.rdf";
    public static final String SAMPLE_SEL_WRONG_PROFILE = "issue_selecting_wrong_profile.rdf";
    public static final String SAMPLE_SEL_WRONG_PROFILE_MULTI = "issue_selecting_wrong_profile_multi.rdf";

    @BeforeClass
    public static void setupClass() {
        addBCProvider();
    }

    @Test
    public void testIssue10() throws Exception {
        PasswordMaker pm = new PasswordMaker();
        Account account = new Account();
        account.setAlgorithm(AlgorithmType.MD5);
        account.setLength(8);
        account.setCharacterSet(CharacterSets.ALPHANUMERIC);
        account.clearUrlComponents();
        account.addUrlComponent(Account.UrlComponents.Domain);
        final SecureUTF8String masterPassword = new SecureUTF8String("happy");
        saToString(pm.makePassword(masterPassword, account, "google.com"));
        assertEquals("HRdgNiyh", saToString(pm.makePassword(masterPassword, account, "google.com")));
        assertEquals("DsTpW36p", saToString(pm.makePassword(masterPassword, account, "cnn.com")));
        assertEquals("Fn4n23hm", saToString(pm.makePassword(masterPassword, account, "google.co.uk")));
        assertEquals("Fn4n23hm", saToString(pm.makePassword(masterPassword, account, "www.google.co.uk")));
        assertEquals("G78kh9hf", saToString(pm.makePassword(masterPassword, account, "news.bbc.co.uk")));
        assertEquals("BZmxeqtq", saToString(pm.makePassword(masterPassword, account, "random.co.uk")));
    }

    /**
     * This test point is to ensure that we can do a round trip.
     *
     * @throws Exception
     */
    @Test
    public void testIssueOfRoundTripSerialization() throws Exception {
        RDFDatabaseReader reader = new RDFDatabaseReader();
        InputStream source = IssuesTest.class.getResourceAsStream(SAMPLE_2_RDF);
        Database db = reader.read(source);
        source.close();
        Map<String, String> expectedMap = getIdToNameMap();
        assertEquals(expectedMap.size(), db.getRootAccount().getNestedChildCount());
        for (Map.Entry<String, String> expected : expectedMap.entrySet()) {
            Account account = db.findAccountById(expected.getKey());
            assertEquals(expected.getKey(), account.getId());
            assertEquals(expected.getValue(), account.getName());
        }

        RDFDatabaseWriter writer = new RDFDatabaseWriter();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        writer.write(output, db);

        source = IssuesTest.class.getResourceAsStream(SAMPLE_2_RDF);
        String expectedOutput = new String(readFully(source));
        expectedOutput = expectedOutput.replaceAll("\n", "");
        String actual = output.toString().replaceAll("\n", "");
        assertEquals( expectedOutput, actual);
    }

    @Test
    public void testIssueOfSelectingWrongProfile() throws Exception {
        RDFDatabaseReader reader = new RDFDatabaseReader();
        InputStream source = IssuesTest.class.getResourceAsStream(SAMPLE_SEL_WRONG_PROFILE);
        Database db = reader.read(source);
        source.close();
        AccountManager accountManager = new AccountManager();
        accountManager.getPwmProfiles().swapAccounts(db);
        assertEquals(1, accountManager.getPwmProfiles().getAllAccounts().size());
        assertEquals("3BYc3EMvAVfS", saToString(accountManager.generatePassword("happy", "cnn.com")));
    }

    @Test
    public void testIssueOfSelectingWrongProfileMultiProfs() throws Exception {
        RDFDatabaseReader reader = new RDFDatabaseReader();
        InputStream source = IssuesTest.class.getResourceAsStream(SAMPLE_SEL_WRONG_PROFILE_MULTI);
        Database db = reader.read(source);
        source.close();
        AccountManager accountManager = new AccountManager();
        accountManager.getPwmProfiles().swapAccounts(db);
        assertEquals(2, accountManager.getPwmProfiles().getAllAccounts().size());
        assertEquals("3BYc3EMvAVfS", saToString(accountManager.generatePassword("happy", "cnn.com")));
        assertEquals("1XMuoKqNXUMqUv", saToString(accountManager.generatePassword("happy", "everywhere.com")));
    }


    private static Map<String, String> getIdToNameMap() {
        Map<String, String> m = new HashMap<String, String>();
        m.put("rdf:#$58fc633788509272e02e72c9b0c048782cde5d46", "Personal");
        m.put("rdf:#$82202fee07f636e851ace3a6e65eaf3428e9c02f","Work");
        m.put("http://passwordmaker.mozdev.org/defaults","default");
        m.put("rdf:#$a0ca03949dc36323e6ef716e3064eb260531f363","Reddit");
        m.put("rdf:#$1d416d3dc326135b6349532791807b0ba154fb9d","facebook");
        m.put("rdf:#$daec72c4b98769c07069d85f512e069cffdd407d","stackoverflow");
        m.put("rdf:#$32232ef276daca6638ac3826e388e2bb20f841f1","github.com");
        return m;
    }

    @Test
    public void testSomeEncoding() throws Exception {
        final String normalPass = "PasswordMaker\u00A9\u20AC\uD852\uDF62";
        //String normalPass = "happy";
        char[] normal = (normalPass).toCharArray();
        byte[] actual = charArrayToBytesUTFNIO(normal);
        byte[] expected = normalPass.getBytes("UTF-8");
        String actualHexDump = printHexBinary(actual);
        String expectedHexDump = printHexBinary(expected);

        char[] reverseCommute = bytesToCharArrayUTFNIO(actual);
        assertEquals(normalPass, new String(reverseCommute));

        assertEquals("pass=" + normalPass, actualHexDump, expectedHexDump);
    }
}
