package free.servpp.openapi3;

import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-26@version 1.0
 */
public class BaseHandler {
    protected String domainName;
    protected Map<String, SchemaInfo> schemaInfoMap;
    protected Map<String, Schema> schemaMap;
    protected OpenAPI openAPI;

    public BaseHandler(String domainName, Map<String, SchemaInfo> schemaInfoMap, OpenAPI openAPI) {
        this.domainName = domainName;
        this.schemaInfoMap = schemaInfoMap;
        this.openAPI = openAPI;
    }

    public Map<String, Schema> getSchemaMap() {
        return schemaMap;
    }

    public void setSchemaMap(Map<String, Schema> schemaMap) {
        this.schemaMap = schemaMap;
    }

    public Map<String, SchemaInfo> getSchemaInfoMap() {
        return schemaInfoMap;
    }

    public void setSchemaInfoMap(Map<String, SchemaInfo> schemaInfoMap) {
        this.schemaInfoMap = schemaInfoMap;
    }

    protected void addInfo(String domainName, String name, SchemaInfo.Type type) {
        SchemaInfo info = schemaInfoMap.get(name);
        if (info == null) {
            info = new SchemaInfo();
            info.setName(name);
            schemaInfoMap.put(name, info);
        }
        SchemaInfo.Type oldType = info.getType();
        if (oldType != type) {
            switch (type){
                case BQ:
                case CR: {
                    info.setType(type);
                    break;
                }
                case schema:{
                    if (oldType == null)
                    {
                        info.setType(type);
                    }
                    break;
                }
                default:
                    info.setType(type);
                    break;
            }
        }
        if (!info.getDomains().contains(domainName)) {
            info.getDomains().add(domainName);
        }
    }


    protected List<Map.Entry<String,?>> mapToList(Map<String, ?> schemas) {
        List<Map.Entry<String,?>> entries = new ArrayList<>();
        for (Map.Entry<String, ?> entry : schemas.entrySet()) {
            entries.add(entry);
        }
        return entries;
    }

}
