passwordmaker-je-lib
====================

The library portion of [passwordmaker-je](https://code.google.com/p/passwordmaker-je/)

This library does all of the supporting calls to implement [Passwordmaker.org](http://passwordmaker.org) algorithm.  
This hopefully will allow both the Android edition and the java desktop edition to share a very common code base.

The original passwordmaker-je was written by Dave Marotti.  James Stapleton has modified it so that it fits into a library, 
and then made it a maven project.

How to build
===================
0. git clone https://github.com/passwordmaker/java-passwordmaker-lib.git
1. Download a [java 6+ jdk](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. Download [maven](http://maven.apache.org/)
3. Run `$ mvn install`

Any pull requests are welcomed.

This software is GPL licensed. See LICENSE for more information.
