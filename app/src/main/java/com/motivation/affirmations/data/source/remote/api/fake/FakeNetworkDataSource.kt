package com.motivation.affirmations.data.source.remote.api.fake

import com.motivation.affirmations.data.source.AffirmationDataSource
import com.motivation.affirmations.data.source.remote.api.model.AffirmationData
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

class FakeNetworkDataSource @Inject constructor(
    private val assets: AppAssetManager
) : AffirmationDataSource {
    override suspend fun getAffirmations(): List<AffirmationData> =
        withContext(Dispatchers.IO) {
            assets.open(AFFIRMATION_ASSET).bufferedReader().use {
                fromJsonArray(JSONArray(it.readText()))
            }
        }

    fun fromJsonArray(array: JSONArray): List<AffirmationData> {
        val list = mutableListOf<AffirmationData>()
        for (i in 0 until array.length()) {
            val id = array.getJSONObject(i).getInt("id")
            val value = array.getJSONObject(i).getString("value")
            list.add(AffirmationData(id, value))
        }
        return list
    }

    companion object {
        private const val AFFIRMATION_ASSET = "affirmation.json"
    }
}
