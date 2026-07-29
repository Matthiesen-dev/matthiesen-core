package dev.matthiesen.matthiesen_core.common.core.database.queue;

import dev.matthiesen.matthiesen_core.common.api.database.IDatabase;
import dev.matthiesen.matthiesen_core.common.api.database.queue.IQueue;
import dev.matthiesen.matthiesen_core.common.api.database.queue.SqlTask;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The Queue class implements the IQueue interface and provides a thread-safe mechanism for managing and executing SQL tasks
 * in a queue. It allows for the addition of SQL tasks, execution of queued tasks, and checking if the queue is empty. The
 * class is designed to work with a specified database instance and supports both batch and individual execution modes.
 */
public final class Queue implements IQueue {

    private final IDatabase database;
    private final boolean isBatch;
    private final ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();

    /**
     * Creates a new Queue instance associated with the specified database and batch mode.
     * @param database The database instance to which this queue is associated. This database will be used to execute the queued SQL tasks.
     * @param isBatch A boolean flag indicating whether the queued tasks should be executed in batch mode. If true, tasks will
     *                be executed as a batch; if false, they will be executed individually.
     */
    public Queue(IDatabase database, boolean isBatch) {
        this.database = database;
        this.isBatch = isBatch;
    }

    @Override
    public void add(SqlTask task) {
        this.queue.add(task);
    }

    @Override
    public void execute() {
        if (this.queue.isEmpty()) {
            return;
        }
        List<Object> items = new ArrayList<>();
        Object item;
        while ((item = this.queue.poll()) != null) {
            items.add(item);
        }
        this.database.executeQueue(items, isBatch);
    }

    @Override
    public void hello() {
        this.add(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("SELECT 1")) {
                statement.execute();
            } catch (Exception e) {
                this.database.getLogger().createErrorLog("Failed to send hello packet", e);
            }
        });
    }

    @Override
    public boolean isEmpty() {
        return this.queue.isEmpty();
    }
}
