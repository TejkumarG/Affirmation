package com.motivation.affirmations.ui.core

import androidx.annotation.Keep

@Keep
class Resource<out T> private constructor(
    val status: Status,
    val data: T?,
    val message: String?,
    val exception: Throwable?
) {

    override fun toString(): String {
        return "Resource[" +
            "status=" + status + '\'' +
            ",message='" + message + '\'' +
            ",data=" + data +
            ']'
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(status = Status.Success, data = data, message = null, exception = null)
        }

        fun <T> error(msg: String?, data: T? = null): Resource<T> {
            return Resource(status = Status.Error, data = data, message = msg, exception = null)
        }

        fun <T> error(msg: String?, exception: Throwable? = null): Resource<T> {
            return Resource(
                status = Status.Error,
                data = null,
                message = msg,
                exception = exception
            )
        }

        fun <T> error(exception: Throwable?, data: T? = null): Resource<T> {
            return Resource(
                status = Status.Error,
                data = data,
                message = exception?.message,
                exception = exception
            )
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(status = Status.Loading, data = data, message = null, exception = null)
        }
    }
}

@Keep
sealed class Status {
    object Success : Status()
    object Error : Status()
    object Loading : Status()
}
