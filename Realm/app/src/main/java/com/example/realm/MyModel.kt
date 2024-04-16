package com.example.suraido

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class MyModel: RealmObject(), Serializable {
    @PrimaryKey
    var id :Long =0
    var imageView :Int = 0
}