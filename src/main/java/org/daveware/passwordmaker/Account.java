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
package org.daveware.passwordmaker;

import java.security.MessageDigest;
import java.util.*;

import static org.daveware.passwordmaker.ByteArrayUtils.byteArrayToHexString;

/**
 * Represents an account.  This object also functions as a parent account for
 * which it can have any number of child accounts.
 *
 * @author Dave Marotti
 */
public final class Account implements Comparable<Account> {

    public final static String DEFAULT_ACCOUNT_NAME = "default";
    public final static String DEFAULT_USERNAME = "";
    public final static String ROOT_ACCOUNT_URI = "http://passwordmaker.mozdev.org/accounts";
    public final static String DEFAULT_ACCOUNT_URI = "http://passwordmaker.mozdev.org/defaults";
    public final static int DEFAULT_LENGTH = 8;
    private String name = "";
    private String desc = "";
    private String url = "";
    private String username = "";
    private AlgorithmType algorithm = AlgorithmType.MD5;
    private boolean hmac = false;
    private boolean trim = true;
    private int length = 8;
    private String characterSet = CharacterSets.BASE_93_SET;
    private LeetType leetType = LeetType.NONE;
    private LeetLevel leetLevel = LeetLevel.LEVEL1;
    private String modifier = "";
    private String prefix = "";
    private String suffix = "";
    private boolean sha256Bug = false;
    private String id = "";
    private boolean autoPop = false;
    private EnumSet<UrlComponents> urlComponents = defaultUrlComponents();
    private ArrayList<AccountPatternData> patterns = new ArrayList<AccountPatternData>();
    private ArrayList<Account> children = new ArrayList<Account>();
    private boolean isAFolder = false;

    public static Account makeDefaultAccount() {
        Account account = new Account(DEFAULT_ACCOUNT_NAME, "", "");
        account.addUrlComponent(UrlComponents.Domain);
        account.setId(DEFAULT_ACCOUNT_URI);
        return account;
    }

    public Account() {

    }

    public Account(String name, boolean isFolder) {
        this.isAFolder = isFolder;
        this.name = name;
    }

    public Account(String name, String url, String username) {
        this.name = name;
        this.url = url;
        this.username = username;
    }

    public Account(Account toClone) {
        copySettings(toClone);
    }

    /**
     * Constructor which allows all members to be defined except for the ID which
     * will be constructed from a SHA-1 hash of the URL + username. This does not
     * include autopop.
     *
     * @param name         - name for the account
     * @param desc         - description for the account
     * @param url          - the url of the account
     * @param username     - the username for the account
     * @param algorithm    - the algorithm to use against the master password
     * @param hmac         - should you use the hmac version
     * @param trim         - the trim the input text
     * @param length       - the length of the generated password for account
     * @param characterSet - the characterSet to use for generated password
     * @param leetType     - leet-ify the generated password
     * @param leetLevel    - How much leet to use
     * @param modifier     - this field is to allow for adjustment of the outputted password
     * @param prefix       - the prefix to put in front of every generated password
     * @param suffix       - the suffix to add behind every generated password
     * @param sha256Bug    - has the buggy firefox javascript sha256
     */
    public Account(String name, String desc, String url, String username, AlgorithmType algorithm, boolean hmac,
                   boolean trim, int length, String characterSet, LeetType leetType,
                   LeetLevel leetLevel, String modifier, String prefix, String suffix,
                   boolean sha256Bug)
            throws Exception {
        this.name = name;
        this.desc = desc;
        this.url = url;
        this.username = username;
        this.algorithm = algorithm;
        this.hmac = hmac;
        this.trim = trim;
        this.length = length;
        this.characterSet = characterSet;
        this.leetType = leetType;
        this.leetLevel = leetLevel;
        this.modifier = modifier;
        this.prefix = prefix;
        this.suffix = suffix;
        this.sha256Bug = sha256Bug;
        this.id = createId(this.url + this.username);
    }

    /**
     * Alternate constructor that allows the id to also be supplied. (Still no autopop).
     */
    public Account(String name, String desc, String url, String username, AlgorithmType algorithm, boolean hmac,
                   boolean trim, int length, String characterSet, LeetType leetType,
                   LeetLevel leetLevel, String modifier, String prefix, String suffix,
                   boolean sha256Bug, String id) {
        this.name = name;
        this.desc = desc;
        this.url = url;
        this.username = username;
        this.algorithm = algorithm;
        this.hmac = hmac;
        this.trim = trim;
        this.length = length;
        this.characterSet = characterSet;
        this.leetType = leetType;
        this.leetLevel = leetLevel;
        this.modifier = modifier;
        this.prefix = prefix;
        this.suffix = suffix;
        this.sha256Bug = sha256Bug;
        this.id = id;
    }

