# inspectorguidget-maven-plugin

The inspectorguidget plugin that launch front-end analyses on your project to extract data (json format)

## Installation

To be able to use this plugin you must add the following plugin repositories in your pom.xml :
```
<pluginRepositories>
    <pluginRepository>
        <id>mavenInriaSnapshot</id>
        <name>http://maven.inria.fr-snapshots</name>
        <url>http://maven.inria.fr/artifactory/malai-public-snapshot</url>
    </pluginRepository>
    <pluginRepository>
        <id>mavenInriaRelease</id>
        <name>http://maven.inria.fr-releases</name>
        <url>http://maven.inria.fr/artifactory/malai-public-release</url>
    </pluginRepository>
</pluginRepositories>
```

Then add the plugin :

```
<plugin>
    <groupId>fr.inria.inspectorguidget</groupId>
    <artifactId>inspectorguidget-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <configuration>
        <analyserType>java</analyserType>
    </configuration>
</plugin>
```

## Parameters

When using the plugin, you're able to precise some parameters. These parameters must appear in the configuration tag whan adding plugin. You can edit the following parameters :
- __mavenProject__ : the maven project you want to analyse (optionnal, by default : the project where you're using the plugin)
- __dataFileName__ : the name of the file containing the extracted data (optionnal, by default : data.json)
- __analyserType__ : the type of analyse you want to run (REQUIRED, choose among : java)

## Running the plugin

To run the analysis, just enter the following command:
```
mvn inspectorguidget:extractdata
```
the data file is created at the root of the project. You can then visualise the extracted data using inspectorguidget-viz.
