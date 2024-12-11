package dev.prince.monthviewapp.models

import com.google.gson.annotations.SerializedName

data class TaskRequest(
    @SerializedName("user_id")
    val userId: Int,
    val task: TaskDetail
)