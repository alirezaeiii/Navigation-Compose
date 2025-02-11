package com.android.sample.app.database.dashboard

import androidx.room.TypeConverter
import com.google.gson.Gson

class DbLinkConverter {

    @TypeConverter
    fun jsonToList(value: String): List<LinkEntity> =
        Gson().fromJson(value, Array<LinkEntity>::class.java).toList()

    @TypeConverter
    fun listToJson(value: List<LinkEntity?>) = Gson().toJson(value.filterNotNull())
}