package dev.prince.monthviewapp.models

import com.google.gson.annotations.SerializedName

data class TaskResponse(
    val tasks: List<Task>
)

data class Task(
    @SerializedName("task_id")
    val taskId: Int,
    @SerializedName("task_detail")
    val taskDetail: TaskDetailResponse
)

data class TaskDetailResponse(
    val title: String,
    val description: String,
    val date: String? = null // Optional, as date is not present in all tasks
)
