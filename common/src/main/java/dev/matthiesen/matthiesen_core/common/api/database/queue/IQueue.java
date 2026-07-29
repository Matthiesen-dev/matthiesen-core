package dev.matthiesen.matthiesen_core.common.api.database.queue;

/**
 * This interface defines the contract for a queue that manages SQL tasks. It allows adding tasks to the queue, executing
 * them, and checking if the queue is empty. The tasks are represented by the SqlTask interface, which defines a method for
 * executing SQL operations using a database connection.
 */
@SuppressWarnings("unused")
public interface IQueue {

    /**
     * Adds a new SQL task to the queue. The task will be executed when the execute() method is called. This method does not
     * execute the task immediately; it only adds it to the queue for later execution.
     * @param task The SQL task to be added to the queue. This task should implement the SqlTask interface and define the execute
     *             method, which contains the SQL operations to be performed.
     */
    void add(SqlTask task);

    /**
     * Executes all tasks in the queue. This method will process each task in the order they were added to the queue. If a task
     * throws an exception, it will be propagated to the caller, and subsequent tasks may not be executed.
     */
    void execute();

    /**
     * A simple method to test the queue functionality. This method can be used to verify that the queue is operational and
     * can accept tasks.
     */
    void hello();

    /**
     * Checks if the queue is empty.
     * @return true if the queue is empty, false otherwise.
     */
    boolean isEmpty();
}