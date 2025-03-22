package com.nekozouneko.dogPatrol.checks

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.listener.ChatEvent
import com.nekozouneko.dogPatrol.manager.ProfileManager

class DuplicateContent : ChatEvent.CheckHandler {
    companion object{
        const val BASE_BUFFER = 2
    }
    @DogPatrol.CheckInfo(
        checkName = "DuplicateContent",
        blockedMessage = "同じ内容を連続して送信しないでください！"
    )
    override fun handle(profile: ProfileManager, content: String) : Boolean{
        val lastContent = profile.getLastContent() ?: return true

        if(lastContent == content){
            profile.addBuffer(ProfileManager.BufferType.DUPLICATE_CONTENT, 1.0f)
        }else{
            profile.removeBuffer(ProfileManager.BufferType.DUPLICATE_CONTENT, 0.2f)
        }

        if(lastContent == content && profile.getBuffer(ProfileManager.BufferType.DUPLICATE_CONTENT) >= BASE_BUFFER) return false
        return true
    }
}