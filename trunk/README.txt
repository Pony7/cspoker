The build system requires Maven 2 to be installed.

For the JavaFX client 2 third-party artifacts have to be added to the local Maven repository. 
Go to the directory with the artifacts (client/gui/javafx/lib) and execute:
  mvn install:install-file -DgroupId=net.java.javafx -DartifactId=javafxrt -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true -Dfile=javafxrt-1.0.jar 
  mvn install:install-file -DgroupId=javafx.netbeans.fxuserlib -DartifactId=javafx-netbeans-fxuserlib -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true -Dfile=javafx-netbeans-fxuserlib-1.0.jar

Build:
======
Execute:
	mvn compile

The class files are compiled in "target/classes/".

Package to executable jar:
==========================
Currently there are 4 executable modules:
  * server-rmi (server/rmi)
  * server-allcommunication (server/allcommunication)
  * client-gui-text (client/gui/text)
  * client-gui-javafx (client/gui/javafx)
 

To make 1 executable jar with all dependencies, go to the respective directory and execute:
	mvn assembly:assembly
The executable jar can be found in "target/*-jar-with-dependencies.jar".

Test:
=====
Execute:
	mvn test

The class files are compiled in "target/test-classes/".
Errors are reported in "target/surefire-reports/".

Test with coverage:
===================
Execute:
	mvn cobertura:cobertura

The class files are compiled in "target/test-classes/".
Errors are reported in "target/surefire-reports/".
Coverage information is reported in "target/site/cobertura/index.html".

Clean generated content:
========================
Execute:
	mvn clean
