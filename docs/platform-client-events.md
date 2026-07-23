# Platform Client Events

The **Platform Client Events** system provides a unified, priority-ordered event dispatch system for client-side listeners in Matthiesen Core. It replaces callback-based patterns with a clean, subscription-based API that mirrors the server-side [`PlatformEvents`](platform-events.md) system.

## Overview

Client-side events allow mods to hook into key client lifecycle and rendering moments:

- **Lifecycle events**: Client stopping, end of tick
- **HUD rendering**: Render custom elements each frame
- **Block highlighting**: Override default block outline rendering

All events are dispatched through a central registry (`PlatformClientEvents`) and support:
- **Priority-ordered dispatch**: HIGHEST → LOWEST, with registration order as tiebreaker
- **Log-and-continue exceptions**: One mod's error doesn't break others
- **Dynamic unsubscription**: Remove listeners at runtime via `EventSubscription` handles

## Dispatch Semantics

### Lifecycle & HUD Render Events (Void Events)

Events dispatched as **void** (non-cancellable):

- **Dispatch order**: `HIGHEST` → `LOWEST` priority, then by registration sequence
- **Exception handling**: Logged and suppressed; dispatch continues
- **Return value**: Ignored

### Block Highlight Events (Result-Based)

Block highlight is a **cancellable** event:

- **Dispatch order**: Same as void events (HIGHEST → LOWEST, then sequence)
- **Return value**: `InteractionResult.PASS` (continue) or `InteractionResult.FAIL` (cancel)
- **Short-circuit**: First listener returning `FAIL` stops dispatch immediately
- **Exception handling**: Logged and suppressed; dispatch continues

## Events Reference

### `CLIENT_STOPPING`

**Type**: Void event  
**Emitted**: Once when the client is shutting down  
**Use case**: Cleanup, save state, release resources

```java
PlatformClientEvents.CLIENT_STOPPING.subscribe(event -> {
    System.out.println("Client shutting down!");
});
```

### `CLIENT_END_TICK`

**Type**: Void event  
**Emitted**: Every client tick, after all processing  
**Use case**: Per-tick updates, animations, periodic checks

```java
PlatformClientEvents.CLIENT_END_TICK.subscribe(event -> {
    // Update custom state each frame
    updateMyCustomState();
});
```

### `HUD_RENDER`

**Type**: Void event  
**Emitted**: Every client frame during HUD rendering  
**Context**:
- `drawContext: GuiGraphics` — GUI drawing utilities
- `tickCounter: DeltaTracker` — Frame delta for animations

**Use case**: Render custom HUD elements (health bars, overlays, info text)

```java
PlatformClientEvents.HUD_RENDER.subscribe(EventPriority.NORMAL, event -> {
    GuiGraphics guiGraphics = event.drawContext();
    DeltaTracker tickCounter = event.tickCounter();
    
    // Draw at screen coordinates
    guiGraphics.drawString(Minecraft.getInstance().font, 
        "Custom HUD Text", 10, 10, 0xFFFFFF);
});
```

**Note on priority**: Use `EventPriority.HIGH` to render above other mods, or `EventPriority.LOW` to render below. Registration order breaks ties.

### `BLOCK_HIGHLIGHT`

**Type**: Result-based event (cancellable)  
**Emitted**: Before each block outline is rendered  
**Context**:
- `context: BlockOutlineContext` — Contains level, hit result, pose stack, camera, buffer source

**Return**: `InteractionResult.PASS` (render normally) or `InteractionResult.FAIL` (cancel)

**Use case**: Override block rendering, hide certain blocks, apply custom effects

```java
PlatformClientEvents.BLOCK_HIGHLIGHT.subscribe(event -> {
    BlockOutlineContext context = event.context();
    BlockHitResult hitResult = context.blockHitResult();
    
    // Example: hide outlines for bedrock
    if (context.level().getBlockState(hitResult.getBlockPos()).getBlock() == Blocks.BEDROCK) {
        return InteractionResult.FAIL;  // Cancel rendering
    }
    
    return InteractionResult.PASS;  // Continue normally
});
```

## Priority Table

| Priority | Enum Value | Order |
|----------|-----------|-------|
| Highest  | `EventPriority.HIGHEST` | 1st |
| High     | `EventPriority.HIGH` | 2nd |
| Normal   | `EventPriority.NORMAL` | 3rd (default) |
| Low      | `EventPriority.LOW` | 4th |
| Lowest   | `EventPriority.LOWEST` | 5th |

Within the same priority, listeners fire in **registration order** (FIFO).

## Subscription Patterns

### Simple Subscription (Default Priority)

```java
PlatformClientEvents.HUD_RENDER.subscribe(event -> {
    // Rendered at NORMAL priority
});
```

### With Custom Priority

```java
PlatformClientEvents.HUD_RENDER.subscribe(EventPriority.HIGH, event -> {
    // Rendered after HIGHEST but before NORMAL
});
```

### Dynamic Unsubscription

Retain the `EventSubscription` handle to remove the listener later:

```java
EventSubscription subscription = PlatformClientEvents.HUD_RENDER.subscribe(event -> {
    doSomething();
});

// Later, when no longer needed:
subscription.unsubscribe();
```

