package dev.matthiesen.matthiesen_core.neoforge.events;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

public final class RunnableReloadListener extends SimplePreparableReloadListener<Void> {
    private final Runnable runnable;

    public RunnableReloadListener(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    protected @NotNull Void prepare(@NotNull ResourceManager arg, @NotNull ProfilerFiller arg2) {
        return null;
    }

    @Override
    protected void apply(@NotNull Void object, @NotNull ResourceManager arg, @NotNull ProfilerFiller arg2) {
        if (runnable != null) {
            runnable.run();
        }
    }
}
