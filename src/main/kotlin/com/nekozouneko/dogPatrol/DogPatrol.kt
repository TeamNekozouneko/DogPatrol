package com.nekozouneko.dogPatrol

import com.nekozouneko.dogPatrol.utils.Utils
import com.nekozouneko.dogPatrol.listener.ChatEvent
import com.nekozouneko.dogPatrol.listener.PlayerDisconnectEvent
import com.nekozouneko.dogPatrol.listener.PostLoginEvent
import com.nekozouneko.dogPatrol.manager.ConfigurationManager
import com.nekozouneko.dogPatrol.manager.ConnectionManager
import com.nekozouneko.dogPatrol.manager.ProfileManager
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import org.w3c.dom.Text
import java.io.File
import java.io.FileWriter
import java.sql.Connection
import java.util.concurrent.TimeUnit

class DogPatrol : Plugin() {
    companion object{
        lateinit var instance: DogPatrol

        private val utils: Utils = Utils()
        private val connectionManager: ConnectionManager = ConnectionManager()
        private val configurationManager: ConfigurationManager = ConfigurationManager()

        fun getUtils(): Utils { return this.utils }
        fun getConnectionManager(): ConnectionManager { return this.connectionManager }
        fun getConfigurationManager(): ConfigurationManager { return this.configurationManager }
    }

    override fun onEnable() {
        instance = this

        //Init Configuration
        configurationManager.initialize()
        configurationManager.loadConfig()

        proxy.pluginManager.registerListener(this, PostLoginEvent())
        proxy.pluginManager.registerListener(this, PlayerDisconnectEvent())
        proxy.pluginManager.registerListener(this, ChatEvent())

        proxy.scheduler.schedule(this, Runnable {
            connectionManager.integrationCheckHandle()
            connectionManager.getAllProfiles().forEach {
                it.profileTickHandle()
            }
        }, 0, 1, TimeUnit.SECONDS)

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    //Annotations
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class CheckInfo(val checkName: String, val blockedMessage: String, val isAsync: Boolean)
}
