package dev.nyxa.modules.impl;

import dev.nyxa.modules.Category;
import dev.nyxa.modules.Module;
import net.minecraft.client.MinecraftClient;

public class FullbrightModule extends Module {
    private double oldGamma = 1.0;

    public FullbrightModule() { super("Fullbright", "See everything at full brightness.", Category.RENDER); }

    @Override
    public void onEnable() {
        MinecraftClient c = MinecraftClient.getInstance();
        oldGamma = c.options.getGamma().getValue();
        c.options.getGamma().setValue(100.0);
    }

    @Override
    public void onDisable() {
        MinecraftClient.getInstance().options.getGamma().setValue(oldGamma);
    }
}
