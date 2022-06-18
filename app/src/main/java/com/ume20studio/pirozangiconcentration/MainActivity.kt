package com.ume20studio.pirozangiconcentration

import android.content.Intent
import android.os.Bundle
import android.media.MediaPlayer
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalTime
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    // 乱数生成用変数
    @RequiresApi(Build.VERSION_CODES.O)
    private val r = Random(LocalTime.now().second.toLong())

    // BGM再生用メディアプレーヤーのインスタンス
    private lateinit var mp:MediaPlayer

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        // もとからある初期化処理
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // BGMスタート
        if(r.nextInt(4) == 0){
            mp = MediaPlayer.create(this,R.raw.gacchanbeatbox)
        } else {
            mp = MediaPlayer.create(this,R.raw.pirozangisong)
        }
        mp.isLooping = true
        mp.start()

        // イベントリスナへの紐付け
        val btn = findViewById<Button>(R.id.button)
        val title = findViewById<ImageButton>(R.id.imageTitle)
        val piro = findViewById<ImageButton>(R.id.imagePirozangi)
        val listener = Tap()
        btn.setOnClickListener(listener)
        title.setOnClickListener(listener)
        piro.setOnClickListener(listener)
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
    }

    private inner class Tap : View.OnClickListener {
        override fun onClick(view: View) {
            mp.stop()
            val intent = Intent(this@MainActivity, MainActivity1::class.java)
            startActivity(intent)
            finish()
        }
    }
}