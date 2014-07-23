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
=======

How to sign the jar for deployment
====================

Just use the `build-for-central` profile.  It will build the source, javadocs, and sign the jars.

    mvn clean install -Pbuild-for-central

To verify the signatures

    find target -name '*.asc' -print -exec gpg2 --verify {} \;
    
To actually deploy change the `install` maven command to `deploy`.
This requires a `sonatype.org` jira account.  And most likely permissions from James Stapleton (@tasermonkey).
