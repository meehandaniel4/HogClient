package com.hogv1.hud;

import com.google.gson.*;
import com.hogv1.module.Module;
import com.hogv1.module.ModuleManager;
import java.nio.file.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;

/** Lightweight 2D HUD with persistent, draggable anchors. */
public final class HUDManager {
    public record Position(int x, int y) {}
    private final ModuleManager modules;
    private final Path file;
    private final Map<String, Position> positions = new HashMap<>();
    public HUDManager(ModuleManager modules, Path configRoot) {
        this.modules=modules; this.file=configRoot.resolve("hud.json");
        int y=8; for (Module m: modules.all()) if (m.category()==com.hogv1.module.Category.HUD) { positions.put(m.id(),new Position(8,y)); y+=18; }
        load();
    }
    public Position position(String id) { return positions.getOrDefault(id,new Position(8,8)); }
    public void move(String id,int x,int y) { positions.put(id,new Position(Math.max(0,x),Math.max(0,y))); }
    public void render(DrawContext context) {
        MinecraftClient c=MinecraftClient.getInstance(); if (c.player==null || c.currentScreen instanceof com.hogv1.gui.ClickGUI) return;
        for (Module m: modules.all()) if (m.category()==com.hogv1.module.Category.HUD && m.isEnabled()) draw(context,m,position(m.id()),false);
        int center=context.getScaledWindowWidth()/2;
        if(modules.enabled("wtap_trainer")&&c.targetedEntity instanceof LivingEntity){String cue=c.player.input.hasForwardMovement()?"W-TAP: release W":"W-TAP: press W";context.drawCenteredTextWithShadow(c.textRenderer,cue,center,context.getScaledWindowHeight()/2+24,0xfff5cb63);}
        if(modules.enabled("hitselect_trainer")&&c.targetedEntity instanceof LivingEntity){float ready=c.player.getAttackCooldownProgress(0);String cue=ready>.9f?"HIT SELECT: ready":String.format(Locale.ROOT,"HIT SELECT: %.0f%%",ready*100);context.drawCenteredTextWithShadow(c.textRenderer,cue,center,context.getScaledWindowHeight()/2+36,ready>.9f?0xff75e0aa:0xfff5cb63);}
        int y=8;if(modules.enabled("cps_counter")){context.drawTextWithShadow(c.textRenderer,"CPS  "+com.hogv1.HogV1.clicks().leftCps()+" | "+com.hogv1.HogV1.clicks().rightCps(),context.getScaledWindowWidth()-90,y,0xffffffff);y+=13;}
        if(modules.enabled("reach_display")){String reach=c.targetedEntity==null?"Reach  —":String.format(Locale.ROOT,"Reach  %.2fm",c.player.distanceTo(c.targetedEntity));context.drawTextWithShadow(c.textRenderer,reach,context.getScaledWindowWidth()-90,y,0xffffffff);y+=13;}
        if(modules.enabled("target_info")&&c.targetedEntity instanceof LivingEntity e){String target=e.getName().getString()+String.format(Locale.ROOT,"  %.1f HP  %d armor  %.1fm",e.getHealth(),e.getArmor(),c.player.distanceTo(e));context.drawTextWithShadow(c.textRenderer,target,center-c.textRenderer.getWidth(target)/2,context.getScaledWindowHeight()-58,0xffffffff);}
    }
    public void draw(DrawContext ctx,Module m,Position p,boolean preview) {
        MinecraftClient c=MinecraftClient.getInstance(); String value=value(m.id(),c);
        int color=m.setting("color") == null ? 0xfff05a78 : m.color("color");
        int width=Math.max(48,c.textRenderer.getWidth(value)+10), height=16;
        ctx.fill(p.x(),p.y(),p.x()+width,p.y()+height,0xb0181a21);
        ctx.fill(p.x(),p.y(),p.x()+2,p.y()+height,color);
        ctx.drawTextWithShadow(c.textRenderer,value,p.x()+6,p.y()+4,0xffeeeeF2);
        if (preview) ctx.drawStrokedRectangle(p.x(),p.y(),width,height,0x99ffffff);
    }
    public int width(Module m) { return Math.max(48,MinecraftClient.getInstance().textRenderer.getWidth(value(m.id(),MinecraftClient.getInstance()))+10); }
    private String value(String id,MinecraftClient c) {
        return switch(id) {
            case "fps" -> "FPS  " + c.getCurrentFps();
            case "coordinates" -> c.player==null?"XYZ":String.format(Locale.ROOT,"XYZ  %.0f  %.0f  %.0f",c.player.getX(),c.player.getY(),c.player.getZ());
            case "clock" -> LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            case "speed" -> c.player==null?"Speed":String.format(Locale.ROOT,"Speed  %.2f m/s",Math.hypot(c.player.getVelocity().x,c.player.getVelocity().z)*20);
            case "compass" -> c.player==null?"Compass":String.format(Locale.ROOT,"Yaw  %.0f°",c.player.getYaw());
            case "ping" -> "Ping  " + (c.getNetworkHandler()==null?"—":c.getNetworkHandler().getPlayerListEntry(c.player.getUuid())==null?"—":c.getNetworkHandler().getPlayerListEntry(c.player.getUuid()).getLatency()+" ms");
            case "armor_status" -> "Armor  " + (c.player==null?0:c.player.getArmor());
            case "potion_status" -> "Effects  " + (c.player==null?0:c.player.getStatusEffects().size());
            case "hud_target_info" -> c.targetedEntity instanceof LivingEntity e ? e.getName().getString()+String.format(Locale.ROOT,"  %.1f HP  %.1fm",e.getHealth(),c.player.distanceTo(e)) : "Target  —";
            case "module_list" -> modules.all().stream().filter(Module::isEnabled).map(Module::name).sorted().limit(6).reduce((a,b)->a+" · "+b).orElse("Hog V1");
            case "keystrokes" -> "W A S D   "+com.hogv1.HogV1.clicks().leftCps()+" / "+com.hogv1.HogV1.clicks().rightCps()+" CPS";
            default -> mName(id);
        };
    }
    private String mName(String id) { Module m=modules.get(id); return m==null?id:m.name(); }
    public void save() { try { Files.createDirectories(file.getParent()); JsonObject o=new JsonObject(); for(var e:positions.entrySet()){JsonObject p=new JsonObject();p.addProperty("x",e.getValue().x());p.addProperty("y",e.getValue().y());o.add(e.getKey(),p);} Files.writeString(file,new GsonBuilder().setPrettyPrinting().create().toJson(o)); } catch(Exception ignored){} }
    private void load() { try { JsonObject o=JsonParser.parseString(Files.readString(file)).getAsJsonObject(); for(String id:o.keySet()){JsonObject p=o.getAsJsonObject(id);move(id,p.get("x").getAsInt(),p.get("y").getAsInt());} } catch(Exception ignored){} }
}
