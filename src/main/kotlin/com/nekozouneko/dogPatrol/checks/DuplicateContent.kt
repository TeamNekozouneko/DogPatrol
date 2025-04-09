package com.nekozouneko.dogPatrol.checks

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.manager.CheckManager
import com.nekozouneko.dogPatrol.manager.ProfileManager

class DuplicateContent : CheckManager.CheckHandler {
    lateinit var chatContent: String
    lateinit var profileManager: ProfileManager
    private val responseData: MutableMap<String, String> = mutableMapOf()
    override fun getContent(): String { return chatContent }
    override fun getProfile(): ProfileManager { return profileManager }
    override fun getResponseData(): MutableMap<String, String> { return responseData }

    companion object{
        const val BASE_BUFFER = 2
    }
    @DogPatrol.CheckInfo(
        checkName = "DuplicateContent",
        blockedMessage = "同じ内容を連続して送信しないでください！",
        isAsync = false
    )
    override fun handle(profile: ProfileManager, content: String) : Boolean{
        this.chatContent = content
        this.profileManager = profile

        val lastContent = profile.getLastContent() ?: return true
        responseData["lastContent"] = lastContent

        if(lastContent == content) profile.addBuffer(ProfileManager.BufferType.DUPLICATE_CONTENT, 1.0f)

        val currentBuffer = profile.getBuffer(ProfileManager.BufferType.DUPLICATE_CONTENT)
        responseData["currentBuffer"] = currentBuffer.toString()
        if(lastContent == content && currentBuffer >= BASE_BUFFER) return false
        return true
    }
}