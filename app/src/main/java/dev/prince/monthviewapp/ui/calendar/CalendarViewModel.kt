package dev.prince.monthviewapp.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prince.monthviewapp.models.DeleteTaskRequest
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

    private val _taskListState = MutableStateFlow<List<Task>>(emptyList())
    val taskListState: StateFlow<List<Task>> = _taskListState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        fetchTasks(1111)
    }

    private fun fetchTasks(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = apiService.getTaskList(UserRequest(userId))
                _taskListState.value = response.tasks
            } catch (e: Exception) {
                handleException(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTask(userId: Int, task: TaskDetail) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                apiService.storeTask(TaskRequest(userId, task))
                fetchTasks(userId)
            } catch (e: Exception) {
                handleException(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTask(userId: Int, taskId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                apiService.deleteTask(DeleteTaskRequest(userId, taskId))
                fetchTasks(userId)
            } catch (e: Exception) {
                handleException(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    private fun handleException(e: Exception) {
        when (e) {
            is java.net.UnknownHostException -> {
                _errorMessage.value = "No internet connection. Please check your network."
            }
            is java.net.SocketTimeoutException -> {
                _errorMessage.value = "Request timed out. Please try again later."
            }
            else -> {
                _errorMessage.value = e.message ?: "An unexpected error occurred"
            }
        }
    }

}
