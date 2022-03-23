package com.example.notetakingapp.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.notetakingapp.databinding.FragmentCalendarBinding
import com.example.notetakingapp.utilities.DailyEntryManager
import io.ktor.util.date.*
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel
    private var _binding: FragmentCalendarBinding? = null
    private lateinit var adapter: CalendarAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var dailyEntryManager: DailyEntryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dailyEntryManager = DailyEntryManager.instance!!
        calendarViewModel =
            ViewModelProvider(this).get(CalendarViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val grid = binding.colorBlocks
        val colors = ArrayList<String>()

        val c: Calendar = Calendar.getInstance()

        c.set(Calendar.DAY_OF_MONTH, 0)
        val padding: Int = c.get(Calendar.DAY_OF_WEEK) //- (c.get(Calendar.DATE) % 7)

        for (i in 1..padding) colors.add("none")
        colors.add("happy")
        colors.add("sad")
        colors.add("none")
        colors.add("neutral")
        colors.add("none")
        colors.add("loving")
        colors.add("neutral")
        colors.add("angry")
        colors.add("none")
        colors.add("excited")
        colors.add("doubtful")
        colors.add("none")
        colors.add("neutral")
        colors.add("loving")
        colors.add("none")
        colors.add("happy")
        colors.add("sad")
        colors.add("none")
        colors.add("neutral")
        colors.add("none")
        colors.add("neutral")
        colors.add("excited")

        adapter = CalendarAdapter(requireContext(), colors)
        grid.adapter = adapter

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
        val left: Button = binding.left
        val right: Button = binding.right

        left.setOnClickListener {
            val cal = Calendar.getInstance()
            cal.time = Date(calendar.date)
            cal.add(Calendar.MONTH, -1)
            cal.set(Calendar.DAY_OF_MONTH, 1)
            calendar.date = cal.time.time
            right.isVisible = true

            getMonthMoods(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
            getDailyPrompt(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE))
        }

        right.setOnClickListener {
            val cal = Calendar.getInstance()
            cal.time = Date(calendar.date)
            cal.add(Calendar.MONTH, 1)
            cal.set(Calendar.DAY_OF_MONTH, 1)
            calendar.date = cal.time.time

            if (Calendar.MONTH == cal.get(Calendar.MONTH))
                right.isVisible = false

            getMonthMoods(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH))
            getDailyPrompt(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE))
        }

        promptButton.setOnClickListener{
            onPromptClick()
        }

        calendar.maxDate = getTimeMillis()
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            getDailyPrompt(year, month, dayOfMonth)
        }
    }

    private fun getMonthMoods(year: Int, month: Int){
        // TODO: get list of moods for selected month
        val newColors = ArrayList<String>()

        adapter.setColors(newColors)
    }

    private fun getDailyPrompt(year: Int, month: Int, date: Int){
        val prompt: CardView = binding.entry
        val promptQuestion: TextView = binding.promptQuestion
        val promptAnswer: TextView = binding.promptAnswer
        val image: ImageView = binding.image
        val delete: ImageButton = binding.delete

        //prompt.isVisible = false
        //prompt.setBackgroundColor()

        // TODO: set up fields and delete

    }

    private fun onPromptClick() {
        val action = CalendarFragmentDirections.actionNavigationCalendarToFragmentPrompt()
        NavHostFragment.findNavController(this).navigate(action)
    }
}