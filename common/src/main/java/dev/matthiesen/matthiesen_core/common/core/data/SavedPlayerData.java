package dev.matthiesen.matthiesen_core.common.core.data;

import dev.matthiesen.matthiesen_core.common.MatthiesenCoreCommon;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the saved player data for the server, including player names and aliases. This class provides methods to load, save, and manage player records, allowing for persistent storage of player information across server restarts. Each player's data is stored in a PlayerRecord, which contains the player's name and a list of aliases. The class also includes utility methods to verify and update player data based on their UUID and name.
 */
public final class SavedPlayerData extends SavedData {
    public static String PLAYER_DATA_STORE_ID = MatthiesenCoreCommon.MOD_ID + "_player_data";
    private static final String PLAYER_RECORDS_NBT_KEY = "playerRecords";
    private static final String RECORD_NAME_KEY = "name";
    private static final String RECORD_ALIASES_KEY = "aliases";

    private final Map<String, PlayerRecord> playerRecords = new HashMap<>();

    /**
     * Constructs a new instance of SavedPlayerData. This constructor is private to enforce the use of the static factory methods for creating and loading instances. The playerRecords map is initialized to store player data, where each entry maps a player's UUID (as a string) to their corresponding PlayerRecord, which contains the player's name and a list of aliases.
     */
    public SavedPlayerData() {
    }

    private static SavedPlayerData create() { return new SavedPlayerData(); }

    private static SavedPlayerData load(CompoundTag nbt, HolderLookup.Provider provider) {
        SavedPlayerData data = create();
        // Load player records from NBT
        CompoundTag playerRecordsNBT = nbt.getCompound(PLAYER_RECORDS_NBT_KEY);
        for (String key : playerRecordsNBT.getAllKeys()) {
            CompoundTag recordNBT = playerRecordsNBT.getCompound(key);
            String name = recordNBT.getString(RECORD_NAME_KEY);
            List<String> aliases = recordNBT.getList(RECORD_ALIASES_KEY, 8).stream().map(Tag::getAsString).toList();
            data.playerRecords.put(key, new PlayerRecord(name, aliases));
        }
        return data;
    }

