package com.snowa.snowaproject

sealed class ViewState<out R> {

    data class Success<out T>(val response: T) : ViewState<T>()
    data class Failed(val exception: Exception) : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$response]"
            is Failed -> "Failed[exception=${exception.message}]"
            is Loading -> "Loading"
        }
    }
}

suspend fun <T> getViewState(call: suspend () -> T): ViewState<T> {
    return try {
        val result = call.invoke()
        ViewState.Success(result)
    } catch (e: Exception) {
        ViewState.Failed(e)
    }
}
