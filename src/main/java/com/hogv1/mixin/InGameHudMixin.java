package com.hogv1.mixin;

import com.hogv1.HogV1;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(method="render",at=@At("TAIL"))
    private void hogv1$render(DrawContext context, RenderTickCounter counter, CallbackInfo ci) {
        if (HogV1.ready()) HogV1.hud().render(context);
    }
}
