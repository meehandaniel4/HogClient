package com.hogv1;

import com.hogv1.config.*;
import com.hogv1.event.EventBus;
import com.hogv1.gui.ClickGUI;
import com.hogv1.hud.HUDManager;
import com.hogv1.module.Module;
import com.hogv1.module.ModuleCatalog;
import com.hogv1.module.ModuleManager;
import java.util.HashMap;
import java.util.Map;
import com.hogv1.util.ClickTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class HogV1 implements ClientModInitializer {
    public static final String MOD_ID="hogv1";
    private static ModuleManager modules; private static ConfigManager config; private static ProfileManager profiles; private static HUDManager hud; private static EventBus events; private static KeyBinding guiKey; private static final Map<Integer,Boolean> held=new HashMap<>(); private static final ClickTracker clicks=new ClickTracker(); private static boolean leftMouse,rightMouse;
    @Override public void onInitializeClient() {
        modules=new ModuleManager(); ModuleCatalog.registerAll(modules);
        config=new ConfigManager(modules); config.initialize(); profiles=new ProfileManager(config); hud=new HUDManager(modules,config.root()); events=new EventBus();
        KeyBinding.Category category=KeyBinding.Category.create(Identifier.of(MOD_ID,"main"));
        guiKey=KeyBindingHelper.registerKeyBinding(new KeyBinding("key.hogv1.open_gui",InputUtil.Type.KEYSYM,GLFW.GLFW_KEY_RIGHT_SHIFT,category));
        ClientTickEvents.END_CLIENT_TICK.register(HogV1::tick);
        ClientLifecycleEvents.CLIENT_STOPPING.register(client->{config.save();hud.save();});
    }
    private static void tick(MinecraftClient client) {
        while(guiKey.wasPressed()) client.setScreen(new ClickGUI());
        long window=client.getWindow().getHandle();boolean left=GLFW.glfwGetMouseButton(window,GLFW.GLFW_MOUSE_BUTTON_LEFT)==GLFW.GLFW_PRESS;boolean right=GLFW.glfwGetMouseButton(window,GLFW.GLFW_MOUSE_BUTTON_RIGHT)==GLFW.GLFW_PRESS;if(left&&!leftMouse)clicks.left();if(right&&!rightMouse)clicks.right();leftMouse=left;rightMouse=right;
        if(client.currentScreen==null) for(Module m:modules.all()) if(m.keybind()!=-1){boolean down=InputUtil.isKeyPressed(client.getWindow(),m.keybind());boolean old=held.getOrDefault(m.keybind(),false);if(down&&!old){if(m.id().equals("panic")){modules.disableAll();config.save();}else m.toggle();}held.put(m.keybind(),down);}
        modules.tick(); events.post(new EventBus.Tick());
    }
    public static boolean ready(){return modules!=null&&hud!=null;}
    public static ModuleManager modules(){return modules;}
    public static ConfigManager config(){return config;}
    public static ProfileManager profiles(){return profiles;}
    public static HUDManager hud(){return hud;}
    public static EventBus events(){return events;}
    public static ClickTracker clicks(){return clicks;}
}
