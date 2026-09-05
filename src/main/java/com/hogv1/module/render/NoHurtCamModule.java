package com.hogv1.module.render;

import com.hogv1.module.Category;
import com.hogv1.module.Module;
import com.hogv1.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;

public final class NoHurtCamModule extends Module {
    private Double previous;
    public NoHurtCamModule(){super("no_hurt_cam","NoHurtCam","Reduces or disables hurt-camera motion.",Category.RENDER);add(new NumberSetting("strength","Strength",0,0,1,.05));}
    @Override public void onEnable(){previous=MinecraftClient.getInstance().options.getDamageTiltStrength().getValue();apply();}
    @Override public void onTick(){apply();}
    private void apply(){MinecraftClient.getInstance().options.getDamageTiltStrength().setValue(number("strength"));}
    @Override public void onDisable(){if(previous!=null)MinecraftClient.getInstance().options.getDamageTiltStrength().setValue(previous);previous=null;}
}
