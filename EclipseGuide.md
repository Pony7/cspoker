# Maven Plugin #
There are too many projects in CSPoker to import them all manually in to your IDE. Most IDE's have a Maven plugin that will make life easier for you. For Eclipse this is [m2eclipse](http://m2eclipse.codehaus.org/). Install it with the Eclipse update manager from the update url:
```
http://m2eclipse.sonatype.org/sites/m2e
```

# Setting Up Projects #
## Importing ##
m2eclipse can automatically import and configure the projects defined in the "pom.xml" files. Go to **_File->Import_** and then choose **_General->Maven Projects_**.
Point the root directory towards the directory where you checked out the code with Subversion.

Alternatively, if you have the [subclipse](http://subclipse.tigris.org/install.html) plugin installed you can choose **_Check out as Maven Project..._** to check out the trunk and import all sub-projects.

Now you should see all sub-projects in the package explorer. Give m2eclipse some time to download all dependencies.

## Fix Missing Source Folder ##
m2eclipse doesn't pick up the extra source folder in the external-plcafe project at the first import. You have to right-click the project, **Maven->Update Project Configuration** to fix this.

## Fixing Dependency Issues ##
You might still see compile errors saying _Access restriction: The type ... is not accessible due to restriction on required library ..._ in the server-xml project. The problem here is that the HTTP server used in that project is part of the Sun JRE6 but not part of standardized Java. Eclipse therefore blocks access to it. This can be fixed by going to the **_Window->Preferences_** window, selecting **_Java->Compiler->Errors/Warnings_**, selecting **_Deprecated and Restricted API_** and then changing the setting for **_Forbidden Reference_** and **_Discouraged Reference_** to Warning in stead of Error.