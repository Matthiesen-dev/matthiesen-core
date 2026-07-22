# Matthiesen Core [Work in Progress]

Matthiesen Core is a library mod for NeoForge and Fabric that enables developers to create mods with a shared common codebase. 
It provides a set of core functionalities and utilities that can be used across multiple mods, allowing for easier development and maintenance.
Instead of maintaining separate implementations for each platform, you can write your mod logic once in common code and let Matthiesen core 
handle the platform-specific details.

> Note: For regular users, This library does nothing on its own aside from setting up the core functionality for other mods to utilize. 
> If you are not a mod developer, simply drop the mod in your `mods/` folder alongside the other mods that depend on it, and you are good to go.
 
Matthiesen Core is the successor to [Matthiesen Lib](https://modrinth.com/mod/matthiesen-lib) and [Matthiesen Lib Api](https://modrinth.com/mod/matthiesen-lib-api), which 
were two separate libraries that have now been combined into a single unified library mod, that provides a more streamlined and efficient experience for mod developers.
 
## Features

Matthiesen Core provides a unified API for:

- **Content Registration**: Register blocks, items, entities, and other game content in a platform-agnostic way.
- **Event Handling**: Listen to and handle game events across different platforms.
- **Networking**: Send and receive network packets between the client and server.
- **Configuration Management**: Manage mod configurations and settings in a consistent manner.
- **Utility Functions**: Access a variety of utility functions for common tasks, such as logging, resource management, and more.
- **Client-Side Features**: Includes client-specific features like HUD management, keybinding handling, and screen management.
- **Permission Management**: Provides a permission system for managing access to certain features or commands.

## Optional Dependencies

- [Ember's Text API](https://modrinth.com/mod/embers-text-api) - Use fancy text effects anywhere, including custom fonts!

## Version Compatibility

| Minecraft Version | Mod Version |
|-------------------|-------------|
| 1.21.1            | 1.x.x       |

## Developer Quick Start

Matthiesen Core development versions and sources are available from [Matthiesen Dev Maven](https://maven.matthiesen.dev).

### Gradle

**Step 1**: Add the Maven repository to your `build.gradle.kts` file

```kotlin
repositories {
    maven("https://maven.matthiesen.dev/releases") {
        name = "devMatthiesenMaven-releases"
    }
    // For Snapshot versions, also include the snapshots repository
    maven("https://maven.matthiesen.dev/snapshots") {
        name = "devMatthiesenMaven-snapshots"
    }
}
```

**Step 2**: Add the version property to your `gradle.properties` file

```properties
matthiesen_core_version=1.x.x
```

> Note: Replace `1.x.x` with the latest version of Matthiesen Core. You can find the latest version on [Matthiesen Dev Maven](https://maven.matthiesen.dev) or see the table below for the latest version.

| Release Type | Version Badge                                                                                                                                  |
|--------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| Latest Release | [![Latest Release](https://maven.matthiesen.dev/api/badge/latest/releases/dev/matthiesen/matthiesen-core-common?name=matthiesen-core-common)](https://maven.matthiesen.dev/#/releases/dev/matthiesen/) |
| Snapshot | [![Latest Snapshot](https://maven.matthiesen.dev/api/badge/latest/snapshots/dev/matthiesen/matthiesen-core-common?name=matthiesen-core-common)](https://maven.matthiesen.dev/#/snapshots/dev/matthiesen/) |

**Step 3**: Add the dependency to your `build.gradle.kts` file

```kotlin
// In common/build.gradle.kts
dependencies {
    modImplementation("dev.matthiesen:matthiesen-core-common:${property("matthiesen_core_version")}") {
        isTransitive = false
    }
}

// In fabric/build.gradle.kts
dependencies {
    modImplementation("dev.matthiesen:matthiesen-core-fabric:${property("matthiesen_core_version")}") {
        isTransitive = false
    }
}

// In neoforge/build.gradle.kts
dependencies {
    modImplementation("dev.matthiesen:matthiesen-core-neoforge:${property("matthiesen_core_version")}") {
        isTransitive = false
    }
}
```

**Step 4**: Loader-specific Dependency Setup

For Fabric and NeoForge, you will need to add the appropriate loader-specific dependency to your mod metadata file (e.g., `fabric.mod.json` for Fabric or `neoforge.mods.toml` for NeoForge). This ensures that the correct version of Matthiesen Core is loaded alongside your mod.

**Fabric Example** (`fabric.mod.json`):

```json
{
  "depends": {
    "matthiesen_core": ">=1.x.x"
  }
}
```

**NeoForge Example** (`neoforge.mods.toml`):

```toml
[[dependencies.yourmodid]]
modId = "matthiesen_core"
type = "required"
versionRange = "[1.x.x,)"
ordering = "AFTER"
side = "BOTH"
```

**Step 5**: Sync your project and set up your common mod codebase to use Matthiesen Core's APIs for content registration, event handling, networking, and other features.

**AbstractCommonMod**: Extend the `AbstractCommonMod` class in your common mod codebase to leverage Matthiesen Core's core functionalities. This class provides a foundation for your mod, allowing you to focus on implementing your mod's unique features while Matthiesen Core handles the platform-specific details.

```java
package com.example.mymod;

import dev.matthiesen.matthiesen_core.common.abstracts.AbstractCommonMod;
import dev.matthiesen.libs.faststats.Token;

public final class ExampleModCommon extends AbstractCommonMod {
    public static final String MOD_ID = "examplemod";
    public static final String MOD_NAME = "Example Mod";
    public static @Token final String METRICS_TOKEN = ""; // Provided by FastStats.dev (Optional, can be null to disable metrics for your mod)

    public static final ExampleModCommon INSTANCE = new ExampleModCommon();

    private ExampleModCommon() {
        super(MOD_ID, MOD_NAME);
    }

    @Override
    public @Nullable @Token String getMetricsToken() {
        return METRICS_TOKEN;
    }
    
    @Override
    public void initialize() {
        super.initialize();
        createInfoLog("Initializing Example Mod Common...");
    }
}
```

**AbstractCommonClientMod**: Extend the `AbstractCommonClientMod` class in your client mod codebase to leverage Matthiesen Core's client-specific features. This class provides a foundation for your client-side mod, allowing you to focus on implementing your mod's unique client-side features while Matthiesen Core handles the platform-specific details.

```java
package com.example.mymod.client;

import dev.matthiesen.matthiesen_core.common.abstracts.AbstractCommonClientMod;
import com.example.mymod.ExampleModCommon;

public final class ExampleModCommonClient extends AbstractCommonClientMod {
    public static final ExampleModCommonClient INSTANCE = new ExampleModCommonClient();
    
    private ExampleModCommonClient() {
        super(ExampleModCommon.INSTANCE);
    }

    @Override
    public void initialize() {
        createInfoLog("Initializing Example Mod Common Client logic...");
    }
}
```

## Further Reading

Documentation for this mod can be found at [mods.matthiesen.dev](https://mods.matthiesen.dev/matthiesen-core/)

## FastStats Metrics

This mod uses [FastStats](https://faststats.dev) to collect anonymous usage statistics. This helps the developer understand
how this mod is being used and improve it over time. You can learn more about the data collected and how it is used by visiting
[FastStats: Information](https://faststats.dev/info).

You can also view the data collected by this mod on the [FastStats: Matthiesen Core](https://faststats.dev/project/matthiesen-core) page.

To opt out of this data collection, set the `enabled` property to `false` in the `<game_directory>/config/matthiesen_core/metrics.properties` file.

## License

MIT - see `LICENSE`.
