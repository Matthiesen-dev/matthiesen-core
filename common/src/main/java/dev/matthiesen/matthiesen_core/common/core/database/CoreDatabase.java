package dev.matthiesen.matthiesen_core.common.core.database;

import dev.matthiesen.matthiesen_core.common.api.database.IDatabase;
import dev.matthiesen.matthiesen_core.common.api.database.config.DatabaseConfig;
import dev.matthiesen.matthiesen_core.common.api.database.dialect.IDatabaseDialect;
import dev.matthiesen.matthiesen_core.common.api.database.queue.IQueue;
import dev.matthiesen.matthiesen_core.common.api.database.queue.SqlTask;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.core.database.dialect.MySQLDialect;
import dev.matthiesen.matthiesen_core.common.core.database.dialect.SQLiteDialect;
import dev.matthiesen.matthiesen_core.common.core.database.queue.Queue;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.sql.*;
import java.util.List;

/**
 * The CoreDatabase class is an implementation of the IDatabase interface that provides functionality for managing database
 * connections and executing SQL statements. It supports both MySQL and SQLite databases, allowing for flexible configuration
 * based on the provided DatabaseConfig. The class handles connection creation, statement execution, and queue management for
 * SQL tasks, ensuring thread-safe operations through synchronization.
 */
@SuppressWarnings("unused")
public final class CoreDatabase implements IDatabase {

    @Nullable
    private Connection connection;
    private IDatabaseDialect dialect;

    private final Object lock = new Object();
    private final DatabaseConfig config;
    private final String MOD_ID;

    /**
     * The queue is a thread-safe queue that allows for the execution of SQL tasks in individual mode. It is used to manage and
     * execute SQL tasks one at a time, ensuring that each task is completed before the next one begins. The queue is initialized
     * in the constructor and is associated with the CoreDatabase instance, ensuring that all queued tasks are executed within the
     * context of the same database connection. This queue is particularly useful for scenarios where tasks need to be executed in a
     * specific order or when the execution of one task depends on the completion of another.
     */
    public final IQueue queue;

    /**
     * The batchQueue is a thread-safe queue that allows for the execution of SQL tasks in batch mode. It is used to manage and execute
     * multiple SQL tasks together, improving performance and efficiency when dealing with large numbers of database operations. The batchQueue
     * is initialized in the constructor and is associated with the CoreDatabase instance, ensuring that all queued tasks are executed within
     * the context of the same database connection.
     */
    public final IQueue batchQueue;

    /**
     * Constructs a new instance of the CoreDatabase class with the specified mod ID and database configuration. This constructor
     * initializes the database queues for both individual and batch execution modes.
     * @param MOD_ID The mod ID associated with this database instance. This is typically used for logging and configuration purposes.
     * @param config The database configuration object containing settings for connecting to the database, such as connection
     *               details and options for MySQL or SQLite.
     */
    public CoreDatabase(String MOD_ID, DatabaseConfig config) {
        this.MOD_ID = MOD_ID;
        this.config = config;
        queue = new Queue(this, false);
        batchQueue = new Queue(this, true);
    }

    @Override
    public boolean createConnection() {
        boolean connected;
        if (config.useMySQL) {
            connected = createMySqlConnection();
            dialect = new MySQLDialect();
        } else {
            connected = createSqliteConnection();
            dialect = new SQLiteDialect();
        }
        if (connection != null) {
            MatthiesenCoreCommon.INSTANCE.createInfoLog("Database connection established");
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to set auto commit to false", e);
                return false;
            }
        }
        return connected && connection != null;
    }

    @Override
    public boolean createMySqlConnection() {
        String host = config.mySQLConfig.host;
        int port = config.mySQLConfig.port;
        String database = config.mySQLConfig.database;
        String user = config.mySQLConfig.username;
        String password = config.mySQLConfig.password;
        int timeout = config.mySQLConfig.timeout;

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?allowReconnect=true&autoReconnect=true&connectTimeout=" + timeout;

        if (!loadJdbcDriver("MySQL", "dev.matthiesen.matthiesen_core.shadow.com.mysql.cj.jdbc.Driver", "com.mysql.cj.jdbc.Driver")) {
            return false;
        }

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to create MySQL connection", e);
            return false;
        }

        return connection != null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public boolean createSqliteConnection() {
        if (!loadJdbcDriver("SQLite", "dev.matthiesen.matthiesen_core.shadow.org.sqlite.JDBC", "org.sqlite.JDBC")) {
            return false;
        }

        Path gameDir = MatthiesenCoreCommon.INSTANCE.getCommonUtils().getGameDirectory();
        Path configDir = gameDir.resolve("config/" + MOD_ID);

        if (!configDir.toFile().exists()) {
            configDir.toFile().mkdirs();
        }

        Path dbLocation = configDir.resolve(config.sqLiteConfig.fileName);

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);
        } catch (SQLException e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to create SQLite connection", e);
            return false;
        }

        return connection != null;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean loadJdbcDriver(String dbType, String... classNames) {
        ClassNotFoundException lastException = null;
        for (String className : classNames) {
            try {
                Class.forName(className);
                return true;
            } catch (ClassNotFoundException e) {
                lastException = e;
            }
        }
        MatthiesenCoreCommon.INSTANCE.createErrorLog(dbType + " JDBC Driver not found. Tried: " + String.join(", ", classNames), lastException);
        return false;
    }

    @Override
    public void createTable(String sql) {
        execute(sql, true);
    }

    @Override
    public void execute(String sql, boolean logError) {
        if (connection == null) return;

        synchronized (lock) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
                connection.commit();
            } catch (SQLException e) {
                if (logError) {
                    MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to execute statement", e);
                }
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to rollback", ex);
                }
            }
        }
    }

    @Override
    public PreparedStatement prepareStatement(String query) throws SQLException {
        if (connection != null) {
            return connection.prepareStatement(query);
        } else {
            throw new SQLException("Connection is null");
        }
    }

    @Override
    public void executeQueue(List<Object> items, boolean isBatch) {
        if (connection == null) return;

        synchronized (lock) {
            try {
                for (Object item : items) {
                    if (item instanceof PreparedStatement preparedStatement) {
                        if (preparedStatement.isClosed()) {
                            continue;
                        }
                        try (preparedStatement) {
                            if (isBatch) {
                                preparedStatement.executeBatch();
                            } else {
                                preparedStatement.executeUpdate();
                            }
                        }
                    } else if (item instanceof SqlTask task) {
                        task.execute(connection);
                    }
                }
                if (!items.isEmpty()) {
                    if (connection != null && !connection.isClosed()) {
                        connection.commit();
                    }
                }
            } catch (SQLException e) {
                MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to execute database queue", e);
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.rollback();
                    }
                } catch (SQLException ex) {
                    MatthiesenCoreCommon.INSTANCE.createErrorLog("Failed to rollback transaction", ex);
                }
            }
        }
    }

    @Override
    public IDatabaseDialect getDialect() {
        return dialect;
    }
}
