package com.mystichorizonsmc.worldlimit.logic;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStateTracker {
    private static final Map<UUID, LastLocation> lastSafeLocations = new HashMap<>();

    public static void updateLastSafeLocation(ServerPlayerEntity player) {
        ServerWorld world = (ServerWorld) player.getWorld();
        lastSafeLocations.put(player.getUuid(), new LastLocation(world, player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch()));
    }

    public static LastLocation getLastSafeLocation(ServerPlayerEntity player) {
        return lastSafeLocations.getOrDefault(player.getUuid(), new LastLocation((ServerWorld) player.getServerWorld(), 0, 100, 0, 0f, 0f));
    }

    public record LastLocation(ServerWorld world, double x, double y, double z, float yaw, float pitch) {}
}
