package com.nekozouneko.dogPatrol.listener

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.utils.Utils
import com.nekozouneko.dogPatrol.checks.DuplicateContent
import com.nekozouneko.dogPatrol.manager.ConnectionManager
import com.nekozouneko.dogPatrol.manager.ProfileManager
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import sun.java2d.cmm.Profile

class ChatEvent : Listener {
    private val utils: Utils = DogPatrol.getUtils()
    private val connectionManager: ConnectionManager = DogPatrol.getConnectionManager()

    companion object {
        val checks: ArrayList<CheckHandler> = arrayListOf(
            DuplicateContent()
        )
    }
    interface CheckHandler {
        fun handle(profile: ProfileManager, content: String) : Boolean
    }
    @EventHandler
    fun onChat(e: ChatEvent){
        if(e.isCommand || e.isProxyCommand) return

        val profile: ProfileManager = connectionManager.getProfile(e.sender) ?: return

        for(check in checks){
            if(check.handle(profile, e.message)) continue

            //Get Annotations
            val kFunction = check::class.members.find { it.name == "handle" }
            val annotation = kFunction?.annotations?.find { it is DogPatrol.CheckInfo } as? DogPatrol.CheckInfo
            if(annotation == null) continue

            profile.getPlayer().sendMessage("§6§lDogPatrol §7>> §c${annotation.blockedMessage}")
            utils.sendNotify("§6§lDogPatrol §7>> §f${profile.getPlayer()}§7の発言が §f${annotation.checkName} §7によりブロックされました。（内容：§f${e.message}§7）")
            e.isCancelled = true
            break
        }

        profile.setVariable("lastContent", e.message)
    }
}