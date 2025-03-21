package com.nekozouneko.dogPatrol.manager

import net.md_5.bungee.api.connection.ProxiedPlayer
import java.nio.Buffer
import java.util.concurrent.ConcurrentHashMap

class ProfileManager(private val player: ProxiedPlayer) {
    private var variables = ConcurrentHashMap<String, Any>()
    private var buffer = ConcurrentHashMap<BufferType, Float>()

    //Getter
    fun getPlayer() : ProxiedPlayer { return player }
    enum class BufferType{
        DUPLICATE_CONTENT
    }

    fun setVariable(name: String, value: Any){
        this.variables.compute(name) { _, _ -> value }
    }
    fun getVariable(name: String) : Any? {
        return variables[name]
    }
    fun setBuffer(type: BufferType, value: Float){
        buffer.compute(type) { _, _ -> value }
    }
    fun addBuffer(type: BufferType, affectValue: Float){
        buffer.compute(type) { _, value -> (value ?: 0.0f)+affectValue }
    }
    fun removeBuffer(type: BufferType, affectValue: Float){
        buffer.compute(type) { _, value -> if ((value ?: 0.0f)-affectValue < 0) 0.0f else (value ?: 0.0f)-affectValue }
    }
    fun getBuffer(type: BufferType) : Float{
        return buffer[type] ?: 0.0f
    }
}