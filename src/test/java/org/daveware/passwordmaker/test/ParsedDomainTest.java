package org.daveware.passwordmaker.test;

import org.daveware.passwordmaker.ParsedDomain;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParsedDomainTest {
    @Test
    public void testSimple() {
        ParsedDomain domain = new ParsedDomain("google.com");
        assertEquals("com", domain.getTld());
        assertEquals("google.com", domain.getDomain());
        assertEquals("", domain.getSubdomains());
        assertEquals("google.com", domain.fullDomain());
        domain = new ParsedDomain("google.us");
        assertEquals("us", domain.getTld());
        assertEquals("google.us", domain.getDomain());
        assertEquals("", domain.getSubdomains());
        assertEquals("google.us", domain.fullDomain());
        domain = new ParsedDomain("google.doesnotexist");
        assertEquals("doesnotexist", domain.getTld());
        assertEquals("google.doesnotexist", domain.getDomain());
        assertEquals("", domain.getSubdomains());
        assertEquals("google.doesnotexist", domain.fullDomain());
    }
    @Test
    public void testMultiLevelTLD() {
        ParsedDomain domain = new ParsedDomain("google.co.uk");
        assertEquals("co.uk", domain.getTld());
        assertEquals("google.co.uk", domain.getDomain());
        assertEquals("", domain.getSubdomains());
        domain = new ParsedDomain("google.chiyoda.tokyo.jp");
        assertEquals("chiyoda.tokyo.jp", domain.getTld());
        assertEquals("google.chiyoda.tokyo.jp", domain.getDomain());
        assertEquals("", domain.getSubdomains());
        domain = new ParsedDomain("google.sa.edu.au");
        assertEquals("sa.edu.au", domain.getTld());
        assertEquals("google.sa.edu.au", domain.getDomain());
        assertEquals("", domain.getSubdomains());
    }
    @Test
    public void testSimpleWithSubDomains() {
        ParsedDomain domain = new ParsedDomain("www.google.com");
        assertEquals("com", domain.getTld());
        assertEquals("google.com", domain.getDomain());
        assertEquals("www", domain.getSubdomains());
        assertEquals("www.google.com", domain.fullDomain());
        domain = new ParsedDomain("www.sci.google.us");
        assertEquals("us", domain.getTld());
        assertEquals("google.us", domain.getDomain());
        assertEquals("www.sci", domain.getSubdomains());
        assertEquals("www.sci.google.us", domain.fullDomain());
        domain = new ParsedDomain("x1.x2.google.doesnotexist");
        assertEquals("doesnotexist", domain.getTld());
        assertEquals("google.doesnotexist", domain.getDomain());
        assertEquals("x1.x2", domain.getSubdomains());
        assertEquals("x1.x2.google.doesnotexist", domain.fullDomain());
    }
    @Test
    public void testMultiLevelTLDWithSubDomains() {
        ParsedDomain domain = new ParsedDomain("www.google.co.uk");
        assertEquals("co.uk", domain.getTld());
        assertEquals("google.co.uk", domain.getDomain());
        assertEquals("www", domain.getSubdomains());
        domain = new ParsedDomain("www.cs.google.co.uk");
        assertEquals("co.uk", domain.getTld());
        assertEquals("google.co.uk", domain.getDomain());
        assertEquals("www.cs", domain.getSubdomains());
        domain = new ParsedDomain("www.sci.google.chiyoda.tokyo.jp");
        assertEquals("chiyoda.tokyo.jp", domain.getTld());
        assertEquals("google.chiyoda.tokyo.jp", domain.getDomain());
        assertEquals("www.sci", domain.getSubdomains());
        domain = new ParsedDomain("x1.x2.google.sa.edu.au");
        assertEquals("sa.edu.au", domain.getTld());
        assertEquals("google.sa.edu.au", domain.getDomain());
        assertEquals("x1.x2", domain.getSubdomains());
    }
    @Test
    public void testEmptyStringParsing() {
        ParsedDomain domain = new ParsedDomain("");
        assertEquals("", domain.getTld());
        assertEquals("", domain.getDomain());
        assertEquals("", domain.getSubdomains());
    }
}
