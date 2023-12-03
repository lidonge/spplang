package free.servpp.openapi3;

import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.media.Schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-29@version 1.0
 */
public class DomainHandler {
    String domainName;
    OpenAPI openAPI;
    Map<String, SchemaInfo> infoMap = new HashMap<>();
    Map<String, Schema> allSchemas = new HashMap<>();
    PathHandler pathHandler;

    Map<String, Schema> enums = new HashMap<>();

    public DomainHandler(String domainName, OpenAPI openAPI) {
        this.domainName = domainName;
        this.openAPI = openAPI;
        selectAllSchemas();
    }

    public PathHandler getPathHandler() {
        return pathHandler;
    }

    public Map<String, Schema> getEnums() {
        return enums;
    }

    public String getDomainName() {
        return domainName;
    }

    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    public void deal(){
        dealSchema();
        dealPath();
        selectEnums();
        setSppType();
    }

    public void dealSchema() {
        SchemaHandler schemaHandler = new SchemaHandler(domainName, infoMap, openAPI);
        schemaHandler.setSchemaInfoMap(infoMap);
        schemaHandler.dealOpenApi();
        selectAllSchemas();
    }
    public void dealPath() {
        pathHandler = new PathHandler(domainName, infoMap, openAPI);
        pathHandler.setSchemaMap(allSchemas);
        pathHandler.setSchemaInfoMap(infoMap);
        pathHandler.dealOpenApi();
    }

    public void selectEnums( ) {
        for (SchemaInfo schemaInfo : infoMap.values()) {
            Schema schema = allSchemas.get(schemaInfo.getName());
            schemaInfo.setSchema(schema);
            List<String> enmu = schema.getEnum();
            if (schema != null && schema.getEnum() != null) {
                schemaInfo.setType(SchemaInfo.Type.Enum);
                enums.put(schemaInfo.getName(), schemaInfo.getSchema());
                for(int i =0;i<enmu.size();i++){
                    String orig = enmu.get(i);
                    String s = orig;
                    s = replaceChar(s, ".");
                    s = replaceChar(s, "-");
                    s = replaceChar(s, "(");
                    s = replaceChar(s, ")");
                    if(s != orig){
                        enmu.set(i, s + "(\"" + orig+"\")");
                    }
                }
            }
        }
    }

    private String replaceChar(String s, String str) {
        if (s.indexOf(str) != -1) {
            s = s.replace(str, "_");
        }
        return s;
    }

    private void setSppType( ) {
        for (Map.Entry<String, Schema> entry : openAPI.getComponents().getSchemas().entrySet()) {
            SchemaInfo schemaInfo = infoMap.get(entry.getKey());
            if (entry.getValue() instanceof SppSchema)
                ((SppSchema) entry.getValue()).setSppType(schemaInfo.getSppTypeString());
        }
    }

    public void testReference() {
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
    private SchemaInfo matchRef(TwoPartName qname, Map<String, SchemaInfo> allSchemas) {
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
    private TwoPartName nextLargestName(String name) {
        for (int i = name.length() - 1; i > 0; i--) {
            if (Character.isUpperCase(name.charAt(i))) {
                return new TwoPartName(name.substring(0, i), name.substring(i));
            }
        }
        return null;
    }

    public record TwoPartName(String first, String last) {
    }

    private void selectAllSchemas() {
        for (Map.Entry<String, Schema> entry : openAPI.getComponents().getSchemas().entrySet()) {
            allSchemas.put(entry.getKey(), entry.getValue());
        }
    }

}
