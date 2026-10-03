package dev.nyxa.hud;

import dev.nyxa.util.CpsCounter;
import dev.nyxa.util.Glass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class KeystrokesHud extends HudElement {
    private record Key(String label, int code, boolean mouse, int gx, int gy, int gw) {}

    private static final int K = 22, G = 2;
    private static final List<Key> KEYS = List.of(
            new Key("W", GLFW.GLFW_KEY_W, false, 1, 0, 1),
            new Key("A", GLFW.GLFW_KEY_A, false, 0, 1, 1),
            new Key("S", GLFW.GLFW_KEY_S, false, 1, 1, 1),
            new Key("D", GLFW.GLFW_KEY_D, false, 2, 1, 1),
            new Key("LMB", GLFW.GLFW_MOUSE_BUTTON_LEFT, true, 0, 2, 1),
            new Key("RMB", GLFW.GLFW_MOUSE_BUTTON_RIGHT, true, 2, 2, 1),
            new Key("SPACE", GLFW.GLFW_KEY_SPACE, false, 0, 3, 3));

    public KeystrokesHud() { super("Keystrokes", "Keystrokes"); }

    @Override
    public int getWidth() { return 3 * K + 2 * G; }

    @Override
    public int getHeight() { return 4 * K + 3 * G; }

    private boolean pressed(Key k, long window) {
        return k.mouse
                ? GLFW.glfwGetMouseButton(window, k.code) == GLFW.GLFW_PRESS
                : InputUtil.isKeyPressed(window, k.code);
    }

    @Override
    public void render(DrawContext ctx, float delta) {
        MinecraftClient c = MinecraftClient.getInstance();
        long window = c.getWindow().getHandle();
        for (Key k : KEYS) {
            int x = k.gx * (K + G);
            int y = k.gy * (K + G);
            int wpx = k.gw * K + (k.gw - 1) * G;
            boolean p = pressed(k, window);
            Glass.glassPanel(ctx, x, y, wpx, K, 5, p ? 0.78f : 0.40f);
            if (p) Glass.outlineRoundRect(ctx, x, y, wpx, K, 5, 0x99FFFFFF);
            int tw = c.textRenderer.getWidth(k.label);
            ctx.drawText(c.textRenderer, k.label, x + (wpx - tw) / 2, y + (K - 8) / 2,
                    p ? 0xFFFFFFFF : 0xFF9A9A9A, true);
            if (k.mouse) {
                String s = Integer.toString(k.code == GLFW.GLFW_MOUSE_BUTTON_LEFT
                        ? CpsCounter.leftCps() : CpsCounter.rightCps());
                ctx.drawText(c.textRenderer, s, x + (wpx - c.textRenderer.getWidth(s)) / 2, y - 10, 0xFFFFFFFF, true);
            }
        }
    }
}
