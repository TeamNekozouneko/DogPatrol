package com.nekozouneko.dogPatrol.manager

import com.nekozouneko.dogPatrol.DogPatrol
import net.md_5.bungee.api.connection.Connection
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.net.Socket
import java.net.SocketAddress
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs

class ConnectionManager : Listener{

    private var connectionMap = ConcurrentHashMap<Connection, ProfileManager>()

    fun getProfile(connection: Connection) : ProfileManager? { return connectionMap[connection] }
    fun integrationCheckHandle() {
        connectionMap.entries.removeIf { it.value.getPlayer() !in DogPatrol.instance.proxy.players}
        DogPatrol.instance.proxy.players.forEach {
            connectionMap.putIfAbsent(it, ProfileManager(it))
        }

    }
    fun addConnection(connection: Connection, profile: ProfileManager){
        connectionMap.compute(connection) { _, _ -> profile }
    }
    fun removeConnection(connection: Connection){
        connectionMap.remove(connection)
    }
    fun getAllProfiles() : MutableCollection<ProfileManager> {
        return connectionMap.values
    }

}