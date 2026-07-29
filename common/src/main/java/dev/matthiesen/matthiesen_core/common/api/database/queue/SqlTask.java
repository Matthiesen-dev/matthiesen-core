package dev.matthiesen.matthiesen_core.common.api.database.queue;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents a task that can be executed using a SQL database connection. This functional interface allows for the
 * execution of SQL operations within a provided database connection context.
 */
@FunctionalInterface
public interface SqlTask {

    /**
     * Executes the SQL task using the provided database connection.
     * @param connection The database connection to be used for executing the task.
     * @throws SQLException If an SQL error occurs during the execution of the task.
     */
    void execute(Connection connection) throws SQLException;
}
