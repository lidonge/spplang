package free.servpp.openapi3;

import io.swagger.oas.models.media.Schema;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-11-26@version 1.0
 */
public class SchemaInfo {
    String name;
    Schema schema;
    public enum Type{
        CR,BQ,Enum,schema,Reference
    }
    Type type;
    List<String> domains = new ArrayList<>();

    public String getSppTypeString(){
        String ret = type.toString();
        switch (type){
            case CR:
                ret = "Contract";
                break;
            case schema:
                ret = "Entity";
                break;
            case BQ:
                ret = "Entity";
                break;
            case Enum:
                ret = "Enum";
                break;
            case Reference:
                ret = "Entity";
                break;
        }
        return ret;
    }
    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    @Override
    public String toString() {
        return "SchemaInfo{" +
                "name='" + name + '\'' +
                ", types=" + type +
                ", domains=" + domains +
                '}';
    }

    public String toCSV(){
        return  name +
                ", " + type+
                ", " + domains.toString().replaceAll(",",";")+
                ", " + domains.size() ;

    }
}
