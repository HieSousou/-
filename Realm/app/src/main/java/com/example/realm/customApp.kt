package com.example.suraido

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

//初期化と構築
class customApp :Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        //Configuration構築
        val config =RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}