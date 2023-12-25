package free.servpp.petstore;

import com.fasterxml.jackson.databind.Module;
import free.servpp.petstore.configuration.PetStoreConfiguration;
import free.servpp.service.server.DefaultServiceContainer;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

import javax.annotation.PostConstruct;

@SpringBootApplication(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@ComponentScan(
        basePackages = {"free.servpp.openapi.petstore.handler"
                , "free.servpp.openapi.petstore.handler"
                , "free.servpp.petstore.configuration"
                , "free.servpp.openapi.petstore.service"
                , "org.openapitools.configuration"},
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
//@MapperScan(basePackages = "free.servpp.openapi.petstore.mapper")
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @Bean(name = "free.servpp.test.DemoApplication.jsonNullableModule")
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }

    @Autowired
    PetStoreConfiguration configuration;

    @PostConstruct
    public void initService(){
        DefaultServiceContainer.getInstance().loadFromList(configuration.getServiceList());
    }
}
