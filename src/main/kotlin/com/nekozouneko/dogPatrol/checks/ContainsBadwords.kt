package com.nekozouneko.dogPatrol.checks

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.listener.ChatEvent
import com.nekozouneko.dogPatrol.manager.ProfileManager

class ContainsBadwords : ChatEvent.CheckHandler {
    companion object{
        val config = DogPatrol.getConfigurationManager()
    }
    @DogPatrol.CheckInfo(
        checkName = "ContainsBadwords",
        blockedMessage = "不適切な内容を含むチャットを送信しないでください！",
        isAsync = false
    )
    override fun handle(profile: ProfileManager, content: String): Boolean {
        val badwordConfig = config.getBadwords()
        badwordConfig.keys.forEach {
            var wordSegment = badwordConfig.getSection(it)
            wordSegment.getStringList("list").forEach {
                if(content.contains(it)) return false
            }
        }
        return true
    }
}