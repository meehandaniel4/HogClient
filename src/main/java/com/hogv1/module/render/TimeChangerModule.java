package com.hogv1.module.render;

import com.hogv1.module.Category;
import com.hogv1.module.Module;
import com.hogv1.module.setting.ModeSetting;
import com.hogv1.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;

public final class TimeChangerModule extends Module {
    public TimeChangerModule(){super("time_changer","TimeChanger","Overrides visual client time.",Category.RENDER);add(new ModeSetting("time","Time","Day","Day","Sunset","Night","Custom"));add(new NumberSetting("custom","Custom time",6000,0,24000,100));}
    @Override public void onTick(){var w=MinecraftClient.getInstance().world;if(w==null)return;long visual=switch(mode("time")){case "Sunset"->12500;case "Night"->18000;case "Custom"->(long)number("custom");default->6000;};w.setTime(w.getTime(),visual,false);}
}
