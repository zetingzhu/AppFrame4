package com.zzt.coroutinessample.net.factory

import retrofit2.Response

open class ApiResponse<T>(
    var success: Boolean = false,
    var errorCode: String?,
    var errorInfo: String?,
    val body: T?
) {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse("", error.message ?: "unknown error")
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body != null && response.code() == 200) {
                    ApiResponse(true, "", "", body = body)
                } else {
                    ApiErrorResponse("", "unknown error")
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse("", errorMsg ?: "unknown error")
            }
        }
    }

    override fun toString(): String {
        return "ApiResponse(success=$success, errorCode=$errorCode, errorInfo=$errorInfo, body=$body)"
    }
}

class ApiErrorResponse<T>(errorCode: String?, errorInfo: String?) :
    ApiResponse<T>(false, errorCode, errorInfo, null)
