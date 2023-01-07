package com.example.composition.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSettings(
    val gameTimeInSeconds: Int,
    val maxSumValue: Int,
    val minPercentOfRightAnswers: Int,
    val minCountOfRightAnswers: Int
): Parcelable