package dev.matthiesen.matthiesen_core.common.api.platform.loader;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum ModConfigType implements StringRepresentable {
    /**
     * Common mod config for configuration that needs to be loaded on both environments.
     * Loaded on both servers and clients.
     * Stored in the global config directory.
     * Not synced.
     * Suffix is "-common" by default.
     */
    COMMON,
    /**
     * Client config is for configuration affecting the ONLY client state such as graphical options.
     * Only loaded on the client side.
     * Stored in the global config directory.
     * Not synced.
     * Suffix is "-client" by default.
     */
    CLIENT,
    /**
     * Server type config is configuration that is associated with a server instance.
     * Only loaded during server startup.
     * Stored in a server/save specific "serverconfig" directory.
     * Synced to clients during connection.
     * Suffix is "-server" by default.
     */
    SERVER,
    /**
     * Startup configs are for configurations that need to run as early as possible.
     * Loaded as soon as the config is registered to FML.
     * Please be aware when using them, as using these configs to enable/disable registration and anything that must be present on both sides
     * can cause clients to have issues connecting to servers with different config values.
     * Stored in the global config directory.
     * Not synced.
     * Suffix is "-startup" by default.
     */
    STARTUP;

    /**
     * Returns the file extension associated with this mod config type.
     * The extension is derived from the name of the enum constant, converted to lowercase.
     * @return The file extension for this mod config type.
     */
    public String extension() {
        // Forge Config Api Port: replace NeoForge helper class method call
        return this.name().toLowerCase(Locale.ROOT);
    }

    /**
     * Returns the serialized name of this mod config type.
     * This is used for vanilla argument types in commands such as /config.
     * @return The serialized name of this mod config type.
     */
    @Override
    public @NotNull String getSerializedName() {
        return this.extension();
    }
}
