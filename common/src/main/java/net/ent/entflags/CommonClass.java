package net.ent.entflags;

import net.ent.entflags.block.HangingBannerWood;
import net.ent.entflags.platform.Services;

public class CommonClass {

    // Debug: register a mod's hanging banners even when that mod isn't installed. 
    // IMPORTANT: Keep false for releases.
    public static final boolean DEBUG_BOP = true;
    public static final boolean DEBUG_BETTER_END = true;
    public static final boolean DEBUG_BETTER_NETHER = true;
    public static final boolean DEBUG_TWILIGHT_FOREST = false;
    public static final boolean DEBUG_AETHER = false;

    public static void init() {

        if (Services.PLATFORM.isModLoaded("entflags")) {
            Constants.LOG.info("Ent's Flags... Loaded");
        }
        logEnabled("Biomes O' Plenty", isBiomesOPlentyEnabled(), DEBUG_BOP);
        logEnabled("Better End", isBetterEndEnabled(), DEBUG_BETTER_END);
        logEnabled("Better Nether", isBetterNetherEnabled(), DEBUG_BETTER_NETHER);
        logEnabled("Twilight Forest", isTwilightForestEnabled(), DEBUG_TWILIGHT_FOREST);
        logEnabled("Aether", isAetherEnabled(), DEBUG_AETHER);
    }

    public static boolean isBiomesOPlentyEnabled() {
        return DEBUG_BOP || Services.PLATFORM.isModLoaded(HangingBannerWood.BIOMES_O_PLENTY_ID);
    }

    public static boolean isBetterEndEnabled() {
        return DEBUG_BETTER_END || Services.PLATFORM.isModLoaded(HangingBannerWood.BETTER_END_ID);
    }

    public static boolean isBetterNetherEnabled() {
        return DEBUG_BETTER_NETHER || Services.PLATFORM.isModLoaded(HangingBannerWood.BETTER_NETHER_ID);
    }

    public static boolean isTwilightForestEnabled() {
        return DEBUG_TWILIGHT_FOREST || Services.PLATFORM.isModLoaded(HangingBannerWood.TWILIGHT_FOREST_ID);
    }

    public static boolean isAetherEnabled() {
        return DEBUG_AETHER || Services.PLATFORM.isModLoaded(HangingBannerWood.AETHER_ID);
    }

    private static void logEnabled(String modName, boolean enabled, boolean debug) {
        if (enabled) {
            Constants.LOG.info("{} hanging banners enabled{}", modName, debug ? " (DEBUG)" : "");
        }
    }
}
