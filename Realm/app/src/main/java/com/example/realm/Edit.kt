package com.example.realm


import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where

class EditActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val btstart = findViewById<Button>(R.id.btstart)
        val btback = findViewById<Button>(R.id.btback)
        val btnext = findViewById<Button>(R.id.btnext)
        val btchange = findViewById<Button>(R.id.btchange)
        val iv = findViewById<ImageView>(R.id.iv)
        val iv2 = findViewById<ImageView>(R.id.iv2)
        realm = Realm.getDefaultInstance()
        val imageResources: MutableList<Int> = mutableListOf()

// MainActivity内で画像データを取得した後

        val imageData = intent.getParcelableExtra("imageData") as Drawable?
        if (imageData != null) {
            iv.setImageDrawable(imageData)
        }

        val realmResults: List<MyModel> = realm.where(MyModel::class.java).findAll()
        for (i  in realmResults.indices) {
            val resourceId = realmResults[i].imageView
            imageResources.add(resourceId)
        }

        val imagestr = intArrayOf(
            R.drawable.strb, R.drawable.stry,
        )
        var strIndex = 0

        if (imageData != null) {
            iv.setImageDrawable(imageData)
        }
        iv2.setImageResource(R.drawable.stry)

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
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}

