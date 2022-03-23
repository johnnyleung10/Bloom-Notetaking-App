package com.example.notetakingapp.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notetakingapp.models.DailyEntryModel

class CalendarViewModel : ViewModel() {

    private val _dailyEntries = MutableLiveData<HashMap<Int, DailyEntryModel>>()
    val dailyEntries: LiveData<HashMap<Int, DailyEntryModel>> = _dailyEntries

    fun setDailyEntries(entries: HashMap<Int, DailyEntryModel>){
        _dailyEntries.value = entries
    }
}