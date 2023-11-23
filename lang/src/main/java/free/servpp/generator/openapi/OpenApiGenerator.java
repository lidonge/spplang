package free.servpp.generator.openapi;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import free.servpp.ILogable;
import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.IFileGenerator;
import free.servpp.generator.general.BaseClassGenerator;
import free.servpp.generator.models.*;
import free.servpp.generator.models.app.AppHeader;
import free.servpp.generator.models.app.RuleBlock;
import free.servpp.generator.models.app.SppExtendField;
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

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-13@version 1.0
 */
public class OpenApiGenerator extends BaseClassGenerator implements ILogable {
    List<SppService> services = new ArrayList<>();
    List<SppService> scenarios = new ArrayList<>();

    public OpenAPI generate(SppDomain sppDomain, File domainPath, String basePackage) {
        String domainName = sppDomain.getName();
        dealMaps(sppDomain);
        OpenAPI openAPI = new OpenAPI();
        Map<String, SppClass> sppClassMap = sppDomain.getMapsOfClass();
        openAPI.addTagsItem(new Tag().name(domainName).description(""));
        setAdditionalPackage(basePackage, domainName, sppClassMap);
        genAppHeaders(sppDomain, openAPI);
        genSppClasses(domainPath, basePackage, sppClassMap, openAPI, domainName);
        services.addAll(scenarios);

        return openAPI;
    }

    private void genAppHeaders(SppDomain sppDomain, OpenAPI openAPI) {
        RuleBlock ruleBlock = sppDomain.getRuleBlock();
        for (AppHeader appHeader : ruleBlock.getHeaders()) {
            createASchema(appHeader, openAPI);
        }
    }

    private void genSppClasses(File domainPath, String basePackage, Map<String, SppClass> sppClassMap, OpenAPI openAPI, String domainName) {
        for (SppClass sppClass : sppClassMap.values()) {
//            if(sppClass.getType() == IConstance.CompilationUnitType.scenario)
//                System.out.println(sppClass);
            IConstance.CompilationUnitType type = sppClass.getType();
            if (type == IConstance.CompilationUnitType.entity
                    || type == IConstance.CompilationUnitType.role
                    || type == IConstance.CompilationUnitType.contract
                    || type == IConstance.CompilationUnitType.reference) {
                createASchema(sppClass, openAPI);
            } else if (type == IConstance.CompilationUnitType.atomicservice) {
                SppService sppService = (SppService) sppClass;
                if (sppService.getScopeItem().isLocal()) {
                    createAPath(domainName, sppClass, openAPI);
                }
                services.add((SppService) sppClass);
            } else if (type == IConstance.CompilationUnitType.scenario) {
                createAPath(domainName, sppClass, openAPI);
                scenarios.add((SppService) sppClass);
            }
//            new OpenAPIClassWriter(domainPath,sppClass, basePackage, domainName).generate();
            new MustacheClassWriter(domainPath, sppClass, basePackage, domainName).generate();
        }
    }

    public void createJavaSpringMustache(File yamlOutPath) {
        File dir = new File(yamlOutPath, "JavaSpring");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        InputStream in = OpenApiGenerator.class.getResourceAsStream("/JavaSpring/methodBody.mustache");
        File mustache = new File(dir, "methodBody.mustache");

        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(mustache));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    public void createServicesProperties(File genRoot) {
        File resources = new File(genRoot.getParent(), "resources");
        Template template = Mustache.compiler().compile(new InputStreamReader(OpenApiGenerator.class.getResourceAsStream("/servicesprop.mustache")));
        if (!resources.exists()) {
            resources.mkdirs();
        }
        File servicesFile = new File(resources, "services.properties");

        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(servicesFile));
//            out = new PrintWriter(System.out);
            out.println(template.execute(new Object() {
                List<SppService> getServices() {
                    return services;
                }
            }));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    private static void setAdditionalPackage(String basePacakge, String javaPackage, Map<String, SppClass> sppClassMap) {
        for (SppClass sppClass : sppClassMap.values()) {
            String additional = null;
            IConstance.CompilationUnitType type = sppClass.getType();
            if (type == IConstance.CompilationUnitType.role ||
                    type == IConstance.CompilationUnitType.rolemapper ||
                    type == IConstance.CompilationUnitType.entity ||
                    type == IConstance.CompilationUnitType.contract ||
                    type == IConstance.CompilationUnitType.reference) {
                additional = "model";
            } else if (type == IConstance.CompilationUnitType.scenario ||
                    type == IConstance.CompilationUnitType.atomicservice) {
                additional = "handler";
            }
            sppClass.setBasePackage(basePacakge);
            sppClass.setJavaPackage(javaPackage);
            sppClass.setAdditionalPackage(additional);
        }
    }

