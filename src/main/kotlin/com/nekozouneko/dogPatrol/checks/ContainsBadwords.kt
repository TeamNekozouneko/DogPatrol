package com.nekozouneko.dogPatrol.checks

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.manager.CheckManager
import com.nekozouneko.dogPatrol.manager.ProfileManager

class ContainsBadwords : CheckManager.CheckHandler {
    lateinit var chatContent: String
    lateinit var profileManager: ProfileManager
    override fun getContent(): String { return chatContent }
    override fun getProfile(): ProfileManager { return profileManager }
    override fun getResponseData(): MutableMap<String, String> { return mutableMapOf() }
    companion object{
        private val config = DogPatrol.getConfigurationManager()
    }
    @DogPatrol.CheckInfo(
        checkName = "ContainsBadwords",
        blockedMessage = "不適切な内容を含むチャットを送信しないでください！",
        isAsync = false
    )
    override fun handle(profile: ProfileManager, content: String): Boolean {
        this.chatContent = content
        this.profileManager = profile
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