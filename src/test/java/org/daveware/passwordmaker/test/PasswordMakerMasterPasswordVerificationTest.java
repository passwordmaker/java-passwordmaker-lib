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
import org.junit.Before;
import org.junit.Test;

import static org.daveware.passwordmaker.test.TestUtils.saToString;
import static org.junit.Assert.assertEquals;

public class PasswordMakerMasterPasswordVerificationTest {

    PasswordMaker pwm;

    @Before
    public void before() {
        pwm = new PasswordMaker();
    }

    @Test
    public void testEmptyMasterPassword() throws Exception {
        assertEquals("gNV", saToString(pwm.generateVerificationCode(new SecureCharArray(""))));
    }

    @Test
    public void testVeryShortMasterPassword() throws Exception {
        assertEquals("YJO", saToString(pwm.generateVerificationCode(new SecureCharArray("h"))));
    }

    @Test
    public void testLongMasterPassword() throws Exception {
        assertEquals("RHd", saToString(pwm.generateVerificationCode(new SecureCharArray("happybirthday"))));
    }

    /**
     * This unit test shows that configuring a password maker profile with the same settings that
     * the {@link PasswordMaker#generateVerificationCode} uses will generate the same output.
     * <p/>
     * This is just a sanity check
     */
    @Test
    public void testPwmWithSameProfileSettings() throws Exception {
        Account profile = new Account();
        profile.setCharacterSet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        profile.setAlgorithm(AlgorithmType.SHA256);
        profile.setLength(3);
        profile.setModifier("");
        profile.setPrefix("");
        profile.setSuffix("");
        profile.setLeetType(LeetType.NONE);
        assertEquals("KPA", saToString(pwm.makePassword(new SecureCharArray("happy"), profile, "")));
    }
}
