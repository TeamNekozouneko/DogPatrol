package com.nekozouneko.dogPatrol.utils

import com.nekozouneko.dogPatrol.DogPatrol
import net.md_5.bungee.api.connection.Connection
import net.md_5.bungee.api.connection.ProxiedPlayer
import kotlin.math.max
import kotlin.math.min

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

    fun getLevenshteinDistance(X: String, Y: String): Int {
        val m = X.length
        val n = Y.length
        val T = Array(m + 1) { IntArray(n + 1) }
        for (i in 1..m) {
            T[i][0] = i
        }
        for (j in 1..n) {
            T[0][j] = j
        }
        var cost: Int
        for (i in 1..m) {
            for (j in 1..n) {
                cost = if (X[i - 1] == Y[j - 1]) 0 else 1
                T[i][j] = min(min(T[i - 1][j] + 1, T[i][j - 1] + 1),
                    T[i - 1][j - 1] + cost)
            }
        }
        return T[m][n]
    }
    fun getLevenshteinDistanceRatio(x: String, y: String): Double {
        val maxLength = max(x.length, y.length)
        if (maxLength > 0) return (maxLength * 1.0 - getLevenshteinDistance(x, y)) / maxLength * 1.0
        return 1.0
    }
}