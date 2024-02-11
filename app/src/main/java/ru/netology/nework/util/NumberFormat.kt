package ru.netology.nework.util

import android.widget.TextView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Converter {
    fun convertNumber(number: Int): String {
        return when {
            number >= 1100000 -> "${number / 1000000}.${number / 100000 % 10}M"
            number >= 1000000 -> "${number / 1000000}M"
            number >= 10000 -> "${number / 1000}K"
            number >= 1100 -> "${number / 1000}.${number / 100 % 10}K"
            number >= 1000 -> "${number / 1000}К"
            else -> number.toString()
        }
    }
}
fun TextView.formattedDate(dateString: String) {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault())
    try {
        val date = inputFormat.parse(dateString)
        val formattedDate = outputFormat.format(date)
        text = formattedDate
    } catch (e: ParseException) {
        e.printStackTrace()
    }
}
