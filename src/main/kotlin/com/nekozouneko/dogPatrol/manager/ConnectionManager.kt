package com.nekozouneko.dogPatrol.manager

import com.nekozouneko.dogPatrol.DogPatrol
import net.md_5.bungee.api.connection.Connection
import net.md_5.bungee.api.plugin.Listener
import java.util.concurrent.ConcurrentHashMap

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