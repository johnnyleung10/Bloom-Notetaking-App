package com.example.notetakingapp.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.notetakingapp.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    private var _binding: FragmentCalendarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendarViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)

        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textCalendar
//        calendarViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        // Setup all listeners for fragment
        addListeners()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addListeners(){
        val promptButton: Button = binding.prompt
        val calendar: CalendarView = binding.calendar
        val delete: ImageButton = binding.delete

        promptButton.setOnClickListener{
            onPromptClick()
        }

        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->

        }

    }

    private fun onPromptClick() {
        val action = CalendarFragmentDirections.actionNavigationCalendarToFragmentPrompt()
        NavHostFragment.findNavController(this).navigate(action)
    }
}