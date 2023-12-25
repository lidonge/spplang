package free.servpp.service.server;

import free.servpp.ILogable;
import free.servpp.service.Service;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lidong@date 2023-11-16@version 1.0
 */
public class DefaultServiceContainer implements IServiceContainer {
    private static DefaultServiceContainer instance;
    private static ILogable logable = new ILogable() {
        @Override
        public Logger getLogger() {
            return ILogable.super.getLogger();
        }
    };

    public static DefaultServiceContainer getInstance(){
        synchronized (DefaultServiceContainer.class) {
            if (instance == null) {
                instance = new DefaultServiceContainer();
                InputStream stream = DefaultServiceContainer.class.getResourceAsStream("/services.properties");
                try {
                    Properties properties = new Properties();
                    properties.load(stream);
                    for(String key: properties.stringPropertyNames()){
                        instance.registerService(key, (Service) Class.forName(properties.getProperty(key)).newInstance());
                        logable.getLogger().info("Service {} loaded,class is {}.",key,properties.getProperty(key));
                    }
                } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return instance;
    }

    private DefaultServiceContainer(){
    }
    private Map<String,Service> serviceMap = new ConcurrentHashMap<>();

    public void loadFromList(List<Service> services){
        for(Service service:services){
            serviceMap.put(service.getName(),service);
        }
    }

    @Override
    public Service getService(String name) {
        return serviceMap.get(name);
    }

    public Service registerService(String name, Service service){
        return serviceMap.put(name,service);
    }
}
