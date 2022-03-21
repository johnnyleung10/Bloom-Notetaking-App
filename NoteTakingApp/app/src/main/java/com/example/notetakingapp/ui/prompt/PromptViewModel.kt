package com.example.notetakingapp.ui.prompt

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notetakingapp.models.DailyEntryModel
import com.example.notetakingapp.models.DailyPromptModel
import com.example.notetakingapp.models.NoteCellViewModel
import com.example.notetakingapp.models.NoteModel

class PromptViewModel : ViewModel() {

    private val _dailyEntry = MutableLiveData<DailyEntryModel>()
    val dailyEntry: LiveData<DailyEntryModel> = _dailyEntry

    fun setDailyEntry(currDailyEntry: DailyEntryModel){
        _dailyEntry.value = currDailyEntry
    }
}