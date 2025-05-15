
# 🌍 WorldLimit (Fabric Mod)

**WorldLimit** is a powerful server-side Fabric mod that restricts access to specific dimensions or worlds unless players meet customizable requirements. It’s perfect for RPG progression servers, MMO-style gameplay, or challenge-based survival worlds.

---

## 🚪 Features

- 🔐 **Dimension Access Restrictions**  
  Block player teleportation or portal use unless they meet set requirements.

- ⚙️ **Custom Requirements per World**  
  Restrict access based on:
  - PlayerEx level (Director's Cut)
  - XP amount
  - Item possession (In Progress)
  - Kills of specific mobs (upcoming)
  
- 🌀 **Teleport Cancellation & Pushback**  
  Stops players mid-teleport (End, Twilight Forest, Eden Ring, etc.) and either:
  - Teleports them back to a safe location
  - Pushes them away from the portal (if configured)

- 🧠 **Smart Entry Detection**  
  Uses server tick and event-based logic to detect portal entry and unauthorized travel, even when teleportation is triggered by commands or other mods.

- 🧭 **Last Known Safe Location Tracking**  
  Saves a safe location before travel, returning the player if denied.

- 🛡️ **Permission Support**  
  Operators and users with specific permissions can bypass restrictions.

- 📂 **Modular Config System**  
  All config files are located in:

> config/WorldLimit/


---

## 🧾 Configuration

### 📄 `worldlimit_config.jsonc`
```jsonc
{
  // The dimension ID must be the full namespaced string (e.g. "minecraft:the_end").
  // Each dimension entry defines the requirements for entering that world.

  "minecraft:the_end": {
    // Required PlayerEX Level (if PlayerEX is installed).
    // If PlayerEX is NOT installed, this is ignored.
    // Set to 0 to disable level requirement.
    "level": 30,

    // Required vanilla XP levels if PlayerEX is not present.
    // This XP will be SUBTRACTED from the player when entering the dimension.
    // Set to 0 to disable XP-based restriction.
    "xp": 500

    // Required items the player must be holding in their inventory to enter.
    // You can include any item ID — vanilla or modded.
    // WARNING: THIS IS NOT WORKING YET!
    // To disable item requirement, set this to an empty array: []
//    "requiredItems": ["minecraft:ender_pearl"]
  },

  "minecraft:the_nether": {
    "level": 15,
    "xp": 100
//    "requiredItems": ["minecraft:flint_and_steel"]
  },

  // Example for a modded dimension (replace with real mod ID):
  "modid:sky_realm": {
    "level": 50,
    "xp": 0 // No XP cost, just PlayerEX level
//    "requiredItems": [] // No item requirement
  }
}
````

* `level`: Required PlayerEX level
* `xp`: Required vanilla XP
* `items`: Required items to enter (more features coming)

> NOTE: When `PlayerEX` is not detected it will only use the `xp` option or any other option that is enabled in the config.

---

### 📄 `portal_blocks.json`

Defines what blocks are treated as **portals** for denial logic (for pushback / detection).

```json
{
  "ground_portals": [
    "edenring:portal_block",
    "twilightforest:portal",
    "aether:glowstone_portal"
  ],
  "frame_portals": [
    "minecraft:nether_portal",
    "deeperdarker:echo_portal"
  ]
}
```

---

## 🧪 Compatibility

* ✅ **Fabric API**
* ✅ **PlayerEx: Director’s Cut** *(Level checking)*
* 🔄 **KubeJS Integration Planned**
* ❌ Not required on client

---

## 📦 Installation

1. Install [Fabric Loader](https://fabricmc.net/use/)
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Drop `worldlimit-x.x.x.jar` into your `/mods` folder
4. Customize configs in `config/WorldLimit/`

---

## 📢 Commands

* `/worldlimit reload`
  Reloads all WorldLimit configs without a restart.

---

## 🔒 Permissions

| Node                | Description                 |
| ------------------- | --------------------------- |
| `worldlimit.bypass` | Allows bypassing all limits |
| `worldlimit.reload` | Allows using reload command |

---

## 🔮 Planned Features

* Mob kill requirements (e.g. kill 10 blaze to enter Nether)
* Timed restrictions (e.g. unlock after X playtime)
* GUI warnings and clickable prompts
* Command reward hooks for first-time access

---

## 🧑‍💻 Credits

* Developed by [MysticHorizonsMC](https://github.com/MysticHorizons-MC)
* Powered by Fabric
* Special thanks to the PlayerEx and Fabric communities

---

## 📜 License

This mod is licensed under the **MIT License**. You are free to use, modify, and redistribute it.

---

## ❤️ Support

If you enjoy using **WorldLimit**, consider giving it a ⭐ on GitHub or supporting the MysticHorizons project.
