package free.servpp.test;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import free.servpp.openapi3.*;
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
        String[] files = dir.list();
        Map<String, DomainHandler> domainHandlerHashMap = new HashMap<>();
        for (String f : files) {
            if (f.indexOf(".yaml") != -1) {
                String domainName = f.substring(0, f.indexOf(".yaml"));
                if(!domainName.equals("ConsumerLoan"))
                    continue;
                SwaggerParseResult result = convert(domainName, new File(dir, f).getAbsolutePath(), sppOutPath);
                OpenAPI openAPI = result.getOpenAPI();
                DomainHandler domainHandler = new DomainHandler(domainName,openAPI);
                domainHandlerHashMap.put(domainName, domainHandler);
                domainHandler.deal();
                domainHandler.testReference();
            }
        }
        for(Map.Entry<String, DomainHandler> entry:domainHandlerHashMap.entrySet()){
            String domainName = entry.getKey();
            if(!domainName.equals("ConsumerLoan"))
                continue;
            DomainHandler domainHandler = entry.getValue();
            PrintWriter sppOut = createPrintWriter(sppOutPath,domainName, domainName+".spp" );
            PrintWriter enumsOut = createPrintWriter( sppOutPath,domainName,domainName+".enum");
            PrintWriter appOut = createPrintWriter( sppOutPath,domainName,domainName+".app");
            try {
                writeObject(sppOut, new Object() {
                    String domain = domainName;
                    OpenAPI openApi = entry.getValue().getOpenAPI();
                }, "/oas2spp.mustache");
                writeObject(sppOut,new Object(){
                    PathHandler path = domainHandler.getPathHandler();
                },"/path.mustache");
            } finally {
                sppOut.close();
            }
            try {
                String s = getObjectString(new Object() {
                    String domain = domainName;
                    Map<String, Schema> allEnums = domainHandler.getEnums();
                }, "/enums.mustache");
                s = s.replace(".","").replace("-","_").
                        replace("(","_").replace(")","");

                enumsOut.println(s);

            } finally {
                enumsOut.close();
            }
            try {
                writeObject(appOut, new Object() {
                    String domain = domainName;
                    PathHandler path = domainHandler.getPathHandler();
                }, "/oas2app.mustache");
            } finally {
                appOut.close();
            }
        }
//        for (SchemaInfo schemaInfo:infoMap.values())
//            System.out.println(schemaInfo.toCSV());
    }

    private static SwaggerParseResult convert(String domainName, String inputSpec, File sppOutPath) throws FileNotFoundException {
        ParseOptions options = new ParseOptions();
        options.setResolve(true);
        SwaggerParseResult result = new OpenAPIV3Parser().readLocation(inputSpec, null, options);
        return result;
    }

    private static PrintWriter createPrintWriter(File sppOutPath,String domainName, String fileName) throws FileNotFoundException {
        sppOutPath = new File(sppOutPath,domainName);
        if (!sppOutPath.exists())
            sppOutPath.mkdirs();
        File outFile = new File(sppOutPath, fileName);
        PrintWriter out = null;
        out = new PrintWriter(new FileOutputStream(outFile));
        return out;
    }

    private static void writeObject(PrintWriter out, Object obj, String mastache) {
        out.println(getObjectString(obj,mastache));
    }
    private static String getObjectString( Object obj, String mastache) {
        InputStream mustache = Test.class.getResourceAsStream(mastache);
        Template template = Mustache.compiler().escapeHTML(false).compile(new InputStreamReader(mustache));
        String s = template.execute(obj);
        return s;
    }
}
