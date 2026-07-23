# Platform Events

Matthiesen Core provides a unified, server-side event system through the `PlatformEvents` class.
All server lifecycle and player events are exposed as typed, priority-ordered observables that any
mod can subscribe to from common code, with no loader-specific setup required.

---

## Overview

```
PlatformEvents           – static entry point, holds all event observables
├── EventObservable<T>   – void dispatch (server lifecycle, player join/leave)
└── ResultEventObservable<T> – InteractionResult dispatch (player interactions)

EventPriority            – HIGHEST → LOWEST ordering enum
EventSubscription        – unsubscribe handle returned on every subscribe call
ServerEvent              – typed records for server lifecycle phases
PlayerEvent              – typed records for player events
```

---

## Dispatch Semantics

| Rule | Behaviour |
|------|-----------|
| **Order** | `HIGHEST` → `LOWEST`. Within the same priority, registration order is preserved. |
| **Short-circuit** | For `ResultEventObservable`: the first listener to return `InteractionResult.FAIL` stops dispatch immediately and cancels the action. All lower-priority listeners are skipped. |
| **Exceptions** | Any exception thrown by a listener is logged and suppressed. Remaining listeners are still called. |
| **Unsubscribe timing** | Calling `EventSubscription.unsubscribe()` takes effect on the **next** emit cycle. The current in-flight dispatch is not interrupted. |

---

## Subscribing to Events

### Basic subscription

```java
// Subscribe at the default NORMAL priority — no handle needed if you never unsubscribe
PlatformEvents.SERVER_STARTING.subscribe(event -> {
    MinecraftServer server = event.server();
    // perform startup logic
});
```

### Subscription at a specific priority

```java
PlatformEvents.PLAYER_JOIN.subscribe(EventPriority.HIGH, event -> {
    ServerPlayer player = event.player();
    // runs before NORMAL-priority listeners for the same event
});
```

### Keeping an unsubscribe handle

```java
EventSubscription sub = PlatformEvents.SERVER_START_TICK.subscribe(EventPriority.LOW, event -> {
    // runs every tick
});

// Later — remove the listener (takes effect next tick)
sub.unsubscribe();

// Check whether it is still active
boolean active = sub.isSubscribed();
```

---

## Cancelling Interaction Events

Interaction events are dispatched through `ResultEventObservable<T>`. Return
`InteractionResult.FAIL` from any listener to immediately cancel the action:

```java
// Prevent players from using items in a specific dimension
PlatformEvents.PLAYER_USE_ITEM.subscribe(EventPriority.HIGH, event -> {
    if (event.level().dimension() == Level.NETHER) {
        return InteractionResult.FAIL; // cancels; no lower-priority listeners are called
    }
    return InteractionResult.PASS; // allow dispatch to continue
});

// Prevent players from interacting with blocks they don't have permission for
PlatformEvents.PLAYER_USE_BLOCK.subscribe(event -> {
    if (!hasPermission(event.player(), event.pos())) {
        return InteractionResult.FAIL;
    }
    return InteractionResult.PASS;
});
```

---

## Available Events

### Server Lifecycle — `EventObservable<T>`

| Field | Event type | When it fires |
|-------|-----------|---------------|
| `PlatformEvents.SERVER_STARTING` | `ServerEvent.Starting` | Server begins startup; worlds not yet loaded |
| `PlatformEvents.SERVER_STARTED` | `ServerEvent.Started` | Server fully started; players can connect |
| `PlatformEvents.SERVER_STOPPING` | `ServerEvent.Stopping` | Server begins shutdown |
| `PlatformEvents.SERVER_STOPPED` | `ServerEvent.Stopped` | Server fully stopped; all worlds saved |
| `PlatformEvents.SERVER_START_TICK` | `ServerEvent.StartTick` | Start of every server tick (~20 Hz) |
| `PlatformEvents.SERVER_END_TICK` | `ServerEvent.EndTick` | End of every server tick (~20 Hz) |
| `PlatformEvents.SERVER_RELOAD` | `ServerEvent.Reload` | Data pack reload completed |

