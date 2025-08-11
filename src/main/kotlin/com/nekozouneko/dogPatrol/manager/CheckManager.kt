package com.nekozouneko.dogPatrol.manager

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.utils.Utils
import com.nekozouneko.dogPatrol.models.Checks
import com.nekozouneko.dogPatrol.utils.DiscordWebhookNotifier
import net.md_5.bungee.api.event.ChatEvent

class CheckManager {
    private val connectionManager = DogPatrol.getConnectionManager()
    private val discordWebhookNotifier = DiscordWebhookNotifier()
    private val annotationManager = DogPatrol.getAnnotationManager()
    private val configurationManager = DogPatrol.getConfigurationManager()

    interface CheckHandler {
        fun handle(profile: ProfileManager, content: String) : Boolean
        fun getProfile() : ProfileManager
        fun getContent() : String
        fun getResponseData(): MutableMap<String, String>
    }
    fun check(e: ChatEvent){
        val profile: ProfileManager = connectionManager.getProfile(e.sender) ?: return

        val checks: ArrayList<CheckHandler> = Checks.get()
        for(check in checks){
            val annotation = annotationManager.getAnnotation(check::class) ?: continue

            //Exclude Regex Check
            var isExcludeRegexFlag = false
            val regexExcludes = configurationManager.getConfig().getStringList("exclude_regex.${annotation.checkName}").map { Regex(it) }
            if (regexExcludes.isNotEmpty()){
                for (regex in regexExcludes){
                    if(!e.message.matches(regex)) continue
                    isExcludeRegexFlag = true
                    break
                }
            }
            if(isExcludeRegexFlag) continue

            //Check Process
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