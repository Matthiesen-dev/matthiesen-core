package dev.matthiesen.matthiesen_core.common.api.database;

import dev.matthiesen.matthiesen_core.common.api.database.dialect.IDatabaseDialect;
import dev.matthiesen.matthiesen_core.common.api.platform.LoggerMethods;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents a database interface that provides methods for managing database connections, executing SQL statements, and handling
 * database-specific operations. This interface defines the contract for interacting with different types of databases (e.g., MySQL, SQLite)
 * and allows for the execution of SQL tasks in a structured manner. Implementations of this interface should handle connection management,
 * error logging, and transaction control to ensure reliable and efficient database operations.
 */
@SuppressWarnings("unused")
public interface IDatabase {

    /**
     * Returns the logger associated with this database instance. The logger is used for logging messages, errors, and warnings
     * related to database operations. It provides a standardized way to capture and report issues that may arise during the execution of
     * SQL statements, connection management, and other database-related activities. Implementations of this interface should ensure that the
     * logger is properly initialized and configured to capture relevant information for debugging and maintenance purposes.
     * @return The LoggerMethods instance associated with this database, which can be used to log messages, errors, and warnings related to
     * database operations.
     */
    LoggerMethods getLogger();

    /**
     * Creates a connection to the database. This method is responsible for establishing a connection to the database using
     * the appropriate JDBC driver and connection string. It should handle any exceptions that may occur during the connection
     * process, such as issues with the database server, authentication failures, or network problems. The method should return
     * true if the connection is successfully established, and false if there are any errors. It is important to ensure that the
     * database server is running and accessible, and that the necessary credentials (username and password) are correct. Additionally,
     * proper logging should be implemented to capture any errors or warnings that occur during the connection process, which can aid
     * in debugging and maintenance of the database operations.
     * @return true if the connection to the database is successfully established, false otherwise. This return value allows the calling
     * code to determine whether the database connection was successful and take appropriate action based on the result.
     */
    boolean createConnection();

    /**
     * Creates a connection to a MySQL database. This method is responsible for establishing a connection to the MySQL database
     * using the appropriate JDBC driver and connection string. It should handle any exceptions that may occur during the connection
     * process, such as issues with the database server, authentication failures, or network problems. The method should return true
     * if the connection is successfully established, and false if there are any errors. It is important to ensure that the MySQL
     * server is running and accessible, and that the necessary credentials (username and password) are correct. Additionally, proper
     * logging should be implemented to capture any errors or warnings that occur during the connection process, which can aid in
     * debugging and maintenance of the database operations.
     * @return true if the connection to the MySQL database is successfully established, false otherwise. This return value allows
     * the calling code to determine whether the database connection was successful and take appropriate action based on the result.
     */
    boolean createMySqlConnection();

    /**
     * Creates a connection to a SQLite database. This method is responsible for establishing a connection to the SQLite database
     * using the appropriate JDBC driver and connection string. It should handle any exceptions that may occur during the connection
     * process, such as issues with the database file path or driver availability. The method should return true if the connection is
     * successfully established, and false if there are any errors. It is important to ensure that the SQLite database file exists and
     * is accessible, and that the necessary permissions are in place for the application to read from and write to the database file.
     * Additionally, proper logging should be implemented to capture any errors or warnings that occur during the connection process,
     * which can aid in debugging and maintenance of the database operations.
     * @return true if the connection to the SQLite database is successfully established, false otherwise. This return value allows the
     * calling code to determine whether the database connection was successful and take appropriate action based on the result.
     */
    boolean createSqliteConnection();

    /**
     * Creates a table in the database using the provided SQL statement. This method is responsible for executing the SQL
     * command that defines the structure of the table, including its columns, data types, and any constraints. It is important
     * to ensure that the SQL statement is valid and adheres to the syntax rules of the specific database dialect being used (e.g., MySQL, SQLite).
     * The method should handle any exceptions that may arise during the execution of the SQL statement, such as syntax errors or connection issues,
     * and provide appropriate logging or error handling mechanisms to aid in debugging and maintenance.
     * @param sql The SQL statement that defines the structure of the table to be created. This should be a valid SQL "CREATE TABLE" command
     *            that specifies the table name, columns, data types, and any constraints (e.g., primary keys, foreign keys, unique constraints).
     *            It is important to ensure that the SQL statement is compatible with the database dialect being used, as different databases may
     *            have variations in syntax and supported features.
     */
    void createTable(String sql);

