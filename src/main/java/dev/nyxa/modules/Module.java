package dev.nyxa.modules;

import dev.nyxa.settings.Setting;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting<?>> settings = new ArrayList<>();
    private boolean enabled;

    /** toggle animation progress 0..1, driven by the ClickGUI each frame */
    public float anim;

    protected Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public List<Setting<?>> getSettings() { return settings; }
    public boolean isEnabled() { return enabled; }

    protected void add(Setting<?> s) { settings.add(s); }

    public void toggle() { setEnabled(!enabled); }

    public void setEnabled(boolean e) {
        if (e == enabled) return;
        enabled = e;
        if (e) onEnable(); else onDisable();
        try { dev.nyxa.config.NyxaConfig.save(); } catch (Throwable ignored) {}
    }

    public void onEnable() {}
    public void onDisable() {}
    public void tick(MinecraftClient client) {}
}
