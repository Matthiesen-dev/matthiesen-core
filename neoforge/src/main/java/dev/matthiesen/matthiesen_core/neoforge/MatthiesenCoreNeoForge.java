package dev.matthiesen.matthiesen_core.neoforge;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.neoforge.utility.MatthiesenCoreNeoForgeRegistry;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(MatthiesenCoreCommon.MOD_ID)
public final class MatthiesenCoreNeoForge {
    public static MatthiesenCoreCommon INSTANCE;
    public static volatile MinecraftServer SERVER_INSTANCE;

    public MatthiesenCoreNeoForge(IEventBus modBus) {
        INSTANCE = MatthiesenCoreCommon.INSTANCE;
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader");
        MatthiesenCoreNeoForgeRegistry.init(modBus);
        INSTANCE.initialize();
    }
}
