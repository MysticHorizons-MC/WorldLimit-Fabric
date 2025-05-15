package com.mystichorizonsmc.worldlimit.config;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class WorldLimitConfig {

    public static class Requirement {
        public int level = 0;
        public int xp = 0;
    }

    public static Map<String, Requirement> restrictedWorlds = new HashMap<>();
    public static Set<Identifier> groundPortals = new HashSet<>();
    public static Set<Identifier> framePortals = new HashSet<>();

    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("WorldLimit");
    private static final Path MAIN_CONFIG_PATH = CONFIG_DIR.resolve("worldlimit_config.jsonc");
    private static final Path PORTAL_BLOCKS_PATH = CONFIG_DIR.resolve("portal_blocks.json");

    public static void loadConfig() {
        try {
            if (Files.notExists(MAIN_CONFIG_PATH)) {
                copyDefaultConfig("config/WorldLimit/worldlimit_config.jsonc", MAIN_CONFIG_PATH);
            }

            if (Files.notExists(PORTAL_BLOCKS_PATH)) {
                copyDefaultConfig("config/WorldLimit/portal_blocks.json", PORTAL_BLOCKS_PATH);
            }

            // Load world restriction config
            String rawJson = Files.readString(MAIN_CONFIG_PATH, StandardCharsets.UTF_8);
            String cleanedJson = stripJsonComments(rawJson);
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Requirement>>() {}.getType();
            restrictedWorlds = gson.fromJson(cleanedJson, type);

            // Load portal config
            try (Reader reader = Files.newBufferedReader(PORTAL_BLOCKS_PATH)) {
                JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                groundPortals.clear();
                framePortals.clear();

                if (obj.has("ground_portals")) {
                    for (JsonElement el : obj.getAsJsonArray("ground_portals")) {
                        groundPortals.add(new Identifier(el.getAsString()));
                    }
                }
                if (obj.has("frame_portals")) {
                    for (JsonElement el : obj.getAsJsonArray("frame_portals")) {
                        framePortals.add(new Identifier(el.getAsString()));
                    }
                }
            }

            System.out.println("[WorldLimit] Config loaded successfully from: " + CONFIG_DIR);
        } catch (Exception e) {
            System.err.println("[WorldLimit] Failed to load config:");
            e.printStackTrace();
        }
    }

    public static void reloadConfig() {
        loadConfig();
    }

    public static Requirement getRequirement(String dimension) {
        return restrictedWorlds.get(dimension);
    }

    private static void copyDefaultConfig(String resourcePath, Path targetPath) throws IOException {
        InputStream in = WorldLimitConfig.class.getClassLoader().getResourceAsStream(resourcePath);
        if (in == null) {
            throw new FileNotFoundException("Missing default config resource: " + resourcePath);
        }
        Files.createDirectories(targetPath.getParent());
        Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }

    private static String stripJsonComments(String input) {
        input = input.replaceAll("(?m)^\\s*//.*", "");
        input = input.replaceAll("(?s)/\\*.*?\\*/", "");
        return input;
    }
}
