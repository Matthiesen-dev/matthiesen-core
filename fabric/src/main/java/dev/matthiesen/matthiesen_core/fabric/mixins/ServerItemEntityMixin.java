package dev.matthiesen.matthiesen_core.fabric.mixins;

import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import dev.matthiesen.matthiesen_core.common.api.events.server.PlayerEvent;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ServerItemEntityMixin {
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
