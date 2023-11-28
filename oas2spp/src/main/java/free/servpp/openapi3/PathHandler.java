package free.servpp.openapi3;

import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.Paths;
import io.swagger.oas.models.media.ObjectSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.parameters.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-26@version 1.0
 */
public class PathHandler extends BaseHandler{
    List<SppPathItem> sppPathItems = new ArrayList<>();
    public PathHandler(String domainName, Map<String, SchemaInfo> allSchemas, OpenAPI openAPI) {
        super(domainName, allSchemas, openAPI);
    }

    public void dealOpenApi(){
        Paths paths = openAPI.getPaths();
        for(Map.Entry<String, PathItem> entry: paths.entrySet()){
            PathItem pathItem = entry.getValue();
            dealOperation(entry.getKey(),pathItem.getGet());
            dealOperation(entry.getKey(),pathItem.getDelete());
            dealOperation(entry.getKey(),pathItem.getHead());
            dealOperation(entry.getKey(),pathItem.getPatch());
            dealOperation(entry.getKey(),pathItem.getPost());
            dealOperation(entry.getKey(),pathItem.getOptions());
            dealOperation(entry.getKey(),pathItem.getPut());
            dealOperation(entry.getKey(),pathItem.getTrace());
            dealOperation(entry.getKey(),pathItem.getPost());
        }
//        paths
    }

    private void dealOperation(String path,Operation operation){
        if(operation != null){
            List<String> tags= operation.getTags();
            for(String tag : tags){
                String[] conts = tag.split("-");
                addInfo(domainName,conts[1].trim(), SchemaInfo.Type.valueOf(conts[0].trim()));
            }
            SppPathItem sppPathItem = new SppPathItem();
            sppPathItem.setOperationId(operation.getOperationId());
            RequestBody requestBody = operation.getRequestBody();
            String ref = null;
            if(requestBody != null){
                ref = requestBody.get$ref();
            }else{
                ref = operation.getResponses().get("200").get$ref();
            }
            ref = ref.substring(ref.lastIndexOf("/") +1);
            if(ref.indexOf("Request") != -1){
                Schema schema = schemaMap.get(ref);
                Map<String,Schema> props = schema.getProperties();
                boolean old = false;
                for(Map.Entry<String,Schema> entry : props.entrySet()){
                    String ref1 = entry.getValue().get$ref();
                    if(ref1 == null ){
                        if(entry.getValue() instanceof ObjectSchema){
                            sppPathItem.getParameters().put("param" + entry.getKey(), entry.getKey());
                            continue;
                        }else{
                            old = true;
                            break;
                        }
                    }

                    ref1.substring(ref1.lastIndexOf("/")+1);
                    sppPathItem.getParameters().put("param"+ref1, entry.getKey());
                }
                if(old)
                    sppPathItem.getParameters().put("param"+ref,ref);
            }else
                sppPathItem.getParameters().put("param"+ref,ref);
            sppPathItem.setModifier(path.substring(path.lastIndexOf("/") +1));
            sppPathItems.add(sppPathItem);
        }
    }
}
