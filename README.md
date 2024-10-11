# Evil-Luckytool
这里记录Lucktool模块被曝光的恶意行为，和其开发者luckyxyz的抽象小丑发言

Here is a record of the malicious behavior of the lucktool module that was exposed, and the clown’s statement from its developer luckyxyz

由于luckyxyz极其擅长洗脑煽动粉丝，大部分批评质疑luckytool的证据被其粉丝举报删除，所以本项目将会记载这些不该被遗忘的事实

## 证据

下面大家可以先看一段代码

### [Click here to see the complete code](AppAnalyticsUtils.kt)

如果你读不懂代码，你可以将其发给AI，任何AI都会告诉你

这是一段包含恶意行为的黑名单代码，包含远程控制，锁定文件和收集用户隐私用于跟踪定位

并且使用Base64编码隐藏数据和使用模糊且具有误导性的方法和名称，刻意隐藏这一行为

```java

            val latestUrl =
                "https://api.github.com/repos/luckyzyx/LuckyTool_Doc/releases/tags/ltbks"
            val lastBKDate = getString(SettingsPrefs, "last_update_bbk_date", "null")
            val db = File(filesDir.path + "/bbk")
            val getDoc = Get<String>(latestUrl).await()
            JSONObject(getDoc).apply {
                val date = optString("name").takeIf { e -> e.isNotBlank() } ?: return@scopeNet
                val json = optString("body").takeIf { e -> e.isNotBlank() } ?: return@scopeNet
//                json = "ee1wicWJrXCI6W1wiMTE1MDMyNTYxOVwiLFwiOTA3OTg5MDU0XCIsXCIzMTA4NDQwMTgyXCIsXCIzNDMxMjk5MDU5XCIsXCIxOTMzNTgyMzY3XCIsXCIxOTE1Mjg3NjUyXCIsXCIzODI5NzMzNTJcIixcIjI4MTM0Njc3MDVcIl0sXCJjYmtcIjpbXCIxMzA0NDgwXCIsXCIxNjE0OTkwOFwiLFwiMjQ3MDAxNFwiLFwiMTk5OTYyMjlcIl0sXCJkaWtcIjpbXCJlM2RiMzM0NWMyZGUyM2JmMDI0NzdjZTIxYTNjMTJjOTUzOWViOWRmMzZkYzIzM2Q4MWI5MDI0Nzc0MzVmODE2XCJdfQ=="

```

在去年12月有人发现了luckytool的这一恶意行为，然后曝光在了酷安平台（现已被举报删除）

我们可以看一下luckyxyz对此的回应

[后门代码都开源了你还想怎么样，真要写后门还能让你从源码里翻到？](https://t.me/LuckyTool/199)

### - 如果你不害怕危险代码被别人找到，那你为什么公开的是混淆过的代码呢

### - 为什么在被曝光后你要立即删库呢，如果你只是不想公开源码，完全可以选择归档仓库

### - 你这是这么心虚的删除仓库，是在通过销毁证据，而回避事实吗

```java

                if (date != lastBKDate) {
                    ShellUtils.execCommand(
                        arrayOf(
                            "chattr -i ${db.absolutePath}",
                            "chattr -i /data/local/tmp/bbk",
                            "echo $json > ${db.absolutePath}",
                            "echo $json > /data/local/tmp/bbk",
                            "chattr +i ${db.absolutePath}",
                            "chattr +i /data/local/tmp/bbk"
                        ), true

```


"开源了还有人黑" 他的某一位小学生粉丝这样说到

开源的目的是什么，*公开的允许审计和技术分享*，而某些低质的小学生粉丝群体只会认为是开源就不能被质疑

## 闭源不安全，开源就一定安全吗，luckyxyz在它的项目文件里这样写道

虽然不知道他怎么有脸写下这句话的，不过luckyxyz还是用他自己的项目完美诠释了开源不安全这句话

闭源之后，自然是更加为所欲为

### [我有义务维护用户的应用安全](https://www.coolapk1s.com/feed/57764747)

- 意思很明确,模块会读取你所以安装的应用

- 模块会删除他认为有危险的应用，不需要你允许

- 此功能强制开启，不允许关闭

如此明显的收集隐私的行为他的粉丝只知道拍手叫好,真的令人感叹

由于其强混淆，现在模块是否还有其他危险行为，我们不得而知，不过就已公开的信息，足以说明问题

luckyxyz如此变态以至于病态的控制欲，在可预见的未来luckytool应该会采取更加过分的隐私收集策略，并且冠以安全之名。

# 关于开源协议

不想多说，看代码方法名都懒得换一下，也是敢说没有抄袭,是别人在造谣

也对，只要让粉丝去把证据全举报删除，那么没有证据不就是造谣了吗

这里建议去看一下 [GNU对专有软件的看法](https://www.gnu.org/proprietary/proprietary.html)

用来形容现在的luckytool真的非常合适

# 危险行为代码是因为防止倒卖

这是luckyxyz卖惨的一种手段，是个智力正常的人就该知道，防盗根本不需要收集这么多用户隐私

# 发布

最后，我使对lucktool最后一版源码进行处理，删除了危险行为代码，编译了一个版本供大家使用

> [!WARNING]
>为了你的信息安全，请不用使用官方发布版本，并尽可能劝说你的朋友不要使用




