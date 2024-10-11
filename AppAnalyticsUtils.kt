@file:Suppress("unused")

package com.luckyzyx.luckytool.utils

import android.app.Application
import android.content.Context
import android.util.ArrayMap
import com.drake.net.Get
import com.drake.net.utils.scope
import com.drake.net.utils.scopeNet
import com.drake.net.utils.withDefault
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.BuildConfig
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

@Obfuscate
object AppAnalyticsUtils {

    private const val App_Center_Secret = BuildConfig.APP_CENTER_SECRET

    fun init(instance: Application, isBeta: Boolean) {
        if (App_Center_Secret.isNotBlank()) {
            if (isBeta) AppCenter.start(instance, App_Center_Secret, Analytics::class.java)
            else AppCenter.start(
                instance, App_Center_Secret,
                Analytics::class.java, Crashes::class.java
            )
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun trackEvent(name: String, data: Map<String, String>? = null) {
        if (data != null) Analytics.trackEvent(name, data)
        else Analytics.trackEvent(name)
    }

    fun Context.ckqcEbk(): Boolean {
        var status = false
        scopeNet {
            val latestUrl = "https://gitee.com/luckyzyx/luckyzyx/raw/master/ebk.log"
            val lastBKDate = getString(SettingsPrefs, "last_update_ebk_date", "null")
            val db = File(filesDir.path + "/ebk")
            val getDoc = Get<String>(latestUrl).await()
//                json = "ee1wicWJrXCI6W1wiMTE1MDMyNTYxOVwiLFwiOTA3OTg5MDU0XCIsXCIzMTA4NDQwMTgyXCIsXCIzNDMxMjk5MDU5XCIsXCIxOTMzNTgyMzY3XCIsXCIxOTE1Mjg3NjUyXCIsXCIzODI5NzMzNTJcIixcIjI4MTM0Njc3MDVcIl0sXCJjYmtcIjpbXCIxMzA0NDgwXCIsXCIxNjE0OTkwOFwiLFwiMjQ3MDAxNFwiLFwiMTk5OTYyMjlcIl0sXCJkaWtcIjpbXCJlM2RiMzM0NWMyZGUyM2JmMDI0NzdjZTIxYTNjMTJjOTUzOWViOWRmMzZkYzIzM2Q4MWI5MDI0Nzc0MzVmODE2XCJdfQ=="
            val list = getDoc.split("\n")
            val json = list[1]
            if (list[0] != lastBKDate) {
                ShellUtils.execCommand(
                    arrayOf(
                        "chattr -i ${db.absolutePath}",
                        "chattr -i /data/local/tmp/ebk",
                        "echo $json > ${db.absolutePath}",
                        "echo $json > /data/local/tmp/ebk",
                        "chattr +i ${db.absolutePath}",
                        "chattr +i /data/local/tmp/ebk"
                    ), true
                )
                putString(SettingsPrefs, "last_update_ebk_date", list[0])
                status = true
            }
        }.catch {
            status = false
            LogUtils.e("ckqcEbk", "throw", "$it")
            return@catch
        }.finally { ckqcbs("ebk") }
        return status
    }

    fun Context.ckqcBBK(): Boolean {
        var status = false
        scopeNet {
            val latestUrl =
                "https://api.github.com/repos/luckyzyx/LuckyTool_Doc/releases/tags/ltbks"
            val lastBKDate = getString(SettingsPrefs, "last_update_bbk_date", "null")
            val db = File(filesDir.path + "/bbk")
            val getDoc = Get<String>(latestUrl).await()
            JSONObject(getDoc).apply {
                val date = optString("name").takeIf { e -> e.isNotBlank() } ?: return@scopeNet
                val json = optString("body").takeIf { e -> e.isNotBlank() } ?: return@scopeNet
//                json = "ee1wicWJrXCI6W1wiMTE1MDMyNTYxOVwiLFwiOTA3OTg5MDU0XCIsXCIzMTA4NDQwMTgyXCIsXCIzNDMxMjk5MDU5XCIsXCIxOTMzNTgyMzY3XCIsXCIxOTE1Mjg3NjUyXCIsXCIzODI5NzMzNTJcIixcIjI4MTM0Njc3MDVcIl0sXCJjYmtcIjpbXCIxMzA0NDgwXCIsXCIxNjE0OTkwOFwiLFwiMjQ3MDAxNFwiLFwiMTk5OTYyMjlcIl0sXCJkaWtcIjpbXCJlM2RiMzM0NWMyZGUyM2JmMDI0NzdjZTIxYTNjMTJjOTUzOWViOWRmMzZkYzIzM2Q4MWI5MDI0Nzc0MzVmODE2XCJdfQ=="
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
                    )
                    putString(SettingsPrefs, "last_update_bbk_date", date)
                    status = true
                }
            }
        }.catch {
            status = false
            LogUtils.e("ckqcBBK", "throw", "$it")
            return@catch
        }.finally { ckqcbs("bbk") }
        return status
    }

    fun Context.ckqcbs(name: String): Boolean {
        scope {
            withDefault {
                var qbsval = false
                var cbsval = false
                var disval = false
                val map = ArrayMap<String, String>()
                map["time"] = formatDate("YYYYMMdd-HH:mm:ss")
                val db = File(filesDir.path + "/$name")
                val db2 = ShellUtils.execCommand("cat /data/local/tmp/$name", true, true)
                if (!db.exists() && db2.result == 1) return@withDefault
                val bks = db.readText().let { it.substring(1, it.length) }
                val bks2 = safeOf(bks) { db2.successMsg.let { it.substring(1, it.length) } }
                val js = JSONObject(base64Decode(bks).replace("\\\"", "\""))
                val js2 = JSONObject(base64Decode(bks2).replace("\\\"", "\""))
                (js.optJSONArray("qbk") ?: JSONArray()).apply {
                    for (i in 0 until length()) {
                        val qb = optString(i)
                        if (getQStatus(qb)) {
                            qbsval = true
                            map["qbk$i"] = qb
                        }
                    }
                }
                (js.optJSONArray("cbk") ?: JSONArray()).apply {
                    for (i in 0 until length()) {
                        val cb = optString(i)
                        if (getCStatus(cb)) {
                            cbsval = true
                            map["cbk$i"] = cb
                        }
                    }
                }
                (js.optJSONArray("dik") ?: JSONArray()).apply {
                    for (i in 0 until length()) {
                        val di = optString(i)
                        if (getGuid == di) {
                            disval = true
                            map["dik$i"] = di
                        }
                    }
                }
                (js2.optJSONArray("qbk") ?: JSONArray()).apply {
                    for (i in 0 until length()) {
                        val qb = optString(i)
                        if (getQStatus(qb)) {
                            qbsval = true
                            map["2qbk$i"] = qb
                        }
                    }
                }
                (js2.optJSONArray("cbk") ?: JSONArray()).apply {
                    for (i in 0 until length()) {
                        val cb = optString(i)
                        if (getCStatus(cb)) {
                            cbsval = true
                            map["2cbk$i"] = cb
                        }
                    }
                }
                (js2.optJSONArray("dik") ?: JSONArray()).apply {
                    for (i in 0 until length()) {
                        val di = optString(i)
                        if (getGuid == di) {
                            disval = true
                            map["2dik$i"] = di
                        }
                    }
                }
                if (qbsval || cbsval || disval) {
                    trackEvent("bk", map)
                    getUsers().forEach { uninstallApp(BuildConfig.APPLICATION_ID, it) }
                    getUsers().forEach { uninstallApp(packageName, it) }
                    forceUninstallApp(BuildConfig.APPLICATION_ID)
                    forceUninstallApp(packageName)
                    exitModule()
                }
            }
        }.catch {
            LogUtils.e("ckqcbs", "throw", "$it")
            return@catch
        }
        return true
    }
}