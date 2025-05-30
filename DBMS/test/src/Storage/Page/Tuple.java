package Storage.Page;

//import Conditions.*;
//import Conditions.Condition;
import Storage.Value.*;
import Table.Schema;

import java.io.*;
import java.util.ArrayList;

public class Tuple {

    protected Value[] values;

    protected Value primaryV;

    public Tuple() {}

    public Tuple(Value[] values) {
        this.values = values;
    }

    public Value[] getValues() {
        return values;
    }

    public Value getPrimaryV() {
        return primaryV;
    }

    public Value getValue(int index) { return values[index];}

    public void setValues(Value[] values) {
        this.values = values;
    }

    public int getLength() {
        int sum = 0;
        for (Value value : values) {
            sum += value.getLength();
        }
        return sum;
    }

    /**
     * 联合索引比较的时候,先比较第一个索引值,若相等则再比较下一个索引值,依次类推
     */
    public int compare(Tuple tuple) {
        return primaryV.compare(tuple.getPrimaryV());
    }

    /**
     * @param index 修改值下标
     * @param value 修改值
     */
    public void set(int index, Value value) {
        values[index] = value;
    }

//
//    // Method to check if the Tuple satisfies the given condition
//    public boolean checkCondition(ConditionNode conditionNode,Schema schema) {//对于每条tuple，传入条件筛选树和schema
//        if (conditionNode instanceof Condition) {
//            //叶节点
//            Condition condition = (Condition) conditionNode;
//            int columnIndex = getColumnIndex(condition.getColumn(),schema);//获取condition所约束的字段位置,需要对照schema
//            return check(condition, columnIndex);//判断是否符合条件
//        } else if (conditionNode instanceof ConditionOperator) {
//            // 运算符节点，递归
//            ConditionOperator operatorNode = (ConditionOperator) conditionNode;
//            boolean leftResult = getLeftCheckResult(operatorNode.getLeft(),schema);
//            boolean rightResult = getRightCheckResult(operatorNode.getRight(),schema);
//
//            if (operatorNode.getOperator().equals("AND")) {
//                return leftResult && rightResult;
//            } else if (operatorNode.getOperator().equals("OR")) {
//                return leftResult || rightResult;
//            }
//        }
//        return false;
//    }
//
//    private boolean getLeftCheckResult(ConditionNode leftNode,Schema schema) {
//        return checkCondition(leftNode,schema);
//    }
//
//    private boolean getRightCheckResult(ConditionNode rightNode,Schema schema) {
//        return checkCondition(rightNode,schema);
//    }

    // Helper method to get the index of the column (this assumes columns are ordered in the table schema)
    private int getColumnIndex(String columnName,Schema schema) {
        // Assuming the table's schema is available through a method `getSchema()` on the table object
        // You would fetch the index of the column based on its name

        return schema.getIndex(columnName);
    }

    /**
     * @param condition 比较条件
     * @param index 比较的下表
     * @return 是否符合条件
     */
//
//    public boolean check(Condition condition, int index) {
//        switch (condition.getOperator()) {
//            case "=":
//                if (values[index].compare(condition.getValue())==0) {
//                    return true;
//                } else return false;
//            case "!=":
//                if (values[index].compare(condition.getValue())!=0) {
//                    return true;
//                } else return false;
//            case "<":
//                if (values[index].compare(condition.getValue()) == -1) {
//                    return true;
//                } else return false;
//            case ">":
//                if (values[index].compare(condition.getValue()) == 1) {
//                    return true;
//                } else return false;
//            case "<=":
//                if (values[index].compare(condition.getValue()) <= 0) {
//                    return true;
//                } else return false;
//            case ">=":
//                if (values[index].compare(condition.getValue()) >= 0) {
//                    return true;
//                } else return false;
//        }
//        return false;
//    }

    /**
     * 添加值到末尾
     * @param value 添加的value
     */

    public void appendValue(Value value) {
        Value[] newValues = new Value[values.length + 1];
        System.arraycopy(values, 0, newValues, 0, values.length);
        newValues[values.length] = value;
        this.values = newValues;
    }

    /**
     * 删除某个列的元素
     * @param index 要删除的列的下表
     */
    public void removeValue(int index) {
        if (index < 0 || index >= values.length) return;
        Value[] newValues = new Value[values.length - 1];
        for (int i = 0, j = 0; i < values.length; i++) {
            if (i != index) {
                newValues[j++] = values[i];
            }
        }
        this.values = newValues;
    }

    /**
     * 将tuple序列化，考量每个value的种类
     * @return tuple的序列化后的字节数组
     * @throws IOException
     */
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);

        for (Value value : values) {
            int type = value.getType();
            byte[] valueBytes = value.toBytes();
            int len = valueBytes.length;

            dataOut.writeInt(type);
            dataOut.writeInt(len);
            dataOut.write(valueBytes);
        }

        return out.toByteArray();
    }

    /**
     * 将字节数组读下来反序列化为tuple对象
     * @param data 字节数组
     * @return 反序列化得到的tuple对象
     * @throws IOException
     */
    public static Tuple fromBytes(byte[] data) throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
        ArrayList<Value> values = new ArrayList<>();

        while(in.available() > 0) {
            int type = in.readInt();
            int len = in.readInt();
            byte[] valueBytes = new byte[len];
            in.readFully(valueBytes);
            Value value = decodeTypedValue(type,valueBytes);
            values.add(value);
        }
        return new Tuple(values.toArray(new Value[0]));
    }


    private static Value decodeTypedValue(int type, byte[] valueBytes) throws IOException {
        DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(valueBytes));
        switch (type) {
            case 0:
                return new NullValue();
            case 1:
                return new StringValue(dataIn.readUTF());
            case 2:
                return new IntValue(dataIn.readInt());
            case 3:
                return new LongValue(dataIn.readLong());
            case 4:
                return new BooleanValue(dataIn.readBoolean());
            default:
                throw new IOException("未知类型: " + type);
        }
    }
}
