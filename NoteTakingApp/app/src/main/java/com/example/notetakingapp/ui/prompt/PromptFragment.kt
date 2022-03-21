package com.example.notetakingapp.ui.prompt

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.notetakingapp.R
import com.example.notetakingapp.databinding.FragmentPromptBinding
import android.widget.TextView
import com.example.notetakingapp.models.DailyEntryModel
import com.example.notetakingapp.utilities.DailyEntryManager
import com.example.notetakingapp.utilities.FileManager
import com.example.notetakingapp.utilities.Mood
import com.example.notetakingapp.viewmodels.FoldersViewModel


class PromptFragment : Fragment() {

    private lateinit var promptViewModel: PromptViewModel
    private var _binding: FragmentPromptBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var dailyEntryManager: DailyEntryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dailyEntryManager = DailyEntryManager.instance!!
        promptViewModel = ViewModelProvider(this).get(PromptViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPromptBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getDailyPrompt()

        setupDate()
        setupPrompt()

        // Setup all listeners for fragment
        addListeners()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupPrompt(){
        val promptQuestion: TextView = binding.promptQuestion
        promptQuestion.text = promptViewModel.dailyPrompt.prompt
    }

    private fun setupDate(){
        val textView: TextView = binding.date
        promptViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

    private fun getDailyPrompt(){
        promptViewModel.dailyPrompt = dailyEntryManager.getDailyPrompt()
    }

    private fun addListeners(){
        val calendarButton: Button = binding.calendar
        val prompt: CardView = binding.prompt
        val promptAnswer: EditText = binding.promptAnswer
        val attachNote: TextView = binding.attachNote
        val attachImage: ImageButton = binding.attachImage
        val moodPicker: CardView = binding.moodPicker
        val spinner: Spinner = binding.moods
        val submit: Button = binding.submit

        calendarButton.setOnClickListener{
            onCalendarClick()
        }

        submit.setOnClickListener{
            submitDailyEntry()
        }

        ArrayAdapter.createFromResource(
            requireContext(), R.array.moods, R.layout.moods_dropdown
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.moods_item_dropdown)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var mood = ""
                parent?.getChildAt(1)?.setBackgroundColor(Color.BLUE)
                when(position){
                    0 -> mood = "no_selection"
                    1 -> mood = "happy"
                    2 -> mood = "loving"
                    3 -> mood = "excited"
                    4 -> mood = "neutral"
                    5 -> mood = "sad"
                    6 -> mood = "angry"
                    7 -> mood = "doubtful"
                }

                promptViewModel.moodId = position.toLong()

                val colorId: Int = requireContext().resources.getIdentifier(mood, "color", requireContext().packageName)
                val color = ContextCompat.getColor(requireContext(), colorId)

                submit.backgroundTintList = ColorStateList.valueOf(color)
                moodPicker.setCardBackgroundColor(color)
                prompt.setCardBackgroundColor(color)

            }
        }
    }

    private fun submitDailyEntry(){
        val promptResponse = binding.promptAnswer.text.toString()
        val moodId = promptViewModel.moodId
        val dailyImage = promptViewModel.dailyImage.value
        val linkedNoteId = promptViewModel.linkedNoteId
        val dailyPromptId = promptViewModel.dailyPrompt.id
        dailyEntryManager.createDailyEntry(promptResponse, moodId, dailyImage, linkedNoteId, dailyPromptId)
    }

    private fun onCalendarClick() {
        val action = PromptFragmentDirections.actionNavigationPromptToFragmentCalendar()
        NavHostFragment.findNavController(this).navigate(action)
    }
}