package com.motivation.affirmations.data.extensions

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CancellableContinuation

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 02.02.2022.
 */

/**
 * wrapper of the [CancellableContinuation.resume] with additional check if continuation isActive
 * [CancellableContinuation.isActive]
 */
fun <T> CancellableContinuation<T>.resumeSafe(value: T) {
    if (isActive) {
        resume(value)
    }
}

/**
 * wrapper of the [CancellableContinuation.resumeWithException] with additional check
 * if continuation isActive [CancellableContinuation.isActive]
 */
fun <T> CancellableContinuation<T>.resumeWithExceptionSafe(exception: Throwable) {
    if (isActive) {
        resumeWithException(exception)
    }
}
