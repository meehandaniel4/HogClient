package com.hogv1.module.player;

import com.hogv1.module.Category;
import com.hogv1.module.Module;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;

public final class AutoToolModule extends Module {
    public AutoToolModule() { super("auto_tool", "AutoTool", "Selects the fastest hotbar tool while breaking.", Category.PLAYER); }
    @Override public void onTick() {
        var c = MinecraftClient.getInstance();
        if (c.player == null || c.world == null || c.currentScreen != null || !c.options.attackKey.isPressed() || !(c.crosshairTarget instanceof BlockHitResult hit)) return;
        BlockState state = c.world.getBlockState(hit.getBlockPos());
        int best = c.player.getInventory().getSelectedSlot();
        float speed = c.player.getInventory().getStack(best).getMiningSpeedMultiplier(state);
        for (int i = 0; i < 9; i++) {
            float candidate = c.player.getInventory().getStack(i).getMiningSpeedMultiplier(state);
            if (candidate > speed) { speed = candidate; best = i; }
        }
        c.player.getInventory().setSelectedSlot(best);
    }
}
