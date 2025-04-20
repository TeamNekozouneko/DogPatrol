package com.nekozouneko.dogPatrol.listener

import com.nekozouneko.dogPatrol.DogPatrol
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class ChatEvent : Listener {
    private val checkManager = DogPatrol.getCheckManager()
    private val configurationManager = DogPatrol.getConfigurationManager()
    @EventHandler
    fun onChat(e: ChatEvent){
        if(e.isCommand || e.isProxyCommand) return

        //Exclude Servers
        val player = e.sender as ProxiedPlayer
        val configFile = configurationManager.getConfig()
        val excludeServers = configFile.getStringList("exclude_servers")
        if(excludeServers.contains(player.server.info.name)) return

        //Handle
        checkManager.check(e)
    }
}