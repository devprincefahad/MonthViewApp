package dev.prince.monthviewapp.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun getDaysInMonth(year: Int, month: Int): List<String> {
    val yearMonth = YearMonth.of(year, month)
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfMonth = LocalDate.of(year, month, 1).dayOfWeek.value

    val days = mutableListOf<String>()

    // Add empty strings for days before the first day
    for (i in 1 until firstDayOfMonth) {
        days.add("")
    }

    // Add actual days of the month
    for (day in 1..daysInMonth) {
        days.add(day.toString())
    }

    return days
}

@RequiresApi(Build.VERSION_CODES.O)
fun getMonthName(month: Int): String {
    return YearMonth.of(0, month).month.getDisplayName(TextStyle.FULL, Locale.getDefault())
}
