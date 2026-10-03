package dev.nyxa.hud;

import net.minecraft.client.gui.DrawContext;

public abstract class HudElement {
    public final String name;
    public final String moduleName;
    public int x = 20, y = 20;
    public float scale = 1.0f;

    protected HudElement(String name, String moduleName) {
        this.name = name;
        this.moduleName = moduleName;
    }

    public abstract int getWidth();
    public abstract int getHeight();
    public abstract void render(DrawContext ctx, float delta);

    public void renderScaled(DrawContext ctx, float delta) {
        ctx.getMatrices().push();
        ctx.getMatrices().translate(x, y, 0);
        ctx.getMatrices().scale(scale, scale, 1f);
        render(ctx, delta);
        ctx.getMatrices().pop();
    }

    public boolean contains(double mx, double my) {
        return mx >= x && my >= y && mx <= x + getWidth() * scale && my <= y + getHeight() * scale;
    }
}
