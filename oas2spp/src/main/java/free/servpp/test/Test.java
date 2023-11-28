package free.servpp.test;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import free.servpp.openapi3.PathHandler;
import free.servpp.openapi3.SchemaHandler;
import free.servpp.openapi3.SchemaInfo;
import free.servpp.openapi3.SppSchema;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.media.Schema;
import io.swagger.parser.models.ParseOptions;
import io.swagger.parser.models.SwaggerParseResult;
import io.swagger.parser.v3.OpenAPIV3Parser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-24@version 1.0
 */
public class Test {

    public static final String FACILITY = "Facility";
    public static final String REFERENCE = "Reference";
    public static final String FUNCTION = "Function";

    public static void main(String[] args) throws FileNotFoundException {
//        final List<AuthorizationValue> authorizationValues = AuthParser.parse(this.auth);
//        convert("ConsumerLoan");
//        File dir = new File("/Users/lidong/gitspace/spplang/oas2spp/src/main/resources");
        File dir = new File("/Users/lidong/IdeaProjects/bianswagger/public/release12.0.0/semantic-apis/oas3/yamls");
        File sppOutPath = new File("/Users/lidong/gitspace/spplang/oas2spp/target/gen/spp");
        Map<String, SchemaInfo> infoMap = new HashMap<>();
        Map<String, Schema> allSchemas = new HashMap<>();
        Map<String, Schema> enums = new HashMap<>();
        String[] files = dir.list();
        PrintWriter allOut = createPrintWriter("all.spp", sppOutPath);
        PrintWriter enumsOut = createPrintWriter("enums.spp", sppOutPath);
        Map<String, OpenAPI> openAPIMap = new HashMap<>();
        Map<String, PathHandler> pathHandlerMap = new HashMap<>();
        for (String f : files) {
            if (f.indexOf(".yaml") != -1) {
                String domainName = f.substring(0, f.indexOf(".yaml"));
                SwaggerParseResult result = convert(domainName, new File(dir, f).getAbsolutePath(), sppOutPath);
                OpenAPI openAPI = result.getOpenAPI();
                openAPIMap.put(domainName, openAPI);
                selectAllSchemas(openAPI,allSchemas);
            }
        }
        for(Map.Entry<String, OpenAPI> entry:openAPIMap.entrySet()){
            String domainName = entry.getKey();
            OpenAPI openAPI = entry.getValue();
            dealSchema(domainName, infoMap, openAPI, allSchemas);

            dealPath(domainName, infoMap, openAPI, allSchemas, pathHandlerMap);
        }
        selectEnums(infoMap, allSchemas, enums);
        testReference(infoMap);
        for (Map.Entry<String, OpenAPI> entry : openAPIMap.entrySet()) {
//            if(!"ConsumerLoan".equals(entry.getKey()))
//                continue;
            setSppType(entry, infoMap);
            String domainName = entry.getKey();
            PrintWriter sppOut = createPrintWriter(domainName + ".spp", sppOutPath);
            try {
                writeObject(sppOut, new Object() {
                    String domain = domainName;
                    OpenAPI openApi = entry.getValue();
                }, "/oas2spp.mustache");
                writeObject(sppOut,new Object(){
                    PathHandler path = pathHandlerMap.get(entry.getKey());
                },"/path.mustache");
            } finally {
                sppOut.close();
            }
        }
        try {
            writeObject(enumsOut, new Object() {
                Map<String, Schema> allEnums = enums;
            }, "/enums.mustache");
        } finally {
            enumsOut.close();
        }
//        for (SchemaInfo schemaInfo:infoMap.values())
//            System.out.println(schemaInfo.toCSV());
    }

    private static void dealPath(String domainName, Map<String, SchemaInfo> infoMap, OpenAPI openAPI, Map<String, Schema> allSchemas, Map<String, PathHandler> pathHandlerMap) {
        PathHandler pathHandler = new PathHandler(domainName, infoMap, openAPI);
        pathHandler.setSchemaMap(allSchemas);
        pathHandler.setSchemaInfoMap(infoMap);
        pathHandler.dealOpenApi();
        pathHandlerMap.put(domainName,pathHandler);
    }

