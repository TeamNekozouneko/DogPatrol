package com.nekozouneko.dogPatrol.listener

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.manager.ConnectionManager
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class PlayerDisconnectEvent : Listener {
    private val connectionManager: ConnectionManager = DogPatrol.getConnectionManager()
    @EventHandler
    fun onQuit(e: PlayerDisconnectEvent) {
        connectionManager.removeConnection(e.player)
    }
}