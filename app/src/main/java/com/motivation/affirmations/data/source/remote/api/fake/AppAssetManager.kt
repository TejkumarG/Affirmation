package com.motivation.affirmations.data.source.remote.api.fake

import java.io.InputStream

fun interface AppAssetManager {
    fun open(fileName: String): InputStream
}