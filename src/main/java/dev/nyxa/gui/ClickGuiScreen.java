package dev.nyxa.gui;

import dev.nyxa.NyxaClient;
import dev.nyxa.config.NyxaConfig;
import dev.nyxa.hud.HudEditorScreen;
import dev.nyxa.modules.Category;
import dev.nyxa.modules.Module;
import dev.nyxa.settings.BooleanSetting;
import dev.nyxa.settings.ModeSetting;
import dev.nyxa.settings.Setting;
import dev.nyxa.settings.SliderSetting;
import dev.nyxa.util.Colors;
import dev.nyxa.util.Ease;
import dev.nyxa.util.Glass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClickGuiScreen extends Screen {
    private TextFieldWidget search;
    private Category tab = Category.MOVEMENT;
    private Module expanded;
    private SliderSetting dragSlider;
    private Hit dragHit;

    private int sideX, sideY, sideW, listX, listY, listW;

    private enum Action { TAB, TOGGLE, EXPAND, BOOL, SLIDER, MODE, HUD_EDITOR }
    private record Hit(int x, int y, int w, int h, Action action, Object data) {
        boolean in(double mx, double my) { return mx >= x && my >= y && mx < x + w && my < y + h; }
    }
    private final List<Hit> hits = new ArrayList<>();

    public ClickGuiScreen() { super(Text.literal("Nyxa ClickGUI")); }

    @Override
    protected void init() {
        sideX = 16; sideY = 16; sideW = 104;
        listX = sideX + sideW + 12; listY = 16;
        listW = Math.min(380, this.width - listX - 16);
        search = new TextFieldWidget(this.textRenderer, listX + 24, listY + 4, 168, 12, Text.literal("search"));
        search.setDrawsBackground(false);
        search.setEditableColor(0xFFFFFFFF);
        search.setMaxLength(64);
    }

    private String query() { return search.getText().trim().toLowerCase(Locale.ROOT); }

    private List<Module> visibleModules() {
        String q = query();
        List<Module> out = new ArrayList<>();
        for (Module m : NyxaClient.instance.moduleManager.all()) {
            if (q.isEmpty()) { if (m.getCategory() == tab) out.add(m); }
            else if (m.getName().toLowerCase(Locale.ROOT).contains(q)) out.add(m);
        }
        return out;
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        ctx.fill(0, 0, this.width, this.height, 0xB0000000);
        hits.clear();
        renderSidebar(ctx, mx, my);
        renderSearch(ctx, mx, my, delta);
        renderCards(ctx, mx, my, delta);
    }

    private void renderSidebar(DrawContext ctx, int mx, int my) {
        int h = this.height - 32;
        Glass.glassPanel(ctx, sideX, sideY, sideW, h, 8, 0.55f);
        ctx.drawText(this.textRenderer, "NYXA", sideX + 12, sideY + 12, Colors.YELLOW, true);
        ctx.drawText(this.textRenderer, "v3.0 glass", sideX + 12, sideY + 23, 0xFF8A8A8A, false);

        int y = sideY + 40;
        for (Category c : Category.values()) {
            boolean active = c == tab && query().isEmpty();
            boolean hover = mx >= sideX + 8 && mx < sideX + sideW - 8 && my >= y && my < y + 22;
            if (active) {
                Glass.fillRoundRect(ctx, sideX + 8, y, sideW - 16, 22, 5, Colors.alphaF(0xFFD500, 0.18f));
                Glass.outlineRoundRect(ctx, sideX + 8, y, sideW - 16, 22, 5, Colors.alphaF(0xFFD500, 0.6f));
            } else if (hover) {
                Glass.fillRoundRect(ctx, sideX + 8, y, sideW - 16, 22, 5, Colors.alphaF(0xFFFFFF, 0.06f));
            }
            ctx.drawText(this.textRenderer, c.name().charAt(0) + c.name().substring(1).toLowerCase(Locale.ROOT),
                    sideX + 16, y + 7, active ? Colors.YELLOW : (hover ? 0xFFFFFFFF : 0xFFAAAAAA), true);
            hits.add(new Hit(sideX + 8, y, sideW - 16, 22, Action.TAB, c));
            y += 26;
        }

        int btnY = sideY + h - 32;
        boolean hov = mx >= sideX + 8 && mx < sideX + sideW - 8 && my >= btnY && my < btnY + 22;
        Glass.glassPanel(ctx, sideX + 8, btnY, sideW - 16, 22, 5, hov ? 0.7f : 0.5f);
        ctx.drawCenteredTextWithShadow(this.textRenderer, "Edit HUD", sideX + sideW / 2, btnY + 7,
                hov ? Colors.YELLOW : 0xFFFFFFFF);
        hits.add(new Hit(sideX + 8, btnY, sideW - 16, 22, Action.HUD_EDITOR, null));
    }

    private void renderSearch(DrawContext ctx, int mx, int my, float delta) {
        Glass.glassPanel(ctx, listX, listY, 200, 20, 10, 0.5f);
        if (search.isFocused())
            Glass.outlineRoundRect(ctx, listX, listY, 200, 20, 10, Colors.alphaF(0xFFD500, 0.7f));
        // little magnifier icon
        Glass.outlineRoundRect(ctx, listX + 8, listY + 5, 8, 8, 3, Colors.alphaF(0xFFFFFF, 0.5f));
        ctx.fill(listX + 14, listY + 13, listX + 18, listY + 15, Colors.alphaF(0xFFFFFF, 0.5f));
        search.render(ctx, mx, my, delta);
        if (search.getText().isEmpty() && !search.isFocused())
            ctx.drawText(this.textRenderer, "Search modules...", listX + 25, listY + 6, 0xFF777777, false);
    }

    private void renderCards(DrawContext ctx, int mx, int my, float delta) {
        List<Module> mods = visibleModules();
        int y = listY + 30;
        if (mods.isEmpty()) {
            ctx.drawText(this.textRenderer, "No modules found.", listX + 4, y + 8, 0xFF888888, false);
            return;
        }
        for (Module m : mods) {
            int cardH = 40 + (expanded == m ? m.getSettings().size() * 20 + 6 : 0);
            boolean hover = mx >= listX && mx < listX + listW && my >= y && my < y + 40;

            m.anim = Ease.lerp(m.anim, m.isEnabled() ? 1f : 0f, 0.22f, delta);
            Glass.glassPanel(ctx, listX, y, listW, cardH, 7, hover ? 0.62f : 0.5f);

            ctx.drawText(this.textRenderer, m.getName(), listX + 10, y + 8,
                    m.isEnabled() ? 0xFFFFFFFF : 0xFFB3B3B3, true);
            ctx.drawText(this.textRenderer, m.getDescription(), listX + 10, y + 23, 0xFF808080, false);

            // toggle switch
            int tw = 30, th = 14, tx = listX + listW - tw - 10, ty = y + 9;
            int track = Colors.alphaF(m.anim > 0.5f ? 0xFFD500 : 0xFFFFFF, 0.12f + 0.20f * m.anim);
            Glass.fillRoundRect(ctx, tx, ty, tw, th, th / 2, track);
            Glass.outlineRoundRect(ctx, tx, ty, tw, th, th / 2, Colors.alphaF(0xFFFFFF, 0.35f));
            int knob = th - 4;
            int kx = tx + 2 + Math.round(m.anim * (tw - knob - 4));
            Glass.fillRoundRect(ctx, kx, ty + 2, knob, knob, knob / 2,
                    m.anim > 0.02f ? Colors.alphaF(0xFFD500, 0.4f + 0.6f * m.anim) : Colors.alphaF(0xFFFFFF, 0.6f));
            hits.add(new Hit(tx - 4, ty - 4, tw + 8, th + 8, Action.TOGGLE, m));

            // expand button
            if (!m.getSettings().isEmpty()) {
                int ex = tx - 22;
                ctx.drawCenteredTextWithShadow(this.textRenderer, expanded == m ? "-" : "+",
                        ex + 7, ty + 3, 0xFFBBBBBB);
                hits.add(new Hit(ex - 2, ty - 2, 18, 18, Action.EXPAND, m));
            }

            // settings
            if (expanded == m) {
                int sy = y + 40;
                for (Setting<?> s : m.getSettings()) {
                    ctx.drawText(this.textRenderer, s.name, listX + 10, sy + 5, 0xFFCCCCCC, false);
                    if (s instanceof BooleanSetting b) {
                        int bx = listX + listW - 40, bw = 26, bh = 12, by = sy + 4;
                        float a = b.getValue() ? 1f : 0f;
                        Glass.fillRoundRect(ctx, bx, by, bw, bh, bh / 2,
                                Colors.alphaF(0xFFD500, 0.12f + 0.25f * a));
                        Glass.outlineRoundRect(ctx, bx, by, bw, bh, bh / 2, Colors.alphaF(0xFFFFFF, 0.35f));
                        int kn = bh - 4;
                        Glass.fillRoundRect(ctx, bx + 2 + Math.round(a * (bw - kn - 4)), by + 2, kn, kn, kn / 2,
                                b.getValue() ? Colors.YELLOW : 0xFFDDDDDD);
                        hits.add(new Hit(bx - 3, by - 3, bw + 6, bh + 6, Action.BOOL, b));
                    } else if (s instanceof SliderSetting sl) {
                        int sw = 100, sx = listX + listW - sw - 44, syy = sy + 9;
                        ctx.fill(sx, syy + 1, sx + sw, syy + 3, Colors.alphaF(0xFFFFFF, 0.18f));
                        ctx.fill(sx, syy + 1, sx + (int) Math.round(sl.normalized() * sw), syy + 3,
                                Colors.alphaF(0xFFD500, 0.75f));
                        int kx2 = sx + (int) Math.round(sl.normalized() * sw) - 3;
                        Glass.fillRoundRect(ctx, kx2, syy - 2, 7, 9, 3, 0xFFFFFFFF);
                        String dv = sl.display();
                        ctx.drawText(this.textRenderer, dv, listX + listW - 38 - this.textRenderer.getWidth(dv),
                                sy + 5, 0xFFFFFFFF, false);
                        hits.add(new Hit(sx - 4, sy, sw + 8, 18, Action.SLIDER, sl));
                    } else if (s instanceof ModeSetting md) {
                        int mw = 84, mxp = listX + listW - mw - 10, myp = sy + 2;
                        Glass.fillRoundRect(ctx, mxp, myp, mw, 14, 4, Colors.alphaF(0xFFFFFF, 0.08f));
                        Glass.outlineRoundRect(ctx, mxp, myp, mw, 14, 4, Colors.alphaF(0xFFFFFF, 0.25f));
                        ctx.drawCenteredTextWithShadow(this.textRenderer, "< " + md.display() + " ",
                                mxp + mw / 2, myp + 3, 0xFFFFFFFF);
                        hits.add(new Hit(mxp - 2, myp - 2, mw + 4, 18, Action.MODE, md));
                    }
                    sy += 20;
                }
            }
            y += cardH + 8;
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button == 0) {
            boolean inSearch = search.mouseClicked(mx, my, button);
            if (!inSearch) {
                // clicked outside the search box: it stops capturing keys
                // until the user clicks inside it again
                search.setFocused(false);
            } else {
                return true;
            }
            for (Hit h : hits) {
                if (h.in(mx, my)) {
                    switch (h.action) {
                        case TAB -> { tab = (Category) h.data; expanded = null; }
                        case TOGGLE -> ((Module) h.data).toggle();
                        case EXPAND -> { Module mod = (Module) h.data; expanded = expanded == mod ? null : mod; }
                        case HUD_EDITOR -> { close(); MinecraftClient.getInstance().setScreen(new HudEditorScreen()); }
                        case BOOL -> { ((BooleanSetting) h.data).toggle(); NyxaConfig.save(); }
                        case MODE -> { ((ModeSetting) h.data).cycle(); NyxaConfig.save(); }
                        case SLIDER -> {
                            dragSlider = (SliderSetting) h.data;
                            dragHit = h;
                            dragSlider.setFromNormalized((mx - h.x) / h.w);
                        }
                    }
                    return true;
                }
            }
            return true;
        }
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (dragSlider != null && dragHit != null) {
            dragSlider.setFromNormalized((mx - dragHit.x) / dragHit.w);
            return true;
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        if (dragSlider != null) { dragSlider = null; dragHit = null; NyxaConfig.save(); }
        return super.mouseReleased(mx, my, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) { close(); return true; }
        if (search.isFocused()) {
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                List<Module> v = visibleModules(); // quick action: toggle first result
                if (!v.isEmpty()) { v.get(0).toggle(); return true; }
            }
            search.keyPressed(keyCode, scanCode, modifiers);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (search.isFocused()) { search.charTyped(chr, modifiers); return true; }
        return super.charTyped(chr, modifiers);
    }

    @Override
    public void close() { NyxaConfig.save(); super.close(); }

    @Override
    public boolean shouldPause() { return false; }
}
