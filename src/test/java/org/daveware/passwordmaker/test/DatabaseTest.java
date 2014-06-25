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

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.daveware.passwordmaker.Account;
import org.daveware.passwordmaker.Database;
import org.daveware.passwordmaker.DatabaseListener;
import org.junit.*;

import java.security.Security;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Runs test against the Database class.
 *
 * @author Dave Marotti
 */
public class DatabaseTest {

    int numAdd = 0;
    int numRemove = 0;
    int numChange = 0;
    int numDirty = 0;

    public DatabaseTest() {
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
     * Test of addAccount method, of class Database.
     */
    @Test
    public void testAddAccount() throws Exception {
        Database instance = new Database();
        instance.addDatabaseListener(new MyDBListener());

        numChange = 0;

        Account account = new Account("name1", "http://url.org", "username1");
        instance.addAccount(instance.getRootAccount(), account);
        Account subAccount = new Account("name2", "http://url2.org", "username2");
        instance.addAccount(account, subAccount);
        Account account2 = new Account("name3", "http://url3.org", "username3");
        instance.addAccount(instance.getRootAccount(), account2);

        assertEquals(numAdd, 3);

        assertEquals(instance.getRootAccount().getChild(0).getName(), account.getName());
        assertEquals(instance.getRootAccount().getChild(0).getChildren().get(0).getName(), subAccount.getName());
        assertEquals(instance.getRootAccount().getChild(1).getName(), account2.getName());

        assertEquals(numDirty, 3);
    }

    /**
     * Test of changeAccount method, of class Database.
     */
    @Test
    public void testChangeAccount() throws Exception {
        Database instance = new Database();
        instance.addDatabaseListener(new MyDBListener());

        numChange = 0;

        Account account = new Account("name1", "http://url.org", "username1");
        instance.addAccount(instance.getRootAccount(), account);
        Account subAccount = new Account("name2", "http://url2.org", "username2");
        instance.addAccount(account, subAccount);
        Account account2 = new Account("name3", "http://url3.org", "username3");
        instance.addAccount(instance.getRootAccount(), account2);

        account2.setName("name4");
        instance.changeAccount(account2);
        assertEquals(numChange, 1);
        assertEquals(instance.getRootAccount().getChild(1).getName(), account2.getName());
        assertEquals(numDirty, 4);
    }

    /**
     * Test of removeAccount method, of class Database.
     */
    @Test
    public void testRemoveAccount() throws Exception {
        Database instance = new Database();
        instance.addDatabaseListener(new MyDBListener());

        numChange = 0;

        Account account = new Account("name1", "http://url.org", "username1");
        instance.addAccount(instance.getRootAccount(), account);
        Account subAccount = new Account("name2", "http://url2.org", "username2");
        instance.addAccount(account, subAccount);
        Account account2 = new Account("name3", "http://url3.org", "username3");
        instance.addAccount(instance.getRootAccount(), account2);

        instance.removeAccount(account);
        assertEquals(numRemove, 1);
        assertEquals(instance.getRootAccount().getChild(0).getName(), account2.getName());
        assertEquals(numDirty, 4);
    }

    /**
     * Test of printDatabase method, of class Database.
     */
    @Test
    public void testPrintDatabase() throws Exception {
        Database instance = new Database();
        instance.addDatabaseListener(new MyDBListener());

        Account account = new Account("name1", "http://url.org", "username1");
        instance.addAccount(instance.getRootAccount(), account);
        Account subAccount = new Account("name2", "http://url2.org", "username2");
        instance.addAccount(account, subAccount);
        Account account2 = new Account("name3", "http://url3.org", "username3");
        instance.addAccount(instance.getRootAccount(), account2);

        instance.printDatabase();
    }

    /**
     * Test of findParent method, of class Database.
     */
    @Test
    public void testFindParent() throws Exception {
        Database instance = new Database();
        instance.addDatabaseListener(new MyDBListener());

        Account account = new Account("name1", "http://url.org", "username1");
        instance.addAccount(instance.getRootAccount(), account);
        Account subAccount = new Account("name2", "http://url2.org", "username2");
        instance.addAccount(account, subAccount);
        Account account2 = new Account("name3", "http://url3.org", "username3");
        instance.addAccount(instance.getRootAccount(), account2);

        assertEquals(account, instance.findParent(subAccount));
        assertEquals(instance.getRootAccount(), instance.findParent(account));
        assertEquals(instance.getRootAccount(), instance.findParent(account2));
    }

    @Test
    public void testFindAccountById() throws Exception {
        Database instance = new Database();
        instance.addDatabaseListener(new MyDBListener());

        Account account = new Account("name1", "http://url.org", "username1");
        account.setId(Account.createId("name1"));
        instance.addAccount(instance.getRootAccount(), account);
        Account subAccount = new Account("name2", "http://url2.org", "username2");
        subAccount.setId(Account.createId("name2"));
        instance.addAccount(account, subAccount);
        Account account2 = new Account("name3", "http://url3.org", "username3");
        account2.setId(Account.createId("name3"));
        instance.addAccount(instance.getRootAccount(), account2);

        assertEquals(account, instance.findAccountById(account.getId()));
        assertEquals(account2, instance.findAccountById(account2.getId()));
        assertEquals(subAccount, instance.findAccountById(subAccount.getId()));

        assertNull(instance.findAccountById("DNE"));
    }

    @Test
    public void testFindAccountByIdWithPath() throws Exception {
        Database instance = new Database();
        instance.addDatabaseListener(new MyDBListener());

        Account folder1 = new Account("folder1", "", "");
        folder1.setId(Account.createId("folder1"));
        folder1.setIsFolder(true);
        instance.addAccount(instance.getRootAccount(), folder1);
        Account subAccount = new Account("name2", "http://url2.org", "username2");
        subAccount.setId(Account.createId("name2"));
        instance.addAccount(folder1, subAccount);
        Account account2 = new Account("name3", "http://url3.org", "username3");
        account2.setId(Account.createId("name3"));
        instance.addAccount(instance.getRootAccount(), account2);

        Account folder2 = new Account("folder2", "", "");
        folder2.setId(Account.createId("folder2"));
        folder2.setIsFolder(true);
        instance.addAccount(instance.getRootAccount(), folder2);

        Account account3 = new Account("name4", "http://url4.org", "username4");
        account3.setId(Account.createId("name4"));
        instance.addAccount(folder2, account3);

        Account folder3 = new Account("folder3", "", "");
        folder3.setId(Account.createId("folder3"));
        folder3.setIsFolder(true);
        instance.addAccount(folder2, folder3);

        Account account4 = new Account("name5", "http://url5.org", "username5");
        account4.setId(Account.createId("name5"));
        instance.addAccount(folder3, account4);

        List<Account> expected = Arrays.asList(instance.getRootAccount(), folder1, subAccount );
        assertEquals(expected, instance.findPathToAccountById(subAccount.getId()));

        expected = Arrays.asList(instance.getRootAccount(), folder2, account3 );
        assertEquals(expected, instance.findPathToAccountById(account3.getId()));

        expected = Arrays.asList(instance.getRootAccount(), folder2, folder3, account4 );
        assertEquals(expected, instance.findPathToAccountById(account4.getId()));

        expected = Arrays.asList();
        assertEquals(expected, instance.findPathToAccountById("DNE"));
    }

    @Test
    public void verifyLinkedList() {
        List<String> strs = Arrays.asList("a", "b", "c");
        LinkedList<String> stack = new LinkedList<String>();
        for ( String s : strs) stack.push(s);
        assertEquals("c", stack.peek());
        assertEquals("c", stack.pop());
        assertEquals("b", stack.peek());
        assertEquals("b", stack.pop());
        assertEquals("a", stack.peek());
        assertEquals("a", stack.pop());
    }

    class MyDBListener implements DatabaseListener {
        @Override
        public void accountAdded(Account parent, Account account) {
            numAdd++;
        }

        @Override
        public void accountRemoved(Account parent, Account account) {
            numRemove++;
        }

        @Override
        public void accountChanged(Account account) {
            numChange++;
        }

        @Override
        public void dirtyStatusChanged(boolean status) {
            numDirty++;
        }
    }
}