    /**
     * Saves the current state of the SavedPlayerData to a CompoundTag. This method serializes the player records into NBT format, allowing them to be persisted across server restarts. Each player's UUID is used as the key, and their name and aliases are stored in a nested CompoundTag.
     * @param compoundTag The CompoundTag to which the player data will be saved. This tag will contain a nested structure representing all player records.
     * @param provider A HolderLookup.Provider instance that can be used to resolve any necessary references during the save process. This parameter is not used in the current implementation but is provided for potential future use.
     * @return The CompoundTag containing the serialized player data, which can be written to disk or transmitted as needed. This tag will include a nested structure of player records, each identified by the player's UUID.
     */
    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        CompoundTag playerRecordsNBT = new CompoundTag();
        for (Map.Entry<String, PlayerRecord> entry : playerRecords.entrySet()) {
            PlayerRecord record = entry.getValue();
            CompoundTag recordNBT = new CompoundTag();
            recordNBT.putString(RECORD_NAME_KEY, record.name());
            recordNBT.put(RECORD_ALIASES_KEY, record.aliases().stream().map(StringTag::valueOf).collect(Collectors.toCollection(ListTag::new)));
            playerRecordsNBT.put(entry.getKey(), recordNBT);
        }
        compoundTag.put(PLAYER_RECORDS_NBT_KEY, playerRecordsNBT);
        return compoundTag;
    }

    private static final Factory<SavedPlayerData> FACTORY = new Factory<>(
            SavedPlayerData::create,
            SavedPlayerData::load,
            null
    );

    private static SavedPlayerData getStore() {
        MinecraftServer server = MatthiesenCoreCommon.INSTANCE.getCommonUtils().getServer();
        if (server == null) return null;
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY, PLAYER_DATA_STORE_ID);
    }

    /**
     * Verifies and updates the saved player data for the given player. If the player does not have a record, a new one is created. If the player's name has changed, the record is updated with the new name and the old name is added to the list of aliases.
     * @param player The ServerPlayer instance representing the player to verify and update in the saved player data. If the player data store is not available, this method will do nothing.
     */
    public static void verifyPlayerData(ServerPlayer player) {
        SavedPlayerData dataStore = getStore();
        if (dataStore == null) return;

        String playerName = player.getName().getString();
        UUID playerUUID = player.getUUID();

        PlayerRecord existingRecord = dataStore.playerRecords.get(playerUUID.toString());
        if (existingRecord == null) {
            // No record exists, create a new one
            PlayerRecord newRecord = new PlayerRecord(playerName, List.of());
            dataStore.playerRecords.put(playerUUID.toString(), newRecord);
            dataStore.setDirty();
        } else {
            // Record exists, check for name changes
            if (!existingRecord.name().equals(playerName)) {
                // Name has changed, update the record
                List<String> updatedAliases = new ArrayList<>(existingRecord.aliases());
                if (!updatedAliases.contains(existingRecord.name())) {
                    updatedAliases.add(existingRecord.name());
                }
                PlayerRecord updatedRecord = new PlayerRecord(playerName, updatedAliases);
                dataStore.playerRecords.put(playerUUID.toString(), updatedRecord);
                dataStore.setDirty();
            }
        }
    }

    /**
     * Checks if a player with the given UUID exists in the saved player data.
     * @param uuid The UUID of the player to check for.
     * @return true if a player with the given UUID exists; false otherwise. If the player data store is not available, it will also return false.
     */
    public static boolean hasSavedPlayerData(UUID uuid) {
        SavedPlayerData dataStore = getStore();
        if (dataStore == null) return false;
        return dataStore.playerRecords.containsKey(uuid.toString());
    }

    /**
     * Checks if a player with the given name or any of their aliases exists in the saved player data.
     * @param name The name or alias of the player to check for.
     * @return true if a player with the given name or alias exists; false otherwise. If the player data store is not available, it will also return false.
     */
    public static boolean hasSavedPlayerData(String name) {
        SavedPlayerData dataStore = getStore();
        if (dataStore == null) return false;
        for (PlayerRecord record : dataStore.playerRecords.values()) {
            if (record.name().equalsIgnoreCase(name) || record.aliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(name))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds a player's UUID based on their username or any of their aliases.
     * @param username The username or alias of the player to search for.
     * @return The UUID of the player if found; otherwise, returns null if the player data store is not available or if no matching player is found.
     */
    public static UUID findPlayerByUsername(String username) {
        SavedPlayerData dataStore = getStore();
        if (dataStore == null) return null;
        for (Map.Entry<String, PlayerRecord> entry : dataStore.playerRecords.entrySet()) {
            PlayerRecord record = entry.getValue();
            if (record.name().equalsIgnoreCase(username) || record.aliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(username))) {
                return UUID.fromString(entry.getKey());
            }
        }
        return null;
    }

    /**
     * Retrieves the player's name based on their UUID from the saved player data.
     * @param uuid The UUID of the player whose name is to be retrieved.
     * @return The player's name if found; otherwise, returns null if the player data store is not available or if the UUID does not exist in the records.
     */
    public static String findPlayerNameByUUID(UUID uuid) {
        SavedPlayerData dataStore = getStore();
        if (dataStore == null) return null;
        PlayerRecord record = dataStore.playerRecords.get(uuid.toString());
        return record != null ? record.name() : null;
    }

    /**
     * Retrieves the list of aliases for a player based on their UUID.
     * @param uuid The UUID of the player whose aliases are to be retrieved.
     * @return A list of aliases for the player. If no aliases are found or if the player data store is not available, an empty list is returned.
     */
    public static List<String> getPlayerAliases(UUID uuid) {
        SavedPlayerData dataStore = getStore();
        if (dataStore == null) return Collections.emptyList();
        PlayerRecord record = dataStore.playerRecords.get(uuid.toString());
        return record != null ? record.aliases() : Collections.emptyList();
    }

    /**
     * A record representing a player's data, including their name and aliases.
     * @param name The UUID of the player.
     * @param aliases The list of aliases for the player.
     */
    public record PlayerRecord(String name, List<String> aliases) {}
}
