package com.hogv1.util;

import com.hogv1.HogV1;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/** Applies the selected bundled UI typeface through Minecraft's native renderer. */
public final class Fonts {
    private Fonts() {}
    public static Text text(String value) {
        MutableText text=Text.literal(value);String selected=selected();
        if(!selected.equals("Minecraft")){
            String path=switch(selected){case "Comfortaa"->"comfortaa";case "JetBrains Mono"->"jetbrains_mono";case "Roboto"->"roboto";case "Arial Compatible"->"arial_compatible";default->null;};
            if(path!=null)text.setStyle(Style.EMPTY.withFont(new StyleSpriteSource.Font(Identifier.of(HogV1.MOD_ID,path))));
        }
        return text;
    }
    private static String selected(){if(!HogV1.ready())return "Minecraft";var gui=HogV1.modules().get("gui_settings");return gui==null||gui.setting("font")==null?"Minecraft":gui.mode("font");}
    public static int width(TextRenderer renderer,String value){return renderer.getWidth(text(value));}
    public static void draw(DrawContext context,TextRenderer renderer,String value,int x,int y,int color){context.drawTextWithShadow(renderer,text(value),x,y,color);}
    public static void centered(DrawContext context,TextRenderer renderer,String value,int x,int y,int color){context.drawCenteredTextWithShadow(renderer,text(value),x,y,color);}
}
