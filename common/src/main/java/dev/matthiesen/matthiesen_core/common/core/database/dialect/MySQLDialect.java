package dev.matthiesen.matthiesen_core.common.core.database.dialect;

import dev.matthiesen.matthiesen_core.common.api.database.dialect.IDatabaseDialect;

/**
 * The MySQLDialect class implements the IDatabaseDialect interface and provides MySQL-specific SQL syntax for various database operations.
 * It defines how to handle insert operations, conflict resolution, and data type conversions specific to MySQL. This class
 * is used to ensure that SQL statements are compatible with MySQL databases, allowing for seamless integration and
 * interaction with MySQL database systems.
 */
public final class MySQLDialect implements IDatabaseDialect {

    /**
     * Constructs a new instance of the MySQLDialect class. This constructor initializes the MySQLDialect object, allowing it to
     * be used for generating MySQL-specific SQL statements and data type mappings. The MySQLDialect class is designed to be used in
     * conjunction with database repositories and other components that require MySQL compatibility.
     */
    public MySQLDialect() {}

    @Override
    public String getInsertIgnore() {
        return "INSERT IGNORE";
    }

    @Override
    public String getOnConflictUpdate(String key, String update) {
        return "ON DUPLICATE KEY UPDATE " + update;
    }

    @Override
    public String getOnConflictDoNothing(String key) {
        return "ON DUPLICATE KEY UPDATE " + key + " = " + key;
    }

    @Override
    public String getDataType(String type) {
        return switch (type) {
            case "integer" -> "int";
            case "bigint" -> "bigint";
            case "text" -> "text";
            case "varchar" -> "varchar";
            default -> type;
        };
    }
}