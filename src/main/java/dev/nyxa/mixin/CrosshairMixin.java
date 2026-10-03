package dev.nyxa.mixin;

import dev.nyxa.NyxaClient;
import dev.nyxa.modules.impl.CrosshairModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class CrosshairMixin {
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void nyxa$replaceCrosshair(DrawContext context, CallbackInfo ci) {
        CrosshairModule m = (CrosshairModule) NyxaClient.instance.moduleManager.get("Crosshair");
        if (m != null && m.isEnabled()) ci.cancel();
    }
}