    /**
     * Creates an ID from a string.
     * <p/>
     * This is used to create the ID of the account which should be unique.
     * There's no way for this object to know if it is truly unique so the database
     * or GUI should do a check for uniqueness and re-hash if needed.
     *
     * @param str The string to hash.
     * @return A hashed (SHA1) version of the string.
     * @throws Exception Upon hashing error.
     */
    public static String createId(String str)
            throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA1", "BC");
        return "rdf:#$" + byteArrayToHexString(digest.digest(str.getBytes()));
    }

    /**
     * Creates and _returns_ an ID from data in an account.
     *
     * @param acc The account to base the data off.
     * @return The new ID.
     * @throws Exception on error.
     */
    public static String createId(Account acc)
            throws Exception {
        return Account.createId(acc.getName() + acc.getDesc() + (new Random()).nextLong() + Runtime.getRuntime().freeMemory());
    }

    /**
     * Gets the default set of UrlComponents (empty set).
     *
     * @return the default set of UrlComponents (none)
     */
    private static EnumSet<UrlComponents> defaultUrlComponents() {
        return EnumSet.noneOf(UrlComponents.class);
    }

    /**
     * Copies the settings (not including children or ID) from another account.
     * <p/>
     * LEAVE THIS FUNCTION HERE so it's easy to see if new members are ever added
     * so I don't forget to update it.
     *
     * @param a The other account to copy from.
     */
    public void copySettings(Account a) {
        this.name = a.name;
        this.desc = a.desc;
        this.url = a.url;
        this.username = a.username;
        this.algorithm = a.algorithm;
        this.hmac = a.hmac;
        this.trim = a.trim;
        this.length = a.length;
        this.characterSet = a.characterSet;
        this.leetType = a.leetType;
        this.leetLevel = a.leetLevel;
        this.modifier = a.modifier;
        this.prefix = a.prefix;
        this.suffix = a.suffix;
        this.sha256Bug = a.sha256Bug;
        this.isAFolder = a.isAFolder;
        this.autoPop = a.autoPop;
        this.patterns.clear();
        for (AccountPatternData data : a.getPatterns()) {
            this.patterns.add(new AccountPatternData(data));
        }
        // The documentation says EnumSet.copyOf() will fail on empty sets.
        if (!a.urlComponents.isEmpty())
            this.urlComponents = EnumSet.copyOf(a.urlComponents);
        else
            this.urlComponents = defaultUrlComponents();
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String s) {
        desc = s;
    }

    public boolean isFolder() {
        return isAFolder;
    }

    /**
     * @return if the firefox version auto populates the username
     * and password fields of the page if the url is matching
     */
    @SuppressWarnings("UnusedDeclaration")
    public boolean isAutoPop() {
        return autoPop;
    }

    public void setAutoPop(boolean b) {
        autoPop = b;
    }

    /**
     * Determines if this is the default account. The default account has a special ID.
     *
     * @return true if it is.
     */
    public boolean isDefault() {
        return id.compareTo(DEFAULT_ACCOUNT_URI) == 0;
    }

    /**
     * Determines if this is the root account. The root account has a special ID.
     *
     * @return true if it is.
     */
    public boolean isRoot() {
        return id.compareTo(ROOT_ACCOUNT_URI) == 0;
    }

    public void setIsFolder(boolean b) {
        isAFolder = b;
    }

    /**
     * @return the algorithm
     */
    public AlgorithmType getAlgorithm() {
        return algorithm;
    }

    /**
     * @param algorithm the algorithm to set
     */
    public void setAlgorithm(AlgorithmType algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * @return the hmac
     */
    public boolean isHmac() {
        return hmac;
    }

    /**
     * @param hmac the hmac to set
     */
    public void setHmac(boolean hmac) {
        this.hmac = hmac;
    }

    /**
     * @return the trim
     */
    public boolean isTrim() {
        return trim;
    }

    /**
     * @param trim the trim to set
     */
    @SuppressWarnings("UnusedDeclaration")
    public void setTrim(boolean trim) {
        this.trim = trim;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the characterSet
     */
    public String getCharacterSet() {
        return characterSet;
    }

    /**
     * @param characterSet the characterSet to set
     */
    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    /**
     * @return the leetType
     */
    public LeetType getLeetType() {
        return leetType;
    }

    /**
     * @param leetType the leetType to set
     */
    public void setLeetType(LeetType leetType) {
        this.leetType = leetType;
    }

    /**
     * @return the leetLevel
     */
    public LeetLevel getLeetLevel() {
        return leetLevel;
    }

    /**
     * @param leetLevel the leetLevel to set
     */
    public void setLeetLevel(LeetLevel leetLevel) {
        this.leetLevel = leetLevel;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the modifier
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * @param modifier the modifier to set
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param suffix the suffix to set
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * @return the sha256Bug
     */
    @SuppressWarnings("UnusedDeclaration")
    public boolean isSha256Bug() {
        return sha256Bug;
    }

    /**
     * @param sha256Bug the sha256Bug to set
     */
    @SuppressWarnings("UnusedDeclaration")
    public void setSha256Bug(boolean sha256Bug) {
        this.sha256Bug = sha256Bug;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Clears the UrlComponents used with this account
     */
    public final void clearUrlComponents() {
        this.urlComponents.clear();
    }

    /**
     * @param urlComponent - Add a component of the url to be used as the input text for the generated password
     */
    public final void addUrlComponent(UrlComponents urlComponent) {
        this.urlComponents.add(urlComponent);
    }

    /**
     * @param urlComponent - Add a component of the url to be used as the input text for the generated password
     */
    @SuppressWarnings("UnusedDeclaration")
    public final void removeUrlComponent(UrlComponents urlComponent) {
        this.urlComponents.remove(urlComponent);
    }

    /**
     * If the urlComponents field is empty then the entire getUrl field will be used.
     * This set is unmodifiable.  Use the helper functions to set or modify the set.
     *
     * @return the url components specified for this account (may be empty)
     */
    public final Set<UrlComponents> getUrlComponents() {
        return Collections.unmodifiableSet(urlComponents);
    }

    /**
     * @param urlComponents - the Components to use of the url as the input text for the generated password
     */
    public final void setUrlComponents(Set<UrlComponents> urlComponents) {
        this.urlComponents.clear();
        this.urlComponents.addAll(urlComponents);
    }

    /**
     * Gets a list of the child accounts. This list is modifiable.
     *
     * @return The list of accounts (may be empty).
     */
    public ArrayList<Account> getChildren() {
        return children;
    }

    /**
     * Gets the count of all children (of children of children...).
     *
     * @return The total number of all descendants.
     */
    @SuppressWarnings("UnusedDeclaration")
    public int getNestedChildCount() {
        ArrayList<Account> stack = new ArrayList<Account>();
        int size = 0;

        stack.add(this);
        while (stack.size() > 0) {
            Account current = stack.get(0);
            stack.remove(0);
            for (Account child : current.getChildren()) {
                size++;
                if (child.hasChildren())
                    stack.add(child);
            }
        }

        return size;
    }

    /**
     * Gets a specifically indexed child.
     *
     * @param index The index of the child to get.
     * @return The child at the index.
     * @throws IndexOutOfBoundsException upon invalid index.
     */
    public Account getChild(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= children.size())
            throw new IndexOutOfBoundsException("Illegal child index, " + index);
        return children.get(index);
    }

    public boolean hasChildren() {
        return children.size() > 0;
    }

    /**
     * Tests if an account is a direct child of this account.
     *
     * @param account check if the passed in account is a child of 'this' account
     * @return true if given account is a child of 'this' account
     */
    public boolean hasChild(Account account) {
        for (Account child : children) {
            if (child.equals(account))
                return true;
        }
        return false;
    }

    /**
     * Implements the Comparable<Account> interface, this is based first on if the
     * account is a folder or not. This is so that during sorting, all folders are
     * first in the list.  Finally, it is based on the name.
     *
     * @param o The other account to compare to.
     * @return this.name.compareTo(otherAccount.name);
     */
    public int compareTo(Account o) {
        if (this.isFolder() && !o.isFolder())
            return -1;
        else if (!this.isFolder() && o.isFolder())
            return 1;

        // First ignore case, if they equate, use case.
        int result = name.compareToIgnoreCase(o.name);
        if (result == 0)
            return name.compareTo(o.name);
        else
            return result;
    }

    /**
     * Determines if these are the same object or not. "Same objects" are those that have
     * identical IDs.
     */
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Account))
            return false;

        Account thatAccount = (Account) o;

        return thatAccount.id.compareTo(this.id) == 0;
    }

    /**
     * @return the patterns
     */
    public ArrayList<AccountPatternData> getPatterns() {
        return patterns;
    }

    /**
     * This will make a deep clone of the passed in Iterable.
     * This will also replace any patterns already set.
     *
     * @param patterns the patterns to set
     */
    @SuppressWarnings("UnusedDeclaration")
    public void setPatterns(Iterable<AccountPatternData> patterns) {
        this.patterns.clear();
        for (AccountPatternData data : patterns) {
            this.patterns.add(new AccountPatternData(data));
        }
    }

    @Override
    public String toString() {
        return this.name;
    }



    public String toDebugString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", url='" + url + '\'' +
                ", isDefault=" + isDefault() +
                ", isRoot=" + isRoot() +
                ", username='" + username + '\'' +
                ", algorithm=" + algorithm +
                ", hmac=" + hmac +
                ", trim=" + trim +
                ", length=" + length +
                ", characterSet='" + characterSet + '\'' +
                ", leetType=" + leetType +
                ", leetLevel=" + leetLevel +
                ", modifier='" + modifier + '\'' +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", sha256Bug=" + sha256Bug +
                ", id='" + id + '\'' +
                ", autoPop=" + autoPop +
                ", urlComponents=" + urlComponents +
                ", patterns=" + patterns +
                ", children=" + children +
                ", isAFolder=" + isAFolder +
                '}';
    }

    public enum UrlComponents {
        Protocol, Subdomain, Domain, PortPathAnchorQuery
    }
}
