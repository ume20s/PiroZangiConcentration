package com.ume20studio.pirozangiconcentration

import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.RequiresApi
import java.io.BufferedReader
import java.io.File
import java.time.LocalTime
import kotlin.random.Random

class MainActivity1 : AppCompatActivity() {
    val akira = 0                       // アキラさんは特別
    val cardnum: Int = 4                // カード枚数
    val basepoint = 10                  // 基本ポイント
    var score: Int = 0                  // スコア
    var highscore: Int = 0              // ハイスコア
    var combo: Int = 0                  // コンボ数
    var miss: Int = 0                   // ミスした回数
    var cardopen: Int = 0               // 開いたカードの枚数
    var ichimaimePos: Int = -1          // 1枚目に開いたカードの位置
    var ichimaimeZangi: Int = -1        // 1枚目に開いたカードのザンギ番号
    var status: Int = 0                 // 判定状態（0:未判定, 1:ピンポン 2:ブッブー）
    var pos1: Int = -1                  // 判定済みカードの位置の保存用
    var pos2: Int = -1

    // BGM再生用メディアプレーヤーのインスタンス
    private lateinit var mp:MediaPlayer

    // 音声再生用サウンドプールのインスタンス
    private lateinit var soundPool: SoundPool
    private var pinpon = 0
    private var bubuu = 0
    private var omedetou = 0
    private var gameover = 0
    private var pinponAkira = 0
    private var pinponAllback = 0
    private var pinponYoucan = 0
    private var pinponGacchan = 0
    private var pinponKuumaaaa = 0
    private var pinponTefuyasu = 0

    // カードのイメージボタン配列
    val bt = arrayOf(R.id.ImgBtn1_01, R.id.ImgBtn1_02, R.id.ImgBtn1_03, R.id.ImgBtn1_04)

    // カードのザンギグラフィック配列
    val zangi = arrayOf(R.drawable.s_akira, R.drawable.s_allback, R.drawable.s_youcan,
        R.drawable.s_gacchan, R.drawable.s_kuumaaa, R.drawable.s_tefuyasu, R.drawable.c01,
        R.drawable.c02, R.drawable.c03, R.drawable.c04, R.drawable.c05, R.drawable.c06,
        R.drawable.c07, R.drawable.c08, R.drawable.c09, R.drawable.c10, R.drawable.c11,
        R.drawable.c12, R.drawable.c13, R.drawable.c14, R.drawable.c15, R.drawable.c16,
        R.drawable.c17, R.drawable.c18, R.drawable.c19, R.drawable.c20, R.drawable.c21,
        R.drawable.c22, R.drawable.c23, R.drawable.c24, R.drawable.c25, R.drawable.c26,
        R.drawable.c27, R.drawable.c28, R.drawable.c29, R.drawable.c30, R.drawable.c31,
        R.drawable.c32, R.drawable.c33, R.drawable.c34, R.drawable.c35, R.drawable.c36,
        R.drawable.c37, R.drawable.c38, R.drawable.c39, R.drawable.c40, R.drawable.c41,
        R.drawable.c42, R.drawable.c43, R.drawable.c44, R.drawable.c45, R.drawable.c46,
        R.drawable.c47, R.drawable.c48, R.drawable.c49, R.drawable.c50, R.drawable.c51,
        R.drawable.c52, R.drawable.c53, R.drawable.c54, R.drawable.c55, R.drawable.c56,
        R.drawable.c57, R.drawable.c58, R.drawable.c59, R.drawable.c60, R.drawable.c61,
        R.drawable.c62, R.drawable.c63, R.drawable.c64, R.drawable.c65, R.drawable.c66,
        R.drawable.c67, R.drawable.c68, R.drawable.c69, R.drawable.c70, R.drawable.c71,
        R.drawable.c72, R.drawable.c73, R.drawable.c74, R.drawable.c75, R.drawable.c76,
        R.drawable.c77, R.drawable.c78)
    
    // ミスインジケータのグラフィック配列
    val peke = arrayOf(R.id.Miss1_01, R.id.Miss1_02, R.id.Miss1_03,
        R.id.Miss1_04, R.id.Miss1_05, R.id.Miss1_06, R.id.Miss1_07,
        R.id.Miss1_08, R.id.Miss1_09, R.id.Miss1_10 )
    
