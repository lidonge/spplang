package free.servpp.generator;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import free.servpp.generator.db.DDLGenerator;
import free.servpp.generator.general.SppGeneralHandler;
import free.servpp.generator.models.SppDomain;
import free.servpp.generator.models.SppProject;
import free.servpp.generator.openapi.CodeFormator;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
        List<ParseTreeListener> sppTreeListeners = compileAntlr(project, ".enum", (sppFile, sppProject) -> {
            SppGeneralHandler sppGeneralHandler = (SppGeneralHandler) new SppCompiler(sppFile, sppProject).compile();
            return sppGeneralHandler;
        });

        sppTreeListeners = compileAntlr(project, ".spp", (sppFile, sppProject) -> {
            SppGeneralHandler sppGeneralHandler = (SppGeneralHandler) new SppCompiler(sppFile, sppProject).compile();
            sppGeneralHandler.getSppDomain().dealEntityToRoleMaps();
            sppGeneralHandler.getSppDomain().generateDefaultServices(sppGeneralHandler);
            return sppGeneralHandler;
        });
        List<ParseTreeListener> appTreeListeners = compileAntlr(project, ".app", (sppFile, sppProject) -> {
            return new AppCompiler(sppFile, sppProject).compile();
        });

        appTreeListeners = compileAntlr(project, ".rdb", (sppFile, sppProject) -> {
            return new AppCompiler(sppFile, sppProject).compile();
        });

        InputStream ddlMustache = SppPlugin.class.getResourceAsStream("/ddl.mustache");
        for(ParseTreeListener parseTreeListener: sppTreeListeners){
            SppGeneralHandler sppGeneralHandler = (SppGeneralHandler) parseTreeListener;
            SppDomain sppDomain = sppGeneralHandler.getSppDomain();
            sppDomain.dealAnnotations(sppDomain.getRuleBlock().getAppAnnotationList());
            sppDomain.checkSemanticFinally(sppGeneralHandler);
            DDLGenerator ddlGenerator = new DDLGenerator(sppDomain);
            Template template = Mustache.compiler().compile(new InputStreamReader(ddlMustache));
            String string  = template.execute(ddlGenerator);
            PrintWriter out = null;
            try {
                File ddlFile = new File(yamlOutputDirectory,"sql/ddl.sql");
                ddlFile.getParentFile().mkdirs();
                out = new PrintWriter(new FileOutputStream(ddlFile));
                string = CodeFormator.formatCode(string);
                out.println(string);
            }catch (IOException e) {
                e.printStackTrace();
            }finally {
                out.close();
            }

            try {
                GeneratorUtil.openApi(sppDomain,sppGeneralHandler.getAntlrFile(),yamlOutputDirectory,javaOutputDirectory,basePackage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<ParseTreeListener> compileAntlr(SppProject sppProject, String suffix, IAntlrCompiler antlrCompiler) {
        File[] files = sppSourceDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(suffix);
            }
        });
        List<ParseTreeListener> list = new ArrayList<>();
        for (File sppFile: files){
            try {
                ParseTreeListener parseTreeListener = antlrCompiler.parse(sppFile, sppProject);
                list.add(parseTreeListener);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
