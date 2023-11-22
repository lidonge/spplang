package free.servpp.generator;

import free.servpp.generator.models.SppProject;
import org.antlr.v4.runtime.tree.ParseTreeListener;
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

    static interface IAntlrCompiler{
        ParseTreeListener parse(File sppFile, SppProject sppProject) throws IOException;
    }
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        SppProject project = new SppProject();
        compileAntlr(project, ".spp", (sppFile, sppProject) -> {
            return new SppCompiler(sppFile, sppProject).compile();
        });
        compileAntlr(project, ".app", (sppFile, sppProject) -> {
            return new AppCompiler(sppFile, sppProject).compile();
        });
    }

    private void compileAntlr(SppProject sppProject, String suffix,IAntlrCompiler antlrCompiler) {
        File[] files = sppSourceDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(suffix);
            }
        });
        for (File sppFile: files){
            try {
                InputStream inputStream = SppPlugin.class.getResourceAsStream("/spp.mustache");
                ParseTreeListener sppGeneralListener = antlrCompiler.parse(sppFile, sppProject);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