    // カードのザンギ番号用配列
    var nakami = arrayOf(-1,-1,-1,-1)
    
    // 乱数生成用変数
    @RequiresApi(Build.VERSION_CODES.O)
    private val r = Random(LocalTime.now().second.toLong())
    
    @RequiresApi(Build.VERSION_CODES.O)
    // ゲーム起動時の処理
    override fun onCreate(savedInstanceState: Bundle?) {
        // もとからある初期化処理
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)
    
        // サウンドプールのもろもろの初期化
        val sPattr = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build()
        soundPool = SoundPool.Builder().setAudioAttributes(sPattr).setMaxStreams(6).build()
    
        // 音声データのロード
        pinpon = soundPool.load(this, R.raw.pinpon, 1)
        bubuu = soundPool.load(this, R.raw.bubuu, 1)
        omedetou = soundPool.load(this, R.raw.clear, 1)
        gameover = soundPool.load(this, R.raw.gameover, 1)
        pinponAkira = soundPool.load(this, R.raw.pinpon_akira, 1)
        pinponAllback = soundPool.load(this, R.raw.pinpon_allback, 1)
        pinponYoucan = soundPool.load(this, R.raw.pinpon_youcan, 1)
        pinponGacchan = soundPool.load(this, R.raw.pinpon_gacchan, 1)
        pinponKuumaaaa = soundPool.load(this, R.raw.pinpon_kuumaaaa, 1)
        pinponTefuyasu = soundPool.load(this, R.raw.pinpon_tefuyasu, 1)

        // BGMスタート
        mp = MediaPlayer.create(this,R.raw.pirozangimusic)
        mp.isLooping = true
        mp.start()

        // ハイスコアファイルの読み込み
        val readFile = File(applicationContext.filesDir, "HighScore.txt")
        if(readFile.exists()){
            highscore = readFile.bufferedReader().use(BufferedReader::readText).toInt()
        } else {
            highscore = 0
        }
        findViewById<TextView>(R.id.HighScore1).text = highscore.toString()

        // 使うザンギナンバーを決定
        var usezangi = IntArray(cardnum/2)      // 採用するザンギナンバー
        var eratos = IntArray(zangi.size)   // 使ってるフラグ
        for(i in 0 until cardnum/2) {
            var flg: Boolean = false
            do {
                var c = r.nextInt(1,zangi.size)
                if(eratos[c] != 1) {
                    usezangi[i] = c
                    eratos[c] = 1
                    flg = true
                }
            } while(flg == false)
        }

        // 1/100の確率でアキラさんが登場
        if(r.nextInt(100) == akira) {
            usezangi[0] = akira
        }
    
        // ザンギをランダムに配置
        for(i in 0 until cardnum/2) {        // さっき決めたザンギを配置
            for(j in 0..1) {    // 同じザンギナンバーのカードを2枚配置する
                var flg: Boolean = false
                do {
                    var pos = r.nextInt(cardnum)
                    if(nakami[pos] == -1) {
                        nakami[pos] = usezangi[i]
                        flg = true
                    }
                } while(flg == false)
            }
        }

        // カードイメージへのイベントリスナの紐づけ
        val cardListener = CardTap()
        for(i in 0 until cardnum) {
            findViewById<ImageButton>(bt[i]).setOnClickListener(cardListener)
        }

        // 背景へのイベントリスナの紐づけ
        val baseListener = BaseTap()
        findViewById<ImageButton>(R.id.PlainBase1).setOnClickListener(baseListener)

