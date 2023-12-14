package free.servpp.generator.models.app;

import free.servpp.generator.models.IAnnotation;
import free.servpp.generator.models.IComponent;
import free.servpp.generator.models.IContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-21@version 1.0
 */
public class RuleBlock {
    List<AppHeader> headers = new ArrayList<>();
    List<AppMapper> appMappers = new ArrayList<>();
    AppPrimaryKeys appPrimaryKeys = new AppPrimaryKeys();
    AppScope appScope = new AppScope();
    AppServices appServices = new AppServices();
    List<AppTables> appTables = new ArrayList<>();
    Map<String, AppTable> allTables = new HashMap();

    List<AnnotationDefine> currentLineAnns;
    IAnnotation currentAnnotatable;
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
        public void setParent(IContainer parent) {

        }
    };
    List<AppAnnotation> appAnnotationList = new ArrayList<>();

    public Map<String, AppTable> getAllTables() {
        return allTables;
    }

    public void setAllTables(Map<String, AppTable> allTables) {
        this.allTables = allTables;
    }

    public IAnnotation getCurrentAnnotatable() {
        return currentAnnotatable;
    }

    public void setCurrentAnnotatable(IAnnotation currentAnnotatable) {
        this.currentAnnotatable = currentAnnotatable;
    }

    public void addLineAnn(AnnotationDefine lineAnn) {
        if(currentLineAnns == null)
            currentLineAnns = new ArrayList<>();
        currentLineAnns.add(lineAnn);
    }

    public void clearLineAnn(){
        currentLineAnns = null;
    }

    public List<AnnotationDefine> getCurrentLineAnns() {
        return currentLineAnns;
    }

    public void setCurrentLineAnns(List<AnnotationDefine> currentLineAnns) {
        this.currentLineAnns = currentLineAnns;
    }

    public List<AppTables> getAppTables() {
        return appTables;
    }

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
        return appPrimaryKeys.addKey(qualifiedName,key);
    }
    public List<AppHeader> getHeaders() {
        return headers;
    }

    public AppPrimaryKeys getPrimaryKeys() {
        return appPrimaryKeys;
    }

    public AppScope getAppScope() {
        return appScope;
    }

    @Override
    public String toString() {
        return "RuleBlock{" +
                "headers=" + headers +
                ", appMappers=" + appMappers +
                ", primaryKeys=" + appPrimaryKeys +
                ", appScope=" + appScope +
                '}';
    }
}
