package dev.matthiesen.matthiesen_core.common.core.network;

import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

/**
 * Context for a network packet, providing access to the player and a way to enqueue work on the main thread.
 * @param player The player associated with the packet context.
 * @param queueTask A supplier that enqueues a task to be executed on the main thread and returns a result.
 */
@SuppressWarnings("unused")
public record PacketContext(Player player, Supplier<Void> queueTask) {

    /**
     * Enqueues a task to be executed on the main thread and returns a result.
     * @param runnable The task to be executed on the main thread.
     */
    public void enqueue(Runnable runnable) {
        queueTask.get(); // Platform implementations handle thread context execution
        runnable.run();
    }
}
