package dev.prince.monthviewapp.ui.calendar

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.monthviewapp.models.Task
import dev.prince.monthviewapp.models.TaskDetail
import dev.prince.monthviewapp.models.TaskRequest
import dev.prince.monthviewapp.models.UserRequest
import dev.prince.monthviewapp.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> = _taskList

    private val _taskAddStatus = MutableStateFlow<String?>(null)
    val taskAddStatus: StateFlow<String?> = _taskAddStatus

    fun addTask(userId: Int, taskDetail: TaskDetail) {
        viewModelScope.launch {
            try {
                val response = apiService.storeTask(TaskRequest(userId, taskDetail))
                if (response.status == "success") {
                    _taskAddStatus.value = "Task added successfully!"
                } else {
                    _taskAddStatus.value = "Failed to add task"
                }
            } catch (e: Exception) {
                _taskAddStatus.value = "Error adding task: ${e.message}"
            }
        }
    }

    fun getTasks(userId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getTaskList(UserRequest(userId))
                _taskList.value = response.tasks
            } catch (e: Exception) {
                _taskAddStatus.value = "Error fetching tasks: ${e.message}"
            }
        }
    }
}