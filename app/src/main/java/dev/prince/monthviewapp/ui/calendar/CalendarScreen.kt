package dev.prince.monthviewapp.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.prince.monthviewapp.models.TaskDetail
import dev.prince.monthviewapp.ui.components.CalendarView
import java.time.LocalDate
import java.time.Month

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel()
) {

    val currentDate = LocalDate.now()
    var selectedDate by remember { mutableStateOf(currentDate.toString()) }
    val taskList by viewModel.taskListState.collectAsState()
    val filteredTasks = taskList.filter { it.taskDetail.date == selectedDate }

    val startYear = 2001
    val endYear = 2030
    val totalMonths = (endYear - startYear + 1) * 12

    val initialPage = (currentDate.year - startYear) * 12 + currentDate.monthValue - 1

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { totalMonths }
    )

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        ) { page ->
            val monthIndex = page % 12
            val year = startYear + (page / 12)
            val month = Month.of(monthIndex + 1)

            Spacer(Modifier.height(30.dp))

            CalendarView(
                year = year,
                month = month.value,
                currentYear = currentDate.year,
                currentMonth = currentDate.monthValue,
                currentDay = currentDate.dayOfMonth,
//                tasksForDate = filteredTasks,
                onDateClick = { dateClicked ->
                    selectedDate = dateClicked
                },
                onTaskCreated = { taskTitle, taskDescription ->
                    val task = TaskDetail(
                        title = taskTitle,
                        description = taskDescription,
                        date = selectedDate
                    )
                    viewModel.addTask(1111, task)
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Tasks for $selectedDate",
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (filteredTasks.isEmpty()) {
                Text(
                    text = "No tasks for this date",
                    color = Color.Gray
                )
            } else {
                LazyColumn {
                    items(filteredTasks) { task ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = task.taskDetail.title,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = task.taskDetail.description,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}