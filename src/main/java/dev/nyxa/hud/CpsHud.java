package dev.nyxa.hud;

import dev.nyxa.util.CpsCounter;
import net.minecraft.client.MinecraftClient;

public class CpsHud extends TextHud {
    public CpsHud() { super("CPS", "CPS"); }

    @Override
    protected String getText(MinecraftClient c) { return CpsCounter.leftCps() + " CPS"; }
}
