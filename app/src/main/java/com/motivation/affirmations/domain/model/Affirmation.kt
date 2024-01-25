package com.motivation.affirmations.domain.model

data class Affirmation(
    var id: Int = -1,
    var text: String = "",
    var fileName: String,
    var filePath: String,
    var duration: String,
    var date: Long,
    var isPlayList: Boolean,
    var isSelected: Boolean,
) {
    var isRecorded = filePath.isNotEmpty()

    constructor() : this(-1, "", "", "", "", 0L, false, false)
}
