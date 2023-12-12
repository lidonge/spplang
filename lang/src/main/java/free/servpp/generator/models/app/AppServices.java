package free.servpp.generator.models.app;

import free.servpp.generator.models.Annotation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-11-23@version 1.0
 */
public class AppServices extends Annotation {
    List<AppService> appServiceList=new ArrayList<>();

    public List<AppService> getAppServiceList() {
        return appServiceList;
    }

    public String addService(AppService appService){
        return appService.addToList(appServiceList,appService);
    }
}
