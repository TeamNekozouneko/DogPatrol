package com.nekozouneko.dogPatrol.utils

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.manager.CheckManager
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.URL

class DiscordWebhookNotifier {
    enum class NotifyType{
        Blocked(),
        Detected()
    }
    private fun getPostData(check: CheckManager.CheckHandler, type: NotifyType) : String{
        val kFunction = check::class.members.find { it.name == "handle" }
        val annotation = kFunction?.annotations?.find { it is DogPatrol.CheckInfo } as? DogPatrol.CheckInfo
        if(annotation == null) return ""

        if(type == NotifyType.Blocked){
            val responseValues = arrayListOf<String>()
            check.getResponseData().forEach { responseName, responseData ->
                responseValues.add("${responseName} `${responseData}`")
            }
            return """
                {
                  "content": null,
                  "embeds": [
                    {
                      "title": ":x: 不適切なチャットをブロックしました。",
                      "color": 12398383,
                      "fields": [
                        {
                          "name": "検出内容",
                          "value": "MCID `${check.getProfile().getPlayer().name}`\nチェック `${annotation.checkName}`\n内容 `${check.getContent()}`"
                        },
                        {
                          "name": "詳細",
                          "value": "${responseValues.joinToString("\\n")}"
                        }
                      ]
                    }
                  ],
                  "username": "DogPatrol",
                  "attachments": []
                }
            """.trimIndent()
        }else if(type == NotifyType.Detected){
            val responseValues = arrayListOf<String>()
            check.getResponseData().forEach { responseName, responseData ->
                responseValues.add("${responseName} `${responseData}`")
            }
            return """
                {
                  "content": null,
                  "embeds": [
                    {
                      "title": ":warning: 不適切なチャットが検出されました。",
                      "color": 13609490,
                      "fields": [
                        {
                          "name": "検出内容",
                          "value": "MCID `${check.getProfile().getPlayer().name}`\nチェック `${annotation.checkName}`\n内容 `${check.getContent()}`"
                        },
                        {
                          "name": "詳細",
                          "value": "${responseValues.joinToString("\\n")}"
                        }
                      ]
                    }
                  ],
                  "username": "DogPatrol",
                  "attachments": []
                }
            """.trimIndent()
        }
        return ""
    }

    fun send(check: CheckManager.CheckHandler, type: NotifyType){
        DogPatrol.instance.proxy.scheduler.runAsync(DogPatrol.instance, Runnable {
            val config = DogPatrol.getConfigurationManager()
            val requestUrl = config.getConfig().getString("webhook_url") ?: return@Runnable

            try{
                val jsonData = getPostData(check, type)
                val conn = URL(requestUrl).openConnection()
                conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                val outputStream = conn.outputStream
                val writer = OutputStreamWriter(outputStream, "UTF-8")
                writer.write(jsonData)
                writer.flush()
                writer.close()
                BufferedReader(InputStreamReader(conn.getInputStream()))
            }catch(e: Exception){
                e.printStackTrace()
            }
        })
    }

}