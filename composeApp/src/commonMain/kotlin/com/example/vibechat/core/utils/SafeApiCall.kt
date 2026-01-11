package com.example.vibechat.core.utils

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Throwable) : ApiResult<Nothing>()
}




suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): ApiResult<T> {
    return try {
        ApiResult.Success(apiCall())
    } catch (e: Exception) {
        ApiResult.Error(e)
    }
}

fun main() {

    GlobalScope.launch {
        val result = safeApiCall {
            getUser()
        }

        when(result){
            is ApiResult.Error -> TODO()
            is ApiResult.Success -> TODO()
        }


    }
}

suspend fun getUser() : User{
    delay(1000)
    return User("John", 25)
}

data class User(
    val name : String,
    val age : Int
)