package com.example.Realm

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where


class MainActivity : AppCompatActivity() {
    private lateinit var realm: Realm//realm変数を用意
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btstart = findViewById<Button>(R.id.btstart)
        val btback = findViewById<Button>(R.id.btback)
        val btnext = findViewById<Button>(R.id.btnext)
        val btchange = findViewById<Button>(R.id.btchange)
        val iv = findViewById<ImageView>(R.id.iv)
        val iv2 = findViewById<ImageView>(R.id.iv2)
        realm = Realm.getDefaultInstance()//realmのインスタンス
        val imageResources = intArrayOf(
            R.drawable.nezumi, R.drawable.ushi, R.drawable.tora, R.drawable.usagi, R.drawable.tatsu, R.drawable.hebi,
            R.drawable.uma, R.drawable.saru, R.drawable.tori, R.drawable.hitsuji, R.drawable.inu, R.drawable.inosisi,
        )
        val imagestr = intArrayOf(
            R.drawable.stry, R.drawable.strb,
        )
        var strIndex = 0

        iv2.setImageResource(R.drawable.strb)

        var currentIndex = 0
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                iv.setImageResource(imageResources[currentIndex])
                currentIndex = (currentIndex + 1) % imageResources.size // 次の画像に切り替え
                handler.postDelayed(this, 2000) // 2秒ごとに切り替え
            }
        }
        var stronoff = false
        iv2.setOnClickListener {
            if (stronoff) {
                // 切り替えが実行中の場合、一時停止する
                stronoff = false
                iv2.setImageResource(R.drawable.strb)
            } else {
                // 切り替えが停止中の場合、再開する
                stronoff = true
                iv2.setImageResource(R.drawable.stry)
            }
        }
        var sraidonoff = false // 切り替えが実行中かどうかを示すフラグ

        btstart.setOnClickListener {
            if (sraidonoff) {
                // 切り替えが実行中の場合、一時停止する
                sraidonoff = false
                handler.removeCallbacks(runnable)
                btstart.text = "START"
            } else {
                // 切り替えが停止中の場合、再開する
                sraidonoff = true
                handler.post(runnable)
                btstart.text = "STOP"
            }
        }
        btnext.setOnClickListener {
            currentIndex = (currentIndex + 1) % imageResources.size
            iv.setImageResource(imageResources[currentIndex])
        }
        btback.setOnClickListener {
            currentIndex = (currentIndex - 1 + imageResources.size) % imageResources.size
            iv.setImageResource(imageResources[currentIndex])
        }
        btchange.setOnClickListener {
            val intent =Intent(this,EditActivity::class.java)
            startActivity(intent)
        }
        iv2.setOnClickListener {
            // imageView から Drawable を取得
            if (stronoff == false){
                stronoff = true
                val imageData = imageResources[currentIndex]
                iv2.setImageResource(R.drawable.stry)
// Realm トランザクション内でデータを保存
                realm.executeTransaction {
                    val currentId = realm.where<MyModel>().max("id")
                    val nextId = (currentId?.toLong() ?: 0L) + 1L
                    val myModel = realm.createObject<MyModel>(nextId)
                    myModel.imageView = imageData
                }


// Intent を作成するが、まだ遷移は行わない
                val intent = Intent(this, EditActivity::class.java)
                intent.putExtra("imageData", imageData) // "imageData"はキー、imageDataは画像データ

// この後に必要なタイミングで startActivity(intent) を呼び出す

            }else{
                stronoff = false
                iv2.setImageResource(R.drawable.strb)
            }

        }
    }

    override fun onResume() {
        super.onResume()

        val realmResults: List<MyModel> = realm.where(MyModel::class.java).findAll()

        val iv = findViewById<ImageView>(R.id.iv)
        if (realmResults.isNotEmpty()) {
            val firstImageResourceId = realmResults[0].imageView
            iv.setImageResource(firstImageResourceId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}


