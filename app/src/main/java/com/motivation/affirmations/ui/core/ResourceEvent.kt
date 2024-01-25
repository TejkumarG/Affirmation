package com.motivation.affirmations.ui.core

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 21.06.2022.
 */
sealed class ResourceEvent<out T>(resource: Resource<T>) : Event<Resource<T>>(resource) {

    class Loading : ResourceEvent<Nothing>(Resource.loading())
    data class Success<out T>(val data: T) : ResourceEvent<T>(Resource.success(data))
    data class Error(
        val message: String?,
        val exception: Throwable
    ) : ResourceEvent<Nothing>(Resource.error(message, exception))
}
