package dev.prince.monthviewapp.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.prince.monthviewapp.util.getDaysInMonth
import dev.prince.monthviewapp.util.getMonthName

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    year: Int,
    month: Int,
) {
    val days = getDaysInMonth(year, month)

    Column(Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "${getMonthName(month)} $year")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            daysOfWeek.forEach { day ->
                Text(text = day, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(days.size) { index ->
                val day = days[index]
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp)
                        .background(if (day.isNotEmpty()) Color.LightGray else Color.Transparent)
                        .clickable(enabled = day.isNotEmpty()) {
                            if (day.isNotEmpty()) {
//                                onDateClick("$year-$month-$day")
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
