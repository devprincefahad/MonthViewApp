package dev.prince.monthviewapp.ui.calendar

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.prince.monthviewapp.ui.components.CalendarView
import java.time.LocalDate
import java.time.Month

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen() {
    val currentDate = LocalDate.now()
    val selectedDate = remember { mutableStateOf("") }
    val context = LocalContext.current

    val startYear = 2000
    val totalMonths = (currentDate.year - startYear) * 12 + currentDate.monthValue

    val initialPage = totalMonths - 1

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { totalMonths }
    )

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        val monthIndex = page % 12
        val year = startYear + (page / 12)
        val month = Month.of(monthIndex + 1)

        CalendarView(
            year = year,
            month = month.value,
            currentYear = currentDate.year,
            currentMonth = currentDate.monthValue,
            currentDay = currentDate.dayOfMonth,
            onDateClick = { dateClicked ->
                Toast.makeText(context, "Selected date: $dateClicked", Toast.LENGTH_SHORT).show()
                selectedDate.value = dateClicked
            }
        )
    }
}