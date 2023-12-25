package free.servpp.petstore.configuration;

import free.servpp.openapi.petstore.service.SimpleCreateCategory;
import free.servpp.service.Service;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author lidong@date 2023-12-18@version 1.0
 */
@Configuration
@MapperScan(basePackages = "free.servpp.openapi.petstore.mapper")
public class PetStoreConfiguration {

    @Autowired
    private List<Service> serviceList;
    @Autowired
    private SimpleCreateCategory simpleCreateCategory;

    public List<Service> getServiceList() {
        return serviceList;
    }

    public SimpleCreateCategory getCategoryMapper() {
        return simpleCreateCategory;
    }
}
