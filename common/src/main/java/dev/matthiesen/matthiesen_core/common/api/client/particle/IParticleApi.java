/**
 * Inspired and adapted from the Particle API in MonkeyLib538 by OffsetMonkey538.
 */
package dev.matthiesen.matthiesen_core.common.api.client.particle;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Interface for particle spawning APIs.
 */
@SuppressWarnings("unused")
public interface IParticleApi {

    /**
     * Spawns particles along a line defined by two points in 3D space.
     *
     * @param spawner The particle spawner to use for spawning particles.
     * @param start The starting point of the line.
     * @param end The ending point of the line.
     * @param step The distance between each particle along the line.
     */
    void spawnLine(final ParticleSpawner spawner, final Vec3 start, final Vec3 end, final double step);

    /**
     * Spawns particles along the edges of a box defined by an axis-aligned bounding box (AABB).
     *
     * @param spawner The particle spawner to use for spawning particles.
     * @param box The axis-aligned bounding box defining the edges of the box.
     * @param step The distance between each particle along the edges of the box.
     */
    void spawnBoxEdges(final ParticleSpawner spawner, final AABB box, final double step);

    /**
     * Spawns particles in a spherical shape.
     *
     * @param spawner The particle spawner to use for spawning particles.
     * @param center The center of the sphere.
     * @param radius The radius of the sphere.
     * @param step The distance between each particle along the circumference of the sphere.
     */
    void spawnSphere(final ParticleSpawner spawner, final Vec3 center, final double radius, final double step);
}
