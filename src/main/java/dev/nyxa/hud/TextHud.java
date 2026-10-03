package dev.nyxa.hud;

import dev.nyxa.util.Glass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class TextHud extends HudElement {
    protected int measuredW = 60;
    protected int measuredH = 16;

    protected TextHud(String name, String module) { super(name, module); }

    protected abstract String getText(MinecraftClient c);

    @Override
    public int getWidth() { return measuredW; }

    @Override
    public int getHeight() { return measuredH; }

    @Override
    public void render(DrawContext ctx, float delta) {
        MinecraftClient c = MinecraftClient.getInstance();
        String t = getText(c);
        measuredW = c.textRenderer.getWidth(t) + 12;
        measuredH = 16;
        Glass.glassPanel(ctx, 0, 0, measuredW, measuredH, 5, 0.45f);
        ctx.drawText(c.textRenderer, t, 6, 4, 0xFFFFFFFF, true);
    }
}
