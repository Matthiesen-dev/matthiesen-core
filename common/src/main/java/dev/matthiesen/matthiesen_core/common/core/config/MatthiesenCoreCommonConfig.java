package dev.matthiesen.matthiesen_core.common.core.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class MatthiesenCoreCommonConfig {
    public static final MatthiesenCoreCommonConfig INSTANCE;
    public static final ModConfigSpec SPEC;

    static {
        Pair<MatthiesenCoreCommonConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(MatthiesenCoreCommonConfig::new);
        INSTANCE = specPair.getLeft();
        SPEC = specPair.getRight();
    }

    public ModConfigSpec.IntValue data_maxPlayerAliases;
    public ModConfigSpec.BooleanValue permissions_luckpermsPermissionValidatorEnabled;

    public MatthiesenCoreCommonConfig(ModConfigSpec.Builder builder) {
        builder.comment("Data Config")
                .translation("matthiesen_core.configuration.common.data")
                .push("data");
        data_maxPlayerAliases = builder
                .comment(
                        "The maximum number of username aliases to store for each player. This setting determines how many previous usernames will be retained in the player's record.",
                        "This feature is used for tracking player name changes and maintaining a history of usernames associated with a player's UUID."
                )
                .translation("matthiesen_core.configuration.common.data.maxPlayerAliases")
                .defineInRange("maxPlayerAliases", 5, 1, Integer.MAX_VALUE);
        builder.pop(); // End of data

        builder.comment("Permissions Config")
                .translation("matthiesen_core.configuration.common.permissions")
                .push("permissions");
        permissions_luckpermsPermissionValidatorEnabled = builder
                .comment(
                        "If true, the LuckPerms permission validator will be used when the Server is running with LuckPerms installed.",
                        "If false, MatthiesenCore will fallback to the loader's default permission validator, or the VanillaPermissionValidator if no other validator is provided.",
                        "This setting is only relevant when LuckPerms is installed on the server, and will be ignored if LuckPerms is not present."
                )
                .translation("matthiesen_core.configuration.common.permissions.luckpermsPermissionValidatorEnabled")
                .define("luckpermsPermissionValidatorEnabled", true);
        builder.pop(); // End of permissions
    }
}
