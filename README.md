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

## Docs

Documentation for this mod can be found at [mods.matthiesen.dev](https://mods.matthiesen.dev/matthiesen-core/)

## Version Compatibility

| Minecraft Version | Mod Version |
|-------------------|-------------|
| 1.21.1            | 1.x.x       |

## FastStats Metrics

This mod uses [FastStats](https://faststats.dev) to collect anonymous usage statistics. This helps the developer understand
how this mod is being used and improve it over time. You can learn more about the data collected and how it is used by visiting
[FastStats: Information](https://faststats.dev/info).

You can also view the data collected by this mod on the [FastStats: Matthiesen Core](https://faststats.dev/project/matthiesen-core) page.

To opt out of this data collection, set the `enabled` property to `false` in the `<game_directory>/config/matthiesen_core/metrics.properties` file.

## License

MIT - see `LICENSE`.
