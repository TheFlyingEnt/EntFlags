package net.ent.entflags;

import net.ent.entflags.block.HangingBannerWood;
import net.ent.entflags.platform.Services;

public class CommonClass {

    // Debug: register a mod's hanging banners even when that mod isn't installed. Keep false for releases.
    public static final boolean DEBUG_BOP = false;
    public static final boolean DEBUG_BETTER_END = false;
    public static final boolean DEBUG_BETTER_NETHER = false;

    public static void init() {

        if (Services.PLATFORM.isModLoaded("entflags")) {
            Constants.LOG.info("Ent's Flags... Loaded");
        }
        logEnabled("Biomes O' Plenty", isBiomesOPlentyEnabled(), DEBUG_BOP);
        logEnabled("Better End", isBetterEndEnabled(), DEBUG_BETTER_END);
        logEnabled("Better Nether", isBetterNetherEnabled(), DEBUG_BETTER_NETHER);
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

    private static void logEnabled(String modName, boolean enabled, boolean debug) {
        if (enabled) {
            Constants.LOG.info("{} hanging banners enabled{}", modName, debug ? " (DEBUG)" : "");
        }
    }
}
