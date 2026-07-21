package dev.matthiesen.matthiesen_core.common.api.interfaces;

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
