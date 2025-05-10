package com.cipta.ciptajagonyawifi.ui.article

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatTimestamp(seconds: Long): String {
    val dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(seconds),
        ZoneId.of("Asia/Jakarta")
    )
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm")
    return dateTime.format(formatter)
}
