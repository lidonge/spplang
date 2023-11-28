package free.servpp.openapi3;

import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.media.ObjectSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-26@version 1.0
 */
public class SchemaHandler extends BaseHandler {

    public SchemaHandler(String domainName, Map<String, SchemaInfo> allSchemas, OpenAPI openAPI) {
        super(domainName, allSchemas, openAPI);
    }

    public void dealOpenApi(){
        Map<String, Schema> schemas = openAPI.getComponents().getSchemas();
        removeRequestRespons(schemas);
        replaceObjectSchema(schemas);

        for(Schema schema:schemas.values()) {
            Map<String, Schema> properties = schema.getProperties();
            if (properties != null) {
                for (Map.Entry<String, Schema> entry : properties.entrySet()) {
                    Schema value = entry.getValue();
                    String key = entry.getKey();
                    String type = value.getType();
                    if ("string".equals(type)) {
                        value.setType("String");
//                        if (key.endsWith("Reference")) {
//                            addInfo(domainName,key, SchemaInfo.Type.Reference);
//                        }
                    } else if ("number".equals(type)) {
                        value.setType("double");
                    } else {
                        String $ref = value.get$ref();
                        if (type == null && $ref != null) {
                            String ref = $ref.substring($ref.lastIndexOf("/") + 1);
                            value.setType(ref);
                            addInfo(domainName,ref, SchemaInfo.Type.Reference);
                        }
                    }
                }
            }
        }
    }

    private void replaceObjectSchema( Map<String, Schema> schemas) {
        List<Map.Entry<String, ?>> entries = mapToList(schemas);
        for(Map.Entry<String, ?> entry:entries){
            Schema schema = (Schema) entry.getValue();
            SppSchema sppSchema = new SppSchema();
            BeanUtils.copyProperties(schema,sppSchema);
            if(schema instanceof ObjectSchema){
                sppSchema.setSppType("Entity");
                addInfo(domainName,entry.getKey(), SchemaInfo.Type.schema);
            }else if(schema instanceof StringSchema){
                sppSchema.setSppType("enum");
                addInfo(domainName,entry.getKey(), SchemaInfo.Type.Enum);
            }
            schemas.put(entry.getKey(), sppSchema);
        }
    }

    private void removeRequestRespons(Map<String, Schema> schemas) {
        List<String> keys = new ArrayList<>();
        for(String key: schemas.keySet()){
            if(key.indexOf("Response") != -1 || key.indexOf("Request") != -1){
                keys.add(key);
            }
        }

        for(String key:keys){
            schemas.remove(key);
        }
    }
}
