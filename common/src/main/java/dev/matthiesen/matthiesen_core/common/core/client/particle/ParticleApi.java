/**
 * Inspired and adapted from the Particle API in MonkeyLib538 by OffsetMonkey538.
 */
package dev.matthiesen.matthiesen_core.common.core.client.particle;

import dev.matthiesen.matthiesen_core.common.api.client.particle.IParticleApi;
import dev.matthiesen.matthiesen_core.common.api.client.particle.ParticleSpawner;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the IParticleApi interface for spawning particles in a Minecraft world.
 */
@SuppressWarnings("unused")
public final class ParticleApi implements IParticleApi {
    @Override
    public void spawnLine(ParticleSpawner spawner, Vec3 start, Vec3 end, double step) {
        final double totalDistance = start.distanceTo(end);
        final int requiredParticles = (int) (totalDistance / step);
        final double distancePerParticle = totalDistance / requiredParticles;

        final Vec3 offsetPerParticle = end
                .subtract(start)
                .normalize()
                .multiply(distancePerParticle, distancePerParticle, distancePerParticle);

        Vec3 particlePos = start;
        for (int i = -1; i < requiredParticles; i++) {
            spawner.spawnParticle(particlePos);
            particlePos = particlePos.add(offsetPerParticle);
        }
    }

    @Override
    public void spawnBoxEdges(ParticleSpawner spawner, AABB box, double step) {
        Vec3 corner = new Vec3(box.minX, box.minY, box.minZ);
        spawnLine(spawner, corner, new Vec3(box.maxX, box.minY, box.minZ), step);
        spawnLine(spawner, corner, new Vec3(box.minX, box.minY, box.maxZ), step);
        spawnLine(spawner, corner, new Vec3(box.minX, box.maxY, box.minZ), step);

        corner = new Vec3(box.maxX, box.minY, box.maxZ);
        spawnLine(spawner, corner, new Vec3(box.minX, box.minY, box.maxZ), step);
        spawnLine(spawner, corner, new Vec3(box.maxX, box.minY, box.minZ), step);
        spawnLine(spawner, corner, new Vec3(box.maxX, box.maxY, box.maxZ), step);

        corner = new Vec3(box.maxX, box.maxY, box.minZ);
        spawnLine(spawner, corner, new Vec3(box.minX, box.maxY, box.minZ), step);
        spawnLine(spawner, corner, new Vec3(box.maxX, box.maxY, box.maxZ), step);
        spawnLine(spawner, corner, new Vec3(box.maxX, box.minY, box.minZ), step);

        corner = new Vec3(box.minX, box.maxY, box.maxZ);
        spawnLine(spawner, corner, new Vec3(box.maxX, box.maxY, box.maxZ), step);
        spawnLine(spawner, corner, new Vec3(box.minX, box.maxY, box.minZ), step);
        spawnLine(spawner, corner, new Vec3(box.minX, box.minY, box.maxZ), step);
    }

    @Override
    public void spawnSphere(ParticleSpawner spawner, Vec3 center, double radius, double step) {
        final double circumference = 2 * Math.PI * radius;
        final int requiredParticles = (int) (circumference / step);
        final double radiansPerParticle = Math.TAU / requiredParticles;

        for (int i = 0; i < requiredParticles; i++) {
            final Vec3 origin = Vec3.ZERO.yRot((float) (radiansPerParticle * i));
            for (int j = 0; j < requiredParticles; j++) {
                spawner.spawnParticle(center.add(origin.xRot((float) (radiansPerParticle * j)).multiply(radius, radius, radius)));
            }
        }
    }
}
