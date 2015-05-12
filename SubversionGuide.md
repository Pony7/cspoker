# Introduction #
Google is kind enough to host a Subversion server for us and provide a [browsable version](http://code.google.com/p/cspoker/source/browse/) of the code.

# Checkout #
First you need a [Subversion client](http://subversion.tigris.org/links.html#clients). We recommend using [TortoiseSVN](http://tortoisesvn.tigris.org/) on Windows or the command line tool in Linux. CSPoker consists of many small projects and using an integrated client inside an IDE (like [Subclipse](http://subclipse.tigris.org/)) will be more work for you.

Downloading the source code read-only is done by executing
```
svn checkout http://cspoker.googlecode.com/svn/trunk/ cspoker-read-only
```

# Commit #
If you want to send back changes to the repository you need commit access, which can be granted by the administrators of the project.
If you want to share changes with the community and don't have commit access, you can create a patch
```
svn diff > patch.diff
```
and send it to the project [mailing list](http://groups.google.com/group/cspoker-discuss).

## Configuration ##
Some files that are generated in your local copy of the code do not belong in the repository. This includes ".project" and ".classpath" files and class files.
Avoid mistakes by configuring Subversion, on Linux by editing the file:
```
~/.subversion/config
```
and adding/uncommenting the line:
```
global-ignores = .project .classpath .settings .metadata bin logs .cache target
```
In addition you can set
```
enable-auto-props = yes
*.sh = svn:eol-style=native;svn:executable
*.txt = svn:eol-style=native
*.png = svn:mime-type=image/png
*.jpg = svn:mime-type=image/jpeg
*.sql = svn:eol-style=native
*.xcf = svn:mime-type=image/x-xcf
*.ico = svn:mime-type=image/vnd.microsoft.icon
*.java = svn:eol-style=native
*.xml = svn:eol-style=native
```

# Advanced #
The [Subversion book](http://svnbook.red-bean.com/) goes into more detail on using Subversion.