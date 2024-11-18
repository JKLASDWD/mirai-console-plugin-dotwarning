package com.jklasdwd.plugin.dotwarning;

import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.data.java.JavaAutoSavePluginData;
import org.jetbrains.annotations.NotNull;


import java.util.HashMap;
import java.util.Map;

public class DotWarningData extends JavaAutoSavePluginData {
    public DotWarningData(@NotNull String saveName) {
        super(saveName);
    }
    public static final DotWarningData INSTANCE = new DotWarningData("DotWarningData");
    public final Value<Map<Long,Map<Long,Integer>>> warninglist = typedValue
            (
            "warninglist",
            createKType(Map.class,createKType(Long.class),createKType(Map.class,createKType(Long.class),createKType(Integer.class))),
                    new HashMap<Long,Map<Long,Integer>>(){
                        {
                            put(1L,new HashMap<Long,Integer>());
                        }
                    }
            );


}
