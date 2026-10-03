package dev.nyxa.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.nyxa.NyxaClient;
import dev.nyxa.hud.HudElement;
import dev.nyxa.modules.Module;
import dev.nyxa.settings.BooleanSetting;
import dev.nyxa.settings.ModeSetting;
import dev.nyxa.settings.Setting;
import dev.nyxa.settings.SliderSetting;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public final class NyxaConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("nyxa.json");

    public static void save() {
        try {
            JsonObject root = new JsonObject();
            JsonObject mods = new JsonObject();
            for (Module m : NyxaClient.instance.moduleManager.all()) {
                JsonObject mo = new JsonObject();
                mo.addProperty("enabled", m.isEnabled());
                JsonObject st = new JsonObject();
                for (Setting<?> s : m.getSettings()) {
                    if (s instanceof BooleanSetting b) st.addProperty(s.name, b.getValue());
                    else if (s instanceof SliderSetting sl) st.addProperty(s.name, sl.getValue());
                    else if (s instanceof ModeSetting md) st.addProperty(s.name, md.getIndex());
                }
                mo.add("settings", st);
                mods.add(m.getName(), mo);
            }
            root.add("modules", mods);

            JsonObject hud = new JsonObject();
            for (HudElement e : NyxaClient.instance.hudManager.all()) {
                JsonObject eo = new JsonObject();
                eo.addProperty("x", e.x);
                eo.addProperty("y", e.y);
                eo.addProperty("scale", e.scale);
                hud.add(e.name, eo);
            }
            root.add("hud", hud);

            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(root));
        } catch (Exception ignored) {}
    }

    public static void load() {
        try {
            if (!Files.exists(FILE)) return;
            JsonObject root = JsonParser.parseString(Files.readString(FILE)).getAsJsonObject();
            JsonObject mods = root.has("modules") ? root.getAsJsonObject("modules") : null;
            if (mods != null) {
                for (Module m : NyxaClient.instance.moduleManager.all()) {
                    if (!mods.has(m.getName())) continue;
                    JsonObject mo = mods.getAsJsonObject(m.getName());
                    JsonObject st = mo.has("settings") ? mo.getAsJsonObject("settings") : null;
                    if (st != null) {
                        for (Setting<?> s : m.getSettings()) {
                            if (!st.has(s.name)) continue;
                            try {
                                if (s instanceof BooleanSetting b) b.setValue(st.get(s.name).getAsBoolean());
                                else if (s instanceof SliderSetting sl) sl.setValue(st.get(s.name).getAsDouble());
                                else if (s instanceof ModeSetting md) md.setIndex(st.get(s.name).getAsInt());
                            } catch (Exception ignored) {}
                        }
                    }
                    if (mo.has("enabled") && mo.get("enabled").getAsBoolean()) m.setEnabled(true);
                }
            }
            JsonObject hud = root.has("hud") ? root.getAsJsonObject("hud") : null;
            if (hud != null) {
                for (HudElement e : NyxaClient.instance.hudManager.all()) {
                    if (!hud.has(e.name)) continue;
                    try {
                        JsonObject eo = hud.getAsJsonObject(e.name);
                        e.x = eo.get("x").getAsInt();
                        e.y = eo.get("y").getAsInt();
                        e.scale = eo.get("scale").getAsFloat();
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception ignored) {}
    }
}
