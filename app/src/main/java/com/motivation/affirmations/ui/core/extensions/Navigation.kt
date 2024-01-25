package com.motivation.affirmations.ui.core.extensions

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import timber.log.Timber

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 26.04.2022.
 */
internal fun NavController.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
): Boolean {
    val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
    return try {
        if ((action != null && currentDestination?.id != action.destinationId) ||
            (action == null && currentDestination?.id != resId)
        ) {
            navigate(resId, args, navOptions, navExtras)
            true
        } else {
            Timber.d("Action is null or destination screen is set already")
            false
        }
    } catch (ex: IllegalArgumentException) {
        // Navigation action/destination cannot be found from the current destination Destination
        Timber.d(ex)
        false
    }
}

internal fun NavController.navigateSafe(directions: NavDirections): Boolean {
    return navigateSafe(directions.actionId, directions.arguments)
}
