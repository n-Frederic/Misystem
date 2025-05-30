package Table;

import java.util.ArrayList;

/**
 * Schema类用于描述数据库表的结构。
 * 它包含表名和表中每个字段的规则定义。
 */
public class Schema {
    private ArrayList<Field> fields;
    private String primaryKeyName;

    public Schema(ArrayList<Field> fields){
        this.fields = fields;
        initializePrimaryKey();
    }

    private void initializePrimaryKey() {
        for(Field field : fields) {
            if(field.isPrimaryKey()) primaryKeyName = field.getName();
        }
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public boolean hasPrimaryKey() {
        return primaryKeyName != null && !primaryKeyName.isEmpty();
    }

    public void addColumn(Field field) {
        fields.add(field);
    }

    public void dropColumn(String columnName) {
        int index = getIndex(columnName);
        if (index != -1) {
            fields.remove(index);
        }
    }

    public ArrayList<Field> getFields(){
        return this.fields;
    }

    public Field getField(String fieldName) {
        for(Field field : fields) {
            if(field.getName().equals(fieldName))
                return field;
        }
        return null;
    }

    public int getIndex(Field field) {
        for(int i = 0; i < fields.size(); i++) {
            if(fields.get(i).getName().equals(field.getName())) return i;
        }
        return -1;
    }

    public int getIndex(String fieldName) {
        for(int i = 0; i < fields.size(); i++) {
            if(fields.get(i).getName().equals(fieldName)) return i;
        }
        return -1;
    }
}
