package com.nekozouneko.dogPatrol.manager

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.checks.ContainsBadwords
import com.nekozouneko.dogPatrol.checks.DuplicateContent
import com.nekozouneko.dogPatrol.checks.IMEConversionAnalysis
import com.nekozouneko.dogPatrol.checks.SimilarityContent
import com.nekozouneko.dogPatrol.utils.Utils
import com.nekozouneko.dogPatrol.checks.*
import com.nekozouneko.dogPatrol.utils.DiscordWebhookNotifier
import net.md_5.bungee.api.event.ChatEvent

class CheckManager {
    private val connectionManager = DogPatrol.getConnectionManager()
    private val discordWebhookNotifier = DiscordWebhookNotifier()
    interface CheckHandler {
        fun handle(profile: ProfileManager, content: String) : Boolean
        fun getProfile() : ProfileManager
        fun getContent() : String
        fun getResponseData(): MutableMap<String, String>
    }
    fun check(e: ChatEvent){
        val profile: ProfileManager = connectionManager.getProfile(e.sender) ?: return

        val checks: ArrayList<CheckHandler> = arrayListOf(
            DuplicateContent(),
            SimilarityContent(),
            ContainsBadwords(),
            IMEConversionAnalysis()
        )

        for(check in checks){
            //Get Annotations
            val kFunction = check::class.members.find { it.name == "handle" }
            val annotation = kFunction?.annotations?.find { it is DogPatrol.CheckInfo } as? DogPatrol.CheckInfo
            if(annotation == null) continue

            if(!annotation.isAsync){
                //Sync Process
                if(check.handle(profile, e.message)) continue

                profile.getPlayer().sendMessage("§6§lDogPatrol §7>> §c${annotation.blockedMessage}")
                Utils.sendNotify("§6§lDogPatrol §7>> §f${profile.getPlayer()}§7の発言が §f${annotation.checkName} §7によりブロックされました。（内容：§f${e.message}§7）")
                discordWebhookNotifier.send(check, DiscordWebhookNotifier.NotifyType.Blocked)
                e.isCancelled = true
                break
            }else{
                //Async process
                DogPatrol.instance.proxy.scheduler.runAsync(DogPatrol.instance, Runnable {
                    if(e.isCancelled) return@Runnable
                    if(check.handle(profile, e.message)) return@Runnable
                    discordWebhookNotifier.send(check, DiscordWebhookNotifier.NotifyType.Detected)
                    if(!e.isCancelled) Utils.sendNotify("§6§lDogPatrol §7>> §f${profile.getPlayer()}§7の不適切な発言が §f${annotation.checkName} §7により検出されました。（内容：§f${e.message}§7）")
                })
            }

        }

        profile.addContent(e.message)
    }
}