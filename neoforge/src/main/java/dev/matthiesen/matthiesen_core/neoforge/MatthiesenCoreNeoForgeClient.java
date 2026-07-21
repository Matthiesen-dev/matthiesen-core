package dev.matthiesen.matthiesen_core.neoforge;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommonClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = MatthiesenCoreCommon.MOD_ID, dist = Dist.CLIENT)
public final class MatthiesenCoreNeoForgeClient {
    public static MatthiesenCoreCommonClient INSTANCE;

    public MatthiesenCoreNeoForgeClient() {
        INSTANCE = MatthiesenCoreCommonClient.INSTANCE;
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader (Client)");
        INSTANCE.initialize();
    }
}