> **Note:** `ServerEvent.Reload` carries no `MinecraftServer` parameter because the reload
> pipeline does not expose the server uniformly across all loaders. Cache the server reference
> from `SERVER_STARTED` if needed.

> **Performance:** `SERVER_START_TICK` and `SERVER_END_TICK` fire ~20 times per second.
> Keep handlers lightweight; avoid allocations or blocking I/O.

### Player Events — `EventObservable<T>`

| Field | Event type | When it fires |
|-------|-----------|---------------|
| `PlatformEvents.PLAYER_JOIN` | `PlayerEvent.Join` | Player successfully joins the server |
| `PlatformEvents.PLAYER_LEAVE` | `PlayerEvent.Leave` | Player disconnects for any reason |

### Player Interaction Events — `ResultEventObservable<T>` (cancellable)

| Field | Event type | When it fires |
|-------|-----------|---------------|
| `PlatformEvents.PLAYER_USE_ITEM` | `PlayerEvent.UseItem` | Player uses an item (server-side only) |
| `PlatformEvents.PLAYER_USE_BLOCK` | `PlayerEvent.UseBlock` | Player right-clicks a block (server-side only) |

---

## `EventPriority` Values

| Value | Dispatch order | Typical use |
|-------|---------------|-------------|
| `HIGHEST` | First | Security checks, permission enforcement |
| `HIGH` | Second | Systems that need early access |
| `NORMAL` | Third (default) | Most mod listeners |
| `LOW` | Fourth | Reactions that depend on normal-priority results |
| `LOWEST` | Last | Post-processing, logging, cleanup |

---

## Full Example: Per-Mod Event Setup

```java
import dev.matthiesen.matthiesen_core.common.api.events.EventPriority;
import dev.matthiesen.matthiesen_core.common.api.events.EventSubscription;
import dev.matthiesen.matthiesen_core.common.api.events.PlatformEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionResult;

public final class MyModEvents {

    private static MinecraftServer cachedServer;
    private static EventSubscription tickSubscription;

    private MyModEvents() {}

    public static void register() {
        // Cache the server reference when fully started
        PlatformEvents.SERVER_STARTED.subscribe(event -> {
            cachedServer = event.server();
        });

        // Clear it when stopped
        PlatformEvents.SERVER_STOPPED.subscribe(event -> {
            cachedServer = null;
        });

        // Greet players on join
        PlatformEvents.PLAYER_JOIN.subscribe(event -> {
            MyMod.LOGGER.info("Player {} joined the server.", event.player().getName().getString());
        });

        // Subscribe a tick listener and keep the handle so it can be removed later
        tickSubscription = PlatformEvents.SERVER_END_TICK.subscribe(EventPriority.LOW, event -> {
            MyScheduler.tick(event.server());
        });

        // Prevent item use in a custom dimension
        PlatformEvents.PLAYER_USE_ITEM.subscribe(EventPriority.HIGH, event -> {
            if (isRestrictedDimension(event.level())) {
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });
    }

    /** Stop the tick listener (e.g. during a reload). */
    public static void unregisterTickListener() {
        if (tickSubscription != null) {
            tickSubscription.unsubscribe();
            tickSubscription = null;
        }
    }
}
```

Call `MyModEvents.register()` from your common mod's `initialize()` method.

---

## API Reference

| Class / Interface | Location |
|-------------------|----------|
| `PlatformEvents` | `dev.matthiesen.matthiesen_core.common.api.events` |
| `EventObservable<T>` | `dev.matthiesen.matthiesen_core.common.api.events` |
| `ResultEventObservable<T>` | `dev.matthiesen.matthiesen_core.common.api.events` |
| `ResultEventObservable.ResultListener<T>` | `dev.matthiesen.matthiesen_core.common.api.events` |
| `EventPriority` | `dev.matthiesen.matthiesen_core.common.api.events` |
| `EventSubscription` | `dev.matthiesen.matthiesen_core.common.api.events` |
| `ServerEvent` | `dev.matthiesen.matthiesen_core.common.api.events` |
| `PlayerEvent` | `dev.matthiesen.matthiesen_core.common.api.events` |

