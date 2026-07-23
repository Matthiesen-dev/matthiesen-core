package dev.matthiesen.matthiesen_core.common.core.data.fakeplayer;

import com.mojang.authlib.GameProfile;
import dev.matthiesen.matthiesen_core.common.core.MatthiesenCoreCommon;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.stats.Stat;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.entity.player.Player;

import java.util.Set;
import java.util.UUID;

/**
 * A specialized implementation of ServerPlayer that represents a fake player in the Minecraft server environment. This class is designed to simulate a player entity without requiring an actual client connection. It overrides various methods to provide no-op implementations for actions such as sending messages, awarding stats, and handling damage, ensuring that the fake player does not interact with the game world in the same way as a real player. The FakePlayer class is typically used for testing, automation, or simulating player behavior in server-side logic.
 */
public final class FakePlayer extends ServerPlayer {
    private static final GameProfile MINECRAFT = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), "[Minecraft]");
    private static final ClientInformation FAKE_CLIENT_INFO;
    private static final CommonListenerCookie COOKIE;

    /**
     * Constructs a new FakePlayer instance with the specified server level and game profile. This constructor initializes the fake player with a dummy network connection and a predefined client information object, allowing it to exist in the server environment without an actual client connection. The FakePlayer is associated with the given server level and game profile, which uniquely identifies the player entity within the game world.
     * @param level The ServerLevel instance representing the level in which the FakePlayer should exist. This is used to determine the dimension and context for the FakePlayer.
     * @param name The GameProfile instance representing the player's profile, including their UUID and name. This is used to uniquely identify the FakePlayer and ensure that it is associated with the correct player data.
     */
    public FakePlayer(ServerLevel level, GameProfile name) {
        super(level.getServer(), level, name, FAKE_CLIENT_INFO);
        this.connection = new FakePlayerNetHandler(level.getServer(), this);
    }

    public void displayClientMessage(Component chatComponent, boolean actionBar) {
    }

    public void awardStat(Stat stat, int amount) {
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    public boolean canHarmPlayer(Player player) {
        return false;
    }

    public void die(DamageSource source) {
    }

    public void tick() {
    }

    public void updateOptions(ClientInformation packet) {
    }

    /**
     * Retrieves the MinecraftServer instance associated with this FakePlayer. This method provides access to the server context in which the FakePlayer exists, allowing for interactions with server-level functionality and data. It is useful for obtaining server-related information or performing server-side operations that may involve the FakePlayer.
     * @return The MinecraftServer instance associated with this FakePlayer, representing the server context in which the player exists.
     */
    public MinecraftServer getServer() {
        return MatthiesenCoreCommon.INSTANCE.getCommonUtils().getServer();
    }

    static {
        FAKE_CLIENT_INFO = new ClientInformation("en_US", 16, ChatVisiblity.FULL, true, 0, HumanoidArm.LEFT, false, false);
        COOKIE = CommonListenerCookie.createInitial(MINECRAFT, false);
    }

    private static class FakePlayerNetHandler extends ServerGamePacketListenerImpl {
        private static final Connection DUMMY_CONNECTION;

        public FakePlayerNetHandler(MinecraftServer server, ServerPlayer player) {
            super(server, DUMMY_CONNECTION, player, FakePlayer.COOKIE);
        }

        public void tick() {
        }

        public void resetPosition() {
        }

        public void disconnect(Component message) {
        }

        public void handlePlayerInput(ServerboundPlayerInputPacket packet) {
        }

        public void handleMoveVehicle(ServerboundMoveVehiclePacket packet) {
        }

        public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket packet) {
        }

        public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket packet) {
        }

        public void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket packet) {
        }

        public void handleSeenAdvancements(ServerboundSeenAdvancementsPacket packet) {
        }

        public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket packet) {
        }

        public void handleSetCommandBlock(ServerboundSetCommandBlockPacket packet) {
        }

        public void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket packet) {
        }

        public void handlePickItem(ServerboundPickItemPacket packet) {
        }

        public void handleRenameItem(ServerboundRenameItemPacket packet) {
        }

        public void handleSetBeaconPacket(ServerboundSetBeaconPacket packet) {
        }

        public void handleSetStructureBlock(ServerboundSetStructureBlockPacket packet) {
        }

        public void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket packet) {
        }

        public void handleJigsawGenerate(ServerboundJigsawGeneratePacket packet) {
        }

        public void handleSelectTrade(ServerboundSelectTradePacket packet) {
        }

        public void handleEditBook(ServerboundEditBookPacket packet) {
        }

        public void handleEntityTagQuery(ServerboundEntityTagQueryPacket packet) {
        }

        public void handleBlockEntityTagQuery(ServerboundBlockEntityTagQueryPacket packet) {
        }

        public void handleMovePlayer(ServerboundMovePlayerPacket packet) {
        }

        public void teleport(double x, double y, double z, float yaw, float pitch) {
        }

        public void handlePlayerAction(ServerboundPlayerActionPacket packet) {
        }

        public void handleUseItemOn(ServerboundUseItemOnPacket packet) {
        }

        public void handleUseItem(ServerboundUseItemPacket packet) {
        }

        public void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket packet) {
        }

        public void handleResourcePackResponse(ServerboundResourcePackPacket packet) {
        }

        public void handlePaddleBoat(ServerboundPaddleBoatPacket packet) {
        }

        public void send(Packet<?> packet) {
        }

        public void send(Packet<?> packet, PacketSendListener sendListener) {
        }

        public void handleSetCarriedItem(ServerboundSetCarriedItemPacket packet) {
        }

        public void handleChat(ServerboundChatPacket packet) {
        }

        public void handleAnimate(ServerboundSwingPacket packet) {
        }

        public void handlePlayerCommand(ServerboundPlayerCommandPacket packet) {
        }

        public void handleInteract(ServerboundInteractPacket packet) {
        }

        public void handleClientCommand(ServerboundClientCommandPacket packet) {
        }

        public void handleContainerClose(ServerboundContainerClosePacket packet) {
        }

        public void handleContainerClick(ServerboundContainerClickPacket packet) {
        }

        public void handlePlaceRecipe(ServerboundPlaceRecipePacket packet) {
        }

        public void handleContainerButtonClick(ServerboundContainerButtonClickPacket packet) {
        }

        public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket packet) {
        }

        public void handleSignUpdate(ServerboundSignUpdatePacket packet) {
        }

        public void handleKeepAlive(ServerboundKeepAlivePacket packet) {
        }

        public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket packet) {
        }

        public void handleClientInformation(ServerboundClientInformationPacket packet) {
        }

        public void handleCustomPayload(ServerboundCustomPayloadPacket packet) {
        }

        public void handleChangeDifficulty(ServerboundChangeDifficultyPacket packet) {
        }

        public void handleLockDifficulty(ServerboundLockDifficultyPacket packet) {
        }

        public void teleport(double x, double y, double z, float yaw, float pitch, Set<RelativeMovement> relativeSet) {
        }

        public void ackBlockChangesUpTo(int sequence) {
        }

        public void handleChatCommand(ServerboundChatCommandPacket packet) {
        }

        public void handleChatAck(ServerboundChatAckPacket packet) {
        }

        public void addPendingMessage(PlayerChatMessage message) {
        }

        public void sendPlayerChatMessage(PlayerChatMessage message, ChatType.Bound boundChatType) {
        }

        public void sendDisguisedChatMessage(Component content, ChatType.Bound boundChatType) {
        }

        public void handleChatSessionUpdate(ServerboundChatSessionUpdatePacket packet) {
        }

        static {
            DUMMY_CONNECTION = new Connection(PacketFlow.CLIENTBOUND);
        }
    }
}