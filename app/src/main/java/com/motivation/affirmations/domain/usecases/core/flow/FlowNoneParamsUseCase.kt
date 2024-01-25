package com.motivation.affirmations.domain.usecases.core.flow

import com.motivation.affirmations.domain.usecases.core.NoneParamsUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 16.03.2022.
 */
abstract class FlowNoneParamsUseCase<out Type : Any> : NoneParamsUseCase<Type>() {

    abstract fun run(): Flow<Type>

    operator fun invoke(): Flow<Type> {
        return run()
    }
}
