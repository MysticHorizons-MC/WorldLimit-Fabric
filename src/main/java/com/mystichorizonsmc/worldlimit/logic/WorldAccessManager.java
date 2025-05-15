package com.mystichorizonsmc.worldlimit.logic;

import com.mystichorizonsmc.worldlimit.WorldLimitMod;
import com.mystichorizonsmc.worldlimit.config.WorldLimitConfig;
import com.mystichorizonsmc.worldlimit.config.WorldLimitConfig.Requirement;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class WorldAccessManager {

    public static AccessResult canAccess(ServerPlayerEntity player, String dimension) {
        if (player.hasPermissionLevel(2)) return AccessResult.allow(); // OP bypass

        Requirement req = WorldLimitConfig.getRequirement(dimension);
        if (req == null) return AccessResult.allow(); // No config = unrestricted

        double level = 0;
        if (WorldLimitMod.isPlayerExLoaded) {
            try {
                EntityAttribute attr = Registries.ATTRIBUTE.get(new Identifier("playerex:level"));
                if (attr != null) {
                    level = player.getAttributeValue(attr);
                    if (level < req.level) {
                        return AccessResult.deny("You need PlayerEX level " + req.level + " to enter.");
                    }
                } else {
                    return AccessResult.deny("PlayerEX level attribute not registered.");
                }
            } catch (Exception e) {
                return AccessResult.deny("Unable to fetch PlayerEX level.");
            }
        } else {
            if (player.experienceLevel < req.level) {
                return AccessResult.deny("You need level " + req.level + " to enter.");
            }
        }

        if (req.xp > 0 && player.totalExperience < req.xp) {
            return AccessResult.deny("You need " + req.xp + " XP to enter.");
        }
            //TODO: Fix Item Detection
//        if (req.requiredItems != null) {
//            for (String itemId : req.requiredItems) {
//                boolean found = player.getInventory().main.stream()
//                        .anyMatch(stack -> stack.getItem().toString().equals(itemId));
//                if (!found) return AccessResult.deny("Missing required item: " + itemId);
//            }
//        }

        // Subtract XP if configured
        if (req.xp > 0) {
            player.addExperience(-req.xp);
            return new AccessResult(true, true, null);
        }

        return AccessResult.allow();
    }
}
