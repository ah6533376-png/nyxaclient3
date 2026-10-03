package dev.nyxa.hud;

import dev.nyxa.NyxaClient;
import dev.nyxa.modules.Module;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class HudManager {
    public final KeystrokesHud keystrokes = new KeystrokesHud();
    public final FpsHud fps = new FpsHud();
    public final CoordinatesHud coords = new CoordinatesHud();
    public final CpsHud cps = new CpsHud();

    public List<HudElement> all() { return List.of(keystrokes, fps, coords, cps); }

    public boolean visible(HudElement e) {
        Module m = NyxaClient.instance.moduleManager.get(e.moduleName);
        return m != null && m.isEnabled();
    }

    public void render(DrawContext ctx, float delta) {
        for (HudElement e : all()) if (visible(e)) e.renderScaled(ctx, delta);
    }
}
