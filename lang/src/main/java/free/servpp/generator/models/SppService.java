package free.servpp.generator.models;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.models.app.ScopeItem;

/**
 * @author lidong@date 2023-11-02@version 1.0
 */
public class SppService extends SppClass{
    protected IConstance.ServiceType serviceType;
    protected String funcName;

    protected ServiceBody serviceBody;

    private ScopeItem scopeItem;
    public SppService(String name) {
        super(name);
        this.serviceBody = new ServiceBody(this);
    }
    public SppService(String name, IConstance.CompilationUnitType type) {
        super(name,type);
        this.serviceBody = new ServiceBody(this);
    }

    public ScopeItem getScopeItem() {
        return scopeItem;
    }

    public void setScopeItem(ScopeItem scopeItem) {
        this.scopeItem = scopeItem;
    }

    public ServiceBody getServiceBody() {
        return serviceBody;
    }
    public String addLocalVar(SppLocalVar sppLocalVar){
        return serviceBody.addLocalVar(sppLocalVar);
    }

    public SppLocalVar getLocalVar(String varName){
        return serviceBody.getLocalVar(varName);
    }
    public SppLocalVar getLocalVar(int index){
        return serviceBody.getLocalVar(index);
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public IConstance.ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(IConstance.ServiceType returnType) {
        this.serviceType = returnType;
    }

    public void copyFrom(SppClass cls) {
        super.copyFrom(cls);
        SppService sppService = (SppService) cls;
        serviceBody = sppService.serviceBody;
        serviceType = sppService.serviceType;
        funcName = sppService.funcName;
    }

    @Override
    public String toString() {
        return "SppService{" +
                "serviceType=" + serviceType +
                ", funcName='" + funcName + '\'' +
                ", serviceBody=" + serviceBody +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", sppFieldMap=" + sppFieldMap +
                ", sppFieldList=" + sppFieldList +
                '}';
    }
}
