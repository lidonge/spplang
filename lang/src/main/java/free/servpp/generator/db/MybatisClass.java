package free.servpp.generator.db;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidong@date 2023-12-15@version 1.0
 */
public class MybatisClass implements IMybatisClass{
    String table;
    String type;
    List<MybatisField> fields;
    public MybatisClass(String table, String type) {
        this.table = table;
        this.type = type;
    }

    @Override
    public void createFields() {
        fields = new ArrayList<>();
    }

    @Override
    public List<MybatisField> getFields() {
        return fields;
    }
}
