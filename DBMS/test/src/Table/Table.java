package Table;

import Storage.BPlusTree.BpTree;
import Storage.Page.Page;
import Storage.Page.PageManager;
import Storage.Page.Tuple;
import Storage.Value.Value;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Table类用于管理数据库表的操作。
 * 它支持插入数据、添加列、更新数据、删除数据等功能，表的结构定义和数据分别存储在JSON文件中。
 */
public class Table {
    private String tableName;
    private Schema schema ;
    private BpTree tree;
    PageManager pageManager;
    private static final String DIRECTORY = "../TestData/DatabaseManager";

    public Table(Schema s) throws IOException {
        this.schema = s;
        this.tree = new BpTree();
    }


    public void delete(Tuple tuple){
        tree.remove(tuple);
    }

    public RandomAccessFile From(String table) throws FileNotFoundException {
        String path = DIRECTORY + "/" + table + "/" + table + ".ibd";
        RandomAccessFile raf = new RandomAccessFile(path, "rw");
        return raf;
    }

    // 最终返回只有fieldName的tuple的数组
    public ArrayList<Tuple> select(ArrayList<Tuple> tuples, ArrayList<String> fieldNames){
        ArrayList<Tuple> result = new ArrayList<>();

        ArrayList<Integer> indexes = new ArrayList<>();
        for(String name : fieldNames){
            indexes.add(schema.getIndex(name));
        }

        for(Tuple t : tuples){
            Value[] selected = new Value[fieldNames.size()];
            for (int i = 0; i < indexes.size(); i++) {
                selected[i] = t.getValue(indexes.get(i));
            }
            result.add(new Tuple(selected));
        }

        return result;
    }

    public ArrayList<Tuple> selectAll() {
        ArrayList<Tuple> tuples = new ArrayList<>();
        Page current = tree.getHead();
        while(current != null) {
            tuples.addAll(current.getTuples());
            current = current.getNext();
        }
        return tuples;
    }

//    public ArrayList<Tuple> where(Condition condition) {
//        ArrayList<Tuple> tuples = new ArrayList<>();
//        Field field = schema.getField(condition.getColumn());
//        int index = schema.getIndex(field);
//
//        BpNode current = tree.getHead();
//        while(current != null) {
//            ArrayList<Tuple> temp = current.get(condition,index);
//            if(!temp.isEmpty()) {
//                tuples.addAll(temp);
//            }
//            current = current.getNext();
//        }
//
//        return tuples;
//    }

    public ArrayList<Tuple> where (ArrayList<Tuple> t1, ArrayList<Tuple> t2, String mode) {
        ArrayList<Tuple> tuples = new ArrayList<>();
        if (mode.equals("and")) {
            for (Tuple t : t1) {
                if (t2.contains(t)) {
                    tuples.add(t);
                }
            }
            return tuples;
        } else if (mode.equals("or")) {
            for (Tuple t : t1) {
                tuples.add(t);
            }
            for (Tuple t: t2) {
                if (!t1.contains(t)) {
                    tuples.add(t);
                }
            }
            return tuples;
        } else return tuples;
    }


    // TODO: 等待黄爱雷提供单个tuple的完整筛查
//    public void update(ArrayList<Tuple> tuples, HashMap<String,Value> map){
//        BpNode head = tree.getHead();
//        for(Tuple tuple : tuples) {
//            Tuple t = root.get(tuple);
//            for (Map.Entry<String, Value> entry : map.entrySet()) {
//                Field field = schema.getField(entry.getKey());
//                int index = schema.getIndex(field);
//                t.set(index,entry.getValue());
//            }
//        }
//    }

    // TODO: 等待一个能直接操作的
//    public void delete()

//    public void truncate() {
//        tree.truncate();
//    }

    public Schema getSchema(){
        return schema;
    }
    public BpTree getTree(){
        return tree;
    }


    public void insert(Tuple tuple) throws IOException {
        Page root = pageManager.getPage(0);
        root.insert(tuple,tree);
        pageManager.updatePageTo
    }














