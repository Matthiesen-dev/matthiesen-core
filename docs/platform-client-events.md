# Platform Client Events

The **Platform Client Events** API provides a unified client-side event surface for Matthiesen Core.

It currently includes:
- Lifecycle events (`CLIENT_STOPPING`, `CLIENT_END_TICK`)
- HUD layer registration (`HUD_REGISTRATION`)
- Built-in resource pack registration (`RESOURCE_PACK_REGISTRATION`)
- Block outline interception (`BLOCK_HIGHLIGHT`)

## Current HUD Model

HUD behavior now uses a registration-first model:

1. Platform bridge invokes `applyHudRegistrations(...)`
2. `PlatformClientEvents` emits `HUD_REGISTRATION` once
3. Subscribers register layers with ordering + optional target + resource id
4. Platform applies those registrations:
   - **NeoForge**: native `RegisterGuiLayersEvent`
   - **Fabric**: fallback rendering via `PlatformClientEvents.renderHudLayers(...)`

There is no separate `HUD_RENDER` observable in the current API surface; HUD rendering is piggybacked through `PlatformClientEvents` layer registration and renderer.

## Current Resource Pack Model

Resource pack behavior now uses a registration-first model similar to HUD layers:

1. Platform bridge invokes `applyResourcePackRegistrations(...)`
2. `PlatformClientEvents` emits `RESOURCE_PACK_REGISTRATION` once
3. Subscribers register built-in packs through the provided `ResourcePackRegistrar`
4. Platform applies those registrations:
   - **NeoForge**: `AddPackFindersEvent` with built-in pack metadata and activation mode
   - **Fabric**: `ResourceManagerHelper.registerBuiltinResourcePack(...)`

The shared helper methods store resource-pack definitions and replay them when the platform registrar becomes active, so registrations can happen before or after client initialization.

## Dispatch Semantics

### Void events

For `CLIENT_STOPPING`, `CLIENT_END_TICK`, and `HUD_REGISTRATION`:
- Priority order: `HIGHEST -> LOWEST`
- Tie-break: registration order
- Exceptions: logged and suppressed; dispatch continues

For `RESOURCE_PACK_REGISTRATION`:
- Same dispatch behavior as other void events
- Listeners should call the registrar or `PlatformClientEvents.registerResourcePack(...)`

### Result event

For `BLOCK_HIGHLIGHT`:
- Priority order: `HIGHEST -> LOWEST`
- Tie-break: registration order
- Return `InteractionResult.FAIL` to cancel (short-circuits)
- Return `InteractionResult.PASS` to continue
- Exceptions: logged and suppressed

## Events

### `CLIENT_STOPPING`

Fired once during client shutdown.

```java
PlatformClientEvents.CLIENT_STOPPING.subscribe(event -> {
    // cleanup
});
```

### `CLIENT_END_TICK`

Fired every client tick after tick processing.

```java
PlatformClientEvents.CLIENT_END_TICK.subscribe(event -> {
    // per-tick client logic
});
```

### `HUD_REGISTRATION`

Fired when HUD layers should be registered.

The event provides a `HudRegistrar` so listeners can register layers with:
- `HudOrdering` (`BEFORE` / `AFTER`)
- optional `other` target layer id
- unique layer `key`
- `LayeredDraw.Layer` renderer

```java
PlatformClientEvents.HUD_REGISTRATION.subscribe(event -> {
    event.registrar().register(
            HudOrdering.AFTER,
            NeoForgeVanillaGuiLayers.CHAT,
            ResourceLocation.fromNamespaceAndPath("examplemod", "status_overlay"),
            (drawContext, tickCounter) -> {
                // render custom HUD layer
            }
    );
});
```

You can also use helper registration methods directly:

```java
PlatformClientEvents.registerHudLayer(
        HudOrdering.BEFORE,
        NeoForgeVanillaGuiLayers.HOTBAR,
        ResourceLocation.fromNamespaceAndPath("examplemod", "under_hotbar"),
        (drawContext, tickCounter) -> {
            // render
        }
);
```

### `BLOCK_HIGHLIGHT`

Fired before block outline rendering.

```java
PlatformClientEvents.BLOCK_HIGHLIGHT.subscribe(event -> {
    BlockOutlineContext context = event.context();

    if (shouldCancelOutline(context)) {
        return InteractionResult.FAIL;
    }

    return InteractionResult.PASS;
});
```

        ### `RESOURCE_PACK_REGISTRATION`

        Fired when built-in resource packs should be registered.

        The event provides a `ResourcePackRegistrar` so listeners can register packs with:
        - owning `modId`
        - pack `id`
        - display name (`Component` or `String`)
        - `ResourcePackActivationBehaviour` (`NORMAL`, `DEFAULT_ENABLED`, `ALWAYS_ENABLED`)

        ```java
        PlatformClientEvents.RESOURCE_PACK_REGISTRATION.subscribe(event -> {
            event.registrar().register(new ResourcePackDef(
                    "examplemod",
                    "extra_textures",
                    "Example Textures",
                    ResourcePackActivationBehaviour.DEFAULT_ENABLED
            ));
        });
        ```

        You can also use the helper methods directly:

        ```java
        PlatformClientEvents.registerResourcePack(
                "examplemod",
                "extra_textures",
                "Example Textures",
                ResourcePackActivationBehaviour.DEFAULT_ENABLED
        );
        ```

## Loader Behavior

### Fabric

- HUD layers: registrations are collected, then rendered via `HudRenderCallback` calling `PlatformClientEvents.renderHudLayers(...)`
- Block highlight: `WorldRenderEvents.BEFORE_BLOCK_OUTLINE` (`true` cancels)
- Resource packs: built-in packs are registered through `ResourceManagerHelper.registerBuiltinResourcePack(...)`

### NeoForge

- HUD layers: applied natively through `RegisterGuiLayersEvent` (`registerBelow`, `registerAbove`, etc.)
- Block highlight: `RenderHighlightEvent.Block` (`event.setCanceled(true)` on `FAIL`)
- Resource packs: built-in packs are registered through `AddPackFindersEvent`

## Priority Reference

| Priority | Value | Order |
|---|---|---|
| Highest | `EventPriority.HIGHEST` | 1st |
| High | `EventPriority.HIGH` | 2nd |
| Normal | `EventPriority.NORMAL` | 3rd |
| Low | `EventPriority.LOW` | 4th |
| Lowest | `EventPriority.LOWEST` | 5th |

## API Surface (Current)

- `PlatformClientEvents.CLIENT_STOPPING`
- `PlatformClientEvents.CLIENT_END_TICK`
- `PlatformClientEvents.HUD_REGISTRATION`
- `PlatformClientEvents.RESOURCE_PACK_REGISTRATION`
- `PlatformClientEvents.BLOCK_HIGHLIGHT`
- `PlatformClientEvents.registerHudLayer(...)`
- `PlatformClientEvents.registerResourcePack(...)`
- `PlatformClientEvents.renderHudLayers(...)` (platform bridge usage)

## Notes

- Duplicate HUD layer keys throw `IllegalArgumentException`.
- If a HUD registration occurs after platform registration is active, it is applied immediately.
- Duplicate resource pack registrations throw `IllegalArgumentException`.
- If a resource pack registration occurs after platform registration is active, it is applied immediately.
- `EventSubscription.unsubscribe()` takes effect next emit cycle.
