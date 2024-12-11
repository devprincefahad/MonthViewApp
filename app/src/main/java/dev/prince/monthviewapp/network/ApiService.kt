package dev.prince.monthviewapp.network

import dev.prince.monthviewapp.models.DeleteTaskRequest
import dev.prince.monthviewapp.models.SuccessResponse
import dev.prince.monthviewapp.models.TaskRequest
import dev.prince.monthviewapp.models.TaskResponse
import dev.prince.monthviewapp.models.UserRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/storeCalendarTask")
    suspend fun storeTask(@Body request: TaskRequest): SuccessResponse

    @POST("/api/getCalendarTaskList")
    suspend fun getTaskList(@Body request: UserRequest): TaskResponse

    @POST("/api/deleteCalendarTask")
    suspend fun deleteTask(@Body request: DeleteTaskRequest): SuccessResponse
}
