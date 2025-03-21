package com.nekozouneko.dogPatrol.utils

import com.nekozouneko.dogPatrol.DogPatrol
import net.md_5.bungee.api.connection.Connection
import net.md_5.bungee.api.connection.ProxiedPlayer

class Utils {
    fun sendNotify(content: String){
        for(p in DogPatrol.instance.proxy.players){
            if(!p.hasPermission("dogpatrol.notify")) continue
            p.sendMessage(content)
        }
    }

    fun connectionToProxiedPlayer(connection: Connection) : ProxiedPlayer?{
        if(!connection.isConnected) { return null }
        return DogPatrol.instance.proxy.players.filter { it.socketAddress == connection.socketAddress }[0]
    }
}