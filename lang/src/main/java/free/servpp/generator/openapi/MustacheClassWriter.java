package free.servpp.generator.openapi;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import free.servpp.generator.general.IConstance;
import free.servpp.generator.java.BaseClassWriter;
import free.servpp.generator.java.ClassWriterConfig;
import free.servpp.generator.models.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong@date 2023-11-15@version 1.0
 */
public class MustacheClassWriter extends ClassWriterConfig {
    public MustacheClassWriter(File domainPath, SppClass sppClass, String basePackage, String javaPackage) {
        super(domainPath, sppClass, basePackage, javaPackage);
    }

    public void generate() {
        try {
            IConstance.CompilationUnitType type = sppClass.getType();
            if (type == IConstance.CompilationUnitType.rolemapper) {
                takeAllMapFields();

                genClassFile("model", "/rolemapper.mustache",sppClass, sppClass.getName());
            } else if (type == IConstance.CompilationUnitType.atomicservice) {
                genClassFile("handler", "/atomicservice.mustache",sppClass, sppClass.getName());
                genClassFile("service", "/service.mustache", sppClass, "Simple" + sppClass.getName());
            } else if (type == IConstance.CompilationUnitType.scenario) {
                genClassFile("handler", "/scenario.mustache",sppClass, sppClass.getName());
                genClassFile("service", "/scenarioservice.mustache",sppClass, "Simple" +sppClass.getName());
            }
            if (type != null) {
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void takeAllMapFields() {
        SppRoleMapper sppRoleMapper = (SppRoleMapper) sppClass;
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

    private void genClassFile(String additionalPackage, String mustacheFile, SppClass cls, String fileName) throws FileNotFoundException {
        cls.setAdditionalPackage(additionalPackage);
        Template template = Mustache.compiler().withLoader(new Mustache.TemplateLoader() {
            @Override
            public Reader getTemplate(String s) throws Exception {
                return new InputStreamReader(MustacheClassWriter.class.getResourceAsStream(File.separator+s+".mustache"));
            }
        }).compile(new InputStreamReader(MustacheClassWriter.class.getResourceAsStream(mustacheFile)));
        File clsPath = new File(getDomainPath(), additionalPackage);
        if(!clsPath.exists()){
            clsPath.mkdirs();
        }
        File objPath = new File(clsPath, fileName + IConstance.SUFFIX);

        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(objPath));
//            out = new PrintWriter(System.out);
            out.println(template.execute(cls));
        }finally {
            out.close();
        }
    }
}
