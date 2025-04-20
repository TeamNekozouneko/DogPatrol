# DogPatrol
どっかの犬が飼い慣らしているどっかの犬がチャットを自動でモデレートするプラグイン
## 導入方法
プロキシの`/plugins`にJarファイルを導入する
> [!TIP]
> このプラグインはPlugmanの`load/unload/reload`に対応しています。
## 権限
* `dogpatrol.reload` Configのリロードコマンド（/dogpatrol reload）
* `dogpatrol.notify` メッセージがブロックまたは検出された際に通知を受け取る
## 検出種類
### ContainsBadwords
`/plugins/DogPatrol/badwords.yml`に収録している単語を含んでいた場合、ブロックします。
### DuplicateContent
連続した同じ文字列の発言を以降ブロックします。
> [!INFO]
> Bufferレベルに基づくため、一度ブロックされた後は若干ブロックされやすくなります。
### SimilarityContent
連続した似ている文字列の発言を以降ブロックします。
> [!INFO]
> 類似度の計算の問題により、4文字以上のチャットのみ有効です。
### IMEConversionAnalysis
IMEで日本語変換を行い、品詞の組み合わせで単語を検査します。
> [!CAUTION]
> 非同期で多少時間がかかる処理なため、ブロックはされません。