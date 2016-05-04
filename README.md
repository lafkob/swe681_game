# Traverse - SWE 681 Game Project

This is our secure game written for SWE 681 in Spring 2016. Here's some information on the game Traverse: https://boardgamegeek.com/boardgame/3313/traverse.

## Technology
 - Java/Spring
 - REST + a little MVC
 - MySQL

## Tomcat setup
Replace the context.xml in the tomcat config directory with the one provided under /config. You'll also need to put the password for your database in there.

Place the mysql connector jar from /config into the tomcat lib directory.

Create a /certs directory in the tomcat installation and put jks file from /config in there. You'll need to configure a connector in server.xml to utilize SSL with this cert.

## Gradle Stuff
Eclipse integration
- https://marketplace.eclipse.org/content/gradle-ide-pack
