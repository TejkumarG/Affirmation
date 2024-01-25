package com.motivation.affirmations.initializer

import android.content.Context
import androidx.startup.Initializer
import com.backendless.Backendless
import com.motivation.affirmations.util.Defaults

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 13.06.2022.
 */
@Suppress("unused")
class BackendlessInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Backendless.setUrl(Defaults.SERVER_URL)
        Backendless.initApp(context.applicationContext, Defaults.APPLICATION_ID, Defaults.API_KEY)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
