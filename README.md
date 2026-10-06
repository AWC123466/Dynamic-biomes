# Dynamic Biomes

***

## _Did you ever want to..._

* Grow a pale garden around your house?
* Have a mushroom island right next to your base?
* Turn a mountain into an ocean?
* Terraform hell into your second overworld?
* Have complete freedom in how your world is shaped?

## **Dynamic Biomes** allows you to...

**CREATE** biomes in survival mode

**MOVE** your biomes wherever you want, even to another dimension

**DESTROY** forests and oceans, as if they weren't there in the first place

## How it works

The world is split into quads (4x4x4 cubes). The mod counts the blocks in each quad, and the counts change as blocks are placed or broken. Each biome profile gives certain blocks a weight. When a quad's score for a profile passes that profile's threshold, the mod assigns the matching Biome to the quad. This is the same biome data `/fillbiome` writes, so grass and foliage colour, weather, mob spawns and sky all follow the new biome.

***

Profiles come in types, and a profile's type sets its priority when several match:

* **Parent** – base biomes such as snow, ocean, swamp, jungle and badlands.
* **Mixed** – a blend of two parents, e.g. frozen ocean.
* **Beach, River, Forest, Underground, Special** – child biomes that only apply if their parent biome(s) apply.

Each profile can also be limited to specific dimensions.

## Requirements

* Minecraft 26.2
* Fabric Loader 0.19.5 or newer
* Fabric API
* Java 25 or newer

## Configuration

The config file is `config/dynamic-biomes.json`. It reloads on datapack reload.

| Option | Default | Description                                                                                      |
|---|---------|--------------------------------------------------------------------------------------------------|
| `DefaultProfilesOn` | `true`  | Registers the built-in profiles. turn off if you want to write your own profiles from scratch    |
| `NetherGrassRecolor` | `true`  | Changes grass color in nether biomes to the color of their respective ground                     |
| `PerPlayerBiomeUpdatingOn` | `false` | Increase the amount of quads processed per tick according to the amount of players on the server |
| `QuadsPerTick` | `10`    | Amount of quads processed per tick                                                               |
| `Radius` | `4`     | Radius around each player in which biomes are updated (in quads)                                 |
| `DebugMenuOn` | `false` | Enable the debug menu for biome values                                                           |

## Adding your own profiles

Profiles are built through `BiomeProfile.builder(id)`:

```java
BiomeProfile.builder(DynamicBiomes.id("my_biome"))// id of the profile. two profiles with the same id cant exist
    .addBlocks(<WEIGHT>, Blocks.<BLOCK>, Blocks.<BLOCK>)// (points for each block, block, block...)
    .threshold(<THRESHOLD>)// point threshold to apply the biome
    .targetBiome(Biomes.<BIOME>) // biome applied by the profile
    .biomeType(null, null, BiomeType.<TYPE>)// (parent biome resourceKey(if null, the profile is considered a parent), secondary biome ResourceKey (only used for mixed biomes), biome type)
    .parentBiomeType(ParentBiomeType.<TYPE>)// only for parent biomes
    .applicableDimensions(Level.<DIMENSION>)// dimensions that the profile applies to
    .build();
```

Register the result with `BiomeProfileRegistry`. See `src/main/java/com/dynamicbiomes/biome` for the built-in profiles.

## License

Copyright (C) 2026 AWC. Licensed under the [GNU LGPL v3.0](LICENSE). You can depend on this mod and use its API from your own mods, under any license. If you modify and distribute the mod itself, you must share your changes under the LGPL.
