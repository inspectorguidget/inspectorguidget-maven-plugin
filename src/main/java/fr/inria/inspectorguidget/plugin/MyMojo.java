package fr.inria.inspectorguidget.plugin;

import com.beust.klaxon.Klaxon;
import fr.inria.inspectorguidget.api.analyser.UIDataAnalyser;

import fr.inria.inspectorguidget.data.UIData;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

@Mojo( name = "extractdata",requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME )
public class MyMojo extends AbstractMojo {

    //maven project to analyse
    @Parameter( defaultValue = "${project}", required = true, readonly = true)
    private MavenProject mavenProject;

    // file containing data
    @Parameter(defaultValue = "data.json", required =true)
    private String dataFileName;

    // type of the analyser
    @Parameter (required = true)
    private String analyserType;


    public void execute() throws MojoExecutionException {

        UIDataAnalyser analyser = new UIDataAnalyser();
        PrintWriter pw = null;

        System.out.println("adding Input Ressources...");
        analyser.addInputResource(mavenProject.getFile().getAbsolutePath());

        System.out.println("adding dependencies path...");
        List<Dependency> listDependencies = mavenProject.getDependencies();
        String[] dependencies = new String[listDependencies.size()];
        for(int i=0; i<listDependencies.size(); i++){
            dependencies[i] = listDependencies.get(i).getSystemPath();
        }
        analyser.setSourceClasspath(dependencies);

        System.out.println("Extracting data...");
        UIData data = analyser.extractUIData();
        try {
             new PrintWriter(dataFileName);
            pw.print(new Klaxon().toJsonString(data,null));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(pw != null) {
                pw.close();
            }
        }

    }
}
