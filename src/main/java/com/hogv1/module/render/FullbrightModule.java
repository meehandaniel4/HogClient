package com.hogv1.module.render;

import com.hogv1.module.Category;
import com.hogv1.module.Module;
import com.hogv1.module.setting.ModeSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public final class FullbrightModule extends Module {
    private Double previous;
    private StatusEffectInstance injectedNightVision;
    public FullbrightModule(){super("fullbright","Fullbright","Brightens the world using gamma or local night vision.",Category.RENDER);add(new ModeSetting("mode","Mode","Gamma","Gamma","Night Vision"));}
    @Override public void onEnable(){previous=MinecraftClient.getInstance().options.getGamma().getValue();apply();}
    @Override public void onTick(){apply();}
    private void apply(){
        var client=MinecraftClient.getInstance();
        if(mode("mode").equals("Gamma")){removeInjectedEffect();client.options.getGamma().setValue(1.0);return;}
        if(previous!=null)client.options.getGamma().setValue(previous);
        if(client.player!=null&&client.player.getStatusEffect(StatusEffects.NIGHT_VISION)==null){injectedNightVision=new StatusEffectInstance(StatusEffects.NIGHT_VISION,StatusEffectInstance.INFINITE,0,false,false,false);client.player.addStatusEffect(injectedNightVision);}
    }
    private void removeInjectedEffect(){var player=MinecraftClient.getInstance().player;if(player!=null&&player.getStatusEffect(StatusEffects.NIGHT_VISION)==injectedNightVision)player.removeStatusEffect(StatusEffects.NIGHT_VISION);injectedNightVision=null;}
    @Override public void onDisable(){removeInjectedEffect();if(previous!=null)MinecraftClient.getInstance().options.getGamma().setValue(previous);previous=null;}
}
