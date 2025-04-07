package com.nekozouneko.dogPatrol.checks

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.listener.ChatEvent
import com.nekozouneko.dogPatrol.manager.ProfileManager
import com.nekozouneko.dogPatrol.utils.Utils

class SimilarityContent : ChatEvent.CheckHandler {
    companion object{
        const val BASE_RATIO = 0.7
        const val BASE_BUFFER = 2
        const val MIN_CONTENT_LENGTH = 4
    }
    @DogPatrol.CheckInfo(
        checkName = "SimilarityContent",
        blockedMessage = "似ている内容を連続して送信しないでください！",
        isAsync = false
    )
    override fun handle(profile: ProfileManager, content: String): Boolean {
        if(content.length <= MIN_CONTENT_LENGTH) return true
        val contents = profile.getContents()

        val distances = Utils.getLevenshteinDistanceAverageRatio(contents)
        if(distances >= BASE_RATIO) profile.addBuffer(ProfileManager.BufferType.SIMILARITY_CONTENT, 1.0f)

        if(distances >= BASE_RATIO && profile.getBuffer(ProfileManager.BufferType.SIMILARITY_CONTENT) >= BASE_BUFFER) return false
        return true
    }
}