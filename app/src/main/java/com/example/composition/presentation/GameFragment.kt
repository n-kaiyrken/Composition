package com.example.composition.presentation

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.Level

class GameFragment : Fragment() {

    private val args by navArgs<GameFragmentArgs>()
    private val viewModelFactory by lazy{
        GameViewModelFactory(requireActivity().application, args.level)
    }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameViewModel::class.java]
    }

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setOnClickListenersToOptions()
        binding.buttonFinishGame.setOnClickListener {
            viewModel.finishGame()
        }
    }

    private fun observeViewModel() {
        viewModel.formattedTime.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }
        viewModel.question.observe(viewLifecycleOwner){
            with (binding) {
                tvSum.text = it.sum.toString()
                tvVisibleNumber.text = it.visibleNumber.toString()
                for (i in 0 until tvOptions.size) {
                    tvOptions[i].text = it.options[i].toString()
                }
            }
        }
        viewModel.progressAnswers.observe(viewLifecycleOwner){
            binding.tvAnswerProgress.text = it
        }
        viewModel.enoughCount.observe(viewLifecycleOwner){
            binding.tvAnswerProgress.setTextColor(getColorByState(it))
        }
        viewModel.enoughPercent.observe(viewLifecycleOwner){
            val color = getColorByState(it)
            binding.progressBar.progressTintList = ColorStateList.valueOf(color)
        }
        viewModel.percentOfRightAnswers.observe(viewLifecycleOwner){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.progressBar.setProgress(it, true)
            }
        }
        viewModel.gameResult.observe(viewLifecycleOwner) {
            launchGameFinishedFragment(it)
        }
        viewModel.minPercent.observe(viewLifecycleOwner){
            binding.progressBar.secondaryProgress = it
        }
    }

    private fun getColorByState(state: Boolean): Int{
        val colorResId = if (state) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    private fun setOnClickListenersToOptions() {
        for (tvoption in tvOptions) {
            tvoption.setOnClickListener { viewModel.chooseAnswer(tvoption.text.toString().toInt()) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFinishedFragment(result : GameResult) {
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.main_container, GameFinishedFragment.newInstance(result))
//            .addToBackStack(null)
//            .commit()
        findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameFinishedFragment(result))
    }

    companion object {

        const val NAME = "GameFragment"
        const val KEY_LEVEL = "level"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}