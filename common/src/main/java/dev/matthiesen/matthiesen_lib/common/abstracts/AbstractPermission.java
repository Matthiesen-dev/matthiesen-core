package dev.matthiesen.matthiesen_lib.common.abstracts;

import dev.matthiesen.matthiesen_lib.common.api.permissions.Permission;
import dev.matthiesen.matthiesen_lib.common.api.permissions.PermissionLevel;
import net.minecraft.resources.ResourceLocation;

/**
 * Abstract base class for permissions that provides common functionality for generating the permission's identifier and literal
 * string based on the mod ID and permission node.
 * This class implements the Permission interface and provides a constructor that takes a permission node and level, and generates
 * the identifier and literal string based on the mod ID and permission node. Subclasses must implement the getModId and getPermissionNamespace
 * methods to provide the mod ID and permission namespace for the permission. This allows for a standardized way to define permissions across
 * different mods while still allowing for flexibility in how the mod ID and permission namespace are defined. The identifier is generated using
 * the mod ID and permission node, while the literal string is generated using the mod ID and permission node in a format that is compatible with
 * common permission systems (e.g., "modid.permission_node"). This class also provides optional equals, hashCode, and toString methods for data
 * class behavior, which can be useful for comparing permissions and debugging.
 */
@SuppressWarnings("unused")
public abstract class AbstractPermission implements Permission {
    private final String node;
    private final PermissionLevel level;
    private final ResourceLocation identifier;
    private final String literal;

    /**
     * Gets the mod ID for this permission. This method must be implemented by subclasses to provide the mod ID that will
     * be used to generate the permission's identifier and literal string.
     * @return The mod ID for this permission, which is used to generate the permission's identifier and literal string.
     */
    protected abstract String getModId();

    /**
     * Gets the permission namespace for this permission. This method must be implemented by subclasses to provide the permission
     * namespace that will be used in the toString method for debugging purposes. The permission namespace is a string that represents
     * the category or grouping of permissions that this permission belongs to, and is used in the toString method to provide a more descriptive
     * representation of the permission for debugging purposes. For example, if the permission is related to a specific feature of the mod,
     * the permission namespace could be the name of that feature, which would help to identify the permission when debugging.
     * The permission namespace is not used for generating the identifier or literal string, but is purely for providing a more descriptive
     * toString representation of the permission for debugging purposes.
     * <p>
     *     Example for a permission that would be part of the Matthiesen Lib library: <br />
     *     getPermissionNamespace() would return "MatthiesenLib"
     * </p>
     * @return The permission namespace for this permission, which is used in the toString method for debugging purposes. This should be a string that represents
     * the category or grouping of permissions that this permission belongs to, and is used to provide a more descriptive representation of the permission when debugging.
     */
    protected abstract String getPermissionNamespace();

    /**
     * Constructs a new AbstractPermission with the given permission node and level. The identifier and literal string for this permission are generated
     * based on the mod ID and permission node. The identifier is generated using the mod ID and permission node, while the literal string is generated using
     * the mod ID and permission node in a format that is compatible with common permission systems (e.g., "modid.permission_node"). This constructor provides
     * a standardized way to define permissions across different mods while still allowing for flexibility in how the mod ID and permission namespace are defined.
     * Subclasses must implement the getModId and getPermissionNamespace methods to provide the mod ID and permission namespace for the permission, which are used to
     * generate the identifier and literal string for the permission. The level parameter is used to set the permission level for this permission, which determines how
     * it is checked against a player's permissions. For example, in Minecraft's built-in permission system, a permission level of 0 means that the player must be an
     * operator to have the permission, while a level of 1 or higher means that the player must have a certain permission level to have the permission. The specific meaning
     * of the permission level may vary depending on the permission system being used, but it generally indicates the required level of permission a player must have to be
     * granted this permission.
     * @param node The permission node for this permission, which is used to generate the permission's identifier and literal string. This should be a
     *             string that represents the specific permission being defined (e.g., "use_feature_x").
     * @param level The permission level for this permission, which determines how it is checked against a player's permissions. For example, in Minecraft's
     *              built-in permission system, a permission level of 0 means that the player must be an operator to have the permission, while a level of 1 or
     *              higher means that the player must have a certain permission level to have the permission. The specific meaning of the permission level may vary
     *              depending on the permission system being used, but it generally indicates the required level of permission a player must have to be granted this permission.
     */
    public AbstractPermission(String node, PermissionLevel level) {
        this.node = getModId() + "." + node;
        this.level = level;
        this.identifier = ResourceLocation.fromNamespaceAndPath(getModId(), node);
        this.literal = this.node;
    }

    /**
     * Gets the unique identifier for this permission.
     * @return The unique identifier for this permission.
     */
    @Override
    public ResourceLocation getIdentifier() { return identifier; }

    /**
     * Gets the literal string representation of this permission.
     * @return The literal string representation of this permission.
     */
    @Override
    public String getLiteral() { return literal; }

    /**
     * Gets the permission level associated with this permission.
     * @return The permission level associated with this permission.
     */
    @Override
    public PermissionLevel getLevel() { return level; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPermission that = (AbstractPermission) o;
        return java.util.Objects.equals(node, that.node) &&
                java.util.Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(node, level);
    }

    @Override
    public String toString() {
        return getPermissionNamespace() + "{" +
                "node='" + node + '\'' +
                ", level=" + level +
                ", identifier=" + identifier +
                ", literal='" + literal + '\'' +
                '}';
    }
}


