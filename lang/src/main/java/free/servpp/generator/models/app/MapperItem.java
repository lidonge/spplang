package free.servpp.generator.models.app;

import free.servpp.generator.models.SppField;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public class MapperItem implements INamedObject{

    String qualifiedName;

    SppFieldDefine field;

    SQlType sqlType = new SQlType();
    String mapName;

    public MapperItem(String qualifiedName, SppFieldDefine field) {
        this.qualifiedName = qualifiedName;
        this.field = field;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public SppFieldDefine getField() {
        return field;
    }

    public void setField(SppFieldDefine field) {
        this.field = field;
    }

    public SQlType getSqlType() {
        return sqlType;
    }

    public void setSqlType(SQlType sqlType) {
        this.sqlType = sqlType;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    @Override
    public String getName() {
        return qualifiedName;
    }

    @Override
    public String toString() {
        return "MapperItem{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", field=" + field +
                ", sqlType=" + sqlType +
                ", mapName='" + mapName + '\'' +
                '}';
    }
}
