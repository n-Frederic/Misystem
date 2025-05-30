package Table;

import java.util.HashMap;
import java.util.Map;

public class SchemaManager {
    private static SchemaManager instance = new SchemaManager();
    private Map<String, Schema> schemas = new HashMap<>();

    private SchemaManager() {}

    public static SchemaManager getInstance() {
        return instance;
    }

    public Schema get(String tableName) {
        return schemas.get(tableName);
    }

    public boolean contains(String tableName) {
        return schemas.containsKey(tableName);
    }
}
