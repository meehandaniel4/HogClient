package com.hogv1.module;

import static org.junit.jupiter.api.Assertions.*;
import com.hogv1.module.setting.*;
import org.junit.jupiter.api.Test;

final class SettingTest {
    @Test void numbersClampAndSnap(){NumberSetting s=new NumberSetting("cps","CPS",8,1,20,.5);s.set(9.24);assertEquals(9.0,s.get());s.set(99);assertEquals(20.0,s.get());s.set(Double.NaN);assertEquals(8.0,s.get());}
    @Test void modesRejectInvalidValues(){ModeSetting s=new ModeSetting("mode","Mode","A","A","B");s.set("invalid");assertEquals("A",s.get());s.next();assertEquals("B",s.get());}
    @Test void moduleLifecycleIsIdempotent(){class T extends Module{int on,off;T(){super("test","Test","Test",Category.UTILITY);}@Override public void onEnable(){on++;}@Override public void onDisable(){off++;}}T t=new T();t.setEnabled(true);t.setEnabled(true);t.setEnabled(false);assertEquals(1,t.on);assertEquals(1,t.off);}
}
