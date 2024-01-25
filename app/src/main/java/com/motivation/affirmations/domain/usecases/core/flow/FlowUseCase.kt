package com.motivation.affirmations.domain.usecases.core.flow

import com.motivation.affirmations.domain.usecases.core.UseCase
import kotlinx.coroutines.flow.Flow

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 16.03.2022.
 */
abstract class FlowUseCase<out Type : Any, in Params : UseCase.Params> : UseCase<Type, Params>() {

    abstract fun run(params: Params): Flow<Type>

    operator fun invoke(params: Params): Flow<Type> {
        return run(params)
    }
}
