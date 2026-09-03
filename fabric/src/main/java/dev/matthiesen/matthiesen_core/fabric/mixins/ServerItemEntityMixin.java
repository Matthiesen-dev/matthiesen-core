package dev.matthiesen.matthiesen_core.fabric.mixins;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.events.server.PlayerEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The ServerItemEntityMixin class is a Mixin that injects custom behavior into the ItemEntity class in Minecraft.
 * It listens for player interactions with item entities and emits corresponding events to the PlatformEvents event bus.
 */
@Mixin(ItemEntity.class)
public class ServerItemEntityMixin {

    /**
     * Injects custom behavior at the start of the playerTouch method in the ItemEntity class.
     * Emits a PlayerEvent.PickupItem event to the PlatformEvents event bus when a player interacts with an item entity.
     *
     * @param player The player interacting with the item entity.
     * @param ci The CallbackInfo object provided by the Mixin framework.
     */
    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void onPlayerPickUp(Player player, CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) (Object) this;
        if (entity.level().isClientSide) return;
        if (!(player instanceof net.minecraft.server.level.ServerPlayer serverPlayer)) return;
        boolean result = PlatformEvents.PLAYER_PICKUP_ITEM.emit(new PlayerEvent.PickupItem(serverPlayer, entity));
        if (result) {
            ci.cancel();
        }
    }
}
