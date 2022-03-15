package com.example.notetakingapp.ui.prompt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.notetakingapp.R
import com.example.notetakingapp.databinding.FragmentPromptBinding


class PromptFragment : Fragment() {

    private lateinit var promptViewModel: PromptViewModel
    private var _binding: FragmentPromptBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        promptViewModel = ViewModelProvider(this).get(PromptViewModel::class.java)

        _binding = FragmentPromptBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupDate()

        // Setup all listeners for fragment
        addListeners()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDate(){
        val textView: TextView = binding.date
        promptViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
    }

    private fun addListeners(){
        val calendarButton: Button = binding.calendar
        val promptAnswer: EditText = binding.promptAnswer
        val attachNote: TextView = binding.attachNote
        val attachImage: ImageButton = binding.attachImage
        val spinner: Spinner = binding.moods
        val submit: Button = binding.submit

        calendarButton.setOnClickListener{
            onCalendarClick()
        }

        ArrayAdapter.createFromResource(
            requireContext(), R.array.moods, R.layout.dropdown
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }
        }
    }

    private fun onCalendarClick() {
        val action = PromptFragmentDirections.actionNavigationPromptToFragmentCalendar()
        NavHostFragment.findNavController(this).navigate(action)
    }
}