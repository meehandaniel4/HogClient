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

/** Vanilla-outline ESP limited to entities already loaded in the client world. */
public final class EntityEspModule extends Module {
    public enum Kind { ENTITIES, ITEMS }
    private final Kind kind; private final Set<Integer> marked=new HashSet<>(); private int cooldown;
    public EntityEspModule(String id,String name,String description,Kind kind){super(id,name,description,Category.RENDER);this.kind=kind;add(new ColorSetting("color","Color",0xfff05a78));add(new BooleanSetting("players","Players",true));add(new BooleanSetting("mobs","Mobs",true));add(new BooleanSetting("animals","Passive animals",true));add(new BooleanSetting("box","Box",true));add(new BooleanSetting("outline","Outline",true));add(new NumberSetting("distance","Distance limit",96,8,256,8));}
    @Override public void onTick(){if(--cooldown>0)return;cooldown=5;clear();var c=MinecraftClient.getInstance();if(c.player==null||c.world==null)return;double d=number("distance");for(Entity e:c.world.getEntitiesByClass(Entity.class,c.player.getBoundingBox().expand(d),this::selected)){e.setGlowing(true);marked.add(e.getId());}}
    private boolean selected(Entity e){if(e==MinecraftClient.getInstance().player)return false;if(kind==Kind.ITEMS)return e instanceof net.minecraft.entity.ItemEntity;if(e instanceof PlayerEntity)return bool("players");if(e instanceof PassiveEntity)return bool("animals");return e instanceof MobEntity&&bool("mobs");}
    private void clear(){var w=MinecraftClient.getInstance().world;if(w!=null)for(int id:marked){Entity e=w.getEntityById(id);if(e!=null)e.setGlowing(false);}marked.clear();}
    @Override public void onDisable(){clear();}
}
