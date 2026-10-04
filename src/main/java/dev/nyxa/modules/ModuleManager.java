package dev.nyxa.modules;

import dev.nyxa.modules.impl.*;
import dev.nyxa.util.CpsCounter;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    private boolean lastLeft, lastRight;

    public ModuleManager() {
        modules.add(new SprintModule());
        modules.add(new ZoomModule());
        modules.add(new FullbrightModule());
        modules.add(new CrosshairModule());
        modules.add(new KeystrokesModule());
        modules.add(new FpsModule());
        modules.add(new CoordinatesModule());
        modules.add(new CpsModule());
    }

    public List<Module> all() { return modules; }

    public Module get(String name) {
        for (Module m : modules) if (m.getName().equalsIgnoreCase(name)) return m;
        return null;
    }

    public void tick(MinecraftClient client) {
        for (Module m : modules) if (m.isEnabled()) m.tick(client);

        if (client.getWindow() != null) {
            long h = client.getWindow().getHandle();
            boolean l = GLFW.glfwGetMouseButton(h, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
            boolean r = GLFW.glfwGetMouseButton(h, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
            if (l && !lastLeft) CpsCounter.leftClick();
            if (r && !lastRight) CpsCounter.rightClick();
            lastLeft = l;
            lastRight = r;
            CpsCounter.prune();
        }
    }

    public void render(MinecraftClient client, float delta) {
        for (Module m : modules) if (m.isEnabled()) m.render(client, delta);
    }
}
