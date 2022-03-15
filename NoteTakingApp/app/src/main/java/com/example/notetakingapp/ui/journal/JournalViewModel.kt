package com.example.notetakingapp.ui.journal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JournalViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "March 15, 2022"
    }
    val text: LiveData<String> = _text
}