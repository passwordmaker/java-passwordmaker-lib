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
package org.daveware.passwordmaker;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public class AccountBuilder {


    private String name = "";
    private String desc = "";
    private String url = "";
    private String username = "";
    private AlgorithmType algorithm = AlgorithmType.MD5;
    private boolean hmac = false;
    private boolean trim = false;
    private int length = 8;
    private String characterSet = CharacterSets.ALPHANUMERIC;
    private LeetType leetType = LeetType.NONE;
    private LeetLevel leetLevel = LeetLevel.LEVEL1;
    private String modifier = "";
    private String prefix = "";
    private String suffix = "";
    private boolean sha256Bug = false;
    private String id = null;
    private List<AccountPatternData> patterns = new ArrayList<AccountPatternData>();
    private List<Account> childrenAccounts = new ArrayList<Account>();
    private EnumSet<Account.UrlComponents> urlComponents = EnumSet.noneOf(Account.UrlComponents.class);

    public AccountBuilder() {

    }

    public Account build() throws Exception {
        Account account = new Account(name, desc, url, username, algorithm, hmac, trim, length, characterSet,
                leetType, leetLevel, modifier, prefix, suffix, sha256Bug);
        account.setPatterns(patterns);
        if ( childrenAccounts.size() > 0 ) {
            account.getChildren().addAll(childrenAccounts);
        }
        if ( urlComponents != null && ! urlComponents.isEmpty() ) {
            account.setUrlComponents(urlComponents);
        }
        if ( id != null ) account.setId(id);
        return account;
    }

    public Account buildDefaultAccount() throws Exception {
        setId(Account.DEFAULT_ACCOUNT_URI);
        urlComponents = EnumSet.of(Account.UrlComponents.Domain);
        if ( name == null || name.isEmpty() ) {
            name = Account.DEFAULT_ACCOUNT_NAME;
        }
        return build();
    }

    public String getName() {
        return name;
    }

    public AccountBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public AccountBuilder setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AccountBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AccountBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public AlgorithmType getAlgorithm() {
        return algorithm;
    }

    public AccountBuilder setAlgorithm(AlgorithmType algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public boolean isHmac() {
        return hmac;
    }

    public AccountBuilder setHmac(boolean hmac) {
        this.hmac = hmac;
        return this;
    }

    public boolean isTrim() {
        return trim;
    }

    public AccountBuilder setTrim(boolean trim) {
        this.trim = trim;
        return this;
    }

    public int getLength() {
        return length;
    }

    public AccountBuilder setLength(int length) {
        this.length = length;
        return this;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public AccountBuilder setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
        return this;
    }

    public LeetType getLeetType() {
        return leetType;
    }

    public AccountBuilder setLeetType(LeetType leetType) {
        this.leetType = leetType;
        return this;
    }

    public LeetLevel getLeetLevel() {
        return leetLevel;
    }

    public AccountBuilder setLeetLevel(LeetLevel leetLevel) {
        this.leetLevel = leetLevel;
        return this;
    }

    public String getModifier() {
        return modifier;
    }

    public AccountBuilder setModifier(String modifier) {
        this.modifier = modifier;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public AccountBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getSuffix() {
        return suffix;
    }

    public AccountBuilder setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public boolean isSha256Bug() {
        return sha256Bug;
    }

    public AccountBuilder setSha256Bug(boolean sha256Bug) {
        this.sha256Bug = sha256Bug;
        return this;
    }

    public String getId() {
        return id;
    }

    public AccountBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public AccountBuilder clearPatterns() {
        patterns.clear();
        return this;
    }

    public AccountBuilder addPattern(String desc, String pattern, AccountPatternType type, boolean enabled) {
        AccountPatternData apd = new AccountPatternData();
        apd.setDesc(desc);
        apd.setType(type);
        apd.setEnabled(enabled);
        apd.setPattern(pattern);
        patterns.add(apd);
        return this;
    }

    public AccountBuilder addWildcardPattern(String desc, String pattern) {
        return addPattern(desc, pattern, AccountPatternType.WILDCARD, true);
    }

    public AccountBuilder addRegexPattern(String desc, String pattern) {
        return addPattern(desc, pattern, AccountPatternType.REGEX, true);
    }

    public AccountBuilder clearChildren() {
        childrenAccounts.clear();
        return this;
    }

    public AccountBuilder addChildAccount(Account child) {
        childrenAccounts.add(child);
        return this;
    }

    public AccountBuilder addChildAccount(AccountBuilder child) throws Exception {
        return addChildAccount(child.build());
    }

    public AccountBuilder clearUrlComponents() {
        urlComponents.clear();
        return this;
    }

    public AccountBuilder addUrlComponent(Account.UrlComponents component) {
        urlComponents.add(component);
        return this;
    }

}