    private static void dealSchema(String domainName, Map<String, SchemaInfo> infoMap, OpenAPI openAPI, Map<String, Schema> allSchemas) {
        SchemaHandler schemaHandler = new SchemaHandler(domainName, infoMap, openAPI);
        schemaHandler.setSchemaInfoMap(infoMap);
        schemaHandler.dealOpenApi();
        selectAllSchemas(openAPI, allSchemas);
    }

    private static void selectAllSchemas(OpenAPI openAPI, Map<String, Schema> allSchemas) {
        for (Map.Entry<String, Schema> entry : openAPI.getComponents().getSchemas().entrySet()) {
            allSchemas.put(entry.getKey(), entry.getValue());
        }
    }

    private static void setSppType(Map.Entry<String, OpenAPI> entry, Map<String, SchemaInfo> infoMap) {
        for (Map.Entry<String, Schema> entry1 : entry.getValue().getComponents().getSchemas().entrySet()) {
            SchemaInfo schemaInfo = infoMap.get(entry1.getKey());
            if (entry1.getValue() instanceof SppSchema)
                ((SppSchema) entry1.getValue()).setSppType(schemaInfo.getSppTypeString());
        }
    }

    private static void testReference(Map<String, SchemaInfo> infoMap) {
        for (SchemaInfo schemaInfo : infoMap.values()) {
            if (schemaInfo.getType() == SchemaInfo.Type.Reference) {
//                System.out.println("========" +schemaInfo.getName()+"," +schemaInfo.getDomains());
                String refToName = schemaInfo.getName();
//                System.out.println(schemaInfo.getName() + "====" +refTo);
                SchemaInfo refToInfo = matchRef(new TwoPartName(refToName, null), infoMap);
                if (refToInfo == null) {
                    System.out.println("not found ref:" + schemaInfo.getName());
                }
            }
        }
    }

    private static void selectEnums(Map<String, SchemaInfo> infoMap, Map<String, Schema> allSchemas, Map<String, Schema> enums) {
        for (SchemaInfo schemaInfo : infoMap.values()) {
            Schema schema = allSchemas.get(schemaInfo.getName());
            schemaInfo.setSchema(schema);
            if (schema != null && schema.getEnum() != null) {
                schemaInfo.setType(SchemaInfo.Type.Enum);
                enums.put(schemaInfo.getName(), schemaInfo.getSchema());
            }
        }
    }

    private static SchemaInfo matchRef(TwoPartName qname, Map<String, SchemaInfo> allSchemas) {
        String refToName = qname.first;
        SchemaInfo refToInfo = allSchemas.get(refToName);
        if (refToInfo == null) {
            TwoPartName twoPartName = nextLargestName(refToName);
            if (twoPartName == null)
                return null;
            return matchRef(twoPartName, allSchemas);
        } else {
            if (qname.last != null)
                System.out.println(qname.first() + "=====ref prp " + qname.last + " is :" + refToInfo.getSchema().getProperties().get(qname.last));
        }
        return refToInfo;
    }

    private static TwoPartName nextLargestName(String name) {
        for (int i = name.length() - 1; i > 0; i--) {
            if (Character.isUpperCase(name.charAt(i))) {
                return new TwoPartName(name.substring(0, i), name.substring(i));
            }
        }
        return null;
    }

    public static record TwoPartName(String first, String last) {
    }

    private static SwaggerParseResult convert(String domainName, String inputSpec, File sppOutPath) throws FileNotFoundException {
        ParseOptions options = new ParseOptions();
        options.setResolve(true);
        SwaggerParseResult result = new OpenAPIV3Parser().readLocation(inputSpec, null, options);
        return result;
    }

    private static PrintWriter createPrintWriter(String fileName, File sppOutPath) throws FileNotFoundException {
        if (!sppOutPath.exists())
            sppOutPath.mkdirs();
        File outFile = new File(sppOutPath, fileName);
        PrintWriter out = null;
        out = new PrintWriter(new FileOutputStream(outFile));
        return out;
    }

    private static void writeObject(PrintWriter out, Object obj, String mastache) {
        InputStream mustache = Test.class.getResourceAsStream(mastache);
        Template template = Mustache.compiler().escapeHTML(false).compile(new InputStreamReader(mustache));
        String s = template.execute(obj);
        out.println(s);
    }
}
