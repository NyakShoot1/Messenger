package ru.nyakshoot.messenger.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun formatMessageDate(timestamp: Timestamp): String {
    val date = timestamp.toDate()
    val today = Calendar.getInstance()
    val messageCalendar = Calendar.getInstance().apply { time = date }

    return when {
        today.get(Calendar.YEAR) == messageCalendar.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == messageCalendar.get(Calendar.DAY_OF_YEAR) -> "Сегодня"
        today.get(Calendar.YEAR) == messageCalendar.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) - 1 == messageCalendar.get(Calendar.DAY_OF_YEAR) -> "Вчера"
        else -> {
            SimpleDateFormat("d MMMM", Locale.getDefault()).format(date)
        }
    }
}