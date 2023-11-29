package free.servpp.generator.openapi;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import free.servpp.generator.general.IConstance;
import free.servpp.generator.models.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong@date 2023-11-15@version 1.0
 */
public class MustacheClassWriter extends ClassWriterConfig {
    public MustacheClassWriter(File domainPath, SppClass sppClass, String basePackage, String domainName) {
        super(domainPath, sppClass, basePackage, domainName);
    }

    public void generate() {
        try {
            SppClass sppclass = (SppClass) sppCompilationUnit;
            IConstance.CompilationUnitType type = sppclass.getType();
            if (type != null) {
                if (type == IConstance.CompilationUnitType.rolemapper) {
                    takeAllMapFields();

                    genClassFile("model", "/rolemapper.mustache", sppclass, sppclass.getName());
                } else if (type == IConstance.CompilationUnitType.atomicservice) {
                    SppService sppService = (SppService) sppclass;
                    if(sppService.getScopeItem().isLocal()){
                        genClassFile("service", "/service.mustache", sppclass, "Simple" + sppclass.getName());
                    }else {
                        genClassFile("service", "/proxyservice.mustache", sppclass, "Simple" + sppclass.getName());
                    }
                    genClassFile("handler", "/atomicservice.mustache", sppclass, sppclass.getName());
                } else if (type == IConstance.CompilationUnitType.scenario) {
                    genClassFile("handler", "/scenario.mustache", sppclass, sppclass.getName());
                    genClassFile("service", "/scenarioservice.mustache", sppclass, "Simple" + sppclass.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void takeAllMapFields() {
        SppRoleMapper sppRoleMapper = (SppRoleMapper) sppCompilationUnit;
        if (sppRoleMapper.isTakeAll()) {
            Map<String, SppField> nameToEntitys = new HashMap<>();
            for (SppField var : sppRoleMapper.getSppFieldList()) {
                SppRoleField roleField = (SppRoleField) var;
                nameToEntitys.put(roleField.getEntityName(), var);
            }
            for (SppField var : sppRoleMapper.getEntity().getSppFieldList()) {
                SppField entVar = nameToEntitys.get(var.getName());
                if (entVar == null) {
                    sppRoleMapper.addLocalVar(var);
                }
            }
        }
    }

    private void genClassFile(String additionalPackage, String mustacheFile, SppClass cls, String fileName) throws IOException {
        cls.setAdditionalPackage(additionalPackage);
        Template template = Mustache.compiler().escapeHTML(false).withLoader(new Mustache.TemplateLoader() {
            @Override
            public Reader getTemplate(String s) throws Exception {//for import mustache
                return new InputStreamReader(MustacheClassWriter.class.getResourceAsStream(File.separator + s + ".mustache"));
            }
        }).compile(new InputStreamReader(MustacheClassWriter.class.getResourceAsStream(mustacheFile)));
        File clsPath = new File(getDomainPath(), additionalPackage);
        if (!clsPath.exists()) {
            clsPath.mkdirs();
        }
        File objPath = new File(clsPath, fileName + IConstance.SUFFIX);

        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(objPath));
            String text = template.execute(cls);
//            out = new PrintWriter(System.out);
            out.println(CodeFormator.formatCode(text));
        } finally {
            out.close();
        }
    }

}