    /**
     * Executes a SQL statement against the database. This method takes a SQL query as input and executes it using the database connection.
     * @param sql The SQL query to be executed. This should be a valid SQL statement that can be executed against the database.
     * @param logError A boolean flag indicating whether to log errors that occur during the execution of the SQL statement. If true,
     *                 any exceptions thrown during execution will be logged for debugging purposes. If false, errors will not be logged,
     *                 and the method will fail silently. It is recommended to set this flag to true during development and testing to help
     *                 identify issues with the SQL statements being executed.
     */
    void execute(String sql, boolean logError);

    /**
     * Prepares a SQL statement for execution. This method takes a SQL query as input and returns a PreparedStatement object
     * that can be used to execute the query against the database. The PreparedStatement allows for parameterized queries,
     * which can help prevent SQL injection attacks and improve performance by allowing the database to cache the execution
     * plan for the query. It is important to handle any SQLExceptions that may occur during the preparation of the statement,
     * as this can indicate issues with the SQL syntax or connection to the database.
     * @param query The SQL query to be prepared. This should be a valid SQL statement that can be executed against the database.
     *              It may contain placeholders for parameters, which can be set using the PreparedStatement's setter methods.
     * @return A PreparedStatement object that can be used to execute the provided SQL query. This object allows for setting parameters
     * and executing the query in a safe and efficient manner.
     * @throws SQLException If an error occurs while preparing the statement, such as a syntax error in the SQL query or a problem
     * with the database connection, an SQLException will be thrown. It is important to catch and handle this exception
     * appropriately to ensure the stability of the application.
     */
    PreparedStatement prepareStatement(String query) throws SQLException;

    /**
     * Executes a list of SQL tasks in a batch or individually based on the isBatch parameter. This method is responsible for managing
     * the execution of SQL statements and handling any exceptions that may occur during the process. It takes a list of items, which can
     * be either SqlTask instances or raw SQL strings, and executes them using the database connection. If isBatch is true, the tasks will
     * be executed in a batch; otherwise, they will be executed individually. The method also handles transaction management, committing the
     * changes if all tasks are executed successfully, or rolling back in case of an error. It is important to ensure that the database connection
     * is valid and open before calling this method, as it relies on the connection to execute the SQL tasks. Additionally, proper error logging
     * should be implemented to capture any issues that arise during the execution of the tasks, allowing for easier debugging and maintenance of
     * the database operations.
     * @param items A list of items to be executed. Each item can be either a SqlTask instance, which defines a specific SQL operation to be performed,
     *              or a raw SQL string that will be executed directly. The method will iterate through the list and execute each item accordingly.
     * @param isBatch A boolean flag indicating whether the tasks should be executed in a batch or individually. If true, the method will attempt to
     *                execute all tasks in a single batch operation, which can improve performance for large numbers of tasks. If false, each task will
     *                be executed one at a time, allowing for more granular control and error handling.
     */
    void executeQueue(List<Object> items, boolean isBatch);

    /**
     * Returns the database dialect associated with this database instance. The dialect defines the specific SQL syntax and behavior
     * that is used by the underlying database system (e.g., MySQL, SQLite).
     * @return The IDatabaseDialect implementation that corresponds to the database type being used. This allows for database-specific
     *         operations and queries to be executed correctly according to the rules of the specific database system.
     */
    IDatabaseDialect getDialect();

    /**
     * Closes the database connection and releases any associated resources. This method should be called when the database operations
     * are complete and the connection is no longer needed. It is important to ensure that all pending transactions are committed or rolled
     * back before closing the connection to avoid data loss or corruption. Additionally, proper error handling should be implemented to catch
     * any exceptions that may occur during the closing process, such as issues with the underlying database driver or network problems. Closing
     * the connection properly helps to free up resources and maintain the stability and performance of the application.
     */
    void close();
}
