package com.example.vibechat.utils

import kotlinx.datetime.*

fun parseUtcToLocal(utcIso: String): LocalDateTime {
    val instant = Instant.parse(utcIso)
    return instant
        .toLocalDateTime(TimeZone.currentSystemDefault())
}


fun formatMessageTime(
    messageTime: LocalDateTime,
    now: LocalDateTime = Clock.System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
): String {
    val messageDate = messageTime.date
    val today = now.date
    val yesterday = today.minus(DatePeriod(days = 1))

    return when {
        messageDate == today -> {
            formatTo12HourTime(messageTime)
        }
        messageDate == yesterday -> {
            "Yesterday"
        }
        messageDate.year == today.year -> {
            formatDate(messageTime, withYear = false)
        }
        else -> {
            formatDate(messageTime, withYear = true)
        }
    }
}
fun formatTo12HourTime(time: LocalDateTime): String {
    val hour = time.hour
    val minute = time.minute

    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }

    val amPm = if (hour < 12) "AM" else "PM"
    val minuteStr = minute.toString().padStart(2, '0')

    return "$displayHour:$minuteStr $amPm"
}


fun formatDateHeader(
    messageTime: LocalDateTime,
    now: LocalDateTime = Clock.System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
): String {
    val date = messageTime.date
    val today = now.date
    val yesterday = today.minus(DatePeriod(days = 1))

    return when (date) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> formatDate(messageTime, withYear = date.year != today.year)
    }
}

fun formatDate(
    time: LocalDateTime,
    withYear: Boolean
): String {
    val day = time.dayOfMonth
    val month = time.month.name.lowercase()
        .replaceFirstChar { it.uppercase() }
        .take(3)

    return if (withYear) {
        "$day $month ${time.year}"
    } else {
        "$day $month"
    }
}

fun getBubbleTime(utcTime: String) : String{
    if(utcTime.isEmpty()) return ""
    val localTime = parseUtcToLocal(utcTime)
    return formatTo12HourTime(localTime)
}
