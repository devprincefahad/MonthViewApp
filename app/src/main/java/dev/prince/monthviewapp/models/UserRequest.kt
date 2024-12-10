package dev.prince.monthviewapp.models

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("user_id")
    val userId: Int
)