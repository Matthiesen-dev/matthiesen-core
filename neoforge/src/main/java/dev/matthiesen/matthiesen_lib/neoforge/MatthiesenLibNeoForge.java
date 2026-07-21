package dev.matthiesen.matthiesen_lib.neoforge;

import dev.matthiesen.matthiesen_lib.common.MatthiesenLibCommon;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(MatthiesenLibCommon.MOD_ID)
public final class MatthiesenLibNeoForge {
    public static MatthiesenLibCommon INSTANCE;
    public static volatile MinecraftServer SERVER_INSTANCE;

    public MatthiesenLibNeoForge(IEventBus modBus) {
        INSTANCE = MatthiesenLibCommon.INSTANCE;
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader");

        MatthiesenLibNeoForgeRegistry.init(modBus);

        INSTANCE.initialize();
    }
}
