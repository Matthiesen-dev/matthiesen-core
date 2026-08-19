/**
 * Inspired and adapted from the Particle API in MonkeyLib538 by OffsetMonkey538.
 */
package dev.matthiesen.matthiesen_core.common.api.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

/**
 * Functional interface for spawning particles in a Minecraft world.
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface ParticleSpawner {
    /**
     * Creates a ParticleSpawner that spawns particles using the provided ClientLevel and ParticleOptions.
     *
     * @param level    The ClientLevel where the particles will be spawned.
     * @param particle The ParticleOptions for the particles to be spawned.
     * @return A ParticleSpawner that spawns particles in the specified ClientLevel using the provided ParticleOptions.
     */
    static ParticleSpawner of(final ClientLevel level, final ParticleOptions particle) {
        return (x, y, z, xd, yd, zd) -> level.addParticle(particle, x, y, z, xd, yd, zd);
    }

    /**
     * Creates a ParticleSpawner that spawns particles using the provided ClientLevel and a Supplier for ParticleOptions.
     *
     * @param level            The ClientLevel where the particles will be spawned.
     * @param particleSupplier A Supplier that provides the ParticleOptions for the particles to be spawned.
     * @return A ParticleSpawner that spawns particles in the specified ClientLevel using the provided ParticleOptions.
     */
    static ParticleSpawner of(final ClientLevel level, final Supplier<ParticleOptions> particleSupplier) {
        return (x, y, z, xd, yd, zd) -> level.addParticle(particleSupplier.get(), x, y, z, xd, yd, zd);
    }

    /**
     * Spawns a particle at the specified position with no delta (velocity).
     *
     * @param position The position of the particle.
     */
    default void spawnParticle(final Vec3 position) {
        spawnParticle(position.x, position.y, position.z);
    }

    /**
     * Spawns a particle at the specified position with no delta (velocity).
     *
     * @param x The x-coordinate of the particle's position.
     * @param y The y-coordinate of the particle's position.
     * @param z The z-coordinate of the particle's position.
     */
    default void spawnParticle(final double x, final double y, final double z) {
        spawnParticle(x, y, z, x, y, z);
    }

    /**
     * Spawns a particle at the specified position with the specified delta values.
     *
     * @param position The position of the particle.
     * @param delta    The delta (velocity) of the particle.
     */
    default void spawnParticle(final Vec3 position, final Vec3 delta) {
        spawnParticle(position.x, position.y, position.z, delta.x, delta.y, delta.z);
    }

    /**
     * Spawns a particle at the specified position with the specified delta values.
     *
     * @param x  The x-coordinate of the particle's position.
     * @param y  The y-coordinate of the particle's position.
     * @param z  The z-coordinate of the particle's position.
     * @param xd The x-component of the particle's delta (velocity).
     * @param yd The y-component of the particle's delta (velocity).
     * @param zd The z-component of the particle's delta (velocity).
     */
    void spawnParticle(final double x, final double y, final double z, final double xd, final double yd, final double zd);
}
