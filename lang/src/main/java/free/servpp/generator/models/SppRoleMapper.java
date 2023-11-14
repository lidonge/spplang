package free.servpp.generator.models;

import free.servpp.generator.general.IConstance;

/**
 * @author lidong@date 2023-11-03@version 1.0
 */
public class SppRoleMapper extends SppClass{
    SppClass role;
    SppClass entity;
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

    public SppClass getEntity() {
        return entity;
    }

    public void setEntity(SppClass entity) {
        this.entity = entity;
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
                "role=" + role.getName() +
                ", entity=" + entity.getName() +
                ", isMapSame=" + isMapSame +
                ", isTakeAll=" + isTakeAll +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", sppFieldList=" + sppFieldList +
                '}';
    }
}
