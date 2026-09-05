package com.hogv1.gui;

import com.hogv1.HogV1;
import com.hogv1.module.Category;
import com.hogv1.module.Module;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class HudEditorScreen extends Screen {
    private Module dragging; private int dx,dy;
    public HudEditorScreen(){super(Text.literal("Hog V1 HUD Layout"));}
    @Override public void render(DrawContext c,int mx,int my,float delta){
        renderInGameBackground(c); c.drawCenteredTextWithShadow(textRenderer,"HUD Layout — drag widgets, Esc to save",width/2,10,0xffffffff);
        for(Module m:HogV1.modules().all()) if(m.category()==Category.HUD && m.isEnabled()) HogV1.hud().draw(c,m,HogV1.hud().position(m.id()),true);
    }
    @Override public boolean mouseClicked(Click click,boolean doubled){
        if(click.button()==0) for(Module m:HogV1.modules().all()) if(m.category()==Category.HUD&&m.isEnabled()) {var p=HogV1.hud().position(m.id());if(click.x()>=p.x()&&click.x()<p.x()+HogV1.hud().width(m)&&click.y()>=p.y()&&click.y()<p.y()+16){dragging=m;dx=(int)click.x()-p.x();dy=(int)click.y()-p.y();return true;}}
        return super.mouseClicked(click,doubled);
    }
    @Override public boolean mouseDragged(Click click,double ddx,double ddy){if(dragging!=null){HogV1.hud().move(dragging.id(),(int)click.x()-dx,(int)click.y()-dy);return true;}return super.mouseDragged(click,ddx,ddy);}
    @Override public boolean mouseReleased(Click click){dragging=null;return super.mouseReleased(click);}
    @Override public void close(){HogV1.hud().save();super.close();}
    @Override public boolean shouldPause(){return false;}
}
