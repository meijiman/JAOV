# JAOV — MOBA Game

A top-down 2D MOBA prototype built with **libGDX** and **Ashley ECS**, inspired by Arena of Valor / League of Legends.

## Features (current)

- Tiled map: 100×100 tiles @ 64px = 6400×6400 world units with lanes, river, and bases
- Hero movement via right-click with run/idle animation and sprite flip
- 18 towers (3 per lane per team) loaded from TMX object layer, rendered as team-coloured sprites
- Minimap always visible in the top-left corner (screen-space, 200×200px)
- Ashley ECS: entities are composed from components and processed by dedicated systems

## Tech stack

| Library | Version | Purpose |
|---|---|---|
| libGDX | 1.13.1 | Rendering, input, asset loading |
| Ashley | 1.7.4 | Entity Component System |
| LWJGL3 | (bundled) | Desktop backend |
| Java | 19 | Language |
| Gradle | 9.4 | Build system |

## Project structure

```
JAOV/
├── app/
│   ├── build.gradle                  # Dependencies, mainClass, workingDir
│   └── src/main/
│       ├── assets/                   # All runtime assets (working dir for run task)
│       │   ├── maps/
│       │   │   ├── moba_map.tmx      # Tiled map: Ground layer + Towers object layer
│       │   │   └── tileset.png       # 4-tile tileset (grass, lane, river, base)
│       │   ├── hero_idle_0-3.png     # Hero idle animation frames
│       │   ├── hero_run_0-3.png      # Hero run animation frames
│       │   ├── tower_blue.png        # Blue team tower sprite (64×64)
│       │   └── tower_red.png         # Red team tower sprite (64×64)
│       └── java/com/jaov/moba/
│           ├── DesktopLauncher.java  # Entry point — Lwjgl3 config (1280×720, 60fps)
│           ├── MobaGame.java         # libGDX Game — creates SpriteBatch, sets GameScreen
│           ├── GameScreen.java       # Main screen — camera, input, render loop
│           ├── MiniMap.java          # Screen-space minimap renderer (200×200px, top-left)
│           ├── components/
│           │   ├── PositionComponent.java    # World position (Vector2)
│           │   ├── MovementComponent.java    # Speed, target, moving flag, facingRight
│           │   ├── AnimationComponent.java   # Idle/run frame arrays, frame timer
│           │   ├── TextureComponent.java     # Static texture + render size
│           │   └── TowerComponent.java       # team (blue/red), lane (top/mid/bot), rank (inner/mid/outer)
│           └── systems/
│               ├── MovementSystem.java       # Moves entities toward their target
│               ├── AnimationSystem.java      # Advances animation frame timer
│               └── RenderSystem.java         # Draws sprites, towers, fallback circles
├── gradle/libs.versions.toml         # Version catalog (gdx, ashley, testng)
└── settings.gradle                   # Root project name, includes :app
```

## Running

```bash
./gradlew :app:run
```

> **Note:** The run task sets `workingDir = app/src/main/assets` so all asset paths are relative to that directory.

## Controls

| Input | Action |
|---|---|
| Right-click | Move hero to cursor position |

## Map layout

The map is modelled after a standard 3-lane MOBA:

```
Red base (top-right)
    ↑ top lane (vertical) ←→ top lane (horizontal)
    River (diagonal: top-left → bottom-right)
    Mid lane (diagonal: bottom-left → top-right)
    Bot lane (horizontal) ↕ bot lane (vertical)
Blue base (bottom-left)
```

Each team has **9 towers**: inner (closest to base), mid, and outer per lane. Tower positions are defined as objects in the `Towers` layer of `moba_map.tmx`.

## Contributing

### Prerequisites

- JDK 19+
- No IDE required — Gradle wraps everything

### Adding a new component

1. Create `components/FooComponent.java` implementing `com.badlogic.ashley.core.Component`
2. Add it to an entity in `GameScreen.java` (or wherever the entity is created)
3. Query it in a system via `ComponentMapper.getFor(FooComponent.class)`

### Adding a new system

1. Create `systems/FooSystem.java` extending `IteratingSystem` (or `IntervalSystem` for tick-based logic)
2. Define the `Family` filter in the constructor (which components the system requires)
3. Register it in `GameScreen`: `engine.addSystem(new FooSystem())`
4. Systems run in priority order — lower priority number = runs first (default 0)

### Modifying the map

The map is edited with [Tiled Map Editor](https://www.mapeditor.org/). Open `app/src/main/assets/maps/moba_map.tmx`.

- **Ground layer** — CSV tile data using `tileset.png` (gid 1=grass, 2=lane, 3=river, 4=base)
- **Towers object layer** — each object needs `type=tower` and custom properties: `team`, `lane`, `rank`

### Code style

- Standard Java conventions, no enforced formatter
- Keep ECS separation: components are pure data, systems contain all logic
- Asset loading belongs in constructors; rendering logic belongs in `RenderSystem`
