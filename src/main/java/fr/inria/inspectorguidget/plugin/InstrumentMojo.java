package fr.inria.inspectorguidget.plugin;

import android.os.Binder;
import fr.inria.inspectorguidget.processor.BinderClassProcessor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import spoon.Launcher;
import spoon.SpoonAPI;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Mojo(name = "instrument", defaultPhase = LifecyclePhase.PROCESS_CLASSES, threadSafe = true)
public class InstrumentMojo extends AbstractMojo {

    /**
     * A list of class files to include in instrumentation. May use wildcard
     * characters (* and ?). When not specified everything will be included.
     */
    @Parameter
    private List<String> includes;

    /**
     * A list of class files to exclude from instrumentation. May use wildcard
     * characters (* and ?). When not specified nothing will be excluded.
     */
    @Parameter
    private List<String> excludes;

    /**
     * Maven project.
     */
    @Parameter(defaultValue = "${project}", property = "project", readonly = true)
    private MavenProject project;

    @Parameter(property = "instrumentationFileName", defaultValue = "instrumentation.log")
    private String instrumentationFileName;

    private Logger LOGGER = Logger.getLogger(InstrumentMojo.class.getName());

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        try {
            FileHandler fh = new FileHandler(instrumentationFileName);
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.INFO);
            LOGGER.addHandler(fh);
        } catch (Exception e){
            LOGGER.severe("can't log in file");
        }

        LOGGER.info("[Plugin] Searching for classes with binders");

        final File originalClassesDir = new File(project.getBuild().getDirectory(), "generated-classes/Instrumentation");
        originalClassesDir.mkdirs();
        final File classesDir = new File(project.getBuild().getOutputDirectory());

        if (!classesDir.exists()) {
            LOGGER.info("Skipping execution due to missing classes directory");
            return;
        }

        //TODO : get all the filenames in the directory thanks to spoon processor
        SpoonAPI spoon = new Launcher();
        spoon.addInputResource(project.getBuild().getDirectory());

        BinderClassProcessor binderClassProcessor = new BinderClassProcessor();

        spoon.addProcessor(binderClassProcessor);
        spoon.run();

        final List<String> fileNames = binderClassProcessor.getClassNames();

        if(fileNames.size()==0)
            LOGGER.info("no class to process");

        for (String fileName : fileNames) {
            LOGGER.info("[Plugin] Processing" + fileName);
        }

    }
}
