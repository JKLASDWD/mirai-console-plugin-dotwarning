package com.jklasdwd.plugin.dotwarning;

import kotlin.Lazy;
import kotlin.LazyKt;
import kotlinx.coroutines.CoroutineScope;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.permission.*;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

/**
 * 使用 Java 请把
 * {@code /src/main/resources/META-INF.services/net.mamoe.mirai.console.plugin.jvm.JvmPlugin}
 * 文件内容改成 {@code org.example.mirai.plugin.JavaPluginMain} <br/>
 * 也就是当前主类全类名
 * <p>
 * 使用 Java 可以把 kotlin 源集删除且不会对项目有影响
 * <p>
 * 在 {@code settings.gradle.kts} 里改构建的插件名称、依赖库和插件版本
 * <p>
 * 在该示例下的 {@link JvmPluginDescription} 修改插件名称，id 和版本等
 * <p>
 * 可以使用 {@code src/test/kotlin/RunMirai.kt} 在 IDE 里直接调试，
 * 不用复制到 mirai-console-loader 或其他启动器中调试
 */

public final class DotWarningMain extends JavaPlugin {
    public static final DotWarningMain INSTANCE = new DotWarningMain();

    private DotWarningMain() {
        super(new JvmPluginDescriptionBuilder("com.jklasdwd.plugin.dotwarning", "1.0.0")
                .info("Dotwarning")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("DotWarningMain plugin enabled");
        this.reloadPluginConfig(DotWarningConfig.INSTANCE);
        this.reloadPluginData(DotWarningData.INSTANCE);
        DotwarningPermission.getValue(); // 注册权限
        CommandManager.INSTANCE.registerCommand(DotWarningCommand.INSTANCE,false);

        Value<Map<String,Boolean>> grouplist = DotWarningConfig.INSTANCE.grouplist;
        Map<String,Boolean> m= grouplist.get();

        Value<Map<String, List<String>>> regrexlist = DotWarningConfig.INSTANCE.regrexlist;
        Map<String, List<String>> regrex_map= regrexlist.get();

        Value<Map<String,Map<String,Integer>>> warninglist = DotWarningData.INSTANCE.warninglist;
        Map<String,Map<String,Integer>> warning_map= warninglist.get();

        // 注册事件
        for(Map.Entry<String,Boolean> entry: m.entrySet()) {
            if(entry.getValue()) {
                long id = Long.parseLong(entry.getKey());
                EventChannel<Event> channel = GlobalEventChannel.INSTANCE.parentScope(this).filter(event -> event instanceof GroupMessageEvent && ( ((GroupMessageEvent) event).getGroup().getId() == id));
                channel.subscribeAlways(GroupMessageEvent.class, f->{
                    List<String> regrex_group_list = regrex_map.get(entry.getKey());
                    String s = f.getMessage().contentToString();
                    for(String regrex_group: regrex_group_list) {
                        Pattern pattern = Pattern.compile(regrex_group);
                        if(pattern.matcher(s).find()) {
                            MessageChainBuilder message_ = new MessageChainBuilder()
                                    .append(new At(f.getGroup().getOwner().getId()))
                                    .append(" ")
                                    .append(f.getSenderName())
                                    .append("违规\n");
                            try{
                                Map<String,Integer> warning_member = warning_map.get(entry.getKey());
                                String member_id = String.valueOf(f.getSender().getId());
                                warning_member.put(member_id, warning_member.get(member_id)+1);
                                warning_map.put(entry.getKey(), warning_member);
                                warninglist.set(warning_map);
                                message_.append("已连续")
                                        .append(String.valueOf(warning_member.get(member_id)))
                                        .append("次违规");
                                MessageChain messages = message_.build();
                                f.getGroup().sendMessage(messages);
                            }
                            catch (NullPointerException e){
                                Map<String,Integer> warning_member = new HashMap<>();
                                String member_id = String.valueOf(f.getSender().getId());
                                warning_member.put(member_id, 1);
                                warning_map.put(entry.getKey(), warning_member);
                                warninglist.set(warning_map);
                                message_.append("已连续")
                                        .append(String.valueOf(warning_member.get(member_id)))
                                        .append("次违规");
                                MessageChain messages = message_.build();
                                f.getGroup().sendMessage(messages);
                            }
                            break;
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onDisable() {

        super.onDisable();
    }

    // region mirai-console 权限系统示例
    public static final Lazy<Permission> DotwarningPermission = LazyKt.lazy(() -> {
        try {
            return PermissionService.getInstance().register(
                    INSTANCE.permissionId("dotwarning-permission"),
                    "执行dotwarning插件的权限",
                    INSTANCE.getParentPermission()
            );
        } catch (PermissionRegistryConflictException e) {
            throw new RuntimeException(e);
        }
    });

    public static boolean hasCustomPermission(User usr) {
        PermitteeId pid;
        if (usr instanceof Member) {
            pid = new AbstractPermitteeId.ExactMember(((Member) usr).getGroup().getId(), usr.getId());
        } else {
            pid = new AbstractPermitteeId.ExactUser(usr.getId());
        }
        return PermissionService.hasPermission(pid, DotwarningPermission.getValue());
    }
    // endregion
}
