package net.ent.entflags;

import net.ent.entflags.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

public class CommonClass {

    public static void init() {

        if (Services.PLATFORM.isModLoaded("entflags")) {
            Constants.LOG.info("Ent's Flags... Loaded");
        }
    }
}