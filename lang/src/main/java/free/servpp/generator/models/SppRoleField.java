package free.servpp.generator.models;

import free.servpp.generator.general.NameUtil;

/**
 * @author lidong@date 2023-11-03@version 1.0
 */
public class SppRoleField extends SppField{
    String entityName;
    public SppRoleField(SppClass cls, String name) {
        super(cls, name);
    }

    public String getEntityName() {
        return entityName;
    }

    public String getUpperEntityName(){
        return NameUtil.firstToLowerCase(entityName,false);
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
