The build system requires Maven 2 to be installed.

Build:
======
Execute:
	mvn compile

The class files are compiled in "target/classes/".

Test:
=====
Execute:
	mvn test

The class files are compiled in "target/test-classes".
Errors are reported in "target/surefire-reports".

Test with coverage:
===================
Execute:
	mvn cobertura:cobertura

The class files are compiled in "target/test-classes".
Errors are reported in "target/surefire-reports".
Coverage information is reported in "target/site/cobertura/index.html"

Clean generated content:
========================
Execute:
	mvn clean