    private void createAPath(String javaPackage, SppClass sppClass, OpenAPI openAPI) {
        SppService sppService = (SppService) sppClass;
        IConstance.ServiceType serviceType = sppService.getServiceType();
        PathItem pathItem = new PathItem() {
            public String getPath() {
                return javaPackage + "/" + sppService.getFuncName();
            }

            public List<Operation> getOperations() {
                List<Operation> ret = new ArrayList<>();
                if (getGet() != null)
                    ret.add(getGet());
                if (getPatch() != null)
                    ret.add(getPatch());
                if (getDelete() != null)
                    ret.add(getDelete());
                if (getHead() != null)
                    ret.add(getHead());
                if (getOptions() != null)
                    ret.add(getOptions());
                if (getPost() != null)
                    ret.add(getPost());
                if (getPut() != null)
                    ret.add(getPut());
                if (getTrace() != null)
                    ret.add(getTrace());

                return ret;
            }
        }.summary(sppService.getName()).description(sppService.getName());
        Operation operation = findMethod(serviceType, sppService, pathItem).addTagsItem(javaPackage);
        operation.setOperationId(sppService.getFuncName());
        addARequestBody(operation, sppService, openAPI);
        openAPI.path(sppClass.getName(), pathItem);
    }

    private void addARequestBody(Operation operation, SppService sppService, OpenAPI openAPI) {
        List<? extends SppLocalVar> sppFieldList = sppService.getType() == IConstance.CompilationUnitType.scenario ?
                sppService.getServiceBody().getSppLocalVarList()
                : sppService.getSppFieldList();
        String schemaName = sppFieldList.get(0).getType().getName();
        schemaName = sppService.getName() + "Args";
        SppClass cls = new SppClass(schemaName, IConstance.CompilationUnitType.role);
        if(sppService.getScopeItem() == null){
            getLogger().warn("Service {}'s scope is not defined!",sppService.getName());
        }else {
            AppHeader inHeader = sppService.getScopeItem().getScopeDefine().getIn();
            SppClass headerCls = new SppClass(inHeader.getName(), IConstance.CompilationUnitType.role);
            for(SppExtendField sppExtendField: inHeader.getSppExtendFields()){
                headerCls.addField(new SppField(sppExtendField.getType(), sppExtendField.getName()));
            }
            cls.addField(new SppField(headerCls, "AppHeader"));
        }
        for (SppLocalVar field : sppFieldList) {
            cls.addField(new SppField(field.getType(), field.getName()));
        }
        createASchema(cls, openAPI);
        operation.requestBody(new RequestBody().$ref(schemaName));
        createResponses(operation, schemaName);
        if (openAPI.getComponents() == null)
            openAPI.components(new Components());
        final String scName = schemaName;
        openAPI.getComponents().addRequestBodies(schemaName, new RequestBody() {
            public String getName() {
                return scName;
            }
        }.content(new Content().addMediaType("Content-Type",
                new MediaType().addEncoding("json", new Encoding().contentType("application/json")).
                        schema(new Schema().$ref(schemaName)))).required(true).description("Request for " + schemaName));
    }

    private void createResponses(Operation operation, String schemaName) {
        ApiResponse apiResponse = new ApiResponse() {
            public String getCode() {
                return "200";
            }
        }.description("successful operation").content(new Content().addMediaType("Content-Type",
                new MediaType().addEncoding("json", new Encoding().contentType("application/json")).
                        schema(new Schema<>().$ref(schemaName))));
        operation.responses(new ApiResponses().addApiResponse("200", apiResponse));
    }

    private Operation findMethod(IConstance.ServiceType serviceType, SppService sppService, PathItem pathItem) {
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
        } else {
            operation.setMethod("put");
            pathItem.setPut(operation);
        }
        return operation;
    }

    private void createASchema(SppClass sppClass, OpenAPI openAPI) {
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

    private void createSchemaField(SppField field, Schema schema) {
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

    private void createASchema(AppHeader appHeader, OpenAPI openAPI) {
        Schema schema = new Schema();
        schema.setName(appHeader.getName());
        schema.setTitle(appHeader.getName());
        schema.setType("object");
        List<SppExtendField> fields = appHeader.getSppExtendFields();
        for (SppExtendField field : fields) {
            createSchemaField(field, schema);
        }
        openAPI.schema(appHeader.getName(), schema);
    }

}
