package com.hogv1.module.combat;

import com.hogv1.module.Category;
import com.hogv1.module.Module;
import com.hogv1.module.setting.BooleanSetting;
import com.hogv1.module.setting.NumberSetting;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

/** Uses ordinary vanilla attacks; it does not alter or forge packets. */
public final class AutoClickerModule extends Module {
    private long nextClick;
    public AutoClickerModule() {
        super("auto_clicker", "AutoClicker", "Clicks while the attack key is held.", Category.COMBAT);
        add(new NumberSetting("min_cps", "Minimum CPS", 8, 1, 20, 1));
        add(new NumberSetting("max_cps", "Maximum CPS", 12, 1, 20, 1));
        add(new BooleanSetting("randomized", "Randomized timing", true));
        add(new BooleanSetting("target_only", "Only while targeting", true));
    }
    @Override public void onTick() {
        MinecraftClient c = MinecraftClient.getInstance();
        if (c.player == null || c.interactionManager == null || c.currentScreen != null || !c.options.attackKey.isPressed()) return;
        if (bool("target_only") && !(c.targetedEntity instanceof LivingEntity)) return;
        long now = System.nanoTime();
        if (now < nextClick) return;
        if (c.targetedEntity != null) c.interactionManager.attackEntity(c.player, c.targetedEntity);
        c.player.swingHand(Hand.MAIN_HAND);
        double low = Math.min(number("min_cps"), number("max_cps"));
        double high = Math.max(number("min_cps"), number("max_cps"));
        double cps = bool("randomized") ? ThreadLocalRandom.current().nextDouble(low, high + .001) : (low + high) / 2;
        nextClick = now + (long)(1_000_000_000D / cps);
    }
}
