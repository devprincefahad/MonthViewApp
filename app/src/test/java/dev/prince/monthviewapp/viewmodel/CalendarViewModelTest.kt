package dev.prince.monthviewapp.viewmodel

import dev.prince.monthviewapp.models.SuccessResponse
import dev.prince.monthviewapp.models.Task
import dev.prince.monthviewapp.models.TaskDetail
import dev.prince.monthviewapp.models.TaskResponse
import dev.prince.monthviewapp.models.UserRequest
import dev.prince.monthviewapp.network.ApiService
import dev.prince.monthviewapp.ui.calendar.CalendarViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CalendarViewModelTest {

    private val apiService: ApiService = mockk()
    private lateinit var calendarViewModel: CalendarViewModel

    private val mockTask = Task(
        taskId = 4638,
        taskDetail = TaskDetail(
            date = "2024-12-13",
            title = "task",
            description = "complete assignment"
        )
    )

    private val taskResponse = TaskResponse(tasks = listOf(mockTask))

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        kotlinx.coroutines.Dispatchers.setMain(testDispatcher)

        calendarViewModel = CalendarViewModel(apiService)
    }

    @Test
    fun `test fetchTasks successfully sets task list`() = runTest {
        val userId = 1111
        coEvery { apiService.getTaskList(UserRequest(userId)) } returns taskResponse

        calendarViewModel.fetchTasks(userId)

        val taskList = calendarViewModel.taskListState.first()

        assertEquals(listOf(mockTask), taskList)
    }

//    @Test
//    fun `test isLoading is true during fetchTasks call`() = runTest {
//        val userId = 1111
//        coEvery { apiService.getTaskList(UserRequest(userId)) } returns taskResponse
//
//        calendarViewModel.fetchTasks(userId)
//
//        val isLoading = calendarViewModel.isLoading.first()
//
//        assertEquals(true, isLoading)
//    }

    @Test
    fun `test isLoading is true during fetchTasks call`() = runTest {
        val userId = 1111
        coEvery { apiService.getTaskList(UserRequest(userId)) } coAnswers {
            delay(100)
            taskResponse
        }

        val isLoadingStates = mutableListOf<Boolean>()
        val job = launch {
            calendarViewModel.isLoading.toList(isLoadingStates)
        }

        calendarViewModel.fetchTasks(userId)

        advanceUntilIdle()

        job.cancel()

        assertTrue(isLoadingStates.contains(true))
        assertFalse(isLoadingStates.last())
    }

    @Test
    fun `test error handling when network error occurs`() = runTest {
        val userId = 1111
        coEvery { apiService.getTaskList(UserRequest(userId)) } throws java.net.UnknownHostException()

        calendarViewModel.fetchTasks(userId)

        val errorMessage = calendarViewModel.errorMessage.first()

        assertEquals("No internet connection. Please check your network.", errorMessage)
    }

    @Test
    fun `test addTask calls storeTask and fetchTasks again`() = runTest {
        val userId = 1111
        val taskDetail = TaskDetail(date = "2024-12-13", title = "task", description = "complete assignment")
        coEvery { apiService.storeTask(any()) } returns SuccessResponse("Success")
        coEvery { apiService.getTaskList(UserRequest(userId)) } returns taskResponse

        calendarViewModel.addTask(userId, taskDetail)

        coVerify { apiService.getTaskList(UserRequest(userId)) }
    }

    @Test
    fun `test deleteTask calls deleteTask and fetchTasks again`() = runTest {
        val userId = 1111
        val taskId = 4638
        coEvery { apiService.deleteTask(any()) } returns SuccessResponse("Success")
        coEvery { apiService.getTaskList(UserRequest(userId)) } returns taskResponse

        calendarViewModel.deleteTask(userId, taskId)

        coVerify { apiService.getTaskList(UserRequest(userId)) }
    }
}