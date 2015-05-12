# Introduction #
[Maven](http://maven.apache.org/) is a build automation system that allows us to easily compile the source code, run tests and package jars. Maven is also responsible for downloading all dependencies from their repositories.
CSPoker consists of several Maven projects depending on each other. Every project as a "pom.xml" file with the configuration.

# Commands #
## Compiling ##
Execute:
```
mvn compile
```
The class files are compiled in "target/classes/".

If the build process fails, this most likely means than the cspoker maven server has crashed. Please notify the mailing list if you experience this problem.

## Package to executable Jar ##
To make 1 executable jar with all dependencies, go to a directory of an executable project and execute:
```
mvn assembly:assembly
```
The executable jar can be found in "target/ `*` -jar-with-dependencies.jar".

## Test ##
Execute:
```
mvn test
```
The class files are compiled in "target/test-classes/".
Errors are reported in "target/surefire-reports/".

## Test with coverage ##
Execute:
```
mvn cobertura:cobertura
```

The class files are compiled in "target/test-classes/".
Errors are reported in "target/surefire-reports/".
Coverage information is reported in "target/site/cobertura/index.html".

## Clean generated content ##
Execute:
```
mvn clean
```
All target directories and generated content will be removed.