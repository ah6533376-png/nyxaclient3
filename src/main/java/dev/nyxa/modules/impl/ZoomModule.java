package dev.nyxa.modules.impl;

import dev.nyxa.modules.Category;
import dev.nyxa.modules.Module;
import net.minecraft.client.MinecraftClient;

public class ZoomModule extends Module {
    private int oldFov = 70;

    public ZoomModule() { super("Zoom", "Cinematic zoom while enabled.", Category.MISC); }

    @Override
    public void onEnable() {
        MinecraftClient c = MinecraftClient.getInstance();
        oldFov = c.options.getFov().getValue();
        c.options.getFov().setValue(30);
    }

    @Override
    public void onDisable() {
        MinecraftClient.getInstance().options.getFov().setValue(oldFov);
    }
}
