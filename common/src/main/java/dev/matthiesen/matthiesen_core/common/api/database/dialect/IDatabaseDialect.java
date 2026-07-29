package dev.matthiesen.matthiesen_core.common.api.database.dialect;

/**
 * This interface defines the contract for database dialects, which are responsible for providing SQL statements and data
 * type mappings specific to a particular database system (e.g., MySQL, SQLite, PostgreSQL). Implementations of this interface
 * should provide the necessary SQL syntax and data type conversions to ensure compatibility with the target database.
 */
@SuppressWarnings("unused")
public interface IDatabaseDialect {

    /**
     * Returns the SQL statement for inserting a record while ignoring any conflicts that may arise due to unique constraints.
     * @return The SQL statement that represents the "insert ignore" behavior. This is typically used in scenarios where you
     * want to insert a record into a table but avoid raising an error if a record with the same unique key already exists.
     * Instead of throwing an error, the database will simply ignore the insert operation for that record.
     */
    String getInsertIgnore();

    /**
     * Returns the SQL statement for handling conflicts by updating the specified column with the provided update value when
     * a conflict occurs on the specified key.
     * @param key The column name that is expected to have a unique constraint. If an insert operation would violate this constraint,
     *            the database will update the specified column with the provided update value instead of throwing an error.
     * @param update The value to update the specified column with in case of a conflict. This value is typically a SQL expression
     *               or a literal value that will be used to update the column.
     * @return The SQL statement that represents the "update" behavior for conflict resolution. This is typically used in scenarios
     * where you want to avoid duplicate entries in a table by updating existing records instead of raising an error.
     */
    String getOnConflictUpdate(String key, String update);

    /**
     * Returns the SQL statement for handling conflicts by doing nothing when a conflict occurs on the specified key.
     * @param key The column name that is expected to have a unique constraint. If an insert operation would violate this constraint,
     *            the database will ignore the insert operation instead of throwing an error.
     * @return The SQL statement that represents the "do nothing" behavior for conflict resolution. This is typically used in scenarios
     * where you want to avoid duplicate entries in a table without raising an error.
     */
    String getOnConflictDoNothing(String key);

    /**
     * Returns the appropriate data type for the given type based on the database dialect.
     * @param type The data type to be converted. This is typically a generic type like "integer", "bigint", "text", or "varchar".
     * @return The corresponding data type for the specific database dialect. For example, "integer" might be converted to "int" for
     *         MySQL or "integer" for SQLite. If the type is not recognized, it will return the original type.
     */
    String getDataType(String type);
}
