package com.motivation.affirmations.domain.usecases.core

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 16.03.2022.
 */
abstract class UseCase<out Type : Any, in Params : UseCase.Params> {

    interface Params

    object None : Params
}
