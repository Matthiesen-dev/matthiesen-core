package dev.matthiesen.matthiesen_core.neoforge.events;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import dev.matthiesen.matthiesen_core.neoforge.MatthiesenCoreNeoForgeClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = MatthiesenCoreCommon.MOD_ID, value = Dist.CLIENT)
public final class NeoForgeClientEvents {
    public NeoForgeClientEvents() {}

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void clientSetup(FMLClientSetupEvent event) {
        MatthiesenCoreNeoForgeClient.INSTANCE.onClientSetup();
    }
}
