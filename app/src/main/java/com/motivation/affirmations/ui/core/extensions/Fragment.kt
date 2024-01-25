package com.motivation.affirmations.ui.core.extensions

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.motivation.affirmations.ui.core.Resource
import com.motivation.affirmations.ui.core.ResourceEvent
import com.motivation.affirmations.ui.core.Status
import com.motivation.app.R
import kotlinx.coroutines.launch

internal inline fun <T> Fragment.createResourceObserver(
    crossinline onLoading: () -> Unit = {},
    crossinline onHideLoading: () -> Unit = {},
    crossinline onError: (String, Throwable?) -> Unit = { message, _ -> showToastShort(message) },
    crossinline onSuccess: (T) -> Unit = {}
): Observer<Resource<T>> = Observer { resource ->
    resource.handle(onLoading, onHideLoading, onError, onSuccess)
}

private inline fun <T> Resource<T>.handle(
    crossinline onLoading: () -> Unit,
    crossinline onHideLoading: () -> Unit,
    crossinline onError: (String, Throwable?) -> Unit,
    crossinline onSuccess: (T) -> Unit
) {
    when (status) {
        Status.Success -> {
            data?.let { onSuccess(it) } ?: onError(message ?: "", exception)
            onHideLoading()
        }

        Status.Error -> {
            onError(message ?: "", exception)
            onHideLoading()
        }

        Status.Loading -> {
            onLoading()
        }
    }
}

internal inline fun Fragment.createUnitResourceObserver(
    crossinline onLoading: () -> Unit = {},
    crossinline onHideLoading: () -> Unit = {},
    crossinline onError: (String, Throwable?) -> Unit = { message, _ -> showToastShort(message) },
    crossinline onSuccess: () -> Unit = {}
): Observer<Resource<Unit>> = Observer { resource ->
    resource.handleUnit(onLoading, onHideLoading, onError, onSuccess)
}

private inline fun Resource<Unit>.handleUnit(
    crossinline onLoading: () -> Unit,
    crossinline onHideLoading: () -> Unit,
    crossinline onError: (String, Throwable?) -> Unit,
    crossinline onSuccess: () -> Unit
) {
    when (status) {
        Status.Success -> {
            onSuccess()
            onHideLoading()
        }

        Status.Error -> {
            onError(message ?: "", exception)
            onHideLoading()
        }

        Status.Loading -> {
            onLoading()
        }
    }
}

internal inline fun <T> Fragment.createResourceEventObserver(
    crossinline onLoading: () -> Unit = { },
    crossinline onHideLoading: () -> Unit = { },
    crossinline onError: (String, Throwable?) -> Unit = { message, _ -> showToastShort(message) },
    crossinline onSuccess: (T) -> Unit = {}
): Observer<ResourceEvent<T>> = Observer { event ->

    // an event should be handled only once
    event.getContentIfNotHandled()?.handle(onLoading, onHideLoading, onError, onSuccess)
}

internal inline fun Fragment.createUnitResourceEventObserver(
    crossinline onLoading: () -> Unit = { },
    crossinline onHideLoading: () -> Unit = { },
    crossinline onError: (String, Throwable?) -> Unit = { message, _ -> showToastShort(message) },
    crossinline onSuccess: () -> Unit = {}
): Observer<ResourceEvent<Unit>> = Observer { event ->

    // an event should be handled only once
    event.getContentIfNotHandled()?.handleUnit(onLoading, onHideLoading, onError, onSuccess)
}

internal fun Fragment.showToastShort(message: String) {
    if (message.isBlank() || isAdded.not()) return
    lifecycleScope.launch {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}

internal fun Fragment.showToastShort(@StringRes message: Int) {
    if (isAdded.not()) return
    lifecycleScope.launch {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
        toast.show()
    }
}

internal fun Fragment.startActivitySafe(intent: Intent): Boolean {
    return try {
        startActivity(Intent.createChooser(intent, getString(R.string.share_via)))
        true
    } catch (ex: ActivityNotFoundException) {
        showShareError()
        false
    }
}

internal fun Fragment.showShareError() {
    showToastShort(R.string.oops_we_cannot_do_this)
}

/**
 * add onBackPressed callback for parent fragment
 * should be called in [Fragment.onAttach]
 */
internal inline fun Fragment.addOnBackPressedCallback(
    crossinline onBackPressed: () -> Unit
) {
    requireActivity().onBackPressedDispatcher.addCallback(
        this,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
    )
}

fun Fragment.checkSelfPermission(permission: String) = ContextCompat.checkSelfPermission(
    requireActivity().applicationContext,
    permission
) == PackageManager.PERMISSION_GRANTED

val Fragment.mainNavController: NavController
    get() {
        val supportFragmentManager = requireActivity().supportFragmentManager
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as
            NavHostFragment

        return navHostFragment.navController
    }
