package com.motivation.affirmations.ui.core.extensions

import com.motivation.affirmations.ui.core.Resource
import com.motivation.affirmations.ui.core.ResourceEvent

internal fun <T> Result<T>.toResource(): Resource<T> =
    when {
        isSuccess -> Resource.success(getOrNull())
        else -> Resource.error(exceptionOrNull())
    }

internal fun <T : Any> Result<T>.toResourceEvent(): ResourceEvent<T> = fold(
    ::mapToResourceEventSuccess,
    ::mapToResourceEventError
)

private fun <T : Any> mapToResourceEventSuccess(data: T): ResourceEvent<T> {
    return ResourceEvent.Success(data)
}

private fun <T : Any> mapToResourceEventError(throwable: Throwable): ResourceEvent<T> {
    return ResourceEvent.Error(throwable.message, throwable)
}
