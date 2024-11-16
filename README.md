### Dotwarning

- 基于 [Mirai Console ](https://github.com/mamoe/mirai-console) 插件模板构建



*一个简单的根据正则匹配过滤特定字符串并@群主进行处理的Mirai-Console插件*

*Written in Java*





---

配置表

Plugin Config配置 （com.jklasdwd.plugin.dotwarning/DotWarningConfig.yml)

```yaml
grouplist:
  groupnumber1: true
  groupnumber2: false
regrexlist:
  groupnumber1:
    - AAA
    - BBB
  groupnumber2:
    - BBB
    - CCC

#TODO
#(atgroupowner?)
#(kick?)
#(recall?)
```



Plugin Data配置(com.jklasdwd.plugin.dotwarning/DotWarningData.yml)

```yaml
warninglist:
  group1:
    member1: Int
    member2: Int
  group2:
    member1: Int
    member2: Int
```



Command配置

```
/dotwarning set group true/false 设置某个群聊是否开启
/dotwarning add group <regrex> 给某个群聊添加正则过滤器
/dotwarning show group 显示某个群的所有警告记录
/dotwarning reload 重载插件，每次更改后需要重载插件以重新建立监听事件
/dotwarning delete group <regrex> 删除某个正则过滤
/dotwarning print group 打印某个群的所有正则过滤
```



Permission配置

```
仅有一条权限,Command的执行均需该权限
com.jklasdwd.plugin.dotwarning:dotwarning-permission
```

