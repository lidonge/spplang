package free.servpp.generator.openapi;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import free.servpp.ILogable;
import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.IFileGenerator;
import free.servpp.generator.general.BaseClassGenerator;
import free.servpp.generator.models.*;
import free.servpp.generator.models.app.AppAnnotation;
import free.servpp.generator.models.app.AppHeader;
import free.servpp.generator.models.app.RuleBlock;
import free.servpp.generator.models.app.SppExtendField;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
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
//        dealMaps(sppDomain);
        OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(new Info().description("S++ API").version("1.0.0").title("OpenAPI "+domainName));
        Map<String, SppCompilationUnit> sppClassMap = sppDomain.getMapsOfClass();
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

    private void genSppClasses(File domainPath, String basePackage, Map<String, SppCompilationUnit> sppClassMap, OpenAPI openAPI, String domainName) {
        for (SppCompilationUnit sppClass : sppClassMap.values()) {
//            if(sppClass.getType() == IConstance.CompilationUnitType.scenario)
//                System.out.println(sppClass);
            IConstance.CompilationUnitType type = sppClass.getType();
            if (type == IConstance.CompilationUnitType.entity
                    || type == IConstance.CompilationUnitType.role
                    || type == IConstance.CompilationUnitType.contract
                    || type == IConstance.CompilationUnitType.reference) {
                createASchema((SppClass) sppClass, openAPI);
            } else if (type == IConstance.CompilationUnitType.atomicservice) {
                SppService sppService = (SppService) sppClass;
                if (sppService.getScopeItem().isLocal()) {
                    createAPath(domainName, (SppClass) sppClass, openAPI);
                }
                services.add((SppService) sppClass);
            } else if (type == IConstance.CompilationUnitType.scenario) {
                createAPath(domainName, (SppClass) sppClass, openAPI);
                scenarios.add((SppService) sppClass);
            }
//            new OpenAPIClassWriter(domainPath,sppClass, basePackage, domainName).generate();
            if(sppClass instanceof SppClass)
                new MustacheClassWriter(domainPath, (SppClass) sppClass, basePackage, domainName).generate();
            else if(sppClass instanceof SppEnum){
                createAEnum((SppEnum) sppClass,openAPI);
            }
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
        Template template = Mustache.compiler().escapeHTML(false).compile(new InputStreamReader(OpenApiGenerator.class.getResourceAsStream("/servicesprop.mustache")));
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

    private static void setAdditionalPackage(String basePacakge, String javaPackage, Map<String, SppCompilationUnit> sppClassMap) {
        for (SppCompilationUnit sppClass : sppClassMap.values()) {
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
            sppClass.setDomainName(javaPackage);
            sppClass.setAdditionalPackage(additional);
        }
    }

    private void createAPath(String domainName, SppClass sppClass, OpenAPI openAPI) {
        SppService sppService = (SppService) sppClass;
        AppAnnotation param = null;
        for(AppAnnotation annotation : sppService.getAnnotations()){
            if("parameter".equals(annotation.getName())){
                param = annotation;
                break;
            }
        }
        IConstance.ServiceType serviceType = sppService.getServiceType();
        PathItem pathItem = new PathItem().summary(sppService.getName()).description(sppService.getName());
        Operation operation = findMethod(serviceType, sppService, pathItem).addTagsItem(domainName);
        operation.setOperationId(sppService.getFuncName());
        addARequestBody(operation, sppService, openAPI);
        String path = null;
        if(param != null && (path =param.getParameters().get("path") )!= null){
            String[] params = path.split("\\{");
            if(params.length >1) {
                for (String s : params) {
                    int idx = s.indexOf('}');
                    if (idx != -1){
                        String sparam = s.substring(0,idx);
                        operation.addParametersItem(new Parameter().$ref("#/components/parameters/" +sparam));
                        createAParameter(openAPI,sparam);
                    }
                }
            }
            openAPI.path("/" + domainName + "/" + path, pathItem);
        }else {
            openAPI.path("/" + domainName + "/" + sppService.getFuncName(), pathItem);
        }
    }

    private void createAParameter(OpenAPI openAPI, String sparam) {
        openAPI.getComponents().addParameters(sparam,new Parameter().name(sparam).in("path").required(true).
                style(Parameter.StyleEnum.SIMPLE).schema(new Schema<>().type("string")));
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
        openAPI.getComponents().addRequestBodies(schemaName, new RequestBody().content(
                new Content().addMediaType("application/json",
                new MediaType().schema(new Schema().$ref(schemaName)))).required(true).description("Request for " + schemaName));
    }

    private void createResponses(Operation operation, String schemaName) {
        ApiResponse apiResponse = new ApiResponse().description("successful operation").content(
                new Content().addMediaType("application/json",
                new MediaType().schema(new Schema<>().$ref(schemaName))));
        operation.responses(new ApiResponses().addApiResponse("200", apiResponse));
    }

    private Operation findMethod(IConstance.ServiceType serviceType, SppService sppService, PathItem pathItem) {
        Operation operation = new Operation();
        if (serviceType != null) {
            switch (sppService.getServiceType()) {
                case check:
                case query:
                case calculate: {
                    pathItem.setGet(operation);
                    break;
                }
                case update:
                case unknown: {
                    pathItem.setPost(operation);
                    break;
                }
            }
        } else {
            pathItem.setPut(operation);
        }
        return operation;
    }

    private void createAEnum(SppEnum sppEnum, OpenAPI openAPI) {
        Schema schema = new Schema();
        schema.setName(sppEnum.getName());
        schema.setTitle(sppEnum.getName());
        schema.setType("string");
        List<String> fields = sppEnum.getItems();
        for (String field : fields) {
            schema.addEnumItemObject(field);
        }
        openAPI.schema(sppEnum.getName(), schema);
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
        SppCompilationUnit type = field.getType();
        if (type.getType() == null) {
            prop.setType(IFileGenerator.toOpenAPIPrimaryType(type.getName()));
            prop.setFormat(IFileGenerator.toOpenAPITypeFormat(type.getName()));
        } else {
            prop.setType("object");
            prop.set$ref("#/components/schemas/" + type.getName() );
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
