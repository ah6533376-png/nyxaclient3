package dev.nyxa.util;

public final class Ease {
    /** Frame-rate independent lerp. speed ~0.1-0.3 feels smooth. */
    public static float lerp(float current, float target, float speed, float delta) {
        float t = Math.min(1f, speed * delta * 60f);
        return current + (target - current) * t;
    }
}
