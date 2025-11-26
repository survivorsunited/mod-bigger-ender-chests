# Bigger Ender Chests

A Fabric mod for Minecraft that increases the ender chest inventory size from 3 rows (27 slots) to 6 rows (54 slots).

## Features

- **Doubled Storage**: Ender chests now have 6 rows (54 slots) instead of the default 3 rows (27 slots)
- **Fabric Compatible**: Built for the Fabric mod loader
- **Multi-Version Support**: Supports Minecraft versions 1.21.6, 1.21.8, 1.21.9, and 1.21.10

## Requirements

- **Minecraft**: 1.21.6, 1.21.8, 1.21.9, or 1.21.10 (see releases for version-specific builds)
- **Fabric Loader**: 0.17.3 or higher
- **Java**: 21 or higher

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for your Minecraft version
2. Download the appropriate version from the [Releases](https://github.com/fsmdev-net/bigger-ender-chests/releases) page
   - Look for the JAR file matching your Minecraft version (e.g., `biggerenderchests-1.1.0-1.21.8.jar` for Minecraft 1.21.8)
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
# Build for all Minecraft versions (Windows)
.\build.ps1

# Build for all Minecraft versions (Linux/Mac)
./gradlew build

# Build for a specific Minecraft version (Windows)
.\build.ps1 -MinecraftVersion "1.21.8"

# Build for a specific Minecraft version (Linux/Mac)
./gradlew build -Pminecraft_version=1.21.8
```

3. The built mod will be in `build/libs/`

### Version Matrix Build System

This project uses a version matrix system to build for multiple Minecraft versions simultaneously. The configuration is managed through `versions.json`.

#### versions.json Structure

The `versions.json` file defines version mappings for each supported Minecraft version. Each entry contains:

```json
{
  "1.21.8": {
    "yarn_mappings": "1.21.8+build.1",      // Yarn mappings version
    "loader_version": "0.17.3",            // Fabric Loader version
    "fabric_version": "0.134.0+1.21.8",    // Fabric API version (optional)
    "loom_version": "1.7-SNAPSHOT",        // Fabric Loom plugin version
    "gradle_version": "8.14",               // Gradle version
    "java_version": 21                      // Required Java version
  }
}
```

#### Field Descriptions

- **yarn_mappings**: The Yarn mappings version for the Minecraft version. Format: `{mc_version}+build.{build_number}`
- **loader_version**: The Fabric Loader version required for this Minecraft version
- **fabric_version**: The Fabric API version (optional, for reference)
- **loom_version**: The Fabric Loom Gradle plugin version
- **gradle_version**: The Gradle version to use for building
- **java_version**: The minimum Java version required

#### How It Works

1. **Build Process**:
   - When building with `-Pminecraft_version=<version>`, Gradle reads `versions.json`
   - It extracts the version-specific configuration for that Minecraft version
   - The build system uses these values to configure dependencies and build settings

2. **CI/CD Integration**:
   - GitHub Actions workflows use a matrix strategy to build all versions in parallel
   - Each matrix job updates `gradle-wrapper.properties` with the correct Gradle version
   - All built JARs are collected and attached to releases

3. **Local Development**:
   - Use `build.ps1` (Windows) to build locally with the same process as CI/CD
   - Or use `gradlew build -Pminecraft_version=<version>` directly
   - Without specifying a version, uses defaults from `gradle.properties`

#### Adding a New Minecraft Version

1. **Find Version Information**:
   - Check [Fabric Development](https://fabricmc.net/develop) for the latest mappings and loader versions
   - Determine compatible Gradle and Loom versions

2. **Add to versions.json**:
```json
{
  "1.21.11": {
    "yarn_mappings": "1.21.11+build.1",
    "loader_version": "0.17.3",
    "fabric_version": "0.135.0+1.21.11",
    "loom_version": "1.7-SNAPSHOT",
    "gradle_version": "8.14",
    "java_version": 21
  }
}
```

3. **Update CI/CD Matrix**:
   - Add the version to `.github/workflows/build.yml` matrix:
   ```yaml
   minecraft_version: ["1.21.6", "1.21.8", "1.21.9", "1.21.10", "1.21.11"]
   ```
   - Add to `.github/workflows/release.yml` matrix as well

4. **Update README**:
   - Update supported versions in documentation

#### Updating Existing Versions

To update a version's configuration:

1. Edit the corresponding entry in `versions.json`
2. Update the field(s) that changed (e.g., `yarn_mappings`, `loader_version`)
3. Commit and push - CI/CD will automatically rebuild with new versions

#### Version-Specific Build Output

Each build produces a JAR file with the version in the filename:
- Format: `{mod_name}-{mod_version}-{minecraft_version}.jar`
- Example: `biggerenderchests-1.1.0-1.21.8.jar`

The built JAR's `fabric.mod.json` is automatically updated with:
- Correct Minecraft version requirement: `"minecraft": ">=1.21.8"`
- Correct Fabric Loader version requirement: `"fabricloader": ">=0.17.3"`

#### Troubleshooting

**Version not found error**:
- Ensure the Minecraft version exists in `versions.json`
- Check for typos in the version string (must match exactly)

**Build fails with wrong Gradle version**:
- Verify `gradle_version` in `versions.json` is correct
- The CI/CD workflow automatically updates the wrapper, but locally you may need to run `.\gradlew wrapper --gradle-version <version>`

**Dependency resolution errors**:
- Check that `yarn_mappings`, `loader_version`, and `fabric_version` are correct for the Minecraft version
- Verify versions are available in Fabric's Maven repositories

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

