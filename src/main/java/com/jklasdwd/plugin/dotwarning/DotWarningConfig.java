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
    public final Value<Map<Long,Boolean>> grouplist = typedValue
        (
                "grouplist",
                createKType(Map.class,createKType(Long.class),createKType(Boolean.class)),
                new HashMap<Long,Boolean>()
                {
                    // anonymous class,initial Hashmap
                    {
                        put(1L,true);
                    }
                }
        );
    public final Value<Map<Long,List<String>>> regrexlist = typedValue(
            "regrexlist",
            createKType(Map.class,createKType(Long.class),createKType(List.class,createKType(String.class))),
            new HashMap<Long,List<String>>(){
                {
                    put(1L, Arrays.asList("1","2"));
                }
            }
    );
}
