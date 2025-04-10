package com.nekozouneko.dogPatrol.checks

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.manager.CheckManager
import com.nekozouneko.dogPatrol.manager.ProfileManager
import com.atilika.kuromoji.ipadic.Tokenizer
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.nekozouneko.dogPatrol.utils.Utils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlinx.coroutines.*
import com.nekozouneko.dogPatrol.utils.RomaHiraConverter
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent

class IMEConversionAnalysis : CheckManager.CheckHandler{
    lateinit var chatContent: String
    lateinit var profileManager: ProfileManager
    private val responseData: MutableMap<String, String> = mutableMapOf()
    override fun getContent(): String { return chatContent }
    override fun getProfile(): ProfileManager { return profileManager }
    override fun getResponseData(): MutableMap<String, String> { return responseData }
    companion object{
        const val CANDIDATE_DEPTH = 3
    }
    @DogPatrol.CheckInfo(
        checkName = "IMEConversionAnalysis",
        blockedMessage = "",
        isAsync = true
    )
    override fun handle(profile: ProfileManager, content: String): Boolean {
        chatContent = content
        profileManager = profile
        responseData["candidateDepth"] = CANDIDATE_DEPTH.toString()

        //前処理
        var replacedContent = RomaHiraConverter.convert(content.lowercase())
        replacedContent = replacedContent.replace(Regex("\\p{Punct}")  , "")
        replacedContent = replacedContent.replace(" ","").replace("　","")
        replacedContent = Utils.HalfwidthKatakanaToHiragana(replacedContent)
        responseData["preprocessedContent"] = replacedContent

        //Tokenize
        val tokenizer = Tokenizer()
        val rawTokens = tokenizer.tokenize(replacedContent)
        val katakana = rawTokens.joinToString("") { if (it.reading == "*") it.surface else it.reading }
        val hiragana = Utils.KatakanaToHiragana(katakana)
        responseData["processedContent"] = hiragana

        //Hiragana IME Convert (Google API)
        val requestUrl = "http://www.google.com/transliterate?langpair=ja-Hira|ja&text="
        var candidates: List<JsonElement> = listOf()
        try{
            val url = URL(requestUrl + URLEncoder.encode(hiragana , "UTF-8"))
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connect()
            val br = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val response = br.readText()
            val json = Gson().fromJson(response, JsonArray::class.java).asJsonArray
            candidates = json.map { it.asJsonArray.get(1) }
        }catch(e: Exception){
            return true
        }

        //Calcultion all Morphological based Combination patterns
        val startedCalculationTime = System.currentTimeMillis()
        try {
            val isCombinationPatternsBad = runBlocking { isCombinationPatternsBad(candidates) }
            responseData["morphCalculationTime"] = "${( System.currentTimeMillis() - startedCalculationTime )}ms"
            if (isCombinationPatternsBad) return false
        }catch(_: Exception){}
        return true
    }

    suspend fun isCombinationPatternsBad(candidates: List<JsonElement>) : Boolean{
        //Get Badwords
        val badwordConfig = DogPatrol.getConfigurationManager().getBadwords()
        val badwords = badwordConfig.keys.map { badwordConfig.getSection(it).getStringList("list") }.flatten()

        var resultCombination: MutableList<String> = mutableListOf()
        for(candidateIndex in candidates.indices){
            val _combination: MutableList<String> = mutableListOf()
            if(candidateIndex == 0) {
                candidates[0].asJsonArray.forEachIndexed { index, it ->
                    if(index > CANDIDATE_DEPTH) return@forEachIndexed
                    resultCombination.add(it.asString)
                }
            }else{
                resultCombination.forEach { currentCombination ->
                    candidates[candidateIndex].asJsonArray.forEachIndexed { index, it ->
                        if(index >= CANDIDATE_DEPTH) return@forEachIndexed
                        _combination.add(arrayListOf(currentCombination, it.asString).joinToString(""))
                        if(_combination.size % 10 == 0) delay(1)
                    }
                }
                resultCombination = _combination
            }
            for (combination in resultCombination){
                for (badword in badwords){
                    if(!combination.contains(badword)) continue
                    responseData["detectedCombination"] = combination
                    responseData["detectedBadword"] = badword
                    return true
                }
            }
        }
        return false
    }

}