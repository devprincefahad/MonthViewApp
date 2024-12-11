package dev.prince.monthviewapp.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.prince.monthviewapp.R
import dev.prince.monthviewapp.util.getDaysInMonth
import dev.prince.monthviewapp.util.getMonthName

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarView(
    year: Int,
    month: Int,
    currentYear: Int,
    currentMonth: Int,
    currentDay: Int,
    onDateClick: (String) -> Unit,
    onTaskCreated: (String, String) -> Unit
) {
    var showTaskModal by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("$currentYear-$currentMonth-$currentDay") }

    val days = getDaysInMonth(year, month)
    val allDays = days.map { it to true }

    Column(
        modifier = Modifier.padding(16.dp)
        ) {

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "${getMonthName(month)} $year")

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { showTaskModal = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Add Task"
                )
            }
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
            items(allDays.size) { index ->
                val dayInfo = allDays[index]
                val day = dayInfo.first
                val isToday =
                    year == currentYear && month == currentMonth && day == currentDay.toString()
                val isSelected = selectedDate == "$year-$month-${day.padStart(2, '0')}"

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp)
                        .border(
                            width = if (isSelected) 2.dp else 0.dp,
                            color = if (isSelected) Color.Blue else Color.Transparent,
                            shape = RectangleShape
                        )
                        .clickable(enabled = day.isNotEmpty()) {
                            if (day.isNotEmpty()) {
                                selectedDate = "$year-$month-${day.padStart(2, '0')}"
                                onDateClick(selectedDate)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        textAlign = TextAlign.Center,
                        color = when {
                            isToday -> Color.Blue
                            else -> Color.Black
                        }
                    )
                }
            }
        }

        if (showTaskModal) {
            AddTaskDialog(
                date = selectedDate,
                onDismiss = { showTaskModal = false },
                onTaskSave = { title, description ->
                    onTaskCreated(title, description)
                    showTaskModal = false
                }
            )
        }
    }
}

