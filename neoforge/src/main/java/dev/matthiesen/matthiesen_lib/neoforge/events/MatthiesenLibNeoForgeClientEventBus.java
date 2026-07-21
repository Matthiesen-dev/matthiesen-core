package dev.matthiesen.matthiesen_lib.neoforge.events;

import dev.matthiesen.matthiesen_lib.common.MatthiesenLibCommon;
import dev.matthiesen.matthiesen_lib.neoforge.MatthiesenLibNeoForgeClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = MatthiesenLibCommon.MOD_ID, value = Dist.CLIENT)
public final class MatthiesenLibNeoForgeClientEventBus {
    public MatthiesenLibNeoForgeClientEventBus() {}

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void clientSetup(FMLClientSetupEvent event) {
        MatthiesenLibNeoForgeClient.INSTANCE.onClientSetup();
    }
}
