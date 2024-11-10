package ru.nyakshoot.messenger.data.core.db.converters

import androidx.room.TypeConverter
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

internal class TimestampConverter {

    @TypeConverter
    fun fromString(value: String): Timestamp {
        val mapType = object : TypeToken<Timestamp>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromTimestamp(timestamp: Timestamp): String {
        return Gson().toJson(timestamp)
    }
}