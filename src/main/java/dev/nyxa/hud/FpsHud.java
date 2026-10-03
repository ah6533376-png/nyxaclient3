package dev.nyxa.hud;

import net.minecraft.client.MinecraftClient;

public class FpsHud extends TextHud {
    public FpsHud() { super("FPS", "FPS"); }

    @Override
    protected String getText(MinecraftClient c) { return c.getCurrentFps() + " FPS"; }
}
