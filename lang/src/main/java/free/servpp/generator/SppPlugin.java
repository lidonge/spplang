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

/**
 * @author lidong@date 2023-11-13@version 1.0
 */
@Mojo(name = "sppcompile", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class SppPlugin extends AbstractMojo {
    @Parameter(defaultValue = "src/main/resources")
    private File sourceDirectory;

    @Parameter(defaultValue = "target/classes")
    private File outputDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File[] files = sourceDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".spp");
            }
        });
        for (File file: files){
            try {
                Compiler.compile(file,outputDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
