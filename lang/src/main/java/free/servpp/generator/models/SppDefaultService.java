package free.servpp.generator.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-12-14@version 1.0
 */
public class SppDefaultService {
    private SppCompilationUnit realm;
    List<SppService> serviceList = new ArrayList<>();

    public SppCompilationUnit getRealm() {
        return realm;
    }

    public void setRealm(SppCompilationUnit realm) {
        this.realm = realm;
    }

    public List<SppService> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<SppService> serviceList) {
        this.serviceList = serviceList;
    }
}
