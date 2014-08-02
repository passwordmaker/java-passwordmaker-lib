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

import org.daveware.passwordmaker.util.Joiner;
import org.daveware.passwordmaker.util.Splitter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class JoinerSplitterTest {

    @Test
    public void splitterShouldSplitEmptyString() {
        List<String> result = iteratableToList(Splitter.on(",").split(""));
        assertEquals(new ArrayList<String>(), result);

        result = iteratableToList(Splitter.on(",").omitEmptyStrings().split(""));
        assertEquals(new ArrayList<String>(), result);
    }

    @Test
    public void splitterShouldSplitSepOnlyString() {
        List<String> result = iteratableToList(Splitter.on(",").split(","));
        assertEquals(Arrays.asList("", ""), result);

        result = iteratableToList(Splitter.on(",").omitEmptyStrings().split(","));
        assertEquals(new ArrayList<String>(), result);

        result = iteratableToList(Splitter.on(",").omitEmptyStrings().split(",,"));
        assertEquals(new ArrayList<String>(), result);
    }

    @Test
    public void splitterShouldSplit1Value() {
        List<String> result = iteratableToList(Splitter.on(",").split("apple"));
        assertEquals(Arrays.asList("apple"), result);

        result = iteratableToList(Splitter.on(",").omitEmptyStrings().split("apple"));
        assertEquals(Arrays.asList("apple"), result);

        result = iteratableToList(Splitter.on(",").omitEmptyStrings().split("apple,"));
        assertEquals(Arrays.asList("apple"), result);

        result = iteratableToList(Splitter.on(",").omitEmptyStrings().split(",apple,,"));
        assertEquals(Arrays.asList("apple"), result);
    }

    @Test
    public void splitterShouldSplit2Values() {
        List<String> result = iteratableToList(Splitter.on(",").split("a,b"));
        assertEquals(Arrays.asList("a", "b"), result);

        result = iteratableToList(Splitter.on(",").omitEmptyStrings().split("a,,b"));
        assertEquals(Arrays.asList("a", "b"), result);

        result = iteratableToList(Splitter.on(",").omitEmptyStrings().split("a,,b,"));
        assertEquals(Arrays.asList("a", "b"), result);

        result = iteratableToList(Splitter.on(",").omitEmptyStrings().split(",a,,b"));
        assertEquals(Arrays.asList("a", "b"), result);
    }

    @Test
    public void splitterShouldSplitMoreValues() {
        List<String> result = iteratableToList(Splitter.on(",").split("ar,b,ds,d,e,f"));
        assertEquals(Arrays.asList("ar", "b", "ds", "d", "e", "f"), result);

        result = iteratableToList(Splitter.on(",").omitEmptyStrings().split("ar,,b,c,ds,,e,f"));
        assertEquals(Arrays.asList("ar", "b", "c", "ds", "e", "f"), result);

        result = iteratableToList(Splitter.on(",").omitEmptyStrings().split(",ar,,b,c,ds,,e,fp,"));
        assertEquals(Arrays.asList("ar", "b", "c", "ds", "e", "fp"), result);
    }

    @Test
    public void splitterShouldSplitMultiCharSep() {
        List<String> result = iteratableToList(Splitter.on(">,<").split("ar>,<b>,<ds>,<d>,<e>,<f"));
        assertEquals(Arrays.asList("ar", "b", "ds", "d", "e", "f"), result);

        result = iteratableToList(Splitter.on(">,<").omitEmptyStrings().split("ar>,<>,<b>,<c>,<ds>,<>,<e>,<f"));
        assertEquals(Arrays.asList("ar", "b", "c", "ds", "e", "f"), result);

        result = iteratableToList(Splitter.on(">,<").omitEmptyStrings().split(">,<ar>,<>,<b>,<c>,<ds>,<>,<e>,<fp>,<"));
        assertEquals(Arrays.asList("ar", "b", "c", "ds", "e", "fp"), result);
    }


    @Test
    public void joinerShouldJoinEmptyList() {
        assertEquals("", Joiner.on(",").join(new ArrayList<String>()));
        assertEquals("", Joiner.on(">,<").join(new ArrayList<String>()));
    }


    @Test
    public void joinerShouldJoinSingletonList() {
        assertEquals("aaaahhh", Joiner.on(",").join(Collections.singletonList("aaaahhh")));
        assertEquals("aaaahhh", Joiner.on(">,<").join(Collections.singletonList("aaaahhh")));
        assertEquals("a", Joiner.on(">,<").join(Collections.singletonList("a")));
    }

    @Test
    public void joinerShouldJoin2ItemList() {
        assertEquals("aaaahhh,foo", Joiner.on(",").join(Arrays.asList("aaaahhh", "foo")));
        assertEquals("aaaahhh>,<foo", Joiner.on(">,<").join(Arrays.asList("aaaahhh", "foo")));
        assertEquals("a>,<f", Joiner.on(">,<").join(Arrays.asList("a", "f")));
    }

    @Test
    public void joinerShouldJoinMultiItemList() {
        assertEquals("aaaahhh,foo,x,yep", Joiner.on(",").join(Arrays.asList("aaaahhh", "foo", "x", "yep")));
        assertEquals("aaaahhh>,<foo>,<x>,<yep", Joiner.on(">,<").join(Arrays.asList("aaaahhh", "foo", "x", "yep")));
        assertEquals("a>,<f>,<a>,<d", Joiner.on(">,<").join(Arrays.asList("a", "f", "a", "d")));
    }

    private List<String> iteratableToList(Iterable<String> i) {
        List<String> result = new ArrayList<String>();
        for (String s : i ) result.add(s);
        return result;
    }
}
