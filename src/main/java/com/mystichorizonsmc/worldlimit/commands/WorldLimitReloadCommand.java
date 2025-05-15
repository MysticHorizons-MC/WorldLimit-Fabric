package com.mystichorizonsmc.worldlimit.commands;

import com.mystichorizonsmc.worldlimit.config.WorldLimitConfig;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class WorldLimitReloadCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("worldlimit")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("reload")
                        .executes(ctx -> {
                            WorldLimitConfig.reloadConfig();
                            ctx.getSource().sendFeedback(() -> Text.literal("§a[WorldLimit] Config reloaded."), false);
                            return 1;
                        })
                )
        );
    }
}
