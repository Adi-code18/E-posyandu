package com.example.mobilee_posyandu.data.remote

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: T? = null,
    @SerializedName("errors") val errors: Any? = null
)

data class PaginationResponse<T>(
    @SerializedName("current_page") val currentPage: Int = 1,
    @SerializedName("data") val data: List<T> = emptyList(),
    @SerializedName("total") val total: Int = 0,
    @SerializedName("last_page") val lastPage: Int = 1
)

