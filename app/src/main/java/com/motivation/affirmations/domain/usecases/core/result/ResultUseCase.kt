package com.motivation.affirmations.domain.usecases.core.result

import com.motivation.affirmations.domain.usecases.core.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 16.03.2022.
 */
abstract class ResultUseCase<out Type : Any,
    in Params : UseCase.Params> : UseCase<Type, Params>() {

    abstract suspend fun run(params: Params): Result<Type>

    operator fun invoke(
        params: Params,
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onResult: (Result<Type>) -> Unit = {}
    ) {
        scope.launch(Dispatchers.Main) {
            val deferred = async(dispatcher) {
                run(params)
            }
            onResult(deferred.await())
        }
    }

    suspend fun await(
        params: Params,
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Result<Type> {
        return scope.async(Dispatchers.Main) {
            return@async withContext(dispatcher) {
                run(params)
            }
        }.await()
    }

    fun async(
        params: Params,
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Deferred<Result<Type>> {
        return scope.async(Dispatchers.Main) {
            withContext(dispatcher) {
                run(params)
            }
        }
    }
}
