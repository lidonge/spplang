package free.servpp.generator;

import free.servpp.generator.db.DDLGenerator;
import free.servpp.generator.db.DbTable;
import free.servpp.generator.db.MybatisClass;
import free.servpp.generator.general.NameUtil;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppDefaultService;
import free.servpp.generator.models.SppDomain;
import free.servpp.generator.models.SppService;
import free.servpp.generator.openapi.MustacheClassWriter;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-12-14@version 1.0
 */
public class MybatisGenerator {
    private SppDomain sppDomain;
    private DDLGenerator ddlGenerator;

    private Map<String, SppDefaultService> defaultServiceMap = new HashMap<>();

    public MybatisGenerator(SppDomain sppDomain, DDLGenerator ddlGenerator) {
        this.sppDomain = sppDomain;
        this.ddlGenerator = ddlGenerator;
    }

    private void generate() {

    }

    public void generateManager(InputStream in, File genRoot, String basePackage) {
        ByteArrayInputStream mustache = getByteArrayInputStream(in);
        String domainPackage = basePackage +"." + sppDomain.getName();
        final String packName = domainPackage +".mapper";
        for(SppDefaultService sppDefaultService :sppDomain.getSppDefaultServices()) {
            SppClass sppClass = (SppClass) sppDefaultService.getRealm();
            defaultServiceMap.put(sppClass.getName(),sppDefaultService);
            String managerClassName = sppClass.getName()+"Mapper";
            String fileName = (packName+"."+managerClassName).replace('.',File.separatorChar)+".java";
            Object obj = new Object(){
                String packageName = packName;
                String modelPackage = domainPackage + ".model";
                SppClass cls = sppClass;
                String lowerClsName = NameUtil.firstToLowerCase(sppClass.getName(),true);
                List<SppService> methods = sppDefaultService.getServiceList();
            };
            mustache.reset();
            MustacheClassWriter.generateFile(mustache, obj, genRoot, fileName);
        }
    }

    public void generateMapper( InputStream in, File genRoot, String basePackage){
        ByteArrayInputStream mustache = getByteArrayInputStream(in);

        String domainPackage = basePackage +"." + sppDomain.getName();
        final String packName = domainPackage +".mapper";
        genRoot = new File(genRoot,"resources");
        for(DbTable dbTable:ddlGenerator.getTableList().getArrayList()){
            SppClass sppClass = dbTable.getAppTable().getEntity();
            if(sppClass != null){
//                if(!sppClass.getName().equals("MyOrder"))
//                    continue;
                SppDefaultService sppDefaultService = defaultServiceMap.get(sppClass.getName());
                String managerClassName = sppClass.getName()+"Mapper";
                String fileName = (packName+"."+managerClassName).replace('.',File.separatorChar)+".xml";

                Object obj = new Object(){
                    String packageName = packName;
                    String modelPackage = domainPackage + ".model";
                    String lowerClsName = NameUtil.firstToLowerCase(sppClass.getName(),true);
                    DbTable table = dbTable;
                    MybatisClass mybatisClass = dbTable.getMybatisClass();
                };
                mustache.reset();
                MustacheClassWriter.generateFile(mustache, obj, genRoot, fileName,"/mustache/mybatis");
            }
        }
    }

    private ByteArrayInputStream getByteArrayInputStream(InputStream in) {
        ByteArrayInputStream mustache = null;
        try {
            byte[] bytes = in.readAllBytes();
            mustache = new ByteArrayInputStream(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mustache;
    }
}
