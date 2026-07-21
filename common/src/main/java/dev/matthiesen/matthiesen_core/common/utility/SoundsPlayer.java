package dev.matthiesen.matthiesen_core.common.utility;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

/**
 * A utility class for playing sounds to players with customizable volume and pitch.
 * <p>
 * This class allows you to create a sound player for a specific sound event and configure its volume and pitch before
 * playing it to a player. The sound will be played on the master sound channel by default, but this can be changed if needed.
 * <p>
 * Example usage:
 * <pre>
 * SoundEvent mySound = ...; // Obtain a SoundEvent instance
 * ServerPlayer player = ...; // Obtain a ServerPlayer instance
 *
 * new SoundsPlayer(mySound)
 *     .setVolume(0.5F) // Set volume to 50%
 *     .setPitch(1.2F) // Set pitch to 120%
 *     .play(player); // Play the sound to the player
 * </pre>
 */
@SuppressWarnings("unused")
public class SoundsPlayer {
    private final SoundEvent soundEvent;
    private final SoundSource soundSource = SoundSource.MASTER;
    private Float volume = 1.0F;
    private Float pitch = 1.0F;

    /**
     * Default constructor for the SoundsPlayer class. This constructor is used when creating a new instance of the SoundsPlayer class.
     * It takes in a SoundEvent and initializes the sound player with that sound event. The volume and pitch are set to their default values
     * of 1.0F, which means the sound will be played at full volume and normal pitch unless they are changed using the setVolume and setPitch methods.
     * @param sound The SoundEvent that this SoundsPlayer will play when the play method is called. This should be a valid SoundEvent instance that
     *              represents the sound you want to play to the player.
     */
    public SoundsPlayer(SoundEvent sound) {
        this.soundEvent = sound;
    }

    /**
     * Sets the volume for the sound. The volume should be a value between 0.0F (silent) and 1.0F (full volume).
     * Values greater than 1.0F will increase the volume, while values less than 0.0F will decrease it.
     *
     * @param volume The desired volume for the sound, where 1.0F is the default full volume.
     * @return The SoundsPlayer instance, for chaining.
     */
    public SoundsPlayer setVolume(float volume) {
        this.volume = volume;
        return this;
    }

    /**
     * Sets the pitch for the sound. The pitch should be a value where 1.0F is the default pitch. Values greater than
     * 1.0F will increase the pitch (making the sound higher), while values less than 1.0F will decrease it (making the sound lower).
     *
     * @param pitch The desired pitch for the sound, where 1.0F is the default pitch.
     * @return The SoundsPlayer instance, for chaining.
     */
    public SoundsPlayer setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    /**
     * Plays the configured sound to the specified player. This method will execute the sound playback on the
     * server thread to ensure that it is played correctly for the player.
     *
     * @param player The ServerPlayer instance to whom the sound should be played.
     */
    public void play(ServerPlayer player) {
        try {
            player.server.executeIfPossible(() ->
                    player.playNotifySound(this.soundEvent, this.soundSource, this.volume, this.pitch));
        } catch (RuntimeException e) {
            MatthiesenCoreCommon.INSTANCE.createErrorLog("An error occurred while trying to play sound: " + this.soundEvent.getLocation(), e);
        }
    }
}

