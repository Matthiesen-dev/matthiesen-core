package dev.matthiesen.matthiesen_core.common.core.database.dialect;

import dev.matthiesen.matthiesen_core.common.api.database.dialect.IDatabaseDialect;

/**
 * The SQLiteDialect class implements the IDatabaseDialect interface and provides SQLite-specific SQL syntax and data type mappings.
 * It defines how to handle insert operations, conflict resolution, and data type conversions specific to SQLite. This class
 * is used to ensure that SQL statements are compatible with SQLite databases, allowing for seamless integration and
 * interaction with SQLite database systems.
 */
public final class SQLiteDialect implements IDatabaseDialect {

    /**
     * Constructs a new instance of the SQLiteDialect class. This class provides the necessary SQL syntax and data type mappings specific to SQLite databases.
     */
    public SQLiteDialect() {}

    @Override
    public String getInsertIgnore() {
        return "INSERT OR IGNORE";
    }

    @Override
    public String getOnConflictUpdate(String key, String update) {
        return "ON CONFLICT(" + key + ") DO UPDATE SET " + update;
    }

    @Override
    public String getOnConflictDoNothing(String key) {
        return "ON CONFLICT(" + key + ") DO NOTHING";
    }

    @Override
    public String getDataType(String type) {
        return switch (type) {
            case "integer", "bigint" -> "integer";
            case "text", "varchar" -> "text";
            default -> type;
        };
    }
}
