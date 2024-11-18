package com.jklasdwd.plugin.dotwarning;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class DotWarningCommand extends JCompositeCommand {
    public static final DotWarningCommand INSTANCE = new DotWarningCommand();
    public Permission dotwarning_permission = PermissionService.getInstance().get(PermissionId.parseFromString("com.jklasdwd.plugin.dotwarning:dotwarning-permission"));
    public DotWarningCommand() {
        super(DotWarningMain.INSTANCE,"dotwarning");
        setDescription("Dotwarning主指令");
        setPermission(dotwarning_permission);
        this.getUsage();
    }

    @SubCommand("set")
    @Description("设置某个群是否开启dotwarning")
    public void set_group_on(CommandSender sender ,@Name("群号") Long group,@Name("true/false")boolean b) {
        Value<Map<Long,Boolean>> grouplist = DotWarningConfig.INSTANCE.grouplist;
        Map<Long,Boolean> m= grouplist.get();
        m.put(group,b);
        grouplist.set(m);
        sender.sendMessage("设置成功！");
    }
    @SubCommand("add")
    @Description("为某个群添加正则过滤")
    public void add_group_regrex_list(CommandSender sender,@Name("群号") Long group,@Name("正则表达式") String l) {
        Value<Map<Long, List<String>>> regrexlist = DotWarningConfig.INSTANCE.regrexlist;
        Map<Long, List<String>> m= regrexlist.get();
        try {
            List<String> l1= m.get(group);
            if(!l1.contains(l)) {
                l1.add(l);
                m.put(group,l1);
                regrexlist.set(m);
            }
        }
        catch (NullPointerException e) {
            List<String> l1 = new ArrayList<>();
            l1.add(l);
            m.put(group,l1);
            regrexlist.set(m);
        }
        sender.sendMessage("添加成功！");
    }
    @SubCommand("show")
    @Description("显示某个群的所有警告记录")
    public void show_group_list_data(CommandSender sender,@Name("群号") Long group) {
        Value<Map<Long,Map<Long,Integer>>> warninglist = DotWarningData.INSTANCE.warninglist;
        Map<Long,Map<Long,Integer>> m= warninglist.get();
        try{
            Map<Long,Integer> group_warningmember_list = m.get(group);
            MessageChainBuilder mb = new MessageChainBuilder();
            for (Map.Entry<Long,Integer> entry : group_warningmember_list.entrySet()) {
                    mb.append(entry.getKey().toString())
                            .append(":")
                            .append(entry.getValue().toString())
                            .append("\n");
            }
            MessageChain messages = mb.build();
            if(!messages.isEmpty())
                sender.sendMessage(messages.contentToString());
            else
                sender.sendMessage("列表为空！");
        }
        catch (NullPointerException e) {
            sender.sendMessage("列表为空");
        }
    }
    @SubCommand("reload")
    @Description("重载插件，每次更改后需要重载插件以重新建立监听事件")
    public void reload_(CommandSender sender) {
        DotWarningMain.INSTANCE.onDisable();
        DotWarningMain.INSTANCE.onEnable();
        sender.sendMessage("重载成功！");
    }
    @SubCommand("delete")
    @Description("删除特定正则过滤")
    public void delete_(CommandSender sender,@Name("群号") Long group,@Name("正则表达式") String l) {
        Value<Map<Long, List<String>>> regrexlist = DotWarningConfig.INSTANCE.regrexlist;
        Map<Long, List<String>> m= regrexlist.get();
        try{
            List<String> l1= m.get(group);
            boolean b = l1.remove(l);
            m.put(group,l1);
            regrexlist.set(m);
            if(b)
                sender.sendMessage("删除成功！");
            else
                sender.sendMessage("没有这个项！");
        }
        catch(NullPointerException e){
            sender.sendMessage("没有这个项！");
        }
    }
    @SubCommand("print")
    @Description("打印出某个群的所有正则过滤库以及群状态")
    public void print_regrex_list(CommandSender sender,@Name("群号") Long group) {
        Value<Map<Long, List<String>>> regrexlist = DotWarningConfig.INSTANCE.regrexlist;
        Map<Long, List<String>> m = regrexlist.get();
        try{
            List<String> l1= m.get(group);
            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();

            for(String s: l1){
                messageChainBuilder.append(s);
                messageChainBuilder.append("\n");
            }
            MessageChain messages = messageChainBuilder.build();
            if(!messages.isEmpty())
                sender.sendMessage(messages.contentToString());
            else
                sender.sendMessage("正则库为空！");
        }
        catch (NullPointerException e) {
            sender.sendMessage("正则库为空！");
        }

    }
}
