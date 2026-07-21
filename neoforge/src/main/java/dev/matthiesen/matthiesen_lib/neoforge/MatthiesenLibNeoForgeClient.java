package dev.matthiesen.matthiesen_lib.neoforge;

import dev.matthiesen.matthiesen_lib.common.MatthiesenLibCommon;
import dev.matthiesen.matthiesen_lib.common.MatthiesenLibCommonClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = MatthiesenLibCommon.MOD_ID, dist = Dist.CLIENT)
public final class MatthiesenLibNeoForgeClient {
    public static MatthiesenLibCommonClient INSTANCE;

    public MatthiesenLibNeoForgeClient() {
        INSTANCE = MatthiesenLibCommonClient.INSTANCE;
        INSTANCE.createInfoLog("Loading for NeoForge Mod Loader (Client)");
        INSTANCE.initialize();
    }
}
