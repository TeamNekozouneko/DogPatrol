package com.nekozouneko.dogPatrol.commands

import com.nekozouneko.dogPatrol.DogPatrol
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command

class DogPatrolCommand : Command("dogpatrol", "dogpatrol.reload") {
    override fun execute(p0: CommandSender?, p1: Array<out String>?) {
        if(p1 == null || p1.isEmpty()){
            p0?.sendMessage("§c使用方法：/dogpatrol reload")
        }else if(p1?.get(0) == "reload"){
            DogPatrol.getConfigurationManager().loadConfig()
            p0?.sendMessage("§aコンフィグを再読み込みしました。")
        }
    }
}