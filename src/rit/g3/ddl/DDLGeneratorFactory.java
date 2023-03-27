package rit.g3.ddl;

import java.util.HashMap;
import java.util.Map;

public class DDLGeneratorFactory {

    private static final Map<String, CreateDDL> ddl;

    private DDLGeneratorFactory() {
    }

    static {
        ddl = new HashMap();
        ddl.put("0", new CreateDDLMySQL());
        ddl.put("1", new CreateDDLOracle());
    }

    public static CreateDDL get(String type) {
        return ddl.get(type);
    }
}