    /*    public static void addColumn(String tableName, Field newField) {
        Path schemaPath = From_schema(tableName);
        Path dataPath = From_data(tableName);

        JsonObject schema;
        try (FileReader reader = new FileReader(schemaPath.toFile())) {
            schema = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        JsonArray fields = schema.getAsJsonArray("fields");

        // 添加新字段结构
        JsonObject newFieldJson = new JsonObject();
        newFieldJson.addProperty("fieldName", newField.getName());

        JsonArray constraintsArray = new JsonArray();

        JsonObject typeObj = new JsonObject();
        typeObj.addProperty("Type", newField.getType());
        constraintsArray.add(typeObj);
        System.out.println(newFieldJson);
        System.out.println(typeObj);

        if (newField.isNotNull()) {
            JsonObject notNullObj = new JsonObject();
            notNullObj.addProperty("NOT NULL", true);
            constraintsArray.add(notNullObj);
        }

        if (newField.isUnique()) {
            JsonObject uniqueObj = new JsonObject();
            uniqueObj.addProperty("UNIQUE", true);
            constraintsArray.add(uniqueObj);
        }

        if (newField.isPrimaryKey()) {
            JsonObject pkObj = new JsonObject();
            pkObj.addProperty("PRIMARY KEY", true);
            constraintsArray.add(pkObj);
        }

        if (newField.getDefault() != null && !newField.getDefault().isEmpty()) {
            JsonObject defaultObj = new JsonObject();
            defaultObj.addProperty("Default", newField.getDefault());
            constraintsArray.add(defaultObj);
        }

        newFieldJson.add("constraint", constraintsArray);
        fields.add(newFieldJson);

        try (FileWriter writer = new FileWriter(schemaPath.toFile())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(schema, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 更新所有旧数据记录，添加新字段默认值
        JsonArray data = readData(dataPath);
        for (JsonElement e : data) {
            JsonObject obj = e.getAsJsonObject();
            if (newField.getDefault() != null && !newField.getDefault().isEmpty()) {
                obj.addProperty(newField.getName(), newField.getDefault());
            } else {
                obj.add(newField.getName(), JsonNull.INSTANCE);
            }
        }

        try (FileWriter writer = new FileWriter(dataPath.toFile())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    // set是针对于where筛选后的JsonArray修改列值
//    public static void Set(String tableName, HashMap<String,String> map, ConditionNode logicTree) {
////
//        JsonArray all = Table.From_data(tableName);
//
//// 逻辑过滤，返回的是 all 中的部分元素，但它们和 all 中是同一个 JsonObject 引用
//        JsonArray data = logicTree.evaluate();
//
//// 就地修改
//        for (JsonElement e : data) {
//            JsonObject obj = e.getAsJsonObject();
//            for (Map.Entry<String,String> entry : map.entrySet()) {
//                obj.addProperty(entry.getKey(), entry.getValue());
//            }
//        }
//
//// all 已经被改了，后面只要把 all 序列化/写文件就行
//
//    }



//    public static JsonArray Where(JsonArray a, JsonArray b, String mode) {
//        JsonArray array = new JsonArray();
//        if (mode.equals("and")) {
//            for (JsonElement e : a) {
//                if (b.contains(e)) {
//                    array.add(e);
//                }
//            }
//            return array;
//        } else if (mode.equals("or")) {
//            for (JsonElement e : a) {
//                array.add(e);
//            }
//            for (JsonElement e : b) {
//                if (!array.contains(e)) {
//                    array.add(e);
//                }
//            }
//            return array;
//        } else return array;
//    }

//    public static void Where(JsonArray data, Condition condition) {
//        Iterator<JsonElement> iterator = data.iterator();
//
//        while (iterator.hasNext()) {
//            JsonElement element = iterator.next();
//            JsonObject object = element.getAsJsonObject();
//
//            switch (condition.getOperator()) {
//                case "=":
//                    if (!object.get(condition.getColumn()).getAsString().equals(condition.getValue())) {
//                        iterator.remove(); // 使用 iterator.remove() 删除当前元素
//                    }
//                    break;
//                case "!=":
//                    if (object.get(condition.getColumn()).getAsString().equals(condition.getValue())) {
//                        iterator.remove(); // 使用 iterator.remove() 删除当前元素
//                    }
//                    break;
//                case "<":
//                    if (object.get(condition.getColumn()).getAsString().compareTo(condition.getValue()) >= 0) {
//                        iterator.remove(); // 使用 iterator.remove() 删除当前元素
//                    }
//                    break;
//                case ">":
//                    if (object.get(condition.getColumn()).getAsString().compareTo(condition.getValue()) <= 0) {
//                        iterator.remove(); // 使用 iterator.remove() 删除当前元素
//                    }
//                    break;
//                case "<=":
//                    if (object.get(condition.getColumn()).getAsString().compareTo(condition.getValue()) > 0) {
//                        iterator.remove(); // 使用 iterator.remove() 删除当前元素
//                    }
//                    break;
//                case ">=":
//                    if (object.get(condition.getColumn()).getAsString().compareTo(condition.getValue()) < 0) {
//                        iterator.remove(); // 使用 iterator.remove() 删除当前元素
//                    }
//                    break;
//            }
//        }
//
//    }


//    public static void deleteColumn(String tableName, String columnName) {
//        // 处理 schema
//        Path schemaPath = From_schema(tableName);
//        JsonObject schemaObj;
//        try (FileReader reader = new FileReader(schemaPath.toFile())) {
//            schemaObj = JsonParser.parseReader(reader).getAsJsonObject();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        JsonArray fields = schemaObj.getAsJsonArray("fields");
//        JsonArray newFields = new JsonArray();
//
//        for (JsonElement elem : fields) {
//            JsonObject field = elem.getAsJsonObject();
//            if (!field.get("fieldName").getAsString().equals(columnName)) {
//                newFields.add(field);
//            }
//        }
//
//        schemaObj.add("fields", newFields);
//
//        try (FileWriter writer = new FileWriter(schemaPath.toFile())) {
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            gson.toJson(schemaObj, writer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // 处理 data
//        Path dataPath = From_data(tableName);
//        JsonArray data = readData(dataPath);
//
//        for (JsonElement element : data) {
//            JsonObject obj = element.getAsJsonObject();
//            obj.remove(columnName);
//        }
//
//        try (FileWriter writer = new FileWriter(dataPath.toFile())) {
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            gson.toJson(data, writer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    // 更新内存并保存到磁盘


//    public static void saveData(String tableName, JsonArray data) {
//        Path dataPath = From_data(tableName);
//        try (FileWriter writer = new FileWriter(dataPath.toFile())) {
//            Gson gson = new GsonBuilder()
//                    .setPrettyPrinting()
//                    .serializeNulls()
//                    .create();
//            gson.toJson(data, writer);
//        } catch (IOException e) {
//            throw new RuntimeException("写入表 " + tableName + " 失败", e);
//        }
//    }


//    public static void Set(String tableName, HashMap<String,String> map, ConditionNode logicTree) {
//        // 1. 读出整表
//        JsonArray all = readData(From_data(tableName));
//
//        // 2. 逻辑过滤，返回的是 all 中的部分元素（原始引用）
//        JsonArray data = logicTree.evaluate(all);
//
//        // 3. 就地修改这些元素
//        for (JsonElement e : data) {
//            JsonObject obj = e.getAsJsonObject();
//            for (Map.Entry<String,String> entry : map.entrySet()) {
//                obj.addProperty(entry.getKey(), entry.getValue());
//            }
//        }
//
//        // 4. 最后将 all 整个写回文件
//        saveData(tableName, all);
//    }

//    public static void renameColumn(String tableName, String oldName, String newName) {
//        // 修改 schema
//        Path schemaPath = From_schema(tableName);
//        JsonObject schemaObj;
//        try (FileReader reader = new FileReader(schemaPath.toFile())) {
//            schemaObj = JsonParser.parseReader(reader).getAsJsonObject();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        JsonArray fields = schemaObj.getAsJsonArray("fields");
//        for (JsonElement elem : fields) {
//            JsonObject field = elem.getAsJsonObject();
//            if (field.get("fieldName").getAsString().equals(oldName)) {
//                field.addProperty("fieldName", newName);
//                break;
//            }
//        }
//
//        try (FileWriter writer = new FileWriter(schemaPath.toFile())) {
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            gson.toJson(schemaObj, writer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // 修改 data
//        Path dataPath = From_data(tableName);
//        JsonArray data = readData(dataPath);
//
//        for (JsonElement element : data) {
//            JsonObject obj = element.getAsJsonObject();
//            if (obj.has(oldName)) {
//                JsonElement value = obj.remove(oldName);
//                obj.add(newName, value);
//            }
//        }
//
//        try (FileWriter writer = new FileWriter(dataPath.toFile())) {
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            gson.toJson(data, writer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//

//    public static void Delete(JsonArray oldData, JsonArray newData) {
//        Iterator<JsonElement> iterator = newData.iterator();
//
//        while (iterator.hasNext()) {
//            JsonElement element = iterator.next();
//            if(oldData.contains(element)) oldData.remove(element);
//        }
//    }

//    public static Path From_data(String table) {
//        Path dataPath = Paths.get(DIRECTORY, DatabaseManager.getCurrentDatabase(), table + "_data.json");
//        return dataPath;
//    }
//
//    public static Path From_schema(String table) {
//        Path schemaPath = Paths.get(DIRECTORY, DatabaseManager.getCurrentDatabase(), table + "_schema.json");
//        return schemaPath;
//    }

    //    public static JsonArray readData(Path dataPath) {
//        JsonArray data;
//        try {
//            FileReader reader = new FileReader(dataPath.toFile());
//            data = JsonParser.parseReader(reader).getAsJsonArray();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        return data;
//    }

//    public static JsonArray readSchema(Path schemaPath) {
//        JsonArray fields;
//        try {
//            FileReader reader = new FileReader(schemaPath.toFile());
//            fields = JsonParser.parseReader(reader).getAsJsonArray();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        return fields;
//    }
//        //传个al的tup，根据键找到位置，针对每个tp，内层是root调get()

}


