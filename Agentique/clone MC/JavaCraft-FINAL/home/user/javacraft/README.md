# JavaCraft

A voxel sandbox game inspired by Minecraft — built in Java 17+ with LWJGL 3.3.4.

## Features
- Procedural terrain generation (Simplex noise, chunks 16×256×16)
- Greedy meshing + face culling for performance
- Frustum culling + chunk streaming (dynamic load/unload)
- First-person camera (WASD + mouse look)
- Physics: gravity, AABB collision, step climb
- Creative mode: flight, block placement/break with raycasting
- Survival mode: health, hunger, damage, respawn
- Crafting system (UI with shaped/shapeless recipes)
- Mobs: pathfinding A*, melee/ranged AI
- Redstone circuits, repeaters, comparators
- Dimensions: Overworld, Nether, End (with bosses)
- Audio engine (OpenAL): ambient sounds, footsteps, blocks, music
- Enchantments & potions (efficiency, sharpness, invisibility, etc.)
- GUI: main menu, HUD, pause, inventory screen

## Requirements
- Java 17 or 21 (JDK, not JRE)
- 64-bit Windows, Linux, or macOS
- OpenGL 3.3 compatible GPU
- ~512 MB RAM

## Build from source

Requires Maven 3.9+.

```bash
mvn clean package -DskipTests
```

The JAR is built with all native libraries bundled (LWJGL).

## Run

```bash
java -jar target/javacraft-1.0-FINAL.jar
```

On first run, LWJGL extracts platform natives from the JAR to a temp directory.
No system install of GLFW or OpenAL needed.

## Controls
- WASD — move camera
- Mouse — look around
- Left click — break block / attack
- Right click — place block
- Space — jump / fly up
- Shift — descend / sneak
- F — toggle fly mode
- E — inventory
- ESC — pause / quit

## Project structure
```
src/main/java/fr/javacraft/
  core/          Main entry, window, render loop
  renderer/      OpenGL rendering, shaders, VAO/VBO
  world/         Chunk storage, block registry
  noise/         Simplex noise generation
  physics/       AABB collision, gravity, step climb
  input/         Keyboard + mouse handlers
  game/          Game modes, player state, health/hunger
  crafting/      Recipe registry, crafting UI
  mobs/          Mob spawning, AI, pathfinding A*
  redstone/      Circuit simulation, components
  dimensions/    Nether/End portals, boss fights
  audio/         OpenAL sound engine
  enchant/       Enchantment table, potion effects
  gui/           Menus, HUD, inventory screens
```

## License
MIT — built for learning and fun.
