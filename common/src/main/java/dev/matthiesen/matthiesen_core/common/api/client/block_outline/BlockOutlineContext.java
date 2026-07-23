package dev.matthiesen.matthiesen_core.common.api.client.block_outline;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Platform-neutral context passed to block outline highlight listeners.
 *
 * @param level             The current client level.
 * @param blockHitResult    The targeted block hit result.
 * @param poseStack         The active pose stack.
 * @param camera            The active camera.
 * @param multiBufferSource The active render buffer source.
 */
public record BlockOutlineContext(
        ClientLevel level,
        BlockHitResult blockHitResult,
        PoseStack poseStack,
        Camera camera,
        MultiBufferSource multiBufferSource
) {
}

