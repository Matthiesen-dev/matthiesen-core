package dev.matthiesen.matthiesen_core.common.api.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a record of a player, including their name and any aliases they may have. This record is used to store player
 * information in a structured format, allowing for easy serialization and deserialization to and from NBT (Named Binary Tag)
 * format. The PlayerRecordDataV1 class provides methods to convert between the record and NBT, enabling persistent storage of player
 * data in Minecraft's data system. Each PlayerRecordDataV1 contains a player's name and a list of aliases, which can be used to identify
 * the player in various contexts, such as chat, commands, or server logs.
 * @param name The name of the player, which serves as the primary identifier for the player record. This name is expected to be
 *             unique and is used to reference the player in various contexts within the game.
 * @param aliases A list of alternative names or nicknames associated with the player. These aliases can be used to identify the
 *                player in different contexts, such as chat messages, command inputs, or server logs. The aliases are stored as
 *                a list of strings and can be empty if the player has no known aliases.
 * @deprecated This class is deprecated and will be removed in future versions. It is recommended to use PlayerRecordDataV2 for
 * new implementations, as it provides enhanced functionality and better support for player data management.
 */
@Deprecated(forRemoval = true)
public record PlayerRecordDataV1(String name, List<String> aliases) {
    private static final String RECORD_NAME_KEY = "name";
    private static final String RECORD_ALIASES_KEY = "aliases";

    /**
     * Creates a PlayerRecordDataV1 instance from a CompoundTag. This method reads the player's name and aliases from the
     * provided NBT data and constructs a new PlayerRecordDataV1 object.
     * @param nbt The CompoundTag containing the player's data, including their name and aliases. The name is expected
     *            to be stored under the key "name", and the aliases are expected to be stored as a list of strings under the key "aliases".
     * @return A new PlayerRecordDataV1 instance populated with the data extracted from the provided NBT. The name and aliases
     * are retrieved from the NBT structure and used to initialize the PlayerRecordDataV1.
     */
    public static PlayerRecordDataV1 fromNBT(CompoundTag nbt) {
        String name= nbt.getString(RECORD_NAME_KEY);
        List<String> aliases = nbt.getList(RECORD_ALIASES_KEY, 8).stream().map(Tag::getAsString).toList();
        return new PlayerRecordDataV1(name, aliases);
    }

    /**
     * Converts the PlayerRecordDataV1 instance into a PlayerRecordDataV2 object. This method creates a new PlayerRecordDataV2
     * instance using the player's name and aliases, with the current system time as the timestamp for each alias. The
     * resulting PlayerRecordDataV2 can be used for further processing or storage in a different format.
     * @return A PlayerRecordDataV2 instance containing the player's name and aliases, with timestamps set to the current
     * system time. This conversion allows for compatibility with systems that require timestamped alias data, enabling
     * tracking of when each alias was last associated with the player.
     */
    public PlayerRecordDataV2 toPlayerRecordDataV2(long currentTimeMillis) {
        return new PlayerRecordDataV2(name(), aliases().stream().collect(Collectors.toMap(alias -> alias, alias -> currentTimeMillis)));
    }
}
