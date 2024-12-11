package dev.prince.monthviewapp.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.prince.monthviewapp.R
import dev.prince.monthviewapp.ui.components.AddTaskDialog
import dev.prince.monthviewapp.ui.components.CalendarView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    selectedYear: Int,
    selectedMonth: Int,
    userId: Int,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    var selectedDate by remember { mutableStateOf<String?>(null) }

    val taskListState by viewModel.taskList.collectAsState()

    val tasks = taskListState.filter { it.taskDetail.date == selectedDate }

    Column(modifier = Modifier.fillMaxSize()) {

        Spacer(Modifier.height(30.dp))

        // Calendar View
        CalendarView(
            year = selectedYear,
            month = selectedMonth
        )

        Spacer(modifier = Modifier.height(16.dp))

    }
}
