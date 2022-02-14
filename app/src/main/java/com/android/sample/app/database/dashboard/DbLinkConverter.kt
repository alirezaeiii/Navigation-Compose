package com.android.sample.app.database.dashboard

import androidx.room.TypeConverter
import com.google.gson.Gson

class DbLinkConverter {

    @TypeConverter
    fun jsonToList(value: String): List<DbLink> =
        Gson().fromJson(value, Array<DbLink>::class.java).toList()

    @TypeConverter
    fun listToJson(value: List<DbLink?>) = Gson().toJson(value.filterNotNull())
}