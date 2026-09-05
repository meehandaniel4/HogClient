package com.hogv1.module.render;

import com.hogv1.module.Category;
import com.hogv1.module.Module;
import com.hogv1.module.setting.*;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;

/** Shader-style wall outline for entities already known to the vanilla client. */
public final class EntityEspModule extends Module {
    public enum Kind { ENTITIES, ITEMS }
    private final Kind kind;
    private final Set<Integer> marked=new HashSet<>();
    private int cooldown;

    public EntityEspModule(String id,String name,String description,Kind kind){
        super(id,name,description,Category.RENDER);this.kind=kind;
        add(new ModeSetting("color_mode","Color mode","Entity Type","Entity Type","Custom","Health","Distance"));
        add(new BooleanSetting("players","Players",true));add(new BooleanSetting("mobs","Hostile mobs",true));add(new BooleanSetting("animals","Passive animals",true));
        add(new BooleanSetting("invisible","Invisible entities",false));add(new BooleanSetting("highlight_target","Highlight target",true));
        add(new NumberSetting("distance","Distance limit",96,8,256,8));add(new NumberSetting("fade_distance","Fade start",3,0,32,1));
        add(new ColorSetting("color","Custom color",0xfff05a78));add(new ColorSetting("players_color","Player color",0xfff2f2f2));add(new ColorSetting("mobs_color","Hostile color",0xffff5555));add(new ColorSetting("animals_color","Animal color",0xff55ff7f));add(new ColorSetting("items_color","Item color",0xffffcf55));add(new ColorSetting("target_color","Target color",0xff55e6ff));
    }

    @Override public void onTick(){if(--cooldown>0)return;cooldown=3;clear();var c=MinecraftClient.getInstance();if(c.player==null||c.world==null)return;double d=number("distance");for(Entity e:c.world.getEntitiesByClass(Entity.class,c.player.getBoundingBox().expand(d),this::selected)){e.setGlowing(true);marked.add(e.getId());}}
    private boolean selected(Entity e){var c=MinecraftClient.getInstance();if(e==c.player||(!bool("invisible")&&e.isInvisible()))return false;if(bool("highlight_target")&&e==c.targetedEntity)return true;if(kind==Kind.ITEMS)return e instanceof net.minecraft.entity.ItemEntity;if(e instanceof PlayerEntity)return bool("players");if(e instanceof PassiveEntity)return bool("animals");return e instanceof MobEntity&&bool("mobs");}

    public Integer colorFor(Entity entity){
        if(!isEnabled()||!marked.contains(entity.getId()))return null;var c=MinecraftClient.getInstance();
        if(bool("highlight_target")&&entity==c.targetedEntity)return color("target_color")&0xffffff;
        return switch(mode("color_mode")){case "Custom"->color("color")&0xffffff;case "Health"->healthColor(entity);case "Distance"->distanceColor(entity,c);default->typeColor(entity);};
    }
    private int typeColor(Entity e){if(e instanceof PlayerEntity)return color("players_color")&0xffffff;if(e instanceof PassiveEntity)return color("animals_color")&0xffffff;if(e instanceof net.minecraft.entity.ItemEntity)return color("items_color")&0xffffff;return color("mobs_color")&0xffffff;}
    private int healthColor(Entity e){if(!(e instanceof LivingEntity living))return typeColor(e);float f=Math.max(0,Math.min(1,living.getHealth()/living.getMaxHealth()));return rgb((int)(255*(1-f)),(int)(255*f),70);}
    private int distanceColor(Entity e,MinecraftClient c){double start=number("fade_distance"),limit=number("distance"),distance=c.player==null?limit:c.player.distanceTo(e);double f=Math.max(0,Math.min(1,(distance-start)/Math.max(1,limit-start)));return rgb(255,(int)(210*(1-f)),(int)(100+120*(1-f)));}
    private static int rgb(int r,int g,int b){return Math.max(0,Math.min(255,r))<<16|Math.max(0,Math.min(255,g))<<8|Math.max(0,Math.min(255,b));}
    private void clear(){var w=MinecraftClient.getInstance().world;if(w!=null)for(int id:marked){Entity e=w.getEntityById(id);if(e!=null)e.setGlowing(false);}marked.clear();}
    @Override public void onDisable(){clear();}
}
