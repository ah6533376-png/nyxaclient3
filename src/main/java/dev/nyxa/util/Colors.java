package dev.nyxa.util;

public final class Colors {
    public static final int YELLOW = 0xFFFFD500; // Nyxa accent

    public static int argb(int a, int r, int g, int b) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static int alpha(int color, int a) {
        return ((a & 0xFF) << 24) | (color & 0x00FFFFFF);
    }

    public static int alphaF(int color, float a) {
        return alpha(color, (int) (Math.max(0f, Math.min(1f, a)) * 255f));
    }

    public static int rainbow(float seconds, float saturation, float brightness) {
        float hue = (System.currentTimeMillis() % (long) (seconds * 1000f)) / (seconds * 1000f);
        return java.awt.Color.HSBtoRGB(hue, saturation, brightness) & 0x00FFFFFF;
    }
}
