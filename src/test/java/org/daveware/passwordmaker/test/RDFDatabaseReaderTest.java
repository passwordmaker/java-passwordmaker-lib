/*
 * PasswordMaker Java Edition - One Password To Rule Them All
 * Copyright (C) 2011 Dave Marotti
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

import junit.framework.Assert;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.daveware.passwordmaker.Account;
import org.daveware.passwordmaker.Database;
import org.daveware.passwordmaker.RDFDatabaseReader;
import org.junit.*;

import java.io.InputStream;
import java.security.Security;

/**
 * Runs tests against the RDFDatabaseReader class.
 *
 * @author Dave Marotti
 */
public class RDFDatabaseReaderTest {

    public RDFDatabaseReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of read method, of class RDFDatabaseReader.
     */
    @Test
    public void testRead() throws Exception {
        RDFDatabaseReader reader = new RDFDatabaseReader();
        InputStream is = getClass().getResourceAsStream("sample.rdf");
        Database db = reader.read(is);
        db.printDatabase();
    }

    @Test
    public void testReadAccount() throws Exception {
        RDFDatabaseReader reader = new RDFDatabaseReader();
        InputStream is = getClass().getResourceAsStream("sample_account.rdf");
        Account acc = reader.deserializeAccount(is);
        Assert.assertEquals("Yahoo.com", acc.getName());
        Assert.assertEquals("rdf:#$[B@5a676437", acc.getId());
        Assert.assertEquals("Another Modifier", acc.getModifier());
        Assert.assertEquals("Im a description", acc.getDesc());
    }

    /**
     * Test of getExtension method, of class RDFDatabaseReader.
     */
    @Test
    public void testGetExtension() {
    }
}
