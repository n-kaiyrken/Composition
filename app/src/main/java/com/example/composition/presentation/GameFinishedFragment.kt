package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        binding.emojiResult.setImageResource(getSmileResId())
        bindViews()
    }

    private fun setClickListeners(){
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            })
    }

    private fun getSmileResId(): Int {
        return if (gameResult.winner) {
            R.drawable.smile_emoji
        } else {
            R.drawable.sad_emoji
        }
    }

    private fun bindViews() {
        binding.tvScoreAnswers.text = String.format(
            requireContext().resources.getString(R.string.score_answers),
            gameResult.countOfRightAnswers.toString()
        )
        binding.tvRequiredAnswers.text = String.format(
            requireContext().resources.getString(R.string.required_score),
            gameResult.gameSettings.minCountOfRightAnswers.toString()
        )
        binding.tvRequiredPercentage.text = String.format(
            requireContext().resources.getString(R.string.required_percentage),
            gameResult.gameSettings.minPercentOfRightAnswers.toString()
        )
        binding.tvScorePercentage.text = String.format(
            requireContext().resources.getString(R.string.score_percentage),
            calcScorePercentage().toString()
        )
    }

    private fun calcScorePercentage(): Int {
        if (gameResult.countOfQuestions == 0) {
            return 0
        }
        return ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(KEY_RESULT)?.let {
            gameResult = it
        }
    }

    companion object {

        private const val KEY_RESULT = "result"

        fun newInstance(result: GameResult): GameFinishedFragment {
            val args = Bundle()
            args.putParcelable(KEY_RESULT, result)
            val fragment = GameFinishedFragment()
            fragment.arguments = args
            return fragment
        }
    }
}