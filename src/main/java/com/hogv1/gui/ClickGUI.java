package com.hogv1.gui;

import com.hogv1.HogV1;
import com.hogv1.module.Category;
import com.hogv1.module.Module;
import com.hogv1.module.setting.*;
import com.hogv1.util.Fonts;
import java.util.*;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/** Original dark card UI with search, category navigation, settings, tooltips, and binding. */
public final class ClickGUI extends Screen {
    private Category category=Category.COMBAT; private Module expanded,binding; private String search=""; private boolean searchFocused; private double scroll; private long opened;
    private int left,top,panelW,panelH;
    public ClickGUI(){super(Text.literal("Hog V1"));}
    @Override protected void init(){opened=System.currentTimeMillis();layout();}
    private void layout(){panelW=Math.min(720,width-30);panelH=Math.min(430,height-30);left=(width-panelW)/2;top=(height-panelH)/2;}
    @Override public void render(DrawContext c,int mx,int my,float delta){
        renderInGameBackground(c); layout(); float a=Math.min(1f,(System.currentTimeMillis()-opened)/180f); int alpha=(int)(235*a)<<24;
        round(c,left,top,left+panelW,top+panelH,10,alpha|0x101218); c.fill(left+148,top,left+149,top+panelH,0x55373b48);
        Fonts.draw(c,textRenderer,"HOG",left+18,top+18,0xfff05a78); Fonts.draw(c,textRenderer,"V1",left+48,top+18,0xfff2f2f4);
        int cy=top+48; for(Category cat:Category.values()){boolean selected=cat==category;if(selected)round(c,left+10,cy-4,left+138,cy+16,5,0xff2b2029);Fonts.draw(c,textRenderer,cat.displayName(),left+22,cy,selected?0xfff2748e:0xffa7aab4);cy+=27;}
        int sx=left+165, sy=top+15, sw=panelW-182; round(c,sx,sy,sx+sw,sy+25,6,searchFocused?0xff292c36:0xff20232b);Fonts.draw(c,textRenderer,search.isEmpty()?"Search modules…":search,sx+9,sy+8,search.isEmpty()?0xff777b88:0xffe8e9ed);
        if(category==Category.HUD){round(c,sx,top+48,sx+100,top+69,5,0xfff05a78);Fonts.draw(c,textRenderer,"Edit HUD layout",sx+8,top+55,0xff17181d);}
        int y=top+78+(int)scroll; Module hovered=null;
        c.enableScissor(left+150,top+46,left+panelW-8,top+panelH-8);
        for(Module m:visible()){
            int h=34+(expanded==m?settingsHeight(m):0); if(y+h>top+48&&y<top+panelH-8){round(c,sx,y,sx+sw,y+h-5,6,0xee1c1f27);Fonts.draw(c,textRenderer,m.name(),sx+10,y+8,m.isEnabled()?0xfff27a92:0xffe2e3e7);Fonts.draw(c,textRenderer,m.isEnabled()?"ON":"OFF",sx+sw-30,y+8,m.isEnabled()?0xff75e0aa:0xff747986);if(mx>=sx&&mx<sx+sw&&my>=y&&my<y+29)hovered=m;if(expanded==m)drawSettings(c,m,sx+10,y+31,sw-20,mx,my);}
            y+=h;
        }
        c.disableScissor();
        if(hovered!=null)c.drawTooltip(textRenderer,Text.literal(hovered.description()+"  [L toggle · R settings · M bind]"),mx,my);
        if(binding!=null){c.fill(0,0,width,height,0x88000000);Fonts.centered(c,textRenderer,"Press a key for "+binding.name()+" (Esc cancels)",width/2,height/2,0xffffffff);}
    }
    private List<Module> visible(){String q=search.toLowerCase(Locale.ROOT);return HogV1.modules().all().stream().filter(m->(search.isBlank()?m.category()==category:m.name().toLowerCase(Locale.ROOT).contains(q)||m.description().toLowerCase(Locale.ROOT).contains(q))).toList();}
    private int settingsHeight(Module m){return Math.max(25,m.settings().size()*22+5);}
    private void drawSettings(DrawContext c,Module m,int x,int y,int w,int mx,int my){for(Setting<?> s:m.settings()){Fonts.draw(c,textRenderer,s.name(),x,y+5,0xffaeb1ba);String value=format(s);int tw=Fonts.width(textRenderer,value);Fonts.draw(c,textRenderer,value,x+w-tw,y+5,0xfff05a78);if(s instanceof NumberSetting n){int bx=x+w/2, bw=w/2;c.fill(bx,y+17,bx+bw,y+19,0xff343844);int fill=(int)(bw*(n.get()-n.min())/(n.max()-n.min()));c.fill(bx,y+17,bx+fill,y+19,0xfff05a78);}y+=22;}}
    private String format(Setting<?> s){if(s instanceof BooleanSetting)return (Boolean)s.get()?"✓":"□";if(s instanceof NumberSetting)return String.format(Locale.ROOT,"%.2f",(Double)s.get()).replaceAll("\\.?0+$","");if(s instanceof ColorSetting)return String.format("#%08X",(Integer)s.get());return String.valueOf(s.get());}
    @Override public boolean mouseClicked(Click click,boolean doubled){
        if(binding!=null)return true; double mx=click.x(),my=click.y();int sx=left+165,sy=top+15,sw=panelW-182;searchFocused=mx>=sx&&mx<sx+sw&&my>=sy&&my<sy+25;
        int cy=top+44;for(Category cat:Category.values()){if(mx>=left+10&&mx<left+140&&my>=cy&&my<cy+24){category=cat;scroll=0;return true;}cy+=27;}
        if(category==Category.HUD&&mx>=sx&&mx<sx+100&&my>=top+48&&my<top+70){client.setScreen(new HudEditorScreen());return true;}
        int y=top+78+(int)scroll;for(Module m:visible()){int h=34+(expanded==m?settingsHeight(m):0);if(mx>=sx&&mx<sx+sw&&my>=y&&my<y+29){if(click.button()==0)m.toggle();else if(click.button()==1)expanded=expanded==m?null:m;else if(click.button()==2)binding=m;return true;}if(expanded==m&&mx>=sx&&mx<sx+sw&&my>=y+29&&my<y+h){settingClick(m,(int)mx,sx,sw,(int)my,y+31);return true;}y+=h;}return super.mouseClicked(click,doubled);
    }
    private void settingClick(Module m,int mx,int x,int w,int my,int start){int i=(my-start)/22;if(i<0||i>=m.settings().size())return;Setting<?> s=m.settings().get(i);if(s instanceof BooleanSetting b)b.toggle();else if(s instanceof ModeSetting mode)mode.next();else if(s instanceof NumberSetting n){double f=Math.max(0,Math.min(1,(mx-(x+w/2.0))/(w/2.0)));n.set(n.min()+f*(n.max()-n.min()));}else if(s instanceof ColorSetting color)color.set(Integer.rotateLeft(color.get(),8)|0xff000000);else if(s instanceof KeybindSetting)binding=m;}
    @Override public boolean mouseScrolled(double mx,double my,double horizontal,double vertical){scroll=Math.min(0,scroll+vertical*18);return true;}
    @Override public boolean keyPressed(KeyInput key){if(binding!=null){if(key.key()!=GLFW.GLFW_KEY_ESCAPE)binding.setKeybind(key.key());binding=null;return true;}if(searchFocused&&key.key()==GLFW.GLFW_KEY_BACKSPACE&&!search.isEmpty()){search=search.substring(0,search.length()-1);return true;}return super.keyPressed(key);}
    @Override public boolean charTyped(CharInput input){if(searchFocused&&input.isValidChar()&&search.length()<48){search+=input.asString();return true;}return super.charTyped(input);}
    @Override public void close(){HogV1.config().save();super.close();}
    @Override public boolean shouldPause(){return false;}
    private static void round(DrawContext c,int x1,int y1,int x2,int y2,int r,int color){c.fill(x1+r,y1,x2-r,y2,color);c.fill(x1,y1+r,x2,y2-r,color);for(int i=0;i<r;i++){int inset=(int)Math.ceil(r-Math.sqrt(r*r-(r-i)*(r-i)));c.fill(x1+inset,y1+i,x2-inset,y1+i+1,color);c.fill(x1+inset,y2-i-1,x2-inset,y2-i,color);}}
}
