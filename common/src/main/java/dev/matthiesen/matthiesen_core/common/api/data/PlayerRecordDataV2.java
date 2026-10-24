package dev.matthiesen.matthiesen_core.common.api.data;

import dev.matthiesen.matthiesen_core.common.core.config.MatthiesenCoreCommonConfig;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a record of a player, including their last username and any aliases they may have. This record is used to store player
 * information in a structured format, allowing for easy serialization and deserialization to and from NBT (Named Binary Tag)
 * format. The PlayerRecordDataV2 class provides methods to convert between the record and NBT, enabling persistent storage of player
 * data in Minecraft's data system. Each PlayerRecordDataV2 contains a player's last username and a map of aliases with their associated
 * timestamps, which can be used to identify the player in various contexts, such as chat, commands, or server logs.
 * @param lastUsername The last known username of the player, which serves as the primary identifier for the player record. This username is expected to be
 *                     unique and is used to reference the player in various contexts within the game.
 * @param aliases A map of alternative names or nicknames associated with the player, along with their corresponding timestamps. These aliases can be used to identify the
 *                player in different contexts, such as chat messages, command inputs, or server logs. The aliases are stored as a map of strings to longs,
 *                where the string represents the alias and the long represents the timestamp when the alias was last associated with the player.
 */
public record PlayerRecordDataV2(
        String lastUsername,
        Map<String, Long> aliases
) {
    private static final String RECORD_LAST_USERNAME_KEY = "lastUsername";
    private static final String RECORD_ALIASES_KEY = "aliases";

    /**
     * Creates a PlayerRecordDataV2 instance from a CompoundTag. This method reads the player's last username and aliases from the
     * provided NBT data and constructs a new PlayerRecordDataV2 object.
     * @param tag The CompoundTag containing the player's data, including their last username and aliases. The last username is expected
     *            to be stored under the key "lastUsername", and the aliases are expected to be stored as a serialized string under the key "aliases".
     * @return A new PlayerRecordDataV2 instance populated with the data extracted from the provided NBT. The last username and aliases
     * are retrieved from the NBT structure and used to initialize the PlayerRecordDataV2.
     */
    public static PlayerRecordDataV2 fromNBT(CompoundTag tag) {
        String lastUsername = tag.getString(RECORD_LAST_USERNAME_KEY);
        String rawAliases = tag.getString(RECORD_ALIASES_KEY);
        Map<String, Long> aliases = deserializeAliases(rawAliases);
        return new PlayerRecordDataV2(lastUsername, aliases);
    }

    /**
     * Creates a new PlayerRecordDataV2 instance with the specified last username and an empty aliases map. This method is used to
     * initialize a new player record when no previous data exists for the player.
     * @param lastUsername The last known username of the player, which will be set as the primary identifier for the new player record.
     * @return A new PlayerRecordDataV2 instance containing the specified last username and an empty aliases map, ready for further
     * processing or storage.
     */
    public static PlayerRecordDataV2 newRecord(String lastUsername) {
        return new PlayerRecordDataV2(lastUsername, new HashMap<>());
    }

    /**
     * Converts the PlayerRecordDataV2 instance into a CompoundTag for storage. This method serializes the player's last username and
     * aliases into NBT format, allowing them to be saved persistently. The last username is stored under the key "lastUsername", and the
     * aliases are stored as a serialized string under the key "aliases".
     * @return A CompoundTag containing the serialized player data, including the player's last username and aliases. The resulting
     * NBT structure can be used for saving the PlayerRecordDataV2 to disk or transmitting it over a network. The last username and aliases
     * are stored in a format compatible with Minecraft's NBT system.
     */
    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString(RECORD_LAST_USERNAME_KEY, lastUsername);
        tag.putString(RECORD_ALIASES_KEY, serializeAliases(aliases));
        return tag;
    }

    /**
     * Updates the last username of the player record and adds the previous last username to the aliases map with the provided timestamp.
     * This method creates a new PlayerRecordDataV2 instance with the updated last username and modified aliases map, allowing for
     * tracking of username changes over time.
     * @param newUsername The new last username to be set for the player record. This value will replace the current last username.
     * @param timestamp The timestamp associated with the previous last username, indicating when it was last used. This value will be
     *                  stored in the aliases map alongside the previous last username.
     * @return A new PlayerRecordDataV2 instance containing the updated last username and modified aliases map, reflecting the change
     * in username and preserving historical alias information.
     */
    public PlayerRecordDataV2 updateLastUsername(String newUsername, long timestamp) {
        Map<String, Long> updatedAliases = new HashMap<>(aliases);
        updatedAliases.put(lastUsername, timestamp);
        return new PlayerRecordDataV2(newUsername, updatedAliases);
    }

    private static String serializeAliases(Map<String, Long> aliases) {
        StringBuilder sb = new StringBuilder();

        var maxAliases = MatthiesenCoreCommonConfig.INSTANCE.maxPlayerAliases.getAsInt();
        aliases = filterOldAliases(aliases, maxAliases);

        for (Map.Entry<String, Long> entry : aliases.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
        }
        if (!sb.isEmpty()) {
            sb.setLength(sb.length() - 1); // Remove the trailing comma
        }
        return sb.toString();
    }

    private static Map<String, Long> filterOldAliases(Map<String, Long> aliases, int maxAliases) {
        Map<String, Long> filteredAliases = new HashMap<>();
        // Sort the aliases by timestamp in descending order and limit to maxAliases
        aliases.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // Sort by timestamp descending
                .limit(maxAliases)
                .forEach(entry -> filteredAliases.put(entry.getKey(), entry.getValue()));
        return filteredAliases;
    }

    private static Map<String, Long> deserializeAliases(String rawAliases) {
        Map<String, Long> aliases = new HashMap<>();
        if (rawAliases == null || rawAliases.isEmpty()) {
            return aliases;
        }
        String[] pairs = rawAliases.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String alias = keyValue[0];
                try {
                    long timestamp = Long.parseLong(keyValue[1]);
                    aliases.put(alias, timestamp);
                } catch (NumberFormatException e) {
                    // Ignore invalid timestamp entries and continue processing other aliases
                }
            }
        }
        return aliases;
    }
}
