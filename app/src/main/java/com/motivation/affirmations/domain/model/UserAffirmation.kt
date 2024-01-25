package com.motivation.affirmations.domain.model

data class UserAffirmation (
    val affirmations: List<Affirmation> = listOf(),
    val playlist: List<Affirmation> = listOf(),
    val nonPlayList:List<Affirmation> = listOf()
)