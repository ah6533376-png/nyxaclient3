package dev.nyxa.modules.impl;

import dev.nyxa.crosshair.CrosshairStyle;
import dev.nyxa.modules.Category;
import dev.nyxa.modules.Module;
import dev.nyxa.settings.BooleanSetting;
import dev.nyxa.settings.ModeSetting;
import dev.nyxa.settings.SliderSetting;

public class CrosshairModule extends Module {
    public final ModeSetting style   = new ModeSetting("Style", 0, styleNames());
    public final SliderSetting size  = new SliderSetting("Size", 1, 4, 2, 1);
    public final SliderSetting red   = new SliderSetting("Red", 0, 255, 255, 1);
    public final SliderSetting green = new SliderSetting("Green", 0, 255, 255, 1);
    public final SliderSetting blue  = new SliderSetting("Blue", 0, 255, 255, 1);
    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", false);

    public CrosshairModule() {
        super("Crosshair", "Replaces the vanilla crosshair with detailed pixel-art styles.", Category.RENDER);
        add(style); add(size); add(red); add(green); add(blue); add(rainbow);
    }

    private static String[] styleNames() {
        CrosshairStyle[] v = CrosshairStyle.values();
        String[] n = new String[v.length];
        for (int i = 0; i < v.length; i++) n[i] = v[i].display;
        return n;
    }
}
