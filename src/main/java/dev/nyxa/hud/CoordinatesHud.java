package dev.nyxa.hud;

import net.minecraft.client.MinecraftClient;

public class CoordinatesHud extends TextHud {
    public CoordinatesHud() { super("Coordinates", "Coordinates"); }

    @Override
    protected String getText(MinecraftClient c) {
        if (c.player == null) return "X: 0 Y: 0 Z: 0";
        var p = c.player.getBlockPos();
        return "X: " + p.getX() + " Y: " + p.getY() + " Z: " + p.getZ()
                + " [" + c.player.getHorizontalFacing().getName().toUpperCase() + "]";
    }
}
