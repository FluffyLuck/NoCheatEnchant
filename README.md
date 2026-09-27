# NoCheatEnchant

A small server-side mod that adds `/giveenchant <enchantmentid>`. It applies the requested enchantment at level I to the executing player's main-hand item. Use a namespaced ID, for example `/giveenchant minecraft:sharpness`.

The command is available without an operator permission check, matching the supplied examples. Both projects target dedicated loader/version combinations because Fabric 1.21.11 and Forge 1.20.1 are different Minecraft versions.

## Fabric 1.21.11

Requires Java 21 and Gradle.

```sh
cd fabric
gradle build
```

The mod jar is written to `fabric/build/libs/`.

## Forge 1.20.1

Requires Java 17 and Gradle.

```sh
cd forge
gradle build
```

The mod jar is written to `forge/build/libs/`.