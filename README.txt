The build system requires Maven 2 to be installed.

Build:
======
Execute:
	mvn compile

The class files are compiled in "target/classes/".

Package to executable jar:
==========================
Currently there are 5 executable modules:
  * server-rmi
  * server-xml
  * server-allcommunication
  * client-gui-swt
  * ai-experiments
 

To make 1 executable jar with all dependencies, do 
	mvn install
, go to the respective directory and execute:
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
