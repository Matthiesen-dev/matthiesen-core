package dev.matthiesen.matthiesen_core.common.core.discord.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents a Discord message component object.
 *
 * <p>Use this for interactive structures such as action rows and buttons.</p>
 */
@SuppressWarnings("unused")
public final class Component {
    /** Action row component type. */
    public static final int ACTION_ROW = 1;
    /** Button component type. */
    public static final int BUTTON = 2;

    /** Primary button style. */
    public static final int PRIMARY = 1;
    /** Secondary button style. */
    public static final int SECONDARY = 2;
    /** Success button style. */
    public static final int SUCCESS = 3;
    /** Danger button style. */
    public static final int DANGER = 4;
    /** Link button style. */
    public static final int LINK = 5;

    private int type;
    private Integer style;
    private String label;
    private Emoji emoji;
    @SerializedName("custom_id")
    private String customId;
    private String url;
    private Boolean disabled;
    private List<Component> components;

    /**
     * Creates an empty component.
     */
    public Component() {}

    /**
     * Gets the component type.
     *
     * @return the Discord component type value
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the component type.
     *
     * @param type the Discord component type value
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Gets the button style.
     *
     * @return the style value, or {@code null} if not set
     */
    public Integer getStyle() {
        return style;
    }

    /**
     * Sets the button style.
     *
     * @param style the style value
     */
    public void setStyle(Integer style) {
        this.style = style;
    }

    /**
     * Gets the component label.
     *
     * @return the label text
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the component label.
     *
     * @param label the label text
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gets the optional emoji attached to this component.
     *
     * @return the emoji object, or {@code null}
     */
    public Emoji getEmoji() {
        return emoji;
    }

    /**
     * Sets the optional emoji attached to this component.
     *
     * @param emoji the emoji object
     */
    public void setEmoji(Emoji emoji) {
        this.emoji = emoji;
    }

    /**
     * Gets the custom id used for interaction callbacks.
     *
     * @return the custom id
     */
    public String getCustomId() {
        return customId;
    }

    /**
     * Sets the custom id used for interaction callbacks.
     *
     * @param customId the custom id
     */
    public void setCustomId(String customId) {
        this.customId = customId;
    }

    /**
     * Gets the URL used by link buttons.
     *
     * @return the URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL used by link buttons.
     *
     * @param url the URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets whether the component is disabled.
     *
     * @return {@code true} if disabled, otherwise {@code false}, or {@code null} when unset
     */
    public Boolean getDisabled() {
        return disabled;
    }

    /**
     * Sets whether the component is disabled.
     *
     * @param disabled disabled flag
     */
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Gets child components for container component types.
     *
     * @return child component list
     */
    public List<Component> getComponents() {
        return components;
    }

    /**
     * Sets child components for container component types.
     *
     * @param components child component list
     */
    public void setComponents(List<Component> components) {
        this.components = components;
    }

    /**
     * Represents a Discord emoji object embedded in a component.
     */
    public static class Emoji {
        private String id;
        private String name;
        private boolean animated;

        /**
         * Creates an empty emoji.
         */
        public Emoji() {}

        /**
         * Gets the emoji id.
         *
         * @return the emoji id
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the emoji id.
         *
         * @param id the emoji id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Gets the emoji name.
         *
         * @return the emoji name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the emoji name.
         *
         * @param name the emoji name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets whether the emoji is animated.
         *
         * @return {@code true} if animated
         */
        public boolean isAnimated() {
            return animated;
        }

        /**
         * Sets whether the emoji is animated.
         *
         * @param animated animated flag
         */
        public void setAnimated(boolean animated) {
            this.animated = animated;
        }
    }
}

