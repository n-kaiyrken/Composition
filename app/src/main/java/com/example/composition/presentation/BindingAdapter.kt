package com.example.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult

interface ClickListener{
    fun onClick(number: Int)
}

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

@BindingAdapter("tvAnswerProgress")
fun bindtvAnswerProgress(textView: TextView, state: Boolean) {
    textView.setTextColor(getColorByState(state, textView.context)
    )
}

@BindingAdapter("progressBarColor")
fun bindProgressBar(
    progressBar: ProgressBar,
    enoughPercent: Boolean
) {
    val color = getColorByState(enoughPercent, progressBar.context)
    progressBar.progressTintList = ColorStateList.valueOf(color)
}

private fun getColorByState(state: Boolean, context: Context): Int{
    val colorResId = if (state) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int){
    textView.text = number.toString()
}

@BindingAdapter("OnOptionClick")
fun bindOnOptionClick(textView: TextView, clickLister: ClickListener) {
    textView.setOnClickListener {
        clickLister.onClick(textView.text.toString().toInt())
    }
}