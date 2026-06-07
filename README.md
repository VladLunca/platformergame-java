# Platformer Game

A 2D side-scrolling platformer written in pure Java (AWT/Swing), featuring animated characters, tile-based maps, enemy AI, and a gate-based level completion system.

---

## Gameplay

- Navigate through **3 levels**, each with a unique tile theme (grass, castle, stone)
- Defeat all **dragons** on the map — only then do the **portal gates** unlock
- Walk through an unlocked gate to complete the level
- You have **3 hearts**; losing all of them triggers a Game Over

---

## Controls

| Key | Action |
|-----|--------|
| `A` | Move left |
| `D` | Move right |
| `W` | Jump |
| `Space` | Attack |
| `Escape` | Pause / Resume |

---

## Enemies

| Enemy | Behaviour |
|-------|-----------|
| **Snake** | Patrols back and forth on platforms; deals 1 damage on contact |
| **Dragon** | Boss enemy; must be killed to unlock the level gates |

---

## Levels

| Level | Map | Key enemies |
|-------|-----|-------------|
| 1 | Green grass world | 4 snakes + 1 dragon |
| 2 | Red castle | 3 snakes + 1 dragon |
| 3 | Purple stone dungeon | 3 snakes + 1 dragon |

---

## Project Structure

```
app/
├── src/
│   ├── Main.java                   # Entry point (1280×640 window)
│   ├── Game.java                   # Game loop, state machine, draw/update dispatch
│   ├── entity/
│   │   ├── Entity.java             # Abstract base for all entities
│   │   ├── player/Player.java      # Player movement, jump, attack, camera
│   │   ├── enamy/Snake.java        # Snake patrol AI
│   │   ├── enamy/Dragon.java       # Dragon (boss)
│   │   ├── prop/Portal.java        # Interactive gate — unlocks after dragons die
│   │   └── utils/MoveInfo.java     # Tile collision helpers
│   ├── levelmanager/LevelManager.java  # Level loading, enemy/portal management, HUD
│   ├── map/
│   │   ├── Map.java                # Map data model
│   │   └── MapManager.java         # JSON map loader (Gson)
│   ├── graphics/
│   │   ├── assets/Assets.java      # Central image registry
│   │   ├── assets/*Factory.java    # Per-category asset loaders
│   │   ├── tiles/Tile.java         # Tile constants (64×64 px)
│   │   └── utils/SpriteSheet.java  # Sprite sheet cropper
│   ├── handle/
│   │   ├── KeyHandler.java         # Keyboard input
│   │   └── MouseHandler.java       # Mouse click routing per game state
│   └── utils/
│       ├── GameStates.java         # MENU, PLAYING, PAUSE, GAME_OVER, LEVEL_WON
│       └── TileID.java             # Tile ID → asset key mapping
└── res/textures/
    ├── maps/maps.json              # Map grids + enemy spawn data (20×60 tiles)
    ├── player/                     # Player sprite sheets (run, jump, attack, idle, hurt)
    ├── enemies/                    # Snake & dragon sprites
    └── utils/                      # Props, tiles, UI, lives, gates
```

---

## How to Run

Requires **Java 17+** and the Gson library (`gson-2.13.2.jar` included in the repo root).

### IntelliJ IDEA

1. Open the `app/` folder as an IntelliJ project
2. Add `gson-2.13.2.jar` to the module dependencies (*File → Project Structure → Libraries*)
3. Run `Main`

### Command line

```bash
javac -cp ".;../gson-2.13.2.jar" -d out src/**/*.java src/*.java
java  -cp "out;../gson-2.13.2.jar" Main
```

---

## Used Assets

### 🧙 Characters

| Asset | Author | License | Link |
|-------|--------|---------|------|
| **Chibi Knight** | Segel T | — | [Facebook](https://www.facebook.com/Segel.T) |

---

### 🐍 Enemies

| Asset | Author | License | Link |
|-------|--------|---------|------|
| **Stendhal Dragons** | Kimmo Rundelin (kiheru), uploaded by StendhalGame | CC BY-SA 3.0 | [OpenGameArt](https://opengameart.org/content/stendhal-dragons) |

---

### 🏰 Tilesets & Environments

| Asset | Author | License | Link |
|-------|--------|---------|------|
| **Platformer Castle Tileset** | thomaswp (PlatForge) — Artists: Summer Thaxton & Stafford McIntyre | CC BY-SA 3.0 | [OpenGameArt](https://opengameart.org/content/platformer-castle-tileset) |
| **Platformer Night Tileset** | thomaswp (PlatForge) — Artists: Summer Thaxton & Hannah Cohan | CC BY-SA 3.0 | [OpenGameArt](https://opengameart.org/content/platformer-night-tileset) |
| **Platformer Grass Tileset** | thomaswp (PlatForge) — Artists: Summer Thaxton & Hannah Cohan | CC BY-SA 3.0 | [OpenGameArt](https://opengameart.org/content/platformer-grass-tileset) |
| **Golden Treasures** | Ironthunder *(special thanks to Marcus Brumfield)* | CC BY 4.0 | [OpenGameArt](https://opengameart.org/content/golden-treasures) |

---

### 🔥 Effects

| Asset | Author | License | Link |
|-------|--------|---------|------|
| **Fireball Spell** | Clint Bellanger | CC BY 3.0 | [OpenGameArt](https://opengameart.org/content/fireball-spell) |

---

⚠️ **Note:** Some assets may have been resized or scaled to fit the game's requirements. Original files remain the property of their respective authors.

## ❤️ Special Thanks

A big thank you to **Segel T** for the charming Chibi Knight character, and to **Clint Bellanger**, **Disthron**, **Jason-Em (GrafxKid)**, **thomaswp**, and **Kimmo Rundelin** for making free, high-quality game assets available to everyone through **OpenGameArt.org**. You make indie game development possible. 🙏

---

## 🔗 Resources

- [OpenGameArt.org](https://opengameart.org) — Free game assets for everyone
- [PlatForge Collection on OGA](https://opengameart.org/content/art-from-platforge) — All PlatForge art assets
  ##To DO : fix the bug with the knockback, make the dragon attack too, fix level 2 and 3 maps, add interactions with flowers and torch tiles
  
