package dev.matthiesen.matthiesen_core.common.api.client;

import dev.matthiesen.matthiesen_core.common.api.platform.registry.ResourcePackDef;

@FunctionalInterface
public interface ResourcePackRegistrar {
    void register(ResourcePackDef resourcePackDef);
}
