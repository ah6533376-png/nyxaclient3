package dev.nyxa.util;

import net.minecraft.client.gui.DrawContext;

/** Glassmorphism drawing helpers: rounded rects, soft shadows, translucent fills, bright borders. */
public final class Glass {

    public static void fillRoundRect(DrawContext ctx, int x, int y, int w, int h, int r, int color) {
        if (w <= 0 || h <= 0) return;
        r = Math.max(0, Math.min(r, Math.min(w, h) / 2));
        ctx.fill(x + r, y, x + w - r, y + h, color);
        if (r == 0) return;
        ctx.fill(x, y + r, x + r, y + h - r, color);
        ctx.fill(x + w - r, y + r, x + w, y + h - r, color);
        for (int i = 0; i < r; i++) {
            int inset = r - (int) Math.round(Math.sqrt(r * r - (r - i) * (r - i)));
            ctx.fill(x + inset, y + i, x + w - inset, y + i + 1, color);
            ctx.fill(x + inset, y + h - i - 1, x + w - inset, y + h - i, color);
        }
    }

    public static void outlineRoundRect(DrawContext ctx, int x, int y, int w, int h, int r, int color) {
        if (w <= 0 || h <= 0) return;
        r = Math.max(0, Math.min(r, Math.min(w, h) / 2));
        ctx.fill(x + r, y, x + w - r, y + 1, color);
        ctx.fill(x + r, y + h - 1, x + w - r, y + h, color);
        ctx.fill(x, y + r, x + 1, y + h - r, color);
        ctx.fill(x + w - 1, y + r, x + w, y + h - r, color);
        for (int i = 0; i < r; i++) {
            int off = r - (int) Math.round(Math.sqrt(r * r - (r - i) * (r - i)));
            ctx.fill(x + off, y + i, x + off + 1, y + i + 1, color);
            ctx.fill(x + i, y + off, x + i + 1, y + off + 1, color);
            ctx.fill(x + w - off - 1, y + i, x + w - off, y + i + 1, color);
            ctx.fill(x + w - i - 1, y + off, x + w - i, y + off + 1, color);
            ctx.fill(x + off, y + h - i - 1, x + off + 1, y + h - i, color);
            ctx.fill(x + i, y + h - off - 1, x + i + 1, y + h - off, color);
            ctx.fill(x + w - off - 1, y + h - i - 1, x + w - off, y + h - i, color);
            ctx.fill(x + w - i - 1, y + h - off - 1, x + w - i, y + h - off, color);
        }
    }

    /** A glass panel: soft drop shadow, dark translucent body, top sheen, bright hairline border. */
    public static void glassPanel(DrawContext ctx, int x, int y, int w, int h, int radius, float alpha) {
        fillRoundRect(ctx, x + 2, y + 3, w, h, radius, Colors.alphaF(0x000000, alpha * 0.35f));
        fillRoundRect(ctx, x, y, w, h, radius, Colors.alphaF(0x191920, alpha));
        fillRoundRect(ctx, x + 2, y + 1, w - 4, 1, 1, Colors.alphaF(0xFFFFFF, Math.min(1f, alpha + 0.10f)));
        outlineRoundRect(ctx, x, y, w, h, radius, Colors.alphaF(0xFFFFFF, Math.min(1f, alpha + 0.18f)));
    }
}
