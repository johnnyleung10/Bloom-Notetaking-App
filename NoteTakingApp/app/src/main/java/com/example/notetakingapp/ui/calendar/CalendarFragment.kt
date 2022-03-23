package com.example.notetakingapp.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.navigation.fragment.NavHostFragment
import com.example.notetakingapp.R
import com.example.notetakingapp.databinding.FragmentCalendarBinding
import com.example.notetakingapp.models.DailyEntryModel
import com.example.notetakingapp.utilities.DailyEntryManager
import io.ktor.util.date.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

        val c: Calendar = Calendar.getInstance()
        val today = LocalDateTime.now()

        c.set(Calendar.DAY_OF_MONTH, today.dayOfMonth)
        c.set(Calendar.MONTH, today.monthValue)
        c.set(Calendar.YEAR, today.year)

        val colors = ArrayList<Int>()
        adapter = CalendarAdapter(requireContext(), colors)
        grid.adapter = adapter

        // Setup all listeners for fragment
        addListeners()

        // Set the daily entries for the current month/year
        getDailyEntriesForMonth(c.get(Calendar.MONTH), c.get(Calendar.YEAR))

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getColorsForDailyEntries(entries: HashMap<Int, DailyEntryModel>): ArrayList<Int>{
        val c: Calendar = Calendar.getInstance()

        val padding: Int = c.get(Calendar.DAY_OF_WEEK) //- (c.get(Calendar.DATE) % 7)
        val newColors = ArrayList<Int>()
        for (i in 1..padding) newColors.add(R.color.none)

        val numDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dailyEntries = calendarViewModel.dailyEntries.value!!
        for(currDay in 1..numDaysInMonth){
            if(currDay in dailyEntries){
                newColors.add(dailyEntries[currDay]!!.mood.colour)
            } else {
                newColors.add(R.color.none)
            }
        }

        return newColors
    }

    private fun addListeners(){
        val promptButton: Button = binding.prompt
        val calendar: CalendarView = binding.calendar
        val left: Button = binding.left
        val right: Button = binding.right


        calendarViewModel.dailyEntries.observe(viewLifecycleOwner) {
            val colors = getColorsForDailyEntries(calendarViewModel.dailyEntries.value!!)
            adapter.setColors(colors)
        }

        left.setOnClickListener {
            val cal = Calendar.getInstance()
            cal.time = Date(calendar.date)
            cal.add(Calendar.MONTH, -1)
            cal.set(Calendar.DAY_OF_MONTH, 1)
            calendar.date = cal.time.time
            right.isVisible = true

            getDailyEntriesForMonth(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR))
            showDailyEntry(cal.get(Calendar.DATE), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR))
        }

        right.setOnClickListener {
            val cal = Calendar.getInstance()
            cal.time = Date(calendar.date)
            cal.add(Calendar.MONTH, 1)
            cal.set(Calendar.DAY_OF_MONTH, 1)
            calendar.date = cal.time.time

            if (Calendar.MONTH == cal.get(Calendar.MONTH))
                right.isVisible = false

            getDailyEntriesForMonth(cal.get(Calendar.MONTH), cal.get(Calendar.YEAR))
            showDailyEntry(cal.get(Calendar.DATE), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR))
        }

        promptButton.setOnClickListener{
            onPromptClick()
        }

        calendar.maxDate = getTimeMillis()
        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            showDailyEntry(dayOfMonth, month, year)
        }
    }

    private fun getDailyEntriesForMonth(month: Int, year: Int){
        val dailyEntries = dailyEntryManager.getDailyEntriesByDate(month, year)
        calendarViewModel.setDailyEntries(dailyEntries)
    }

    private fun showDailyEntry(day: Int, month: Int, year: Int){
        // TODO: get the daily entry from the view model

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