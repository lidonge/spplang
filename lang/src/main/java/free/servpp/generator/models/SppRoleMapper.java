package free.servpp.generator.models;

import free.servpp.generator.general.IConstance;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-11-03@version 1.0
 */
public class SppRoleMapper extends SppService{
    SppClass role;
    List<SppClass> entities;
    boolean isMapSame = true;
    boolean isTakeAll;

    public SppRoleMapper(String name) {
        super(name, IConstance.CompilationUnitType.rolemapper);
    }

    public SppClass getRole() {
        return role;
    }

    public void setRole(SppClass role) {
        this.role = role;
    }

    public List<SppClass> getEntities() {
        return entities;
    }

    public void addEntity(SppClass entity) {
        if(entities == null)
            entities = new ArrayList<>();
        entities.add(entity);
    }

    public boolean isMapSame() {
        return isMapSame;
    }

    public void setMapSame(boolean mapSame) {
        isMapSame = mapSame;
    }

    public boolean isTakeAll() {
        return isTakeAll;
    }

    public void setTakeAll(boolean takeAll) {
        isTakeAll = takeAll;
    }

    @Override
    public String toString() {
        return "SppRoleMapper{" +
                "role=" + role +
                ", entity=" + entities +
                ", isMapSame=" + isMapSame +
                ", isTakeAll=" + isTakeAll +
                ", serviceType=" + serviceType +
                ", funcName='" + funcName + '\'' +
                ", serviceBody=" + serviceBody +
                ", name='" + getName() + '\'' +
                ", type=" + type +
                ", sppFieldMap=" + sppFieldMap +
                '}';
    }
}
