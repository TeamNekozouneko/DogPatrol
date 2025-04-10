package com.nekozouneko.dogPatrol.utils;

import java.util.*;
import java.util.stream.Collectors;

public final class RomaHiraConverter {

    private RomaHiraConverter() {
        throw new ExceptionInInitializerError();
    }

    private final static Map<String, String> MAP;
    private final static List<String> KEYS;
    static {
        // Google IME 基準で作成
        // 清音

        Map<String, String> MAP_TEMP = new LinkedHashMap<>();

        MAP_TEMP.put("a", "あ");
        MAP_TEMP.put("i", "い");
        MAP_TEMP.put("u", "う");
        MAP_TEMP.put("wu", "う");
        MAP_TEMP.put("e", "え");
        MAP_TEMP.put("o", "お");

        MAP_TEMP.put("ka", "か");
        MAP_TEMP.put("ca", "か");
        MAP_TEMP.put("ki", "き");
        MAP_TEMP.put("ku", "く");
        MAP_TEMP.put("cu", "く");
        MAP_TEMP.put("qu", "く");
        MAP_TEMP.put("ke", "け");
        MAP_TEMP.put("ko", "こ");
        MAP_TEMP.put("co", "こ");

        MAP_TEMP.put("sa", "さ");
        MAP_TEMP.put("si", "し");
        MAP_TEMP.put("shi", "し");
        MAP_TEMP.put("ci", "し");
        MAP_TEMP.put("su", "す");
        MAP_TEMP.put("se", "せ");
        MAP_TEMP.put("ce", "せ");
        MAP_TEMP.put("so", "そ");

        MAP_TEMP.put("ta", "た");
        MAP_TEMP.put("ti", "ち");
        MAP_TEMP.put("chi", "ち");
        MAP_TEMP.put("tu", "つ");
        MAP_TEMP.put("tsu", "つ");
        MAP_TEMP.put("te", "て");
        MAP_TEMP.put("to", "と");

        MAP_TEMP.put("na", "な");
        MAP_TEMP.put("ni", "に");
        MAP_TEMP.put("nu", "ぬ");
        MAP_TEMP.put("ne", "ね");
        MAP_TEMP.put("no", "の");

        MAP_TEMP.put("ha", "は");
        MAP_TEMP.put("hi", "ひ");
        MAP_TEMP.put("hu", "ふ");
        MAP_TEMP.put("fu", "ふ");
        MAP_TEMP.put("he", "へ");
        MAP_TEMP.put("ho", "ほ");

        MAP_TEMP.put("ma", "ま");
        MAP_TEMP.put("mi", "み");
        MAP_TEMP.put("mu", "む");
        MAP_TEMP.put("me", "め");
        MAP_TEMP.put("mo", "も");

        MAP_TEMP.put("ya", "や");
        MAP_TEMP.put("wyi", "ゐ");
        MAP_TEMP.put("yu", "ゆ");
        MAP_TEMP.put("wye", "ゑ");
        MAP_TEMP.put("yo", "よ");

        MAP_TEMP.put("ra", "ら");
        MAP_TEMP.put("ri", "り");
        MAP_TEMP.put("ru", "る");
        MAP_TEMP.put("re", "れ");
        MAP_TEMP.put("ro", "ろ");

        MAP_TEMP.put("wa", "わ");
        MAP_TEMP.put("wo", "を");

        // 小文字

        MAP_TEMP.put("xa", "ぁ");
        MAP_TEMP.put("la", "ぁ");
        MAP_TEMP.put("xi", "ぃ");
        MAP_TEMP.put("li", "ぃ");
        MAP_TEMP.put("xyi", "ぃ");
        MAP_TEMP.put("lyi", "ぃ");
        MAP_TEMP.put("xu", "ぅ");
        MAP_TEMP.put("lu", "ぅ");
        MAP_TEMP.put("xe", "ぇ");
        MAP_TEMP.put("le", "ぇ");
        MAP_TEMP.put("xye", "ぇ");
        MAP_TEMP.put("lye", "ぇ");
        MAP_TEMP.put("xo", "ぉ");
        MAP_TEMP.put("lo", "ぉ");
        MAP_TEMP.put("xka", "ヵ");
        MAP_TEMP.put("lka", "ヵ");
        MAP_TEMP.put("lke", "ヶ");
        MAP_TEMP.put("xke", "ヶ");
        MAP_TEMP.put("ltu", "っ");
        MAP_TEMP.put("ltsu", "っ");
        MAP_TEMP.put("xtu", "っ");
        MAP_TEMP.put("xtsu", "っ");

        // 濁音

        MAP_TEMP.put("ga", "が");
        MAP_TEMP.put("gi", "ぎ");
        MAP_TEMP.put("gu", "ぐ");
        MAP_TEMP.put("ge", "げ");
        MAP_TEMP.put("go", "ご");

        MAP_TEMP.put("za", "ざ");
        MAP_TEMP.put("zi", "じ");
        MAP_TEMP.put("ji", "じ");
        MAP_TEMP.put("zu", "ず");
        MAP_TEMP.put("ze", "ぜ");
        MAP_TEMP.put("zo", "ぞ");

        MAP_TEMP.put("da", "だ");
        MAP_TEMP.put("di", "ぢ");
        MAP_TEMP.put("du", "づ");
        MAP_TEMP.put("de", "で");
        MAP_TEMP.put("do", "ど");

        MAP_TEMP.put("ba", "ば");
        MAP_TEMP.put("bi", "び");
        MAP_TEMP.put("bu", "ぶ");
        MAP_TEMP.put("be", "べ");
        MAP_TEMP.put("bo", "ぼ");

        MAP_TEMP.put("pa", "ぱ");
        MAP_TEMP.put("pi", "ぴ");
        MAP_TEMP.put("pu", "ぷ");
        MAP_TEMP.put("pe", "ぺ");
        MAP_TEMP.put("po", "ぽ");

//        MAP_TEMP.put("an", "ん");
//        MAP_TEMP.put("in", "ん");
//        MAP_TEMP.put("un", "ん");
//        MAP_TEMP.put("en", "ん");
//        MAP_TEMP.put("on", "ん");
//        MAP_TEMP.put("nn", "ん");
//        MAP_TEMP.put("n'", "ん");
//        MAP_TEMP.put("xn", "ん");

        MAP_TEMP.put("-", "ー");

        // 拗音
        MAP_TEMP.put("va", "ゔぁ");
        MAP_TEMP.put("vi", "ゔぃ");
        MAP_TEMP.put("vu", "ゔ");
        MAP_TEMP.put("ve", "ゔぇ");
        MAP_TEMP.put("vo", "ゔぉ");
        MAP_TEMP.put("vya", "ゔゃ");
        MAP_TEMP.put("vyi", "ゔぃ");
        MAP_TEMP.put("vyu", "ゔゅ");
        MAP_TEMP.put("vye", "ゔぇ");
        MAP_TEMP.put("vyo", "ゔょ");

        MAP_TEMP.put("kya", "きゃ");
        MAP_TEMP.put("kyi", "きぃ");
        MAP_TEMP.put("kyu", "きゅ");
        MAP_TEMP.put("kye", "きぇ");
        MAP_TEMP.put("kyo", "きょ");

        MAP_TEMP.put("gya", "ぎゃ");
        MAP_TEMP.put("gyi", "ぎぃ");
        MAP_TEMP.put("gyu", "ぎゅ");
        MAP_TEMP.put("gye", "ぎぇ");
        MAP_TEMP.put("gyo", "ぎょ");

        MAP_TEMP.put("sya", "しゃ");
        MAP_TEMP.put("syi", "しぃ");
        MAP_TEMP.put("syu", "しゅ");
        MAP_TEMP.put("sye", "しぇ");
        MAP_TEMP.put("syo", "しょ");

        MAP_TEMP.put("sha", "しゃ");
        MAP_TEMP.put("shu", "しゅ");
        MAP_TEMP.put("she", "しぇ");
        MAP_TEMP.put("sho", "しょ");

        MAP_TEMP.put("zya", "じゃ");
        MAP_TEMP.put("zyi", "じぃ");
        MAP_TEMP.put("zyu", "じゅ");
        MAP_TEMP.put("zye", "じぇ");
        MAP_TEMP.put("zyo", "じょ");

        MAP_TEMP.put("tya", "ちゃ");
        MAP_TEMP.put("tyi", "ちぃ");
        MAP_TEMP.put("tyu", "ちゅ");
        MAP_TEMP.put("tye", "ちぇ");
        MAP_TEMP.put("tyo", "ちょ");

        MAP_TEMP.put("cha", "ちゃ");
        MAP_TEMP.put("chu", "ちゅ");
        MAP_TEMP.put("che", "ちぇ");
        MAP_TEMP.put("cho", "ちょ");

        MAP_TEMP.put("cya", "ちゃ");
        MAP_TEMP.put("cyi", "ちぃ");
        MAP_TEMP.put("cyu", "ちゅ");
        MAP_TEMP.put("cye", "ちぇ");
        MAP_TEMP.put("cyo", "ちょ");

        MAP_TEMP.put("dya", "ぢゃ");
        MAP_TEMP.put("dyi", "ぢぃ");
        MAP_TEMP.put("dyu", "ぢゅ");
        MAP_TEMP.put("dye", "ぢぇ");
        MAP_TEMP.put("dyo", "ぢょ");

        MAP_TEMP.put("tsa", "つぁ");
        MAP_TEMP.put("tsi", "つぃ");
        MAP_TEMP.put("tse", "つぇ");
        MAP_TEMP.put("tso", "つぉ");

        MAP_TEMP.put("tha", "てゃ");
        MAP_TEMP.put("thi", "てぃ");
        MAP_TEMP.put("t'i", "てぃ");
        MAP_TEMP.put("thu", "てゅ");
        MAP_TEMP.put("t'yu", "てゅ");
        MAP_TEMP.put("the", "てぇ");
        MAP_TEMP.put("tho", "てょ");

        MAP_TEMP.put("dha", "でゃ");
        MAP_TEMP.put("dhi", "でぃ");
        MAP_TEMP.put("d'i", "でぃ");
        MAP_TEMP.put("dhu", "でゅ");
        MAP_TEMP.put("d'yu", "でゅ");
        MAP_TEMP.put("dhe", "でぇ");
        MAP_TEMP.put("dho", "でょ");

        MAP_TEMP.put("twa", "とぁ");
        MAP_TEMP.put("twi", "とぃ");
        MAP_TEMP.put("twu", "とぅ");
        MAP_TEMP.put("t'u", "とぅ");
        MAP_TEMP.put("twe", "とぇ");
        MAP_TEMP.put("two", "とぉ");

        MAP_TEMP.put("dwa", "どぁ");
        MAP_TEMP.put("dwi", "どぃ");
        MAP_TEMP.put("dwu", "どぅ");
        MAP_TEMP.put("d'u", "どぅ");
        MAP_TEMP.put("dwe", "どぇ");
        MAP_TEMP.put("dwo", "どぉ");

        MAP_TEMP.put("nya", "にゃ");
        MAP_TEMP.put("nyi", "にぃ");
        MAP_TEMP.put("nyu", "にゅ");
        MAP_TEMP.put("nye", "にぇ");
        MAP_TEMP.put("nyo", "にょ");

        MAP_TEMP.put("hya", "ひゃ");
        MAP_TEMP.put("hyi", "ひぃ");
        MAP_TEMP.put("hyu", "ひゅ");
        MAP_TEMP.put("hye", "ひぇ");
        MAP_TEMP.put("hyo", "ひょ");

        MAP_TEMP.put("bya", "びゃ");
        MAP_TEMP.put("byi", "びぃ");
        MAP_TEMP.put("byu", "びゅ");
        MAP_TEMP.put("bye", "びぇ");
        MAP_TEMP.put("byo", "びょ");

        MAP_TEMP.put("pya", "ぴゃ");
        MAP_TEMP.put("pyi", "ぴぃ");
        MAP_TEMP.put("pyu", "ぴゅ");
        MAP_TEMP.put("pye", "ぴぇ");
        MAP_TEMP.put("pyo", "ぴょ");

        MAP_TEMP.put("fa", "ふぁ");
        MAP_TEMP.put("fi", "ふぃ");
        MAP_TEMP.put("fe", "ふぇ");
        MAP_TEMP.put("fo", "ふぉ");
        MAP_TEMP.put("fya", "ふゃ");
        MAP_TEMP.put("fyu", "ふゅ");
        MAP_TEMP.put("fyo", "ふょ");

        MAP_TEMP.put("hwa", "ふぁ");
        MAP_TEMP.put("hwi", "ふぃ");
        MAP_TEMP.put("hwyu", "ふゅ");
        MAP_TEMP.put("hwe", "ふぇ");
        MAP_TEMP.put("hwo", "ふぉ");

        MAP_TEMP.put("mya", "みゃ");
        MAP_TEMP.put("myi", "みぃ");
        MAP_TEMP.put("myu", "みゅ");
        MAP_TEMP.put("mye", "みぇ");
        MAP_TEMP.put("myo", "みょ");

        MAP_TEMP.put("rya", "りゃ");
        MAP_TEMP.put("ryi", "りぃ");
        MAP_TEMP.put("ryu", "りゅ");
        MAP_TEMP.put("rye", "りぇ");
        MAP_TEMP.put("ryo", "りょ");

        MAP_TEMP.put("qa", "くぁ");
        MAP_TEMP.put("qi", "くぃ");
        MAP_TEMP.put("qe", "くぇ");
        MAP_TEMP.put("qo", "くぉ");

        MAP_TEMP.put("kwa", "くぁ");
        MAP_TEMP.put("kwi", "くぃ");
        MAP_TEMP.put("kwu", "くぅ");
        MAP_TEMP.put("kwe", "くぇ");
        MAP_TEMP.put("kwo", "くぉ");

        MAP_TEMP.put("gwa", "ぐぁ");
        MAP_TEMP.put("gwi", "ぐぃ");
        MAP_TEMP.put("gwu", "ぐぅ");
        MAP_TEMP.put("gwe", "ぐぇ");
        MAP_TEMP.put("gwo", "ぐぉ");

        MAP_TEMP.put("swa", "すぁ");
        MAP_TEMP.put("swi", "すぃ");
        MAP_TEMP.put("swu", "すぅ");
        MAP_TEMP.put("swe", "すぇ");
        MAP_TEMP.put("swo", "すぉ");

        MAP_TEMP.put("zwa", "ずぁ");
        MAP_TEMP.put("zwi", "ずぃ");
        MAP_TEMP.put("zwu", "ずぅ");
        MAP_TEMP.put("zwe", "ずぇ");
        MAP_TEMP.put("zwo", "ずぉ");

        MAP_TEMP.put("ja", "じゃ");
        MAP_TEMP.put("ju", "じゅ");
        MAP_TEMP.put("je", "じぇ");
        MAP_TEMP.put("jo", "じょ");

        MAP_TEMP.put("wha", "うぁ");
        MAP_TEMP.put("whi", "うぃ");
        MAP_TEMP.put("wi", "うぃ");
        MAP_TEMP.put("we", "うぇ");
        MAP_TEMP.put("whe", "うぇ");
        MAP_TEMP.put("who", "うぉ");

        // ?
        for (String s : "wkcqsthfmyrxlgzjdbp".split("")) {
            MAP_TEMP.put(s+s+"a", "っ"+ MAP_TEMP.get(s+"a"));
            MAP_TEMP.put(s+s+"i", "っ"+ MAP_TEMP.get(s+"i"));
            MAP_TEMP.put(s+s+"u", "っ"+ MAP_TEMP.get(s+"u"));
            MAP_TEMP.put(s+s+"e", "っ"+ MAP_TEMP.get(s+"e"));
            MAP_TEMP.put(s+s+"o", "っ"+ MAP_TEMP.get(s+"o"));
        }
        List<String> threeKeys = new ArrayList<>(
                MAP_TEMP.keySet().stream().filter(str -> str.length() == 3).collect(Collectors.toList())
        );
        for (String s : threeKeys) {
            MAP_TEMP.put(s.charAt(0) + s, "っ" + MAP_TEMP.get(s));
        }

        MAP_TEMP.put("n", "ん");

        MAP = Collections.unmodifiableMap(MAP_TEMP);
        List<String> set = new ArrayList<>(MAP.keySet());
        set.sort(Comparator.comparingInt(String::length).reversed());
        KEYS = Collections.unmodifiableList(set);
    }

    public static String convert(final String text) {
        String res = text;

        for (String key : KEYS) {
            res = res.replace(key, MAP.get(key));
        }

        return res;
    }

}