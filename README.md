This is our SWE 681 project. We will be build a game here (TBD). For now, I just have all the files that gradle automatically generated when I ran the 'gradle init --type java-library' command.

The .gitignore file has a few directories in it that gradle will automatically generate that we shouldn't commit:
build/ - the output directory for builds, test reports, and other cool stuff we get for free
.gradle/ - this is a local cache that gradle will put together (I'm guessing for dependencies it downloads)

Once we set up an eclipse project I would also think we want to keep the eclipse specific project file and settings directory out of source control, so we can each control our project settings locally (or even not have to use eclipse).
