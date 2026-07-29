package dev.matthiesen.matthiesen_core.common.api.database.config;

import com.google.gson.annotations.SerializedName;

/**
 * Configuration class for database settings. This class holds the configuration options for both MySQL and SQLite databases.
 * It allows the user to specify whether to use MySQL or SQLite, and provides the necessary configuration options for each database type.
 */
public final class DatabaseConfig {

    /**
     * If true, the mod will use a MySQL database instead of SQLite. This requires a MySQL server to be running and accessible.
     */
    @SerializedName("useMySQL")
    public boolean useMySQL = false;

    /**
     * Config options when using MySQL. These options will be ignored if useMySQL is false.
     */
    @SerializedName("mySQLConfig")
    public MySQLConfig mySQLConfig = new MySQLConfig();

    /**
     * Config options when using SQLite. These options will be ignored if useMySQL is true.
     */
    @SerializedName("sqLiteConfig")
    public SQLiteConfig sqLiteConfig = new SQLiteConfig();

    /**
     * Config options for MySQL database. These options will be ignored if useMySQL is false.
     */
    public static class MySQLConfig {

        /**
         * The hostname or IP address of the MySQL server. This value is used to establish a connection to the MySQL server.
         * The default value is "localhost", which refers to the local machine. If the MySQL server is running on a different
         * machine, this value should be set to the appropriate hostname or IP address.
         */
        @SerializedName("host")
        public String host = "localhost";

        /**
         * The port number for the MySQL server. This value is used to establish a connection to the MySQL server.
         * The default value is 3306, which is the standard port for MySQL.
         */
        @SerializedName("port")
        public int port = 3306;

        /**
         * The name of the MySQL database to connect to. This value is used to specify which database to use on the MySQL server.
         * The default value is "database". It is recommended to change this value to a unique database name for your application.
         */
        @SerializedName("database")
        public String database = "database";

        /**
         * The username for the MySQL database user. This value is used to authenticate the user when establishing a connection to the MySQL server.
         * The default value is "root". It is recommended to change this value to a secure username for production environments.
         */
        @SerializedName("username")
        public String username = "root";

        /**
         * The password for the MySQL database user. This value is used to authenticate the user when establishing a connection to the MySQL server.
         * The default value is "password". It is recommended to change this value to a secure password for production environments.
         */
        @SerializedName("password")
        public String password = "password";

        /**
         * The timeout in milliseconds for the MySQL connection. This value is used to set the connection timeout when establishing a connection to the MySQL server.
         * The default value is 5000 milliseconds (5 seconds).
         */
        @SerializedName("timeout")
        public int timeout = 5000;
    }

    /**
     * Config options for SQLite database. These options will be ignored if useMySQL is true.
     */
    public static class SQLiteConfig {

        /**
         * The name of the SQLite database file. This file will be created in the mod's configuration directory.
         * The default value is "database.db".
         */
        @SerializedName("fileName")
        public String fileName = "database.db";
    }
}
