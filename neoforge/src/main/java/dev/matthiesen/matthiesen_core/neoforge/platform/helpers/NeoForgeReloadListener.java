package dev.matthiesen.matthiesen_core.neoforge.platform.helpers;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

public final class NeoForgeReloadListener extends SimplePreparableReloadListener<Void> {
    private final Runnable runnable;

    public NeoForgeReloadListener(Runnable runnable) {
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
