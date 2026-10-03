package dev.nyxa;

import dev.nyxa.config.NyxaConfig;
import dev.nyxa.crosshair.CrosshairRenderer;
import dev.nyxa.gui.ClickGuiScreen;
import dev.nyxa.hud.HudEditorScreen;
import dev.nyxa.hud.HudManager;
import dev.nyxa.modules.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class NyxaClient implements ClientModInitializer {
    public static final String MOD_ID = "nyxa";
    public static NyxaClient instance;

    public ModuleManager moduleManager;
    public HudManager hudManager;

    public static KeyBinding clickGuiKey;
    public static KeyBinding hudEditorKey;

    @Override
    public void onInitializeClient() {
        instance = this;
        moduleManager = new ModuleManager();
        hudManager = new HudManager();
        NyxaConfig.load();

        clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nyxa.clickgui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.nyxa"));
        hudEditorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nyxa.hudeditor", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "category.nyxa"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (clickGuiKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new ClickGuiScreen());
            }
            while (hudEditorKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new HudEditorScreen());
            }
            moduleManager.tick(client);
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            hudManager.render(context, tickDelta);
            CrosshairRenderer.render(context);
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> NyxaConfig.save());
    }
}