**Semantics**: The listener is marked for removal and removed before the next emit cycle (not mid-emit).

### Conditional Logic

Perform any logic inside the lambda—listeners can check state and conditionally render:

```java
PlatformClientEvents.HUD_RENDER.subscribe(event -> {
    if (shouldShowHUD()) {
        renderCustomHUD(event.drawContext(), event.tickCounter());
    }
});
```

## Full Example

Here's a complete example combining lifecycle, HUD render, and block highlight subscriptions:

```java
public class MyClientModFeature {
    private static boolean hudEnabled = true;
    private static EventSubscription hudSubscription;
    
    public static void init() {
        // Cleanup on client stop
        PlatformClientEvents.CLIENT_STOPPING.subscribe(event ->
                System.out.println("[MyMod] Client stopping"));
        
        // Periodic tick-based updates
        PlatformClientEvents.CLIENT_END_TICK.subscribe(event -> {
            // Update state, check conditions, etc.
            updateMyFeature();
        });
        
        // Render HUD overlay
        hudSubscription = PlatformClientEvents.HUD_RENDER.subscribe(EventPriority.HIGH, event -> {
            if (hudEnabled) {
                renderCustomOverlay(event.drawContext(), event.tickCounter());
            }
        });
        
        // Override block highlighting
        PlatformClientEvents.BLOCK_HIGHLIGHT.subscribe(event -> {
            BlockOutlineContext ctx = event.context();
            
            // Don't highlight if it's a special block
            if (isSpecialBlock(ctx.blockHitResult().getBlockPos())) {
                return InteractionResult.FAIL;
            }
            
            return InteractionResult.PASS;
        });
    }
    
    public static void disableHUD() {
        hudEnabled = false;
        // Optional: unsubscribe entirely
        // hudSubscription.unsubscribe();
    }
    
    private static void renderCustomOverlay(GuiGraphics graphics, DeltaTracker delta) {
        // Custom HUD rendering logic
        graphics.drawString(Minecraft.getInstance().font, "MyMod Active", 10, 10, 0xFF00FF00);
    }
    
    private static void updateMyFeature() {
        // Per-tick logic
    }
    
    private static boolean isSpecialBlock(BlockPos pos) {
        // Check if block should have special treatment
        return false;
    }
}
```

## Exception Handling

All exceptions thrown in listeners are **logged and suppressed**. This ensures one misbehaving mod doesn't crash the entire event system:

```java
PlatformClientEvents.HUD_RENDER.subscribe(event -> {
    try {
        doSomething();  // May throw
    } catch (Exception e) {
        // Already logged by observable; dispatch continues to next listener
    }
});

// Or just let it throw—observable handles it:
PlatformClientEvents.HUD_RENDER.subscribe(event -> {
    risky Operation();  // Exception logged automatically
});
```

## Platform Bridge Hooks

Internally, each platform (Fabric, NeoForge) wires its native events to `PlatformClientEvents`:

- **Fabric**: `ClientLifecycleEvents`, `ClientTickEvents`, `HudRenderCallback`, `WorldRenderEvents`
- **NeoForge**: `ClientTickEvent`, `RenderGuiLayerEvent`, `RenderHighlightEvent.Block`

This bridging is transparent to API users—simply subscribe and events are emitted automatically.

## API Reference

### Core Classes

| Class | Purpose |
|-------|---------|
| `PlatformClientEvents` | Central registry with 4 observable fields |
| `ClientEvent.Stopping` | Empty record for client stop event |
| `ClientEvent.EndTick` | Empty record for end-of-tick event |
| `ClientEvent.HudRender` | Record with `drawContext` and `tickCounter` |
| `ClientEvent.BlockHighlight` | Record with `context: BlockOutlineContext` |
| `EventPriority` | Enum: `HIGHEST`, `HIGH`, `NORMAL`, `LOW`, `LOWEST` |
| `EventSubscription` | Handle for unsubscription |

### Methods

```java
// Subscribe with default priority (NORMAL)
EventSubscription subscribe(Consumer<T> listener)

// Subscribe with custom priority
EventSubscription subscribe(EventPriority priority, Consumer<T> listener)

// For result-based events: same signatures but listener returns InteractionResult
EventSubscription subscribe(ResultListener<T> listener)
EventSubscription subscribe(EventPriority priority, ResultListener<T> listener)
```

### Common Interfaces

```java
// Void listener
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
}

// Result listener (for BLOCK_HIGHLIGHT)
@FunctionalInterface
public interface ResultListener<T> {
    InteractionResult handle(T event);
}
```

## Performance Considerations

- **Per-frame calls**: `HUD_RENDER` is called **every frame**—keep logic minimal
- **Listener count**: Lots of listeners → slower frame time; use prioritization wisely
- **Exceptions**: Exceptions are logged but don't crash; still add overhead
- **Unsubscription**: Listeners are removed before next emit; no mid-emit overhead

For tight loops (e.g., HUD rendering), consider:
- Using higher priority if your logic must run early
- Caching expensive computations
- Avoiding allocations in hot paths

