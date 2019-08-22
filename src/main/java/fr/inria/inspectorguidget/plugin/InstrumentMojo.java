package fr.inria.inspectorguidget.plugin;

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
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.visitor.filter.AbstractFilter;

import java.io.*;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Mojo(name = "instrument", defaultPhase = LifecyclePhase.PROCESS_CLASSES, threadSafe = true)
public class InstrumentMojo extends AbstractMojo {

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

        SpoonAPI spoon = new Launcher();
        spoon.addInputResource(project.getBasedir().getAbsolutePath() + "/src/main/java/net/sf/latexdraw");

        BinderClassProcessor binderClassProcessor = new BinderClassProcessor();

        spoon.addProcessor(binderClassProcessor);

        spoon.getEnvironment().setComplianceLevel(9); // to avoid spoonException
        spoon.run();

        final List<CtClass> classList = binderClassProcessor.getClassNames();

        if(classList.size()==0)
            LOGGER.info("[Plugin] no class to process");

        for (CtClass clazz : classList) {
            LOGGER.info("[Plugin] Processing : " + clazz.getSimpleName());
            // here copy classes to transform
            //transformClass(clazz);
        }

    }

    public void transformClass(CtClass clazz){

        List<CtInvocation> binders = clazz.getElements(new AbstractFilter<CtInvocation>() {
            @Override
            public boolean matches(CtInvocation invoc) {
                if(invoc.toString().endsWith("bind()"))
                    return true;
                else
                    return false;
            }
        });

        for(CtInvocation binder : binders){
            transformInvoc(binder);
        }
    }

    public void transformInvoc(CtInvocation invoc){
        //Here add : .log(LogLevel.BINDING) before .bind()
    }
}
