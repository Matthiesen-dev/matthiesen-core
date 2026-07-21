package dev.matthiesen.matthiesen_core.common.core.discord.model;

import java.util.List;

/**
 * Fluent builder for creating {@link Component} instances.
 */
@SuppressWarnings("unused")
public final class ComponentBuilder {
    private int type;
    private Integer style;
    private String label;
    private Component.Emoji emoji;
    private String customId;
    private String url;
    private Boolean disabled;
    private List<Component> components;

    /**
     * Creates an empty component builder.
     */
    public ComponentBuilder() {}

    /**
     * Sets the component type.
     *
     * @param type the Discord component type
     * @return this builder
     */
    public ComponentBuilder withType(int type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the component style.
     *
     * @param style the button style value
     * @return this builder
     */
    public ComponentBuilder withStyle(Integer style) {
        this.style = style;
        return this;
    }

    /**
     * Sets the component label.
     *
     * @param label the label text
     * @return this builder
     */
    public ComponentBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    /**
     * Sets the emoji used by the component.
     *
     * @param emoji the emoji object
     * @return this builder
     */
    public ComponentBuilder withEmoji(Component.Emoji emoji) {
        this.emoji = emoji;
        return this;
    }

    /**
     * Sets the component custom id.
     *
     * @param customId the custom id
     * @return this builder
     */
    public ComponentBuilder withCustomId(String customId) {
        this.customId = customId;
        return this;
    }

    /**
     * Sets the component URL.
     *
     * @param url the URL
     * @return this builder
     */
    public ComponentBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Sets whether the component is disabled.
     *
     * @param disabled disabled flag
     * @return this builder
     */
    public ComponentBuilder withDisabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    /**
     * Sets child components.
     *
     * @param components component list
     * @return this builder
     */
    public ComponentBuilder withComponents(List<Component> components) {
        this.components = components;
        return this;
    }

    /**
     * Builds a new {@link Component} instance.
     *
     * @return the built component
     */
    public Component build() {
        Component component = new Component();
        component.setType(type);
        component.setStyle(style);
        component.setLabel(label);
        component.setEmoji(emoji);
        component.setCustomId(customId);
        component.setUrl(url);
        component.setDisabled(disabled);
        component.setComponents(components);
        return component;
    }
}

