package dev.prince.monthviewapp.models

import com.google.gson.annotations.SerializedName

data class TaskResponse(
    val tasks: List<Task>
)

data class Task(
    @SerializedName("task_id")
    val taskId: Int,
    @SerializedName("task_detail")
    val taskDetail: TaskDetail
)

data class TaskDetail(
    val title: String,
    val description: String,
    val date: String
)
