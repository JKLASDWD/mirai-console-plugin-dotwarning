package com.jklasdwd.plugin.dotwarning;
import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.data.java.JavaAutoSavePluginConfig;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DotWarningConfig extends JavaAutoSavePluginConfig {
    public DotWarningConfig(@NotNull String saveName) {
        super(saveName);
    }
    public static final DotWarningConfig INSTANCE = new DotWarningConfig("DotWarningConfig");
    public final Value<Map<String,Boolean>> grouplist = typedValue
        (
                "grouplist",
                createKType(Map.class,createKType(String.class),createKType(Boolean.class)),
                new HashMap<String,Boolean>()
                {
                    // anonymous class,initial Hashmap
                    {
                        put("1",true);
                    }
                }
        );
    public final Value<Map<String,List<String>>> regrexlist = typedValue(
            "regrexlist",
            createKType(Map.class,createKType(String.class),createKType(List.class,createKType(String.class))),
            new HashMap<String,List<String>>(){
                {
                    put("1", Arrays.asList("1","2"));
                }
            }
    );
}
