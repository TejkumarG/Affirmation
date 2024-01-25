package com.motivation.affirmations.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.motivation.affirmations.domain.model.Affirmation
import com.motivation.app.BuildConfig
import java.io.File


class ContentShareHelper(val context: Context) {

    fun share(affirmation: Affirmation){
        if(affirmation.filePath.isEmpty()){
            shareText(affirmation.text)
            return
        }
        val authorities: String = BuildConfig.APPLICATION_ID + ".provider"
        val path = FileProvider.getUriForFile(context, authorities, File(affirmation.filePath))

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, path)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = "audio/*"
        }
        val shareIntent = Intent.createChooser(intent, "Share Affirmation")// choose other extensions
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        context.startActivity(shareIntent)
    }

    private fun shareText(text:String){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share Affirmation")
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        context.startActivity(shareIntent)
    }
}