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
        val currentServer = player.server.info.name
        val configFile = configurationManager.getConfig()
        val regexExcludeServers = configFile.getStringList("exclude_servers").map { Regex(it) }
        regexExcludeServers.forEach {
            if(currentServer.matches(it)) return
        }

        //Handle
        checkManager.check(e)
    }
}