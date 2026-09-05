package com.hogv1.module.render;

import com.hogv1.module.Category;
import com.hogv1.module.Module;
import net.minecraft.client.MinecraftClient;

public final class FullbrightModule extends Module {
    private Double previous;
    public FullbrightModule(){super("fullbright","Fullbright","Improves client-side scene brightness.",Category.RENDER);}
    @Override public void onEnable(){var option=MinecraftClient.getInstance().options.getGamma();previous=option.getValue();option.setValue(1.0);}
    @Override public void onTick(){MinecraftClient.getInstance().options.getGamma().setValue(1.0);}
    @Override public void onDisable(){if(previous!=null)MinecraftClient.getInstance().options.getGamma().setValue(previous);previous=null;}
}
