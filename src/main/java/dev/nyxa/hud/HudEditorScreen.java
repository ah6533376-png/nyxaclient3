package dev.nyxa.hud;

import dev.nyxa.NyxaClient;
import dev.nyxa.config.NyxaConfig;
import dev.nyxa.util.Colors;
import dev.nyxa.util.Glass;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class HudEditorScreen extends Screen {
    private static final int SNAP = 6;

    private HudElement selected, dragging;
    private double grabX, grabY;
    private Integer guideV, guideH;

    public HudEditorScreen() { super(Text.literal("Nyxa HUD Editor")); }

    private List<HudElement> visible() {
        List<HudElement> out = new ArrayList<>();
        for (HudElement e : NyxaClient.instance.hudManager.all()) if (NyxaClient.instance.hudManager.visible(e)) out.add(e);
        return out;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        ctx.fill(0, 0, this.width, this.height, 0x90000000);
        guideV = null;
        guideH = null;

        for (HudElement e : visible()) e.renderScaled(ctx, delta);

        for (HudElement e : visible()) {
            if (e != selected) Glass.outlineRoundRect(ctx, e.x - 2, e.y - 2,
                    (int) (e.getWidth() * e.scale) + 4, (int) (e.getHeight() * e.scale) + 4, 4,
                    Colors.alphaF(0xFFFFFF, 0.18f));
        }

        if (selected != null) {
            int sw = (int) (selected.getWidth() * selected.scale);
            int sh = (int) (selected.getHeight() * selected.scale);
            Glass.outlineRoundRect(ctx, selected.x - 2, selected.y - 2, sw + 4, sh + 4, 4, Colors.YELLOW);
            ctx.drawText(this.textRenderer, selected.name + "  (scroll = scale, drag = move)",
                    selected.x, selected.y - 12, Colors.YELLOW, true);
        }

        if (guideV != null) ctx.fill(guideV, 0, guideV + 1, this.height, Colors.alphaF(0xFFD500, 0.5f));
        if (guideH != null) ctx.fill(0, guideH, this.width, guideH + 1, Colors.alphaF(0xFFD500, 0.5f));

        ctx.drawTextWithShadow(this.textRenderer, "HUD Editor - drag to move, scroll to scale, ESC to save",
                12, 10, 0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == 0) {
            List<HudElement> vis = visible();
            for (int i = vis.size() - 1; i >= 0; i--) {
                HudElement e = vis.get(i);
                if (e.contains(mx, my)) {
                    selected = e;
                    dragging = e;
                    grabX = mx - e.x;
                    grabY = my - e.y;
                    return true;
                }
            }
            selected = null;
            return true;
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (dragging != null) {
            int nx = (int) (mx - grabX);
            int ny = (int) (my - grabY);
            int w = (int) (dragging.getWidth() * dragging.scale);
            int h = (int) (dragging.getHeight() * dragging.scale);

            List<Integer> vx = new ArrayList<>();
            List<Integer> hx = new ArrayList<>();
            vx.add(this.width / 2); hx.add(this.height / 2);
            for (HudElement o : visible()) {
                if (o == dragging) continue;
                int ow = (int) (o.getWidth() * o.scale), oh = (int) (o.getHeight() * o.scale);
                vx.add(o.x); vx.add(o.x + ow / 2); vx.add(o.x + ow);
                hx.add(o.y); hx.add(o.y + oh / 2); hx.add(o.y + oh);
            }
            for (int c : vx) {
                if (Math.abs(nx + w / 2 - c) < SNAP) { nx = c - w / 2; guideV = c; break; }
            }
            for (int c : hx) {
                if (Math.abs(ny + h / 2 - c) < SNAP) { ny = c - h / 2; guideH = c; break; }
            }
            dragging.x = Math.max(0, Math.min(this.width - w, nx));
            dragging.y = Math.max(0, Math.min(this.height - h, ny));
            return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double amount) {
        List<HudElement> vis = visible();
        for (int i = vis.size() - 1; i >= 0; i--) {
            HudElement e = vis.get(i);
            if (e.contains(mx, my) || e == selected) {
                e.scale = (float) Math.max(0.5, Math.min(3.0, e.scale + (amount > 0 ? 0.12 : -0.12)));
                return true;
            }
        }
        return super.mouseScrolled(mx, my, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) { close(); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() { NyxaConfig.save(); super.close(); }

    @Override
    public boolean shouldPause() { return false; }
}
