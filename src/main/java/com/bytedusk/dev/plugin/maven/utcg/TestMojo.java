package com.bytedusk.dev.plugin.maven.utcg;//import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
//import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.InstantiationStrategy;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;

@Mojo(name = "goal1")
public class TestMojo extends AbstractMojo {

    @Parameter(name = "name", defaultValue = "test")
    private String name;

    @Parameter(property = "utcg.generator.outputDir",
            defaultValue = "${project.build.testSourceDirectory}", required = true)
    private String utcgOutputDir;

    @Parameter(property = "utcg.generator.inputDir",
            defaultValue = "${project.build.testSourceDirectory}", required = true)
    private String utcgInputDir;

    public void execute() {
        getLog().info("This is goal1.");
        name.getClass();
        getLog().info("utcgOutputDir:"+utcgOutputDir);

    }
}