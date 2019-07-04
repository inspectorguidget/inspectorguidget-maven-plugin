package fr.inria.inspectorguidget.plugin;


import fr.inria.inspectorguidget.api.analyser.CommandAnalyser;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo( name = "extractdata" )
public class MyMojo extends AbstractMojo {

    @Parameter( defaultValue = "${project}", required = true, readonly = true)
    private File projectDirectory;

    public void execute() throws MojoExecutionException {
        CommandAnalyser analyser = new CommandAnalyser();
        analyser.addInputResource(projectDirectory.getAbsolutePath());
        analyser.run();
    }
}
