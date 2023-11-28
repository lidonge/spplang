package free.servpp.openapi3;

import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.RequestBody;

import java.util.Map;

/**
 * @author lidong@date 2023-11-27@version 1.0
 */
public class ResuestBodyHandler extends BaseHandler{
    public ResuestBodyHandler(String domainName, Map<String, SchemaInfo> schemaInfoMap, OpenAPI openAPI) {
        super(domainName, schemaInfoMap, openAPI);
    }

    public void dealOpenApi(){
        Map<String, RequestBody> requestBodyMap = openAPI.getComponents().getRequestBodies();
        for(Map.Entry<String,RequestBody> entry:requestBodyMap.entrySet()){
            String ref = entry.getValue().get$ref();
            ref = ref.substring(ref.lastIndexOf("/")+1);
            if(ref.indexOf("Request") != -1){
                Schema schema = schemaMap.get(ref);
                Map<String,Schema> props = schema.getProperties();
                for(Map.Entry<String,Schema> entry1 : props.entrySet()){

                }
            }
        }
    }
}
