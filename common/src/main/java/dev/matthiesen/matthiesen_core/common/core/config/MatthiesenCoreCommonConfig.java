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

    public ModConfigSpec.BooleanValue luckpermsPermissionValidatorEnabled;

    public MatthiesenCoreCommonConfig(ModConfigSpec.Builder builder) {
        builder.push("permissions");
        luckpermsPermissionValidatorEnabled = builder
                .comment(
                        "If true, the LuckPerms permission validator will be used when the Server is running with LuckPerms installed.",
                        "If false, MatthiesenCore will fallback to the loader's default permission validator, or the VanillaPermissionValidator if no other validator is provided.",
                        "This setting is only relevant when LuckPerms is installed on the server, and will be ignored if LuckPerms is not present."
                )
                .define("luckpermsPermissionValidatorEnabled", true);
        builder.pop();
    }
}