        // ゲームエンドバナーへのイベントリスナの紐づけ
        val gameendListener = GameEnd()
        findViewById<ImageButton>(R.id.GameEnd1).setOnClickListener(gameendListener)
    }

    // ポーズ状態なら音楽もポーズ
    override fun onPause() {
        super.onPause()
        mp.pause()
    }

    // ゲーム再開なら音楽も再開
    override fun onResume() {
        super.onResume()
        mp.start()
    }

    // 終了ならBGMを止めて音関係のメモリを解放
    override fun onDestroy() {
        super.onDestroy()
        mp.stop()
        mp.release()
        soundPool.release()
    }


    // カードがタップされた時の処理
    private inner class CardTap : View.OnClickListener {
        override fun onClick(v: View){
            var opn: Boolean = false // 開いたフラグ
            var pos: Int = -1        // 開いた位置
            var zng: Int = -1        // 開いたザンギナンバー
            
            if(status == 0) {        // 未判定状態だったらカードをオープンする
                // カードオープン！
                when(v.id){
                    bt[0] -> {
                        if(ichimaimePos != 0) {
                            opn = true
                            pos = 0
                            zng = nakami[0]
                            findViewById<ImageButton>(bt[pos]).setImageResource(zangi[nakami[pos]])
                        }
                    }
                    bt[1] -> {
                        if(ichimaimePos != 1) {
                            opn = true
                            pos = 1
                            zng = nakami[1]
                            findViewById<ImageButton>(bt[pos]).setImageResource(zangi[nakami[pos]])
                        }
                    }
                    bt[2] -> {
                        if(ichimaimePos != 2) {
                            opn = true
                            pos = 2
                            zng = nakami[2]
                            findViewById<ImageButton>(bt[pos]).setImageResource(zangi[nakami[pos]])
                        }
                    }
                    bt[3] -> {
                        if(ichimaimePos != 3) {
                            opn = true
                            pos = 3
                            zng = nakami[3]
                            findViewById<ImageButton>(bt[pos]).setImageResource(zangi[nakami[pos]])
                        }
                    }
                }
    
                // カードを開いた時の処理
                if(opn == true) {
                    cardopen++
                    if(cardopen % 2 == 0) {     // 開いた枚数が偶数だったらあっているか判定する
                        if(zng == ichimaimeZangi){  // あってた（やったー！）
                            // ピンポンステータス
                            status = 1
                            
                            // あたりカードによる効果
                            when(zng) {
                                0 -> {  // アキラさん
                                    soundPool.play(pinponAkira, 1.0f, 1.0f, 0, 0, 1.0f)
                                    combo++
                                    score += 800    // コンボに関係なく800ポイント
                                    miss = 0        // これまでのミスが帳消しに
                                    for(i in 0..9) {
                                        findViewById<ImageView>(peke[i]).visibility = View.INVISIBLE
                                    }
                                }
                                1 -> {  // オールバックさん
                                    soundPool.play(pinponAllback, 1.0f, 1.0f, 0, 0, 1.0f)
                                    combo++
                                    score += 500    // コンボに関係なく500ポイント
                                }
                                2 -> {  // ゆーきゃんさん
                                    soundPool.play(pinponYoucan, 1.0f, 1.0f, 0, 0, 1.0f)
                                    combo++
                                    score += 500    // コンボに関係なく500ポイント
                                }
                                3 -> {  // がっちゃんさん
                                    soundPool.play(pinponGacchan, 1.0f, 1.0f, 0, 0, 1.0f)
                                    combo++
                                    score += 333    // コンボに関係なく333(そそそ)ポイント
                                }
                                4 -> {  // くぅまぁぁぁさん
                                    soundPool.play(pinponKuumaaaa, 1.0f, 1.0f, 0, 0, 1.0f)
                                    combo++
                                    score += basepoint * combo * 2  // コンボ効果が２倍
                                }
                                5 -> {  // てふやすさん
                                    soundPool.play(pinponTefuyasu, 1.0f, 1.0f, 0, 0, 1.0f)
                                    combo++
                                    score += basepoint * combo * 2  // コンボ効果が２倍
                                }
                                else -> {
                                    soundPool.play(pinpon, 1.0f, 1.0f, 0, 0, 1.0f)
                                    combo++
                                    score += basepoint * combo
                                }
                            }
                            
                            // コンボとスコア表示
                            findViewById<TextView>(R.id.Combo1).text = combo.toString()
                            findViewById<TextView>(R.id.Score1).text = score.toString()
                            
                            // ハイスコア処理
                            if(score > highscore) {
                                highscore = score
                                findViewById<TextView>(R.id.HighScore1).text = highscore.toString()
                                File(applicationContext.filesDir, "HighScore.txt").writer().use {
                                    it.write(highscore.toString())
                                }
                            }
                        } else {                    // あってなかった（残念）
                            // ブッブーステータス
                            status = 2
                            
                            // ブッブー
                            soundPool.play(bubuu, 1.0f, 1.0f, 0, 0, 1.0f)
                
                            // ミスを加算
                            findViewById<ImageView>(peke[miss]).visibility = View.VISIBLE
                            miss++
                
                            // コンボをゼロに
                            combo = 0
                            findViewById<TextView>(R.id.Combo1).text = combo.toString()
                        }
                        
                        // 判定済みカードの位置番号をとっておく
                        pos1 = ichimaimePos
                        pos2 = pos
                        
                        // 1枚目情報を破棄
                        ichimaimePos = -1
                        ichimaimeZangi = -1
                    } else {                    // 開いた枚数が奇数だったら位置とザンギナンバーをとっとく
                        ichimaimePos = pos
                        ichimaimeZangi = zng
                    }
                    opn = false
                }
            } else {    // 判定済みなら
                if(status == 1) {   // 判定結果があってたら
                    // 開いたカードを非表示にする
                    findViewById<ImageButton>(bt[pos1]).visibility = View.INVISIBLE
                    findViewById<ImageButton>(bt[pos2]).visibility = View.INVISIBLE
                } else {            // 判定結果がはずれてたら
                    // 開いたカードをもとに戻す
                    findViewById<ImageButton>(bt[pos1]).setImageResource(R.drawable.card)
                    findViewById<ImageButton>(bt[pos2]).setImageResource(R.drawable.card)
                    cardopen -= 2
                }

                // 全部開いたときの処理
                if(cardopen >= cardnum) {
                    mp.stop()
                    findViewById<ImageButton>(R.id.GameEnd1).setImageResource(R.drawable.clear_stage1)
                    findViewById<ImageButton>(R.id.GameEnd1).visibility = View.VISIBLE
                    soundPool.play(omedetou, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                // ミスが１０回になったときの処理
                if(miss >= 10) {
                    mp.stop()
                    findViewById<ImageButton>(R.id.GameEnd1).setImageResource(R.drawable.gameover)
                    findViewById<ImageButton>(R.id.GameEnd1).visibility = View.VISIBLE
                    soundPool.play(gameover, 1.0f, 1.0f, 0, 0, 1.0f)
                }
                // 未判定状態に
                status = 0
            }
        }
    }

    // 背景がタップされたときの処理
    private inner class BaseTap : View.OnClickListener {
        override fun onClick(v: View){
            if(status != 0){        // 判定済みで
                if(status == 1) {   // 判定結果があってたら
                    // 開いたカードを非表示にする
                    findViewById<ImageButton>(bt[pos1]).visibility = View.INVISIBLE
                    findViewById<ImageButton>(bt[pos2]).visibility = View.INVISIBLE
                } else  {            // 判定結果がはずれてたら
                    // 開いたカードをもとに戻す
                    findViewById<ImageButton>(bt[pos1]).setImageResource(R.drawable.card)
                    findViewById<ImageButton>(bt[pos2]).setImageResource(R.drawable.card)
                    cardopen -= 2
                }
                // 未判定状態に
                status = 0
            }

            // 全部開いたときの処理
            if(cardopen >= cardnum) {
                mp.stop()
                findViewById<ImageButton>(R.id.GameEnd1).setImageResource(R.drawable.clear_stage1)
                findViewById<ImageButton>(R.id.GameEnd1).visibility = View.VISIBLE
                soundPool.play(omedetou, 1.0f, 1.0f, 0, 0, 1.0f)
            }

            // ミスが１０回になったときの処理
            if(miss >= 10) {
                mp.stop()
                findViewById<ImageButton>(R.id.GameEnd1).setImageResource(R.drawable.gameover)
                findViewById<ImageButton>(R.id.GameEnd1).visibility = View.VISIBLE
                soundPool.play(gameover, 1.0f, 1.0f, 0, 0, 1.0f)
            }
        }
    }

    // ゲームエンドバナーがタップされた時の処理
    private inner class GameEnd : View.OnClickListener {
        override fun onClick(v:View){
            // 全部開いていたら次のステージに処理を移行
            if(cardopen >= cardnum) {
                val intent = Intent(this@MainActivity1, MainActivity2::class.java)
                intent.putExtra("COMBO", combo)
                intent.putExtra("SCORE", score)
                intent.putExtra("HIGHSCORE", highscore)
                intent.putExtra("MISS", miss)
                startActivity(intent)
            }

            // ミスが１０回だったら最初の画面へ
            if(miss >= 10) {
                val intent = Intent(this@MainActivity1, MainActivity::class.java)
                startActivity(intent)
            }

            // このアクティビティは終了
            finish()
        }
    }
}