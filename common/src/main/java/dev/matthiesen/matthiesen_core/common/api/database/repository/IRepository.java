package dev.matthiesen.matthiesen_core.common.api.database.repository;

/**
 * This interface defines the contract for a repository that interacts with a database. Implementing classes are responsible
 * for providing the necessary logic to create the required database tables and manage data persistence. The createTable method
 * should be implemented to define the schema and structure of the database table associated with the repository.
 */
@SuppressWarnings("unused")
public interface IRepository {

    /**
     * Creates the necessary table in the database for the implementing repository. This method should be called during the initialization
     * phase of the application to ensure that the required database schema is set up before any data operations are performed.
     */
    void createTable();
}
