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
import org.daveware.passwordmaker.*;
import org.daveware.passwordmaker.Account.UrlComponents;
import org.junit.*;

import java.security.Security;
import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.Assert.fail;

/**
 * Runs tests against the PasswordMaker class.
 *
 * @author Dave Marotti
 */
public class PasswordMakerTest {

    private static PWTest[] tests = {

            // Default account, MD5 length=8
            new PWTest(new Account("Yummy Humans", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com"), "123abc!@#/\\'\"", "B}ZR0.@e"),

            // Very long password tests (issue 24)
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, false, true, 50, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "B}ZR0.@exd0Z>3Du@`.&KW}Z(@~tDhJ*S$y_MnN]D.P*9O`\\l_"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, true, true, 50, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "IYKnwMlY@m~j&A8_r$0YG&A~HGiZ1s;^;f``?7+xDrhnJL8h_&"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, false, true, 50, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "F}3+bzw=$P-kFMtp<p\\DkQHvE[rg!Lb@;RZ2/L1\"R3JF)7_`?C"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, true, true, 50, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "Fz7cVJW9,r`S3bm8|k$SM6xe2GP_aoMgcsK}p9{t<QgjK9Q@>j"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, false, true, 50, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "9!,-H}!R`n\";LV#GiK!BnHqn&D<Y$`uI]X(3vA|R;jo(<Xc!EU"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, true, true, 50, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "ZQ7v(aB`T-$eN2Nk`yI;LnQOuK,W-J|Uf<K]J$,u@b=7]_&l9H"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, false, true, 50, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "FV``ev3wbT}3#dBkjhmbBW*IvERuK_H=d&+t4L$:E[Rdd6E-l~"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, true, true, 50, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "Gg1P#zuzhFu#w>h&~C4/~\"yb{BQvjqF>:gPvjQ+<!4><K|o|E%"),

            // MD5, HMACMD5, SHA1, HMACSH1 - no leet
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, false, true, 12, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "B}ZR0.@exd0Z"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, true, true, 12, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "IYKnwMlY@m~j"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, false, true, 12, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "F}3+bzw=$P-k"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, true, true, 12, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "Fz7cVJW9,r`S"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, false, true, 12, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "9!,-H}!R`n\";"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, true, true, 12, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "ZQ7v(aB`T-$e"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, false, true, 12, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "FV``ev3wbT}3"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, true, true, 12, CharacterSets.BASE_93_SET, LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "Gg1P#zuzhFu#"),

            // MD5 BOTH_LEET[1-9]
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "bi77cc1ikc0v"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL2, "", "", "", false, ""), "123abc!@#/\\'\"", "gy?$3y5&gp'-"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL3, "", "", "", false, ""), "123abc!@#/\\'\"", "3?81kw5,4[mf"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL4, "", "", "", false, ""), "123abc!@#/\\'\"", "j6:n29r@1*0~"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL5, "", "", "", false, ""), "123abc!@#/\\'\"", "3!\\/7|2m&|2u"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL6, "", "", "", false, ""), "123abc!@#/\\'\"", "|)|>1}u|>'97"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL7, "", "", "", false, ""), "123abc!@#/\\'\"", ",|22<'/[-1\\/"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL8, "", "", "", false, ""), "123abc!@#/\\'\"", "|)8&|(~0(|=$"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL9, "", "", "", false, ""), "123abc!@#/\\'\"", "_||>(?&|>4&;"),

            // HMACMD5 BOTH_LEET[1-9]
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "j*uc`179[,h@"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL2, "", "", "", false, ""), "123abc!@#/\\'\"", "k;n~3mjpnp|4"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL3, "", "", "", false, ""), "123abc!@#/\\'\"", "'0fmn9k'/j.="),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL4, "", "", "", false, ""), "123abc!@#/\\'\"", "jd'c3}$]r6-v"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL5, "", "", "", false, ""), "123abc!@#/\\'\"", "|3'/@9%''/m0"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL6, "", "", "", false, ""), "123abc!@#/\\'\"", "|3|24?``'/5'"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL7, "", "", "", false, ""), "123abc!@#/\\'\"", "&}\"|2|'/|3\\/"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL8, "", "", "", false, ""), "123abc!@#/\\'\"", "86\\/9^<8|\\|("),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.MD5, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL9, "", "", "", false, ""), "123abc!@#/\\'\"", "_|~8|_|)(|={"),

            // SHA1 BOTH_LEET[1-9]
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "s-!=mk9>ic7v"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL2, "", "", "", false, ""), "123abc!@#/\\'\"", "bkc4'ng22+bp"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL3, "", "", "", false, ""), "123abc!@#/\\'\"", "8\"v23:n0;1'/"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL4, "", "", "", false, ""), "123abc!@#/\\'\"", "cd02'/vx0v:6"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL5, "", "", "", false, ""), "123abc!@#/\\'\"", "c7|2|45;@\\/("),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL6, "", "", "", false, ""), "123abc!@#/\\'\"", "&&|2|3w,|#u9"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL7, "", "", "", false, ""), "123abc!@#/\\'\"", ">|22^^]^^]+["),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL8, "", "", "", false, ""), "123abc!@#/\\'\"", "(,)(,)1\"_|\\/"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL9, "", "", "", false, ""), "123abc!@#/\\'\"", "(6@%/\\/\\\\6|\\"),

            // HMACSHA1 BOTH_LEET[1-9]
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "rdg_nug~\"1%r"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL2, "", "", "", false, ""), "123abc!@#/\\'\"", "fhfmjkmy9;[h"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL3, "", "", "", false, ""), "123abc!@#/\\'\"", "635x=?4r'784"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL4, "", "", "", false, ""), "123abc!@#/\\'\"", "f`h2#@7d{1f;"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL5, "", "", "", false, ""), "123abc!@#/\\'\"", "c6#9dx\"/|<|d"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL6, "", "", "", false, ""), "123abc!@#/\\'\"", "(|>$1|<2w|<9"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL7, "", "", "", false, ""), "123abc!@#/\\'\"", "^^66<|=><\"27"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL8, "", "", "", false, ""), "123abc!@#/\\'\"", "&<;(,)(|)&86"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA1, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL9, "", "", "", false, ""), "123abc!@#/\\'\"", "&86)(\"/_2'6\""),

            // SHA256 BOTH_LEET[1-9]
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "b3}vugy%}\\3."),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL2, "", "", "", false, ""), "123abc!@#/\\'\"", "7p{]rp(7knjx"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL3, "", "", "", false, ""), "123abc!@#/\\'\"", "jx|92m0&;c/0"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL4, "", "", "", false, ""), "123abc!@#/\\'\"", ",~1:1(;u(hxc"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL5, "", "", "", false, ""), "123abc!@#/\\'\"", "1|2|<!187dfx"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL6, "", "", "", false, ""), "123abc!@#/\\'\"", ",|&/|<#=#m[0"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL7, "", "", "", false, ""), "123abc!@#/\\'\"", "|3@&'/&|)/><"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL8, "", "", "", false, ""), "123abc!@#/\\'\"", "@1|\\/|\\/|_|2"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL9, "", "", "", false, ""), "123abc!@#/\\'\"", ">~(,)#!(|{/\\"),

            // HMACSHA256 BOTH_LEET[1-9]
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "k%hx#4p9h]n="),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL2, "", "", "", false, ""), "123abc!@#/\\'\"", "[5>`vp57c2)0"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL3, "", "", "", false, ""), "123abc!@#/\\'\"", "@35_{x.-3-<4"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL4, "", "", "", false, ""), "123abc!@#/\\'\"", "8u[/[{d8@,5@"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL5, "", "", "", false, ""), "123abc!@#/\\'\"", "|37~x7'/$_@~"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL6, "", "", "", false, ""), "123abc!@#/\\'\"", "7!2|>{72+^n~"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL7, "", "", "", false, ""), "123abc!@#/\\'\"", "5(_)}32'/5@|"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL8, "", "", "", false, ""), "123abc!@#/\\'\"", ";\"/_;\\,)|!|)"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.SHA256, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL9, "", "", "", false, ""), "123abc!@#/\\'\"", "/\\/\\,38.(|=|"),

            // RIPEMD160 BOTH_LEET[1-9]
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "c<^9b_5{~7ds"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL2, "", "", "", false, ""), "123abc!@#/\\'\"", "cbppw1>2xu%7"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL3, "", "", "", false, ""), "123abc!@#/\\'\"", "f&=_78)7jn(4"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL4, "", "", "", false, ""), "123abc!@#/\\'\"", "8;8n1h62'/_%"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL5, "", "", "", false, ""), "123abc!@#/\\'\"", "x_w?x9n|>|>1"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL6, "", "", "", false, ""), "123abc!@#/\\'\"", "3]4%31w/@|=?"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL7, "", "", "", false, ""), "123abc!@#/\\'\"", "[,|+5|<><\\/_"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL8, "", "", "", false, ""), "123abc!@#/\\'\"", "'/|-|1\\8)8%*"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, false, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL9, "", "", "", false, ""), "123abc!@#/\\'\"", "6|'/()|\\|=|>"),

            // HMACRIPEMD160 BOTH_LEET[1-9]
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL1, "", "", "", false, ""), "123abc!@#/\\'\"", "gd$g$;x&7\\9u"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL2, "", "", "", false, ""), "123abc!@#/\\'\"", "f:1-[,71f\"f$"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL3, "", "", "", false, ""), "123abc!@#/\\'\"", "85dn>h4?0u9+"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL4, "", "", "", false, ""), "123abc!@#/\\'\"", "3v~892x)@/9&"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL5, "", "", "", false, ""), "123abc!@#/\\'\"", "|3$!n#4@718,"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL6, "", "", "", false, ""), "123abc!@#/\\'\"", "|)!@1|=$_'u2"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL7, "", "", "", false, ""), "123abc!@#/\\'\"", "[|<|<7#(_)5\\"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL8, "", "", "", false, ""), "123abc!@#/\\'\"", "{}:(&*\\|>\"/_"),
            new PWTest(new Account("Yummy Humans", "", "yummyhumans.com", "tyrannosaurus@iwishiwasnotextinct.com", AlgorithmType.RIPEMD160, true, true, 12, CharacterSets.BASE_93_SET, LeetType.BOTH, LeetLevel.LEVEL9, "", "", "", false, ""), "123abc!@#/\\'\"", "&|_|8|>|__||"),

            // Test Url components
            urlComponentTest("C4jcyJU3OITs", "http://subdomain.yummyhumans.com/path/to?everything=true", UrlComponents.Domain),
            urlComponentTest("EkxyAhqWRdNs", "http://subdomain.yummyhumans.com/path/to?everything=true", UrlComponents.Domain, UrlComponents.Subdomain),
            urlComponentTest("FlEkcwvpDedl", "http://subdomain.yummyhumans.com/path/to?everything=true", UrlComponents.Domain, UrlComponents.Subdomain, UrlComponents.PortPathAnchorQuery),
            urlComponentTest("D9nvoh1yCroN", "http://subdomain.yummyhumans.com/path/to?everything=true", UrlComponents.Protocol, UrlComponents.Domain, UrlComponents.Subdomain, UrlComponents.PortPathAnchorQuery),
            urlComponentTest("CFMeyEkYXHIo", "http://subdomain.yummyhumans.com/path/to?everything=true", UrlComponents.Domain, UrlComponents.PortPathAnchorQuery),
            urlComponentTest("C4jcyJU3OITs", "http://subdomain.yummyhumans.com", UrlComponents.Domain),
            urlComponentTest("C4jcyJU3OITs", "http://subdomain.yummyhumans.com", UrlComponents.Domain, UrlComponents.PortPathAnchorQuery),
            urlComponentTest("C4jcyJU3OITs", "http://yummyhumans.com", UrlComponents.Domain, UrlComponents.PortPathAnchorQuery),
            urlComponentTest("C4jcyJU3OITs", "http://yummyhumans.com", UrlComponents.Domain),
            urlComponentTest("C4jcyJU3OITs", "http://yummyhumans.com/path/to?everything=true", UrlComponents.Domain),

            // A default account with no UrlComponents selected will use "" as the url for input
            urlComponentTest("BS0JNF8qbJVN", "http://www.google.com/ig", true),

            // A non-default account with no UrlComponents selected will use the full url as input
            urlComponentTest("DTxSlWBBBIeF", "http://www.google.com/ig", false),
    };

    // public Account(String name, String desc, String url, String username, AlgorithmType algorithm, boolean hmac,
    //                boolean trim, int length, String characterSet, LeetType leetType,
    //                LeetLevel leetLevel, String modifier, String prefix, String suffix,
    //                boolean sha256Bug)

    public PasswordMakerTest() {
    }

    // same as above but with an empty UrlComponent set and as a default account.

    private static PWTest urlComponentTest(String expected, String url, UrlComponents... components) {
        Account acc = new Account("Yummy Humans", "", url, "tyrannosaurus@iwishiwasnotextinct.com",
                AlgorithmType.MD5, false, true, 12, CharacterSets.ALPHANUMERIC,
                LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, "");
        for (UrlComponents urlCom : components) {
            acc.addUrlComponent(urlCom);
        }
        return new PWTest(acc, "password", expected);
    }

    /**
     * Creates a test object with no Url components.
     *
     * @param expected         The expected generated password.
     * @param url              The parsing to use when UrlComponents come into play.
     * @param isDefaultAccount Whether or not to make this a default account. Default
     *                         accounts behave differently when there are no UrlComponent selections.
     * @return The object suitable for test.
     */
    private static PWTest urlComponentTest(String expected, String url, boolean isDefaultAccount) {
        Account acc = new Account("Yummy Humans", "", url, "tyrannosaurus@iwishiwasnotextinct.com",
                AlgorithmType.MD5, false, true, 12, CharacterSets.ALPHANUMERIC,
                LeetType.NONE, LeetLevel.LEVEL1, "", "", "", false, "");
        acc.clearUrlComponents();
        if (isDefaultAccount)
            acc.setId(Account.DEFAULT_ACCOUNT_URI);
        return new PWTest(acc, "password", expected);
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

    @Test
    public void testCalcStrength() {
        double[] expected = {43.0, 77.0, 77.60000000000001, 86.8, 70.0, 20.0, 32.0, 0.0, 95.0};
        String[] input = {"IGp&fwNs", "Ru32j#82@@!m", "JHxZn$&`Xn8JDy-Q(@VeG6k(b", "Lfen/ntAlTcA21O'1{sI$b7_1", "!@#$%^&*()[]asdfghjk345678", "ki{_-", "AAABa", "AAA", "aA1!"};

        for (int i = 0; i < input.length; i++) {
            double strength = PasswordMaker.calcPasswordStrength(new SecureCharArray(input[i].getBytes()));
            if (strength != expected[i]) {
                fail(input[i] + " failed, expected " + expected[i] + " but got " + strength);
            }
        }
    }

    @Test
    public void testGetModifiedInputText() {
        PasswordMaker pwm = new PasswordMaker();

        Account a = new Account();
        a.clearUrlComponents();
        Assert.assertEquals("", pwm.getModifiedInputText("http://user:pass@a.domain.is.here.com:8080/somePage.html?param=value", a));
        a.setUrlComponents(EnumSet.of(UrlComponents.Protocol, UrlComponents.Subdomain, UrlComponents.Domain, UrlComponents.PortPathAnchorQuery));
        Assert.assertEquals("http://a.domain.is.here.com:8080/somePage.html?param=value", pwm.getModifiedInputText("http://a.domain.is.here.com:8080/somePage.html?param=value", a));
        a.setUrlComponents(EnumSet.of(UrlComponents.Domain));
        Assert.assertEquals("here.com", pwm.getModifiedInputText("http://a.domain.is.here.com:8080/somePage.html?param=value", a));
        a.setUrlComponents(EnumSet.of(UrlComponents.Subdomain, UrlComponents.Domain));
        Assert.assertEquals("a.domain.is.here.com", pwm.getModifiedInputText("http://a.domain.is.here.com:8080/somePage.html?param=value", a));
        a.setUrlComponents(EnumSet.of(UrlComponents.Subdomain, UrlComponents.Domain, UrlComponents.PortPathAnchorQuery));
        Assert.assertEquals("a.domain.is.here.com:8080/somePage.html?param=value", pwm.getModifiedInputText("http://a.domain.is.here.com:8080/somePage.html?param=value", a));

        // Tests for Issue 29, '#' character causes errors in calculation of url modified input text
        a.setUrlComponents(EnumSet.of(UrlComponents.Domain));
        Assert.assertEquals("passwordmaker.org", pwm.getModifiedInputText("http://forums.passwordmaker.org/index.php?topic=1750.new;topicseen#new", a));
        Assert.assertEquals("passwordmaker.org", pwm.getModifiedInputText("http://forums.passwordmaker.org/index.php?topic=1750.new;topicseen", a));
        Assert.assertEquals("passwordmaker.org", pwm.getModifiedInputText("forums.passwordmaker.org/index.php?topic=1750.new;topicseen#new", a));
        Assert.assertEquals("passwordmaker.org", pwm.getModifiedInputText("forums.passwordmaker.org/index.php?topic=1750.new;topicseen", a));
        Assert.assertEquals("passwordmaker.org", pwm.getModifiedInputText("http://passwordmaker.org/index.php?topic=1750.new;topicseen#new", a));
        Assert.assertEquals("passwordmaker.org", pwm.getModifiedInputText("http://passwordmaker.org/index.php?topic=1750.new;topicseen", a));
        Assert.assertEquals("passwordmaker.org", pwm.getModifiedInputText("passwordmaker.org/index.php?topic=1750.new;topicseen#new", a));
        Assert.assertEquals("passwordmaker.org", pwm.getModifiedInputText("passwordmaker.org/index.php?topic=1750.new;topicseen", a));

        a.setUrlComponents(EnumSet.of(UrlComponents.Domain, UrlComponents.Protocol));
        Assert.assertEquals("http://passwordmaker.org", pwm.getModifiedInputText("http://forums.passwordmaker.org/index.php?topic=1750.new;topicseen#new", a));
        Assert.assertEquals("http://passwordmaker.org", pwm.getModifiedInputText("http://forums.passwordmaker.org/index.php?topic=1750.new;topicseen", a));
        Assert.assertEquals("passwordmaker.org", pwm.getModifiedInputText("forums.passwordmaker.org/index.php?topic=1750.new;topicseen#new", a));
        Assert.assertEquals("passwordmaker.org", pwm.getModifiedInputText("forums.passwordmaker.org/index.php?topic=1750.new;topicseen", a));
        Assert.assertEquals("http://passwordmaker.org", pwm.getModifiedInputText("http://passwordmaker.org/index.php?topic=1750.new;topicseen#new", a));
        Assert.assertEquals("http://passwordmaker.org", pwm.getModifiedInputText("http://passwordmaker.org/index.php?topic=1750.new;topicseen", a));
        Assert.assertEquals("passwordmaker.org", pwm.getModifiedInputText("passwordmaker.org/index.php?topic=1750.new;topicseen#new", a));
        Assert.assertEquals("passwordmaker.org", pwm.getModifiedInputText("passwordmaker.org/index.php?topic=1750.new;topicseen", a));

        // TODO: Firefox-Plugin and Web versions disagree about how to handle the port when no port is present.
        // This test is different with the Firefox plugin.  The firefox plugin would add a ":" after the
        // domain when "portPathQuery" is selected but no port is actually present. Eric says this is a
        // bug and that the correct behavior is that of the web version.  The test below abides by the
        // web version.  This is an open bug and the behavior is different between implementations.
        a.setUrlComponents(EnumSet.of(UrlComponents.Subdomain, UrlComponents.Domain, UrlComponents.PortPathAnchorQuery));
        Assert.assertEquals("a.domain.is.here.com/somePage.html?param=value", pwm.getModifiedInputText("http://a.domain.is.here.com/somePage.html?param=value", a));
    }

    /**
     * Test of rstr2any method, of class PasswordMaker.
     */
    @Test
    public void testRstr2any() throws Exception {
        // These 2 tests only exist because 1/2 way through porting of the actual
        // pwm hashing, I started getting bad passwords with some of the SHA-1
        // tests.  All my MD5 tests and even HMACMD5 passed just fine.  It turns
        // out that the username/url's that I chose coupled with SHA1 and the
        // password occasionally turned out character-array which would require
        // trim=true on the rstr2any function.  I spent a good 1-2 hours tracking
        // that down before I realized all my PWTest values had trim set to
        // false.
        //
        // It was the char-array below that converts differently based on that
        // little parameter.
        //
        // Damn.

        char[] chars = {0x02, 0x77, 0xBC, 0x40, 0x19, 0xFE, 0x02, 0x20, 0xAD,
                0xC3, 0xE1, 0x62, 0x2B, 0x7D, 0x71, 0xA5, 0x86, 0xE7, 0x04, 0xF1};
        char[] expectedTrim = {0x46, 0x7D, 0x33, 0x2B, 0x62, 0x7A, 0x77, 0x3D, 0x24,
                0x50, 0x2D, 0x6B, 0x46, 0x4D, 0x74, 0x70, 0x3C, 0x70, 0x5C, 0x44, 0x6B,
                0x51, 0x48, 0x76};
        char[] expectedNoTrim = {'A', 0x46, 0x7D, 0x33, 0x2B, 0x62, 0x7A, 0x77, 0x3D, 0x24,
                0x50, 0x2D, 0x6B, 0x46, 0x4D, 0x74, 0x70, 0x3C, 0x70, 0x5C, 0x44, 0x6B,
                0x51, 0x48, 0x76};
        PasswordMaker pwm = new PasswordMaker();
        SecureCharArray sec = pwm.rstr2any(chars, CharacterSets.BASE_93_SET, true);
        if (!Arrays.equals(sec.getData(), expectedTrim)) {
            fail("1 - rstr2any() failed with trim=true");
        }

        // This array happens to convert differently with trim=false
        sec = pwm.rstr2any(chars, CharacterSets.BASE_93_SET, false);
        if (!Arrays.equals(sec.getData(), expectedNoTrim)) {
            fail("2 - rstr2any() failed with trim=false");
        }
    }

    /**
     * Test of generate method, of class PasswordMaker.
     */
    @Test
    public void testGenerate() throws Exception {
        int testNum = 0;
        int failCount = 0;
        for (PWTest test : tests) {
            Account account = test.account;
            PasswordMaker pm = new PasswordMaker();
            SecureCharArray output = pm.makePassword(new SecureCharArray(test.mpw.toCharArray()), account);
            if (!Arrays.equals(output.getData(), test.expectedOutput.toCharArray())) {
                System.out.println("Test" + testNum + test + " failed");
                System.out.println("Expected: " + test.expectedOutput);
                System.out.println("Received: " + new String(output.getData()));
                failCount++;
            }
            testNum++;
        }

        if (failCount != 0)
            fail(Integer.toString(failCount) + " failures");
    }

    public static class PWTest {
        public Account account;
        public String mpw;
        public String expectedOutput;

        public PWTest(Account a, String pw, String expected) {
            account = a;
            mpw = pw;
            expectedOutput = expected;
        }

        @Override
        public String toString() {
            StringBuilder retVal = new StringBuilder();
            retVal.append('(');
            retVal.append(account.getAlgorithm().toString());
            retVal.append(", HMAC=").append(account.isHmac());
            retVal.append(", user=").append(account.getUsername());
            retVal.append(", url=").append(account.getUrl());
            retVal.append(" leetlevel=").append(account.getLeetLevel());
            if (!account.getUrlComponents().isEmpty())
                retVal.append(" UrlComponents=").append(account.getUrlComponents());
            retVal.append(')');
            return retVal.toString();
        }
    }
}
