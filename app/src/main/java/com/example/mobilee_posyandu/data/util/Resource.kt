package com.example.mobilee_posyandu.data.util

import com.example.mobilee_posyandu.ui.components.HttpStatusCodeInfo
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import retrofit2.Response

/**
 * Wrapper serbaguna untuk state operasi data dan jaringan.
 */
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(
        val statusInfo: HttpStatusCodeInfo,
        val message: String? = null,
        val cause: Throwable? = null
    ) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
    data object Idle : Resource<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading

    fun getOrNull(): T? = if (this is Success) data else null
}

/**
 * Ekstensi pembantu untuk mengonversi kode HTTP Response Retrofit menjadi [HttpStatusCodeInfo].
 */
fun <T> Response<T>.toHttpStatusInfo(): HttpStatusCodeInfo {
    return when (code()) {
        200 -> HttpStatusCodeInfo.OK_200
        201 -> HttpStatusCodeInfo.CREATED_201
        400 -> HttpStatusCodeInfo.BAD_REQUEST_400
        401 -> HttpStatusCodeInfo.UNAUTHORIZED_401
        403 -> HttpStatusCodeInfo.FORBIDDEN_403
        404 -> HttpStatusCodeInfo.NOT_FOUND_404
        422 -> HttpStatusCodeInfo.UNPROCESSABLE_422
        500 -> HttpStatusCodeInfo.INTERNAL_SERVER_ERROR_500
        502 -> HttpStatusCodeInfo.BAD_GATEWAY_502
        503 -> HttpStatusCodeInfo.SERVICE_UNAVAILABLE_503
        else -> if (code() in 400..499) HttpStatusCodeInfo.BAD_REQUEST_400 else HttpStatusCodeInfo.INTERNAL_SERVER_ERROR_500
    }
}

/**
 * Mengonversi exception koneksi / jaringan menjadi [HttpStatusCodeInfo.NETWORK_OFFLINE].
 */
fun Throwable.toNetworkStatusInfo(): HttpStatusCodeInfo {
    return when (this) {
        is UnknownHostException,
        is ConnectException,
        is SocketTimeoutException,
        is IOException -> HttpStatusCodeInfo.NETWORK_OFFLINE
        else -> HttpStatusCodeInfo.INTERNAL_SERVER_ERROR_500
    }
}

