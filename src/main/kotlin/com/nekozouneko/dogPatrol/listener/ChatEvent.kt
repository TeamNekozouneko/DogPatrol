package com.nekozouneko.dogPatrol.listener

import com.nekozouneko.dogPatrol.DogPatrol
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class ChatEvent : Listener {
    private val checkManager = DogPatrol.getCheckManager()
    @EventHandler
    fun onChat(e: ChatEvent){
        if(e.isCommand || e.isProxyCommand) return
        checkManager.check(e)
    }
}