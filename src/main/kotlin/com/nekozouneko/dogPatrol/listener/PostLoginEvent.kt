package com.nekozouneko.dogPatrol.listener

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.manager.ConnectionManager
import com.nekozouneko.dogPatrol.manager.ProfileManager
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class PostLoginEvent : Listener {
    private val connectionManager: ConnectionManager = DogPatrol.getConnectionManager()
    @EventHandler
    fun onJoin(e: PostLoginEvent) {
        connectionManager.addConnection(e.player, ProfileManager(e.player))
    }
}