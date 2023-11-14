package free.servpp.generator.openapi;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.IFileGenerator;
import free.servpp.generator.java.BaseClassGenerator;
import free.servpp.generator.models.*;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-13@version 1.0
 */
public class OpenApiGenerator extends BaseClassGenerator {
    public OpenAPI generate(ClassChecker classChecker, File domainPath, String javaPackege) {
        dealMaps(classChecker);
        OpenAPI openAPI = new OpenAPI();
        Map<String, SppClass> sppClassMap = classChecker.getMapsOfClass();
        openAPI.addTagsItem(new Tag().name(javaPackege));
        for (SppClass sppClass : sppClassMap.values()) {
//            if(sppClass.getType() == IConstance.CompilationUnitType.rolemapper)
//                System.out.println(sppClass);
            IConstance.CompilationUnitType type = sppClass.getType();
            if (type == IConstance.CompilationUnitType.entity
                    || type == IConstance.CompilationUnitType.role
                    || type == IConstance.CompilationUnitType.reference) {
                createASchema(sppClass, openAPI);
            } else if (type == IConstance.CompilationUnitType.atomicservice
                    || type == IConstance.CompilationUnitType.scenario) {
                createAPath(javaPackege, sppClass, openAPI);
            }
        }
        return openAPI;
    }

    private static void createAPath(String javaPackege, SppClass sppClass, OpenAPI openAPI) {
        SppService sppService = (SppService) sppClass;
        IConstance.ServiceType serviceType = sppService.getServiceType();
        PathItem pathItem = new PathItem() {
            public String getPath() {
                return javaPackege +"/"+sppService.getFuncName();
            }

//            public String getMethod() {
//                return method;
//            }
//
//            public String getTag() {
//                return javaPackege;
//            }

            public List<Operation> getOperations(){
                List<Operation> ret = new ArrayList<>();
                if(getGet()!= null)
                    ret.add(getGet());
                if(getPatch()!= null)
                    ret.add(getPatch());
                if(getDelete()!= null)
                    ret.add(getDelete());
                if(getHead()!= null)
                    ret.add(getHead());
                if(getOptions()!= null)
                    ret.add(getOptions());
                if(getPost()!= null)
                    ret.add(getPost());
                if(getPut()!= null)
                    ret.add(getPut());
                if(getTrace()!= null)
                    ret.add(getTrace());

                return ret;
            }
        }.summary(sppService.getName()).description(sppService.getName());
        Operation operation = findMethod(serviceType,sppService, pathItem).addTagsItem(javaPackege);
        operation.setOperationId(sppService.getFuncName());
        addARequestBody(operation, sppService,openAPI);
        openAPI.path(sppClass.getName(), pathItem);
    }

    private static void addARequestBody(Operation operation, SppService sppService,OpenAPI openAPI) {
        List<? extends SppLocalVar> sppFieldList = sppService.getType() == IConstance.CompilationUnitType.scenario ?
                sppService.getServiceBody().getSppLocalVarList()
                : sppService.getSppFieldList();
        String schemaName = sppFieldList.get(0).getType().getName();
        if (sppFieldList.size() > 1){
            schemaName = sppService.getName() +"Args";
            SppClass cls = new SppClass(schemaName, IConstance.CompilationUnitType.role);
            for(SppLocalVar field:sppFieldList) {
                cls.addField(new SppField(field.getType(),field.getName()));
            }
            createASchema(cls,openAPI);
        }
        operation.requestBody(new RequestBody().$ref(schemaName));
        createResponses(operation, schemaName);
        if(openAPI.getComponents() == null)
            openAPI.components(new Components());
        final String scName = schemaName;
        openAPI.getComponents().addRequestBodies(schemaName,new RequestBody(){
            public String getName(){
                return scName;
            }
        }.content(new Content().addMediaType("Content-Type",
                new MediaType().addEncoding("json",new Encoding().contentType("application/json")).
                        schema(new Schema().$ref(schemaName)))).required(true).description("Request for " + schemaName));
    }

    private static void createResponses(Operation operation, String schemaName) {
        ApiResponse apiResponse = new ApiResponse() {
            public String getCode(){
                return "200";
            }
        }.description("successful operation").content(new Content().addMediaType("Content-Type",
                new MediaType().addEncoding("json",new Encoding().contentType("application/json")).
                        schema(new Schema<>().$ref(schemaName))));
        operation.responses(new ApiResponses().addApiResponse("200",apiResponse));
    }

    private static Operation findMethod(IConstance.ServiceType serviceType, SppService sppService,PathItem pathItem) {
        MyOperation operation = new MyOperation();
        if (serviceType != null) {
            switch (sppService.getServiceType()) {
                case check:
                case query:
                case calculate: {
                    operation.setMethod("get");
                    pathItem.setGet(operation);
                    break;
                }
                case update:
                case unknown: {
                    operation.setMethod("post");
                    pathItem.setPost(operation);
                    break;
                }
            }
        }else{
            operation.setMethod("put");
            pathItem.setPut(operation);
        }
        return operation;
    }

    private static void createASchema(SppClass sppClass, OpenAPI openAPI) {
        Schema schema = new Schema();
        schema.setName(sppClass.getName());
        schema.setTitle(sppClass.getType().name());
        schema.setType("object");
        List<SppField> fields = sppClass.getSppFieldList();
        for (SppField field : fields) {
            createSchemaField(field, schema);
        }
        openAPI.schema(sppClass.getName(), schema);
    }

    private static void createSchemaField(SppField field, Schema schema) {
        Schema prop = new Schema();
        prop.setName(field.getName());
        SppClass type = field.getType();
        if (type.getType() == null) {
            prop.setType(IFileGenerator.toOpenAPIPrimaryType(type.getName()));
            prop.setFormat(IFileGenerator.toOpenAPITypeFormat(type.getName()));
        } else {
            prop.setType("object");
            prop.set$ref("'#/components/schemas/" + type.getName() + "'");
        }
        schema.addProperty(field.getName(), prop);
    }

}
