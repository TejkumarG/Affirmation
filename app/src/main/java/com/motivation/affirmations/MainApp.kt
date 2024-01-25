package com.motivation.affirmations

import android.app.Application
import android.os.StrictMode
import com.github.anrwatchdog.ANRWatchDog
import com.motivation.app.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            ANRWatchDog().start()
            Timber.plant(Timber.DebugTree())
            val threadPolicy = StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()

            StrictMode.setThreadPolicy(threadPolicy.build())

            val vmPolicy = StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
            StrictMode.setVmPolicy(vmPolicy.build())
        }
    }
}
