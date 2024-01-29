package com.motivation.affirmations.data.source.remote.api

import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessException
import com.backendless.exceptions.BackendlessFault
import com.motivation.affirmations.data.source.remote.api.model.AffirmationData
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

/**
 * Created by Andriy Deputat email(andriy.deputat@gmail.com) on 23.05.2022.
 */
class BackendlessDatabase @Inject constructor() {

    suspend fun getAffirmations(): List<AffirmationData>? {
        return suspendCancellableCoroutine {
            Backendless.Data.of(AffirmationData::class.java).find(
                object : AsyncCallback<List<AffirmationData>?> {
                    override fun handleResponse(response: List<AffirmationData>?) {
                        it.resume(response)
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        try {
                            throw BackendlessException(fault)
                        } catch (ex: BackendlessException) {
                            Timber.e(ex)
                            it.resumeWithException(ex)
                        }
                    }
                }
            )
        }
    }
}
