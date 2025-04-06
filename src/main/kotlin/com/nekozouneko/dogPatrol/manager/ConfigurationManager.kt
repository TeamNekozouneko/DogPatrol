package com.nekozouneko.dogPatrol.manager

import com.nekozouneko.dogPatrol.DogPatrol
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.io.FileWriter

class ConfigurationManager {
    companion object{
        lateinit var badwords: Configuration
        lateinit var dataFolder: File
    }
    fun initialize(){
        dataFolder = DogPatrol.instance.dataFolder

        if(!dataFolder.exists()) dataFolder.mkdir()
        val badwordsFile: File = File(dataFolder.path+ File.separator+"badwords.yml")
        if(!badwordsFile.exists()){
            badwordsFile.createNewFile()
            val configResource = DogPatrol.instance.getResourceAsStream("badwords.yml").bufferedReader().readText()
            val fw = FileWriter(badwordsFile)
            fw.write(configResource)
            fw.flush()
        }
    }
    fun loadConfig(){
        initialize()
        badwords = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(File(dataFolder, "badwords.yml"))
    }

    fun getBadwords() : Configuration { return badwords }
}