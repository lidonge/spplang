package free.servpp.generator.models.app;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-11-21@version 1.0
 */
public class RuleBlock {
    List<AppHeader> headers = new ArrayList<>();
    List<AppMapper> appMappers = new ArrayList<>();
    PrimaryKeys primaryKeys = new PrimaryKeys();
    AppScope appScope = new AppScope();

    public List<AppMapper> getAppMappers() {
        return appMappers;
    }

    public String addMapper(AppMapper appMapper){
        return appMapper.addToList(appMappers,appMapper);
    }
    public String addAppHeader(AppHeader appHeader){
        AppHeader header = searchAppHeader(appHeader);
        if(header != null) {
            if(!header.isTemp()){
                return "Duplicate header " + appHeader.getName();
            }
            headers.remove(header);
        }
        headers.add(appHeader);
        return null;
    }
    public AppHeader newOrExistAppHeader(String name) {
        AppHeader header = new AppHeader();
        header.setName(name);
        AppHeader exist = searchAppHeader(header);
        if(exist != null){
            header = exist;
        }else{
            header.setTemp(true);
            addAppHeader(header);
        }
        return header;
    }

    public AppHeader searchAppHeader(AppHeader appHeader) {
        return (AppHeader) appHeader.searchAppHeader(headers, appHeader);
    }

    public String addPrimaryKey(String qualifiedName, SppFieldDefine key){
        return primaryKeys.addKey(qualifiedName,key);
    }
    public List<AppHeader> getHeaders() {
        return headers;
    }

    public PrimaryKeys getPrimaryKeys() {
        return primaryKeys;
    }

    public AppScope getAppScope() {
        return appScope;
    }

    @Override
    public String toString() {
        return "RuleBlock{" +
                "headers=" + headers +
                ", appMappers=" + appMappers +
                ", primaryKeys=" + primaryKeys +
                ", appScope=" + appScope +
                '}';
    }
}
