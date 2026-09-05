package com.hogv1.module.combat;

import com.hogv1.module.Category;
import com.hogv1.module.Module;
import com.hogv1.module.setting.BooleanSetting;
import com.hogv1.module.setting.ModeSetting;
import com.hogv1.module.setting.NumberSetting;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

/** Uses ordinary vanilla attacks; it does not alter or forge packets. */
public final class AutoClickerModule extends Module {
    private long nextClick;
    public AutoClickerModule() {
        super("auto_clicker", "AutoClicker", "Attacks at full held-item strength while the attack key is held.", Category.COMBAT);
        add(new ModeSetting("timing", "Timing", "Weapon Cooldown", "Weapon Cooldown", "CPS"));
        add(new NumberSetting("charge", "Attack charge", 1.0, 0.8, 1.0, 0.01));
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
        if (mode("timing").equals("Weapon Cooldown")) {
            // Vanilla derives this value from the held item's attack-speed attribute,
            // so swords, axes, tools, effects, and attribute modifiers all time correctly.
            if (c.player.getAttackCooldownProgress(0.0f) + 1.0e-4f < number("charge")) return;
        } else if (now < nextClick) return;
        if (c.targetedEntity != null) c.interactionManager.attackEntity(c.player, c.targetedEntity);
        c.player.swingHand(Hand.MAIN_HAND);
        c.player.resetTicksSinceLastAttack();
        if (mode("timing").equals("Weapon Cooldown")) {
            nextClick = now;
            return;
        }
        double low = Math.min(number("min_cps"), number("max_cps"));
        double high = Math.max(number("min_cps"), number("max_cps"));
        double cps = bool("randomized") ? ThreadLocalRandom.current().nextDouble(low, high + .001) : (low + high) / 2;
        nextClick = now + (long)(1_000_000_000D / cps);
    }
}
