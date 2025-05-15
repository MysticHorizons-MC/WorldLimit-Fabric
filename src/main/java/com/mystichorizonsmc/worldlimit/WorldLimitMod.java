package com.mystichorizonsmc.worldlimit;

import com.mystichorizonsmc.worldlimit.commands.WorldLimitReloadCommand;
import com.mystichorizonsmc.worldlimit.config.WorldLimitConfig;
import com.mystichorizonsmc.worldlimit.events.TeleportInterceptor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

public class WorldLimitMod implements ModInitializer {

    public static final String MOD_ID = "worldlimit";
    public static boolean isPlayerExLoaded = false;

    @Override
    public void onInitialize() {
        isPlayerExLoaded = FabricLoader.getInstance().isModLoaded("playerex");

        System.out.println("[WorldLimit] Initializing...");
        if (isPlayerExLoaded) {
            System.out.println("[WorldLimit] PlayerEX detected. Level-based checks enabled.");
        } else {
            System.out.println("[WorldLimit] PlayerEX not detected. Falling back to XP levels.");
        }

        WorldLimitConfig.loadConfig();
        TeleportInterceptor.register();

        registerCommands();

        System.out.println("[WorldLimit] Successfully initialized.");
    }


    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> WorldLimitReloadCommand.register(dispatcher));
    }
}
