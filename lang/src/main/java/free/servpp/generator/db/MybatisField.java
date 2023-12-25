package free.servpp.generator.db;

/**
 * @author lidong@date 2023-12-15@version 1.0
 */
public class MybatisField {
    private String property;
    private String column;

    private IMybatisClass parent;

    public MybatisField(String property, String column) {
        this.property = property;
        this.column = column;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public IMybatisClass getParent() {
        return parent;
    }

    public void setParent(IMybatisClass parent) {
        this.parent = parent;
    }
}
