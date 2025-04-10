package com.nekozouneko.dogPatrol.utils

import com.nekozouneko.dogPatrol.DogPatrol
import net.md_5.bungee.api.connection.Connection
import net.md_5.bungee.api.connection.ProxiedPlayer
import kotlin.math.max
import kotlin.math.min

class Utils {
    companion object {
        fun sendNotify(content: String){
            for(p in DogPatrol.instance.proxy.players){
                if(!p.hasPermission("dogpatrol.notify")) continue
                p.sendMessage(content)
            }
        }

        fun connectionToProxiedPlayer(connection: Connection) : ProxiedPlayer?{
            if(!connection.isConnected) { return null }
            return DogPatrol.instance.proxy.players.filter { it.socketAddress == connection.socketAddress }[0]
        }

        fun getLevenshteinDistance(X: String, Y: String): Int {
            val m = X.length
            val n = Y.length
            val T = Array(m + 1) { IntArray(n + 1) }
            for (i in 1..m) {
                T[i][0] = i
            }
            for (j in 1..n) {
                T[0][j] = j
            }
            var cost: Int
            for (i in 1..m) {
                for (j in 1..n) {
                    cost = if (X[i - 1] == Y[j - 1]) 0 else 1
                    T[i][j] = min(min(T[i - 1][j] + 1, T[i][j - 1] + 1),
                        T[i - 1][j - 1] + cost)
                }
            }
            return T[m][n]
        }
        fun getLevenshteinDistanceAverageRatio(contents: List<String>) : Double {
            if(contents.size <= 1) return 0.0

            var totalMaxLength = 0
            var totalDistance = 0
            for (i in 0..<contents.size-1){
                val x = contents[i]
                val y = contents[i+1]
                val maxLength = max(x.length, y.length)
                totalMaxLength += maxLength
                totalDistance += maxLength - getLevenshteinDistance(x, y)
            }
            return totalDistance.toDouble() / totalMaxLength
        }

        fun KatakanaToHiragana(string: String): String {
            return string.map {
                if (it.code in 0x30A1..0x30F3) {
                    it - 0x60 //ひらがなとカタカナのコード差分を引く
                } else {
                    it
                }
            }.joinToString("")
        }
        fun HalfwidthKatakanaToHiragana(string: String): String{
            val hiragana = "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをんぁぃぅぇぉっゃゅょゎ"
            val halfwidthKatakana = "ｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜｦﾝｧｨｩｪｫｯｬｭｮ"
            val canAttachDakuten = "かきくけさしすせそたちつてとはひふへほ"
            val canAttachHandakuten = "はひふへほ"
            val map = halfwidthKatakana.zip(hiragana).toMap()
            val stringBuilder = StringBuilder()
            stringBuilder.append(string.map { map[it] ?: it }.joinToString(""))
            return stringBuilder.mapIndexed { index, char ->
                if(index+1 < stringBuilder.length && canAttachDakuten.contains(char) && stringBuilder[index+1].code == 0xFF9E) {
                    //濁点がつけられる文字だったら、濁点をつける処理
                    char + 0x01
                }else if(index+1 < stringBuilder.length && canAttachHandakuten.contains(char) && stringBuilder[index+1].code == 0xFF9F) {
                    //半濁点がつけられる文字だったら、半濁点をつける処理
                    char + 0x02
                }else if(
                    index-1 >= 0 && (
                        (char.code == 0xFF9E && canAttachDakuten.contains(stringBuilder[index-1])) ||
                        (char.code == 0xFF9F && canAttachHandakuten.contains(stringBuilder[index-1]))
                            )
                    ) {
                    //前の文字が濁点・半濁点がつけられる場合、濁点・半濁点は前文字につけたため削除
                    ""
                }else{
                    char
                }
            }.joinToString("")

        }
    }
}