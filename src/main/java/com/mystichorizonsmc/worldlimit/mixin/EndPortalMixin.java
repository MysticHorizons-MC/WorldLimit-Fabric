package com.mystichorizonsmc.worldlimit.mixin;

import com.mystichorizonsmc.worldlimit.config.WorldLimitConfig;
import com.mystichorizonsmc.worldlimit.logic.AccessResult;
import com.mystichorizonsmc.worldlimit.logic.WorldAccessManager;
import com.mystichorizonsmc.worldlimit.util.MessageUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class EndPortalMixin {

    @Inject(method = "moveToWorld", at = @At("HEAD"), cancellable = true)
    private void worldlimit$preventTeleport(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
        String targetDim = destination.getRegistryKey().getValue().toString();

        AccessResult result = WorldAccessManager.canAccess(player, targetDim);
        if (result.allowed) return;

        // ❌ Cancel dimension switch
        cir.cancel();

        // 🌀 Push player back based on portal type
        pushPlayerFromPortal(player);

        // 💬 Denial message
        player.sendMessage(MessageUtil.colorize("&cYou cannot enter this dimension: " + result.failReason), false);
    }

    private void pushPlayerFromPortal(ServerPlayerEntity player) {
        BlockPos pos = player.getBlockPos();
        World world = player.getWorld();
        Block block = world.getBlockState(pos.down()).getBlock();
        Identifier blockId = Registries.BLOCK.getId(block);

        Vec3d motion;

        if (blockId != null && WorldLimitConfig.groundPortals.contains(blockId)) {
            // 🌀 Ground portal — arc back and up
            Vec3d back = player.getRotationVector().normalize().multiply(-1.5);
            motion = new Vec3d(back.x, 0.8, back.z);

        } else if (blockId != null && WorldLimitConfig.framePortals.contains(blockId)) {
            // 🌀 Frame portal — strong backward, less vertical
            Vec3d back = player.getRotationVector().normalize().multiply(-2.5);
            motion = new Vec3d(back.x, 0.3, back.z);

        } else {
            // Default push up
            motion = new Vec3d(0, 1.0, 0);
        }

        player.setVelocity(motion);
        player.velocityModified = true;
        player.fallDistance = 0;
    }
}
