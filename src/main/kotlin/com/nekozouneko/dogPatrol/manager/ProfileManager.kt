package com.nekozouneko.dogPatrol.manager

import net.md_5.bungee.api.connection.ProxiedPlayer
import java.nio.Buffer
import java.util.concurrent.ConcurrentHashMap

class ProfileManager(private val player: ProxiedPlayer) {
    companion object{
        const val LAST_CONTENT_AMOUNT: Int = 5
    }

    private var buffer = ConcurrentHashMap<BufferType, Float>()
    private var lastContents: MutableList<String> = mutableListOf()

    fun getPlayer() : ProxiedPlayer { return player }
    enum class BufferType{
        DUPLICATE_CONTENT
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

    fun addContent(content: String){
        lastContents.add(content)
        if(lastContents.size > LAST_CONTENT_AMOUNT) lastContents.removeAt(0)
    }
    fun getContents() : List<String>{
        return lastContents.toList()
    }
    fun getLastContent() : String?{
        if(lastContents.size == 0) return null
        return lastContents.last()
    }
}