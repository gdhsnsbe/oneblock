# OneBlock — Fabric 1.21.x Server-Side Mod

A fully server-side OneBlock skyblock mod. Break a single block repeatedly and it cycles through 8 biome-themed phases, each with unique blocks, mobs, and loot chests.

---

## 📦 Building

```bash
./gradlew build
```

The output `.jar` will be in `build/libs/`. Drop it into your server's `mods/` folder.

---

## 🚀 Setup

1. Start your Fabric 1.21.x server with the mod installed.
2. Create your skyblock island (e.g. using a void world generator).
3. Place the starting block (grass block) where your OneBlock should be.
4. Stand **on top** of the block and run:

```
/oneblock set
```

This registers the block below your feet as a OneBlock. It will now cycle through phases each time it is broken.

---

## 🎮 Commands

| Command | Permission | Description |
|---|---|---|
| `/oneblock set` | OP (level 2) | Register the block you're standing on as a OneBlock |
| `/oneblock info` | OP (level 2) | Show phase & break count for the block below you |
| `/oneblock list` | OP (level 2) | List all registered OneBlock positions |

---

## 🌍 Phases

| # | Phase | Key Blocks | Notable Mobs |
|---|---|---|---|
| 1 | 🌿 Plains | Grass, Dirt, Oak | Cow, Sheep, Pig |
| 2 | ⛏️ Underground | Stone, Ores, Gravel | Zombie, Skeleton, Spider |
| 3 | 🌊 Ocean | Prismarine, Coral, Sponge | Drowned, Guardian |
| 4 | 🏜️ Desert | Sand, Sandstone, Gold Ore | Husk, Witch |
| 5 | 🌴 Jungle | Jungle Wood, Emerald Ore, Bamboo | Ocelot, Creeper |
| 6 | ❄️ Tundra | Snow, Ice, Blue Ice | Stray, Polar Bear, Wolf |
| 7 | 🔥 Nether | Netherrack, Ancient Debris, Blaze | Blaze, Ghast, Wither Skeleton |
| 8 | 🌌 The End | End Stone, Purpur, Dragon Egg | Enderman, Shulker |

---

## ✨ Features

- **Phase announcements** — all online players see a chat message when a new phase begins
- **Mob spawning** — random chance (1 in 10) to spawn a phase-appropriate mob above the block
- **Loot chests** — random chance (1 in 25) to spawn a chest with phase loot above the block
- **Persistent data** — block counts survive server restarts (saved via Minecraft's PersistentState API)
- **Multiple OneBlocks** — you can register multiple block positions for multiplayer islands
- **Server-side only** — no client mod required

---

## ⚙️ Customization

Edit `PhaseRegistry.java` to:
- Add/remove phases
- Change block weights (duplicate entries = higher weight)
- Change mob/chest spawn chances (edit `CHEST_CHANCE` and `MOB_CHANCE` in `OneBlockMod.java`)
- Add new loot tables per phase

---

## 📋 Requirements

- Fabric Loader `0.16.x`
- Fabric API `0.102.x`
- Minecraft `1.21.1`
- Java 21
