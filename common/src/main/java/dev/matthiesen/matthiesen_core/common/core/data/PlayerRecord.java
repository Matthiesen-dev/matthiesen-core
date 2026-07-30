package dev.matthiesen.matthiesen_core.common.core.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a record of a player, including their name and any aliases they may have. This record is used to store player
 * information in a structured format, allowing for easy serialization and deserialization to and from NBT (Named Binary Tag)
 * format. The PlayerRecord class provides methods to convert between the record and NBT, enabling persistent storage of player
 * data in Minecraft's data system. Each PlayerRecord contains a player's name and a list of aliases, which can be used to identify
 * the player in various contexts, such as chat, commands, or server logs.
 * @param name The name of the player, which serves as the primary identifier for the player record. This name is expected to be
 *             unique and is used to reference the player in various contexts within the game.
 * @param aliases A list of alternative names or nicknames associated with the player. These aliases can be used to identify the
 *                player in different contexts, such as chat messages, command inputs, or server logs. The aliases are stored as
 *                a list of strings and can be empty if the player has no known aliases.
 */
public record PlayerRecord(String name, List<String> aliases) {
    private static final String RECORD_NAME_KEY = "name";
    private static final String RECORD_ALIASES_KEY = "aliases";

    /**
     * Creates a PlayerRecord instance from a CompoundTag. This method reads the player's name and aliases from the
     * provided NBT data and constructs a new PlayerRecord object.
     * @param nbt The CompoundTag containing the player's data, including their name and aliases. The name is expected
     *            to be stored under the key "name", and the aliases are expected to be stored as a list of strings under the key "aliases".
     * @return A new PlayerRecord instance populated with the data extracted from the provided NBT. The name and aliases
     * are retrieved from the NBT structure and used to initialize the PlayerRecord.
     */
    public static PlayerRecord fromNBT(CompoundTag nbt) {
        String name= nbt.getString(RECORD_NAME_KEY);
        List<String> aliases = nbt.getList(RECORD_ALIASES_KEY, 8).stream().map(Tag::getAsString).toList();
        return new PlayerRecord(name, aliases);
    }

    /**
     * Converts the PlayerRecord instance into a CompoundTag for storage. This method serializes the player's name and
     * aliases into NBT format, allowing them to be saved persistently. The name is stored under the key "name", and the
     * aliases are stored as a list of strings under the key "aliases".
     * @return A CompoundTag containing the serialized player data, including the player's name and aliases. The resulting
     * NBT structure can be used for saving the PlayerRecord to disk or transmitting it over a network. The name and aliases
     * are stored in a format compatible with Minecraft's NBT system.
     */
    public CompoundTag toNBT() {
        CompoundTag recordNBT = new CompoundTag();
        recordNBT.putString(RECORD_NAME_KEY, name());
        recordNBT.put(RECORD_ALIASES_KEY, aliases().stream().map(StringTag::valueOf).collect(Collectors.toCollection(ListTag::new)));
        return recordNBT;
    }
}
