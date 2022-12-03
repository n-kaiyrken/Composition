package com.example.composition.domain.entity

data class GameSettings(
    val gameTimeInSeconds: Int,
    val maxSumValue: Int,
    val minPercentOfRightAnswers: Int,
    val minCountOfRightAnswers: Int
)