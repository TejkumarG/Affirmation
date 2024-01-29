package com.motivation.affirmations.data.source.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "affirmations")
data class AffirmationEntity(
    @PrimaryKey
    var id: Int = -1,
    var text: String = "",
    var fileName: String,
    var filePath: String,
    var duration: String,
    var date: Long,
    var isPlayList: Boolean
) {
    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            javaClass != other?.javaClass -> false
            else -> (other as AffirmationEntity).let { _ ->
                (
                    id == other.id && fileName == other.fileName && filePath == other.filePath &&
                        date == other.date && duration == other.duration &&
                        isPlayList == other.isPlayList && text == other.text
                    )
            }
        }
    }

    override fun hashCode(): Int {
        return id
    }
}
