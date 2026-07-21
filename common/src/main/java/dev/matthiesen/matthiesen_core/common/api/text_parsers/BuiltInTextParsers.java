package dev.matthiesen.matthiesen_core.common.api.text_parsers;

public enum BuiltInTextParsers {
    VANILLA("vanilla");

    private final String id;

    BuiltInTextParsers(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
