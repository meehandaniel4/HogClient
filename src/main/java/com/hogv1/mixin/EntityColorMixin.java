package com.hogv1.mixin;

import com.hogv1.HogV1;
import com.hogv1.module.render.EntityEspModule;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityColorMixin {
    @Inject(method="getTeamColorValue",at=@At("HEAD"),cancellable=true)
    private void hogv1$espColor(CallbackInfoReturnable<Integer> cir){
        if(!HogV1.ready())return;Entity self=(Entity)(Object)this;
        for(String id:new String[]{"esp","item_esp"}){var module=HogV1.modules().get(id);if(module instanceof EntityEspModule esp){Integer color=esp.colorFor(self);if(color!=null){cir.setReturnValue(color);return;}}}
    }
}
