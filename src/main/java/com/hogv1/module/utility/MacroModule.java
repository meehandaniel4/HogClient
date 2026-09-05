package com.hogv1.module.utility;

import com.hogv1.module.Category;
import com.hogv1.module.Module;
import com.hogv1.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;

public final class MacroModule extends Module {
    public MacroModule(){super("macros","Macros","Maps one key press to a hotbar slot; no chat or repetition.",Category.UTILITY);add(new NumberSetting("slot","Hotbar slot",1,1,9,1));}
    @Override public void onEnable(){var p=MinecraftClient.getInstance().player;if(p!=null)p.getInventory().setSelectedSlot((int)number("slot")-1);setEnabled(false);}
}
