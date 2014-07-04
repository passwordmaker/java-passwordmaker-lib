package org.daveware.passwordmaker;

import org.daveware.passwordmaker.util.Joiner;
import org.daveware.passwordmaker.util.Splitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class AccountManager implements DatabaseListener {

    // http://stackoverflow.com/questions/1658702/how-do-i-make-a-class-extend-observable-when-it-has-extended-another-class-too
    private final CopyOnWriteArrayList<AccountManagerListener> listeners = new CopyOnWriteArrayList<AccountManagerListener>();
    private PasswordMaker pwm = new PasswordMaker();
    private Database pwmProfiles = new Database();
    private Account selectedProfile = null;
    private List<String> favoriteUrls = new ArrayList<String>();

    private static final Account masterPwdHashAccount = makeDefaultAccount();
    private SecureCharArray currentPasswordHash;
    private String passwordSalt;
    private boolean storePasswordHash;



    private static Account makeDefaultAccount() {
        Account account = Account.makeDefaultAccount();
        account.setAlgorithm(AlgorithmType.MD5);
        account.setCharacterSet(CharacterSets.ALPHANUMERIC);
        return account;
    }

    public AccountManager() {
        // load database
        if ( pwmProfiles.findAccountById(Account.DEFAULT_ACCOUNT_URI) == null ) {
            try {
                pwmProfiles.addAccount(pwmProfiles.getRootAccount(), makeDefaultAccount());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isAutoSelectingAccount() {
        return selectedProfile == null;
    }

    public CharSequence generatePassword(CharSequence masterPassword, String inputText) {
        SecureCharArray securedMasterPassword;
        if ( ! (masterPassword instanceof SecureCharArray) ) {
            securedMasterPassword = new SecureCharArray(masterPassword.toString());
        } else {
            securedMasterPassword = (SecureCharArray)masterPassword;
        }
        SecureCharArray result = null;
        try {
            Account accountToUse = getAccountForInputText(inputText);
            result = pwm.makePassword(securedMasterPassword, accountToUse, inputText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ( result != null ) {
            return result;
        }
        return "";
    }

    // Public for testing
    public Account getAccountForInputText(String inputText) {
        if ( selectedProfile != null ) return selectedProfile;
        Account account = pwmProfiles.findAccountByUrl(inputText);
        if ( account == null ) return getDefaultAccount();
        return account;
    }

    public void selectAccountById(String id) throws IllegalArgumentException {
        if ( id == null ) {
            clearSelectedAccount();
            return;
        }
        Account profile = pwmProfiles.findAccountById(id);
        if ( profile != null ) {
            selectedProfile = profile;

        } else {
            throw new IllegalArgumentException("Profile by the ID of " + id + " is not found.");
        }
    }

    public void clearSelectedAccount() {
        selectedProfile = null;
    }

    public void selectAccountByUrl(String url) throws IllegalArgumentException {
        Account profile = pwmProfiles.findAccountByUrl(url);
        if ( profile != null ) {
            selectedProfile = profile;
        } else {
            throw new IllegalArgumentException("No profile matching the url of " + url + " was not found.");
        }
    }

    public PasswordMaker getPwm() {
        return pwm;
    }

    public Database getPwmProfiles() {
        return pwmProfiles;
    }

    public Account getSelectedProfile() {
        return selectedProfile;
    }

    @Override
    public void accountAdded(Account parent, Account account) {

    }

    @Override
    public void accountRemoved(Account parent, Account account) {
        // The account for the selected profile was removed, so we should no longer reference it
        if ( account.equals(selectedProfile) ) {
            selectedProfile = null;
        }
    }

    @Override
    public void accountChanged(Account account) {

    }

    @Override
    public void dirtyStatusChanged(boolean b) {
        // may want to save this
    }

    public void addDatabaseListener(DatabaseListener listener) {
        pwmProfiles.addDatabaseListener(listener);
    }

    public void removeDatabaseListener(DatabaseListener listener)
    {
        pwmProfiles.removeDatabaseListener(listener);
    }

    public void addListener(AccountManagerListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }
    public void removeListener(AccountManagerListener listener) {
        listeners.remove(listener);
    }

    public List<String> getFavoriteUrls() {
        return favoriteUrls;
    }

    public void addFavoriteUrl(String url) {
        favoriteUrls.add(url);
    }

    public void removeFavoriteUrl(String url) {
        favoriteUrls.remove(url);
    }

    /**
     * Encodes a list of favorites urls as a series of &lt;url1&gt;,&lt;url2&gt;...
     *
     * @return a string encoded version of the urls for use in the globalSettings
     */
    public String encodeFavoriteUrls() {
        if( !favoriteUrls.isEmpty() ) {
            return "<" + Joiner.on(">,<").join(favoriteUrls) + ">";
        } else {
            return "";
        }
    }

    /**
     * Decodes a list of favorites urls from a series of &lt;url1&gt;,&lt;url2&gt;...
     *
     * Will then put them into the list of favorites, optionally clearing first
     *
     * @param encodedUrlList - String encoded version of the list
     * @param clearFirst - clear the list of favorites first
     */
    public void decodeFavoritesUrls(String encodedUrlList, boolean clearFirst) {
        int start = 0;
        int end = encodedUrlList.length();
        if ( encodedUrlList.startsWith("<") ) start++;
        if ( encodedUrlList.endsWith(">") ) end--;
        encodedUrlList = encodedUrlList.substring(start, end);
        if ( clearFirst ) favoriteUrls.clear();
        for (String url : Splitter.on(">,<").omitEmptyStrings().split(encodedUrlList) ) {
            favoriteUrls.add(url);
        }
    }


    public Account getDefaultAccount() {
        Account account = pwmProfiles.findAccountById(Account.DEFAULT_ACCOUNT_URI);
        if ( account != null ) return account;
        return pwmProfiles.getRootAccount();
    }

    public boolean matchesPasswordHash(SecureCharArray masterPassword) {
        if ( ! hasPasswordHash() ) {
            return true;
        }
        SecureCharArray testPassHash = null;
        try {
            testPassHash = pwm.makePassword(masterPassword, masterPwdHashAccount, passwordSalt);
        } catch (Exception ignored) {
        }
        return testPassHash == null || Arrays.equals(testPassHash.getData(), getCurrentPasswordHash().getData());
    }

    public SecureCharArray getCurrentPasswordHash() {
        return currentPasswordHash;
    }

    /**
     * This will salt and hash the password to store
     * @param newPassword - the new password
     */
    public void setCurrentPasswordHashPassword(String newPassword) {
        this.passwordSalt = UUID.randomUUID().toString();

        try {
            this.currentPasswordHash = pwm.makePassword(new SecureCharArray(newPassword),
                    masterPwdHashAccount, getPasswordSalt());
        } catch (Exception ignored) {}
        setStorePasswordHash(true);
    }

    public void replaceCurrentPasswordHash(SecureCharArray hash, String salt) {
        if ( hash.length() > 0 && salt.length() > 0 ) {
            this.passwordSalt = salt;
            this.currentPasswordHash = hash;
            setStorePasswordHash(true);
        } else {
            disablePasswordHash();
        }

    }

    public String getPasswordSalt() {
        return this.passwordSalt;
    }

    public boolean hasPasswordHash() {
        return shouldStorePasswordHash() && this.passwordSalt != null && this.passwordSalt.length() > 0 && this.currentPasswordHash != null && this.currentPasswordHash.length() > 0;
    }

    public boolean shouldStorePasswordHash() {
        return storePasswordHash;
    }

    public void setStorePasswordHash(boolean storePasswordHash) {
        this.storePasswordHash = storePasswordHash;
        if ( !storePasswordHash ) {
            this.passwordSalt = null;
            if ( this.currentPasswordHash != null ) {
                this.currentPasswordHash.erase(); // clear the memory first
                this.currentPasswordHash = null;
            }
        }
    }

    public void disablePasswordHash() {
        setStorePasswordHash(false);
    }
}
