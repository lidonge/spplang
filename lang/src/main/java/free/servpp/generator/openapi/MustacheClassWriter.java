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

                genClassFile("model", "/rolemapper.mustache");
            } else if (type == IConstance.CompilationUnitType.atomicservice) {
                genClassFile("handler", "/atomicservice.mustache");

            } else if (type == IConstance.CompilationUnitType.scenario) {
                genClassFile("handler", "/scenario.mustache");
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

    private void genClassFile(String addtionalPackage, String mustacheFile) throws FileNotFoundException {
        sppClass.packageName(getBasePackage()+"." + getJavaPackage() +"."+addtionalPackage);
        Template template = Mustache.compiler().withLoader(new Mustache.TemplateLoader() {
            @Override
            public Reader getTemplate(String s) throws Exception {
                return new InputStreamReader(MustacheClassWriter.class.getResourceAsStream(File.separator+s+".mustache"));
            }
        }).compile(new InputStreamReader(MustacheClassWriter.class.getResourceAsStream(mustacheFile)));
        String objName = sppClass.getName();
        File clsPath = new File(getDomainPath(), addtionalPackage);
        if(!clsPath.exists()){
            clsPath.mkdirs();
        }
        File objPath = new File(clsPath, objName + IConstance.SUFFIX);

        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(objPath));
//            out = new PrintWriter(System.out);
            out.println(template.execute(sppClass));
        }finally {
            out.close();
        }
    }

}
