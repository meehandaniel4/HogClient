package com.hogv1.module.movement;

import com.hogv1.module.Category;
import com.hogv1.module.Module;
import net.minecraft.client.MinecraftClient;

public final class SprintModule extends Module {
    public SprintModule(String id, String name, String description) { super(id, name, description, Category.MOVEMENT); }
    @Override public void onTick() {
        var c = MinecraftClient.getInstance();
        if (c.player != null && c.currentScreen == null && c.player.input.hasForwardMovement() && !c.player.isSneaking()) c.player.setSprinting(true);
    }
}
