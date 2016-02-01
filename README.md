# SWE 681 Game Project

We will be build a game here (TBD). For now, I just have all the files that gradle automatically generated when I ran the 'gradle init --type java-library' command.

The .gitignore file has a few directories in it that gradle will automatically generate that we shouldn't commit:
- build/ - the output directory for builds, test reports, and other cool stuff we get for free
- .gradle/ - this is a local cache that gradle will put together (I'm guessing for dependencies it downloads)

Once we set up an eclipse project I would also think we want to keep the eclipse specific project file and settings directory out of source control, so we can each control our project settings locally (or even not have to use eclipse).

## Gradle Stuff
installation - just download the binary-only version from their site, unzip it, set the unzipped location \bin in your path environment variable (so your command line picks it up)

gradle init - this is the command to bootstrap a project structure and build.gradle file
- https://docs.gradle.org/current/userguide/build_init_plugin.html

some gradle basics - ran some of these without using their project, it just has some basic instructions
- https://spring.io/guides/gs/gradle/

gradle java resources - haven't looked through this too much, gradle docs on using it with java
- http://gradle.org/getting-started-gradle-java/

gradle eclipse integration - haven't run through this yet
- https://docs.gradle.org/current/userguide/eclipse_plugin.html

This is a test.
