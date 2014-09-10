package org.daveware.passwordmaker.test;

import org.daveware.passwordmaker.*;

import java.util.Arrays;
import java.util.List;

public class AccountManagerSamples {

    public static void addSamples(AccountManager accountManager) {
        Database pwmProfiles = accountManager.getPwmProfiles();
        try {
            Account folder = new Account("Personal", true);
            folder.getChildren().add(new AccountBuilder().setAlgorithm(AlgorithmType.SHA256)
                    .setName("Reddit")
                    .setDesc("Reddit page")
                    .setUrl("http://reddit.com")
                    .setUsername("testUser1")
                    .setCharacterSet(CharacterSets.BASE_93_SET)
                    .setLength(20)
                    .setPrefix("$r3d")
                    .setModifier("1")
                    .addWildcardPattern("reddit", "*://*.reddit.com/*")
                    .addWildcardPattern("redditdomain", "*://reddit.com/*").build());
            folder.getChildren().add(new AccountBuilder().setAlgorithm(AlgorithmType.SHA256)
                    .setName("facebook")
                    .setDesc("facebook page")
                    .setUrl("http://facebook.com")
                    .setUsername("testUser2")
                    .setCharacterSet(CharacterSets.ALPHANUMERIC)
                    .setLength(23)
                    .setSuffix("$f@d")
                    .setModifier("2")
                    .addWildcardPattern("facebook", "*://*.facebook.com/*")
                    .addWildcardPattern("facebookdomain", "*://facebook.com/*").build());
            pwmProfiles.addAccount(pwmProfiles.getRootAccount(), folder);
            folder = new Account("Work", true);
            folder.getChildren().add(new AccountBuilder().setAlgorithm(AlgorithmType.RIPEMD160)
                    .setName("stackoverflow")
                    .setDesc("dev qa")
                    .setUrl("http://stackoverflow.com")
                    .setUsername("devuser1")
                    .setCharacterSet(CharacterSets.ALPHANUMERIC)
                    .setLength(23)
                    .setPrefix("%D3v")
                    .setModifier("3")
                    .addWildcardPattern("stackoverflow", "*://*.stackoverflow.com/*")
                    .addWildcardPattern("stackoverflowdomain", "*://stackoverflow.com/*").build());
            folder.getChildren().add(new AccountBuilder().setAlgorithm(AlgorithmType.SHA1)
                    .setName("github.com")
                    .setDesc("code repo")
                    .setUrl("http://github.com")
                    .setUsername("devuser2")
                    .setCharacterSet(CharacterSets.HEX)
                    .setLength(23)
                    .setPrefix("%c0D3")
                    .setModifier("4")
                    .addWildcardPattern("github", "*://*.github.com/*")
                    .addWildcardPattern("githubdomain", "*://github.com/*").build());
            pwmProfiles.addAccount(pwmProfiles.getRootAccount(), folder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> SAMPLE_URLS = Arrays.asList("http://reddit.com/",
            "http://realnews.com/",
            "http://google.com/",
            "http://goo.gl/",
            "http://markerisred.com/",
            "http://github.com/",
            "http://www.stackoverflow.com/",
            "https://www.facebook.com/",
            "https://gmail.com/");

    public static void addFavorites(AccountManager accountManager) {
        accountManager.getFavoriteUrls().addAll(SAMPLE_URLS);
    }
}
