# Bigger Ender Chests

A Fabric mod for Minecraft that increases the ender chest inventory size from 3 rows (27 slots) to 6 rows (54 slots).

## Features

- **Doubled Storage**: Ender chests now have 6 rows (54 slots) instead of the default 3 rows (27 slots)
- **Fabric Compatible**: Built for the Fabric mod loader
- **Minecraft 1.21.8**: Compatible with Minecraft version 1.21.8

## Requirements

- **Minecraft**: 1.21.8 or higher
- **Fabric Loader**: 0.17 or higher
- **Java**: 21 or higher

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.8
2. Download the latest release from the [Releases](https://github.com/fsmdev-net/bigger-ender-chests/releases) page
3. Place the `.jar` file in your `mods` folder
4. Launch Minecraft

## Building from Source

### Prerequisites

- Java 21 or higher
- Gradle (or use the included Gradle wrapper)

### Build Steps

1. Clone the repository:
```bash
git clone https://github.com/fsmdev-net/bigger-ender-chests.git
cd bigger-ender-chests
```

2. Build the mod:
```bash
# On Windows
gradlew.bat build

# On Linux/Mac
./gradlew build
```

3. The built mod will be in `build/libs/`

## Development

This mod uses:
- **Fabric Loom**: For mod development and building
- **Mixin**: For modifying Minecraft's behavior at runtime
- **Java 21**: As the target Java version

### Project Structure

```
src/main/java/net/fsmdev/biggerenderchests/
├── BiggerEnderChests.java          # Main mod class
└── mixin/
    ├── EnderChestBlockMixin.java   # Ender chest block modifications
    ├── PlayerEntityMixin.java      # Player entity modifications
    └── SimpleInventoryMixin.java   # Inventory size modifications
```

## CI/CD

This project includes automated CI/CD pipelines:

- **Build Pipeline**: Automatically builds the project on every commit and pull request
- **Release Pipeline**: Automatically creates GitHub releases when tags are created

### Creating a Release

You can create a release using the provided PowerShell script:

```powershell
.\release.ps1 -Version "1.1.0"
```

Or manually:

```bash
git tag -a v1.1.0 -m "Release version 1.1.0"
git push origin v1.1.0
```

## License

This project is licensed under the Creative Commons Attribution 1.0 (CC-BY-1.0) license.

## Links

- **Homepage**: https://fsmdev.net/
- **Source Code**: https://github.com/fsmdev-net/bigger-ender-chests
- **Issues**: https://github.com/fsmdev-net/bigger-ender-chests/issues

## Author

**FsmDev**

---

**Note**: This mod requires Fabric Loader and is not compatible with Forge or other mod loaders.

