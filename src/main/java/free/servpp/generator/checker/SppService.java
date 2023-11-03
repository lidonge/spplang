package free.servpp.generator.checker;

import free.servpp.generator.IConstance;

/**
 * @author lidong@date 2023-11-02@version 1.0
 */
public class SppService extends SppClass{
    private IConstance.ServiceType serviceType;
    private String funcName;
    public SppService(String name) {
        super(name);
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
}
