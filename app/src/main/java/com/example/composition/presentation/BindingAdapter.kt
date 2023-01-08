package com.example.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

@BindingAdapter("required_answers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.resources.getString(R.string.required_score),
        count
    )
}

@BindingAdapter("score_answers")
fun bindScoreAnswers(textView: TextView, score: Int) {
    textView.text = String.format(
        textView.context.resources.getString(R.string.score_answers),
        score
    )
}

@BindingAdapter("required_percentage")
fun bindRequiredPercentage(textView: TextView, percent: Int) {
    textView.text = String.format(
        textView.context.resources.getString(R.string.required_percentage),
        percent
    )
}

@BindingAdapter("emoji_result")
fun bindEmojiResult(imageView: ImageView, winner: Boolean) {
    imageView.setImageResource(
        if (winner) {
            R.drawable.smile_emoji
        } else {
            R.drawable.sad_emoji
        }
    )
}

@BindingAdapter("score_percentage")
fun bindScorePercentage(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.resources.getString(R.string.score_percentage),
        calcScorePercentage(gameResult).toString()
    )
}

private fun calcScorePercentage(gameResult: GameResult): Int {
    if (gameResult.countOfQuestions == 0) {
        return 0
    }
    return ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
}