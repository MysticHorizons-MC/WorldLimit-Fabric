package com.mystichorizonsmc.worldlimit.events;

import com.mystichorizonsmc.worldlimit.logic.AccessResult;
import com.mystichorizonsmc.worldlimit.logic.PlayerStateTracker;
import com.mystichorizonsmc.worldlimit.logic.PlayerStateTracker.LastLocation;
import com.mystichorizonsmc.worldlimit.logic.WorldAccessManager;
import com.mystichorizonsmc.worldlimit.util.MessageUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class TeleportInterceptor {

    private static final Map<String, String> lastDimension = new HashMap<>();
    private static final Map<String, Long> lastWarn = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                String currentDim = player.getWorld().getRegistryKey().getValue().toString();
                String uuid = player.getUuidAsString();

                // Detect dimension change
                if (!currentDim.equals(lastDimension.getOrDefault(uuid, ""))) {
                    AccessResult result = WorldAccessManager.canAccess(player, currentDim);

                    if (result.allowed) {
                        PlayerStateTracker.updateLastSafeLocation(player);
                        lastDimension.put(uuid, currentDim);
                    } else {
                        long now = System.currentTimeMillis();
                        long last = lastWarn.getOrDefault(uuid, 0L);
                        if (now - last >= 5000) {
                            lastWarn.put(uuid, now);

                            LastLocation safe = PlayerStateTracker.getLastSafeLocation(player);
                            player.teleport(safe.world(), safe.x(), safe.y(), safe.z(), safe.yaw(), safe.pitch());
                            player.sendMessage(MessageUtil.colorize("&cAccess denied: " + result.failReason), false);
                        }
                    }
                }
            }
        });
    }
}
