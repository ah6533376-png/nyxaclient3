package dev.nyxa.modules.impl;

import dev.nyxa.modules.Category;
import dev.nyxa.modules.Module;
import net.minecraft.client.MinecraftClient;

public class SprintModule extends Module {
    public SprintModule() { super("Sprint", "Automatically sprint when moving forward.", Category.MOVEMENT); }

    @Override
    public void tick(MinecraftClient c) {
        if (c.player == null) return;
        if (c.player.input.movementForward > 0 && !c.player.isSneaking()) c.player.setSprinting(true);
    }
}
