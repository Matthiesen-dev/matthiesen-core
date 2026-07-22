package dev.matthiesen.matthiesen_core.neoforge.platform.helpers;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

/**
 * The NeoForgeReloadListener class extends the SimplePreparableReloadListener class and provides a mechanism to execute a
 * specified Runnable when resources are reloaded in the NeoForge mod loader environment. It is used to perform custom actions
 * during the resource reload process, allowing for dynamic updates or adjustments based on the new resource state.
 */
public final class NeoForgeReloadListener extends SimplePreparableReloadListener<Void> {
    private final Runnable runnable;

    /**
     * Constructs a new NeoForgeReloadListener with the specified Runnable to be executed upon resource reload.
     * @param runnable The Runnable to be executed when resources are reloaded. This allows for custom actions to be performed during the reload process.
     *                 If null, no action will be taken during the reload.
     */
    public NeoForgeReloadListener(Runnable runnable) {
        this.runnable = runnable;
    }

    /**
     * Prepares the reload listener by performing any necessary setup before the actual resource reload occurs. This method is
     * called before the apply method and can be used to gather or process data needed for the reload. In this implementation,
     * it returns null as no preparation is needed.
     * @param arg The ResourceManager instance that provides access to the game's resources, allowing for resource management
     *            and retrieval during the preparation phase.
     * @param arg2 The ProfilerFiller instance used for profiling and performance monitoring during the preparation phase, allowing
     *             for tracking of resource loading and processing times.
     * @return Returns null as no preparation is needed for this reload listener. This indicates that there is no specific data or
     * state to be passed to the apply method.
     */
    @Override
    protected @NotNull Void prepare(@NotNull ResourceManager arg, @NotNull ProfilerFiller arg2) {
        return null;
    }

    /**
     * Applies the reload listener by executing the specified Runnable when resources are reloaded. This method is called after the
     * prepare method and is responsible for performing the actual actions needed during the resource reload process. If a Runnable
     * was provided during construction, it will be executed at this point.
     * @param object The object returned from the prepare method, which in this case is null. This parameter is not used in this
     *               implementation but is part of the method signature for compatibility with the SimplePreparableReloadListener class.
     * @param arg The ResourceManager instance that provides access to the game's resources, allowing for resource management and
     *            retrieval during the apply phase.
     * @param arg2 The ProfilerFiller instance used for profiling and performance monitoring during the apply phase, allowing for
     *             tracking of resource loading and processing times.
     */
    @Override
    protected void apply(@NotNull Void object, @NotNull ResourceManager arg, @NotNull ProfilerFiller arg2) {
        if (runnable != null) {
            runnable.run();
        }
    }
}
