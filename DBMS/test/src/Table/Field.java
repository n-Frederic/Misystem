package Table;

import Storage.Value.Value;

public class Field {
    private String name;
    private String type;
    private boolean primaryKey;
    private boolean unique;
    private boolean notNull;
    private Value Default;

    public Field(String name, String type) {
        this.name = name;
        this.type = type;
        primaryKey = false;
        unique = false;
        notNull = false;
        Default=null;
    }


    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }


    public boolean isNotNull() {
        return notNull;
    }

    public void setNotnull(boolean notNUll) {
        this.notNull =notNUll;
    }

    public Value getDefault() {
        return Default;
    }

    public void setDefault(Value aDefault) {
        Default = aDefault;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", primaryKey=" + primaryKey +
                ", unique=" + unique +
                ", notNull=" + notNull +
                ", Default='" + Default + '\'' +
                '}';
    }


    // 把字符串类型转换成你系统里定义的 Value 子类类型
//    public Class<? extends Value> getTypeClass() {
//        switch (type.getType()) {
//            case 1:
//                return StringValue.class;
//            case 2:
//                return IntValue.class;
//            case 3 :
//                return LongValue.class;
//            case 4:
//                return BooleanValue.class;
//            // 如果有其他类型可以补
//            default:
//                throw new IllegalArgumentException("未知字段类型：" + type);
//        }
//    }



}


