package com.motivation.affirmations.domain.usecases.core

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 16.03.2022.
 */
abstract class SimpleUseCase<out Type : Any,
    in Params : UseCase.Params> : UseCase<Type, Params>() {

    abstract fun run(params: Params): Type

    operator fun invoke(params: Params): Type {
        return run(params)
    }
}
