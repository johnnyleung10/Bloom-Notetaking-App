package com.example.notetakingapp.ui.prompt

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notetakingapp.models.DailyPromptModel

class PromptViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "March 15, 2022"
    }
    val text: LiveData<String> = _text

    private val _promptResponse = MutableLiveData<String>().apply {
        value = ""
    }
    val promptResponse: LiveData<String> = _promptResponse

    var moodId: Long = 0

    private val _dailyImage = MutableLiveData<Bitmap?>().apply {
        value = null
    }
    val dailyImage: LiveData<Bitmap?> = _dailyImage

    val linkedNoteId: Long? = null
    lateinit var dailyPrompt: DailyPromptModel

}