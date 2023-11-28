package free.servpp.generator.models.app;

import free.servpp.generator.models.IComponent;
import free.servpp.generator.models.IContainer;

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
    AppServices appServices = new AppServices();

    IContainer appAnnotations = new IContainer() {
        @Override
        public void _add(IComponent component) {
            appAnnotationList.add((AppAnnotation) component);
        }

        @Override
        public boolean isContains(IComponent component) {
            return appAnnotationList.contains(component);
        }

        @Override
        public IContainer getParent() {
            return null;
        }

        @Override
        public void setParent(IContainer iContainer) {

        }
    };
    List<AppAnnotation> appAnnotationList = new ArrayList<>();

    public List<AppAnnotation> getAppAnnotationList() {
        return appAnnotationList;
    }

    public IContainer getAppAnnotations() {
        return appAnnotations;
    }

    public AppServices getAppServices() {
        return appServices;
    }

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
