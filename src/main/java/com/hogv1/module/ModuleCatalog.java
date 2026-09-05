package com.hogv1.module;

import com.hogv1.module.combat.AutoClickerModule;
import com.hogv1.module.movement.SprintModule;
import com.hogv1.module.player.AutoToolModule;
import com.hogv1.module.render.FullbrightModule;
import com.hogv1.module.render.NoHurtCamModule;
import com.hogv1.module.render.TimeChangerModule;
import com.hogv1.module.render.EntityEspModule;
import com.hogv1.module.utility.MacroModule;
import com.hogv1.module.setting.*;

/** Complete built-in catalog. Render-heavy modules expose conservative, cached configuration. */
public final class ModuleCatalog {
    private ModuleCatalog() {}
    public static void registerAll(ModuleManager m) {
        m.register(new AutoClickerModule());
        m.register(new SprintModule("sprint", "Sprint", "Keeps sprint enabled while moving forward."));
        m.register(b("wtap_trainer", "WTap Trainer", "HUD timing cue for forward-key reset practice.", Category.COMBAT));
        m.register(b("hitselect_trainer", "HitSelect Trainer", "Displays recommended hit timing; never attacks.", Category.COMBAT));
        m.register(b("cps_counter", "CPS Counter", "Displays recent left and right clicks per second.", Category.COMBAT));
        m.register(b("reach_display", "Reach Display", "Displays distance to the latest attacked target.", Category.COMBAT));
        m.register(targetInfo(Category.COMBAT, "target_info", "Target Info"));

        m.register(new SprintModule("toggle_sprint", "ToggleSprint", "Toggle-style sprint movement."));
        m.register(b("toggle_sneak", "ToggleSneak", "Toggle-style sneaking.", Category.MOVEMENT));
        m.register(b("safe_walk", "SafeWalk", "Helps stop movement at block edges.", Category.MOVEMENT));
        m.register(keyed("free_look", "FreeLook", "Hold a key to rotate the camera independently.", Category.MOVEMENT));

        m.register(new FullbrightModule());
        m.register(new EntityEspModule("esp", "ESP", "Highlights selected loaded entity types.", EntityEspModule.Kind.ENTITIES));
        m.register(esp("tracers", "Tracers", "Draws lines toward selected client-known entities."));
        m.register(targetInfo(Category.RENDER, "name_tags", "NameTags"));
        m.register(new EntityEspModule("item_esp", "ItemESP", "Highlights loaded dropped items.", EntityEspModule.Kind.ITEMS));
        m.register(esp("storage_esp", "StorageESP", "Highlights loaded storage block entities."));
        m.register(text("search", "Search", "Highlights configured loaded blocks.", Category.RENDER, "blocks", "Block identifiers", "minecraft:diamond_ore"));
        m.register(color(b("projectiles", "Projectiles", "Estimates held-projectile trajectories.", Category.RENDER)));
        m.register(color(b("hit_color", "HitColor", "Customizes entity damage tint.", Category.RENDER)));
        m.register(color(b("block_overlay", "BlockOverlay", "Customizes the targeted-block outline.", Category.RENDER)));
        m.register(b("clear_water", "ClearWater", "Improves underwater visibility.", Category.RENDER));
        m.register(new NoHurtCamModule());
        m.register(new TimeChangerModule());
        Module weather = b("weather", "Weather", "Overrides visual client weather.", Category.RENDER); weather.add(new ModeSetting("weather", "Weather", "Clear", "Clear", "Rain", "Thunder")); m.register(weather);

        m.register(new AutoToolModule());
        m.register(b("auto_armor", "AutoArmor", "Equips upgrades while an inventory is open.", Category.PLAYER));
        Module inv = text("inventory_manager", "InventoryManager", "Optional inventory sorting and presets; never auto-drops.", Category.INVENTORY, "trash", "Trash list", ""); inv.add(new ModeSetting("preset", "Sort preset", "None", "None", "PvP", "Building", "Mining")); m.register(inv);
        m.register(b("refill", "Refill", "Refills configured hotbar stacks while inventory is open.", Category.INVENTORY));

        m.register(keyed("free_cam", "FreeCam", "Detached local camera; sends no fake movement.", Category.WORLD));
        m.register(color(b("waypoints", "Waypoints", "Named, colored dimension-aware locations.", Category.WORLD)));
        Module crumbs = color(b("breadcrumbs", "Breadcrumbs", "Draws a temporary local movement trail.", Category.WORLD)); crumbs.add(new NumberSetting("seconds", "Trail lifetime", 30, 1, 300, 1)); m.register(crumbs);

        m.register(keyed("panic", "Panic", "Immediately disables every Hog V1 module.", Category.UTILITY));
        m.register(b("profiles", "Profiles", "Named and per-server configurations.", Category.UTILITY));
        m.register(text("friends", "Friends List", "Local usernames used for visual filtering.", Category.UTILITY, "names", "Usernames", ""));
        m.register(new MacroModule());

        String[][] widgets = {{"keystrokes","Keystrokes"},{"fps","FPS"},{"coordinates","Coordinates"},{"armor_status","Armor Status"},{"potion_status","Potion Status"},{"clock","Clock"},{"compass","Compass"},{"ping","Ping"},{"speed","Speed"},{"hud_target_info","Target Info"},{"module_list","Module List"}};
        for (String[] w : widgets) { Module widget = color(b(w[0], w[1], "Draggable HUD widget.", Category.HUD)); widget.add(new NumberSetting("scale", "Scale", 1, .5, 2.5, .05)); widget.add(new NumberSetting("opacity", "Opacity", .9, .1, 1, .05)); m.register(widget); }
        Module gui = color(b("gui_settings", "GUI Settings", "Theme, font, scale, accent, and active-module options.", Category.SETTINGS));
        gui.add(new ModeSetting("theme", "Theme", "Dark", "Dark", "Light"));
        gui.add(new ModeSetting("font", "Font", "Comfortaa", "Minecraft", "Arial Compatible", "Comfortaa", "JetBrains Mono", "Roboto"));
        gui.add(new NumberSetting("scale", "GUI scale", 1, .75, 1.5, .05));
        gui.add(new BooleanSetting("active_modules", "Active modules", true));
        gui.add(new ModeSetting("active_sort", "Active list sort", "Text Width", "Text Width", "Alphabetical"));
        gui.add(new BooleanSetting("active_background", "Active list background", true));
        m.register(gui);
    }
    private static Module b(String id, String name, String description, Category c) { return new BasicModule(id, name, description, c); }
    private static Module keyed(String id, String name, String description, Category c) { Module x=b(id,name,description,c); x.add(new KeybindSetting("activation_key", "Activation key", -1)); return x; }
    private static Module color(Module x) { x.add(new ColorSetting("color", "Color", 0xfff05a78)); return x; }
    private static Module text(String id,String name,String d,Category c,String sid,String sn,String def) { Module x=b(id,name,d,c); x.add(new StringSetting(sid,sn,def)); return x; }
    private static Module targetInfo(Category c,String id,String name) { Module x=b(id,name,"Shows target name, health, armor, and distance.",c); x.add(new BooleanSetting("health","Health",true)); x.add(new BooleanSetting("armor","Armor",true)); x.add(new BooleanSetting("distance","Distance",true)); return x; }
    private static Module esp(String id,String name,String d) { Module x=color(b(id,name,d,Category.RENDER)); x.add(new BooleanSetting("players","Players",true)); x.add(new BooleanSetting("mobs","Mobs",true)); x.add(new BooleanSetting("animals","Passive animals",true)); x.add(new BooleanSetting("box","Box",true)); x.add(new BooleanSetting("outline","Outline",true)); x.add(new NumberSetting("distance","Distance limit",96,8,256,8)); return x; }
}
