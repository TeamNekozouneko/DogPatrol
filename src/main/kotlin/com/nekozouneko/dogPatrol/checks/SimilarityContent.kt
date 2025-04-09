package com.nekozouneko.dogPatrol.checks

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.manager.CheckManager
import com.nekozouneko.dogPatrol.manager.ProfileManager
import com.nekozouneko.dogPatrol.utils.Utils

class SimilarityContent : CheckManager.CheckHandler {
    lateinit var chatContent: String
    lateinit var profileManager: ProfileManager
    private val responseData: MutableMap<String, String> = mutableMapOf()
    override fun getContent(): String { return chatContent }
    override fun getProfile(): ProfileManager { return profileManager }
    override fun getResponseData(): MutableMap<String, String> { return responseData }
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
        chatContent = content
        profileManager = profile
        responseData["baseRatio"] = BASE_RATIO.toString()
        responseData["baseBuffer"] = BASE_BUFFER.toString()
        if(content.length <= MIN_CONTENT_LENGTH) return true
        val contents = profile.getContents()

        val distances = Utils.getLevenshteinDistanceAverageRatio(contents)
        responseData["levenshteinDistanceAvgRatio"] = distances.toString()
        if(distances >= BASE_RATIO) profile.addBuffer(ProfileManager.BufferType.SIMILARITY_CONTENT, 1.0f)

        val currentBuffer = profile.getBuffer(ProfileManager.BufferType.SIMILARITY_CONTENT)
        responseData["currentBuffer"] = currentBuffer.toString()
        if(distances >= BASE_RATIO && currentBuffer >= BASE_BUFFER) return false
        return true
    }
}