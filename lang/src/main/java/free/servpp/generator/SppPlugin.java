package free.servpp.generator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lidong@date 2023-11-13@version 1.0
 */
@Mojo(name = "sppcompile", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class SppPlugin extends AbstractMojo {
    @Parameter(defaultValue = "src/main/spp")
    private File sppSourceDirectory;

    @Parameter(defaultValue = "target/spp/src/main/resources")
    private File yamlOutputDirectory;
    @Parameter(defaultValue = "target/spp/src/main/java")
    private File javaOutputDirectory;

    @Parameter(defaultValue = "free.servpp.openapi")
    private String basePackage;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File[] files = sppSourceDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".spp");
            }
        });
        for (File file: files){
            try {
                InputStream inputStream = SppPlugin.class.getResourceAsStream("/spp.mustache");
                Compiler.compile(inputStream, file, yamlOutputDirectory,javaOutputDirectory, basePackage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
