package ru.nyakshoot.messenger.utils


data class NetworkResult<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR
    }

    companion object {
        fun <T> success(data: T): NetworkResult<T & Any> {
            return NetworkResult(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): NetworkResult<T & Any> {
            return NetworkResult(Status.ERROR, data, message)
        }
    }
}
