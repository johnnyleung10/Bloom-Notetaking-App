package com.example.notetakingapp.ui.prompt

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.Spannable
import android.text.TextWatcher
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
    private var lastCursorPosition: Int = 0

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
        val promptResponse = binding.promptAnswer
        lastCursorPosition = promptResponse.selectionStart

        setupDailyEntry()
        setupObservers()

        // Setup all listeners for fragment
        addListeners()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDailyEntry(){
        val dailyEntry = dailyEntryManager.getDailyEntryToday()
        promptViewModel.setDailyEntry(dailyEntry)

        setupPrompt()
    }

    private fun setupPrompt(){
        val promptQuestion: TextView = binding.promptQuestion
        promptQuestion.text = promptViewModel.dailyEntry.value?.dailyPrompt?.prompt
    }

    private fun setupObservers(){
        val textView: TextView = binding.date
        val promptQuestion: TextView = binding.promptQuestion
        val promptAnswer: EditText = binding.promptAnswer

        promptViewModel.dailyEntry.observe(viewLifecycleOwner) {
            textView.text = it.dateCreated.toString()
            promptQuestion.text = it.dailyPrompt.prompt
            promptAnswer.setText(it.promptResponse)
            // TODO: Update the image!


        }
    }

    private fun setupPromptResponseListener(){
        val promptResponse = binding.promptAnswer

        promptResponse.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                lastCursorPosition = promptResponse.selectionStart
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                lastCursorPosition = promptResponse.selectionStart
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val response: String = promptResponse.text.toString()
                promptViewModel.dailyEntry.value?.promptResponse = response
            }
        })
    }

    private fun setupMoodDropdown(){

        val prompt: CardView = binding.prompt
        val moodPicker: CardView = binding.moodPicker
        val spinner: Spinner = binding.moods
        val submit: Button = binding.submit

        ArrayAdapter.createFromResource(
            requireContext(), R.array.moods, R.layout.moods_dropdown
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.moods_item_dropdown)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                parent?.getChildAt(1)?.setBackgroundColor(Color.BLUE)
                var mood = when(position) {
                    0 -> Mood.NO_SELECTION
                    1 -> Mood.HAPPY
                    2 -> Mood.LOVING
                    3 -> Mood.EXCITED
                    4 -> Mood.NEUTRAL
                    5 -> Mood.SAD
                    6 -> Mood.ANGRY
                    7 -> Mood.DOUBTFUL
                    else -> Mood.NO_SELECTION
                }

                promptViewModel.dailyEntry.value?.mood = mood

                val colorId = mood.colour

                val color = ContextCompat.getColor(requireContext(), colorId)

                submit.backgroundTintList = ColorStateList.valueOf(color)
                moodPicker.setCardBackgroundColor(color)
                prompt.setCardBackgroundColor(color)

            }
        }
    }

    private fun addListeners(){
        val calendarButton: Button = binding.calendar
        val attachNote: TextView = binding.attachNote
        val attachImage: ImageButton = binding.attachImage
        val submit: Button = binding.submit

        calendarButton.setOnClickListener{
            onCalendarClick()
        }

        submit.setOnClickListener{
            updateDailyEntry()
        }

        setupPromptResponseListener()
        setupMoodDropdown()

        // TODO @lucas setup listeners for note and image

    }

    private fun updateDailyEntry(){
        dailyEntryManager.updateDailyEntry(promptViewModel.dailyEntry.value!!)
    }

    private fun onCalendarClick() {
        val action = PromptFragmentDirections.actionNavigationPromptToFragmentCalendar()
        NavHostFragment.findNavController(this).navigate(action)
    }
}