package dev.prince.monthviewapp.network

import dev.prince.monthviewapp.models.DeleteTaskRequest
import dev.prince.monthviewapp.models.TaskDetail
import dev.prince.monthviewapp.models.TaskRequest
import dev.prince.monthviewapp.models.UserRequest
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test storeTask API success`() = runBlocking {
        val responseJson = """{"status": "Success"}"""
        mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(200))

        val response = apiService.storeTask(
            TaskRequest(
                1111,
                TaskDetail(
                    title = "Sample Title 1",
                    description = "Sample desc1",
                    date = "2024-12-9"
                )
            )
        )

        assertEquals("Success", response.status)
    }

    @Test
    fun `test getTaskList API success with one task`() = runBlocking {
        val responseJson = """{
        "tasks": [
            {
                "task_id": 4638,
                "task_detail": {
                    "date": "2024-12-13",
                    "title": "task",
                    "description": "complete assignment"
                }
            }
        ]
    }"""
        mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(200))

        val response = apiService.getTaskList(UserRequest(1))

        assertEquals(1, response.tasks.size)
        assertEquals(4638, response.tasks[0].taskId)
        assertEquals("task", response.tasks[0].taskDetail.title)
        assertEquals("complete assignment", response.tasks[0].taskDetail.description)
    }

    @Test
    fun `test deleteTask API success`() = runBlocking {
        val responseJson = """{"status": "Success"}"""
        mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(200))

        val response = apiService.deleteTask(DeleteTaskRequest(1111, 1))

        assertEquals("Success", response.status)
    }

}
