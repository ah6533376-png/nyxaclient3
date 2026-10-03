package dev.nyxa.crosshair;

import dev.nyxa.NyxaClient;
import dev.nyxa.modules.impl.CrosshairModule;
import dev.nyxa.util.Colors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class CrosshairRenderer {
    public static void render(DrawContext ctx) {
        CrosshairModule m = (CrosshairModule) NyxaClient.instance.moduleManager.get("Crosshair");
        if (m == null || !m.isEnabled()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        int[][] map = CrosshairStyle.byName(m.style.getValue()).bitmap;
        int px = Math.max(1, (int) m.size.getValue());
        int w = map.length, h = map[0].length;
        int cx = client.getWindow().getScaledWidth() / 2;
        int cy = client.getWindow().getScaledHeight() / 2;

        int fill = m.rainbow.getValue()
                ? (0xFF000000 | Colors.rainbow(3f, 0.8f, 1f))
                : Colors.argb(255, (int) m.red.getValue(), (int) m.green.getValue(), (int) m.blue.getValue());
        int outline = Colors.argb(210, 0, 0, 0);

        int x0 = cx - (w * px) / 2;
        int y0 = cy - (h * px) / 2;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int v = map[y][x];
                if (v == 0) continue;
                ctx.fill(x0 + x * px, y0 + y * px, x0 + (x + 1) * px, y0 + (y + 1) * px, v == 2 ? fill : outline);
            }
        }
    }
}
