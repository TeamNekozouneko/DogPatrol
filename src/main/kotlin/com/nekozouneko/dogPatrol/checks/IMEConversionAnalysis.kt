package com.nekozouneko.dogPatrol.checks

import com.nekozouneko.dogPatrol.DogPatrol
import com.nekozouneko.dogPatrol.listener.ChatEvent
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

class IMEConversionAnalysis : ChatEvent.CheckHandler{
    companion object{
        const val CANDIDATE_DEPTH = 3
    }
    @DogPatrol.CheckInfo(
        checkName = "IMEConversionAnalysis",
        blockedMessage = "",
        isAsync = true
    )
    override fun handle(profile: ProfileManager, content: String): Boolean {
        //前処理（記号除去）
        val replacedContent = content.replace(Regex("\\p{Punct}")  , "")

        //Tokenize
        val tokenizer = Tokenizer()
        val rawTokens = tokenizer.tokenize(replacedContent)
        val katakana = rawTokens.joinToString("") { if (it.reading == "*") it.surface else it.reading }
        val hiragana = Utils.KatakanaToHiragana(katakana)

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
            e.printStackTrace()
            return true
        }


        //Get Badwords
        val badwordConfig = ContainsBadwords.config.getBadwords()
        val badwords = badwordConfig.keys.map { badwordConfig.getSection(it).getStringList("list") }.flatten()

        //Calcultion all Morphological based Combination pattern
        var resultCombination: MutableList<String> = mutableListOf()
        for(candidateIndex in candidates.indices){
            if(candidateIndex == 0) {
                candidates[0].asJsonArray.forEachIndexed { index, it ->
                    if(index > CANDIDATE_DEPTH) return@forEachIndexed
                    resultCombination.add(it.asString)
                }
                continue
            }
            val _combination: MutableList<String> = mutableListOf()
            resultCombination.forEach { currentCombination ->
                candidates[candidateIndex].asJsonArray.forEachIndexed { index, it ->
                    if(index > CANDIDATE_DEPTH) return@forEachIndexed
                    _combination.add(currentCombination + it.asString)
                }
            }
            resultCombination = _combination
        }

        //Check Combination contains Badwords
        resultCombination.forEach { _combination ->
            profile.getPlayer().sendMessage(_combination)
            badwords.forEach {
                if(_combination.contains(it)) return false
            }
        }

        return true
    }

}