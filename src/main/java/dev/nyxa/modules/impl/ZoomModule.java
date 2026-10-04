package dev.nyxa.modules.impl;

import dev.nyxa.modules.Category;
import dev.nyxa.modules.Module;
import dev.nyxa.settings.SliderSetting;
import dev.nyxa.util.Ease;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ZoomModule extends Module {
    public final SliderSetting zoomFov = new SliderSetting("Zoom FOV", 10, 70, 30, 1);

    private int oldFov = 70;
    private float currentFov = -1;

    private static final KeyBinding ZOOM_KEY = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.nyxa.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "category.nyxa"));

    public ZoomModule() {
        super("Zoom", "Hold C to zoom smoothly while enabled.", Category.MISC);
        add(zoomFov);
    }

    @Override
    public void onEnable() {
        MinecraftClient c = MinecraftClient.getInstance();
        oldFov = c.options.getFov().getValue();
        currentFov = oldFov;
    }

    @Override
    public void onDisable() {
        MinecraftClient.getInstance().options.getFov().setValue(oldFov);
        currentFov = -1;
    }

    @Override
    public void render(MinecraftClient c, float delta) {
        if (currentFov < 0) { oldFov = c.options.getFov().getValue(); currentFov = oldFov; }
        float target = ZOOM_KEY.isPressed() ? zoomFov.getValue().floatValue() : (float) oldFov;
        currentFov = Ease.lerp(currentFov, target, 0.18f, delta);
        int fov = Math.round(currentFov);
        if (c.options.getFov().getValue() != fov) c.options.getFov().setValue(fov);
    }
}
