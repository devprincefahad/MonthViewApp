package dev.prince.monthviewapp.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.prince.monthviewapp.R
import dev.prince.monthviewapp.models.TaskDetail
import dev.prince.monthviewapp.ui.components.CalendarView
import kotlinx.coroutines.launch
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

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = errorMessage!!,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            modifier = Modifier.padding(16.dp),
            text = "CALENDAR",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
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
                text = "${filteredTasks.size} ${if (filteredTasks.size == 1) "task" else "tasks"} for $selectedDate",
                modifier = Modifier.padding(bottom = 8.dp),
                fontSize = 16.sp
            )

            if (filteredTasks.isEmpty()) {
                Text(
                    text = "No tasks for this date",
                    color = Color.Gray
                )
            } else if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
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
                                Row {
                                    Column {
                                        Text(
                                            text = task.taskDetail.title,
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = task.taskDetail.description,
                                            color = Color.Gray
                                        )
                                    }

                                    Spacer(Modifier.weight(1f))

                                    IconButton(
                                        onClick = {
                                            viewModel.deleteTask(1111, task.taskId)
                                        }
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(26.dp),
                                            painter = painterResource(id = R.drawable.ic_delete),
                                            contentDescription = "Delete Task"
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}