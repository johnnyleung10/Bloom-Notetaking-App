package com.example.notetakingapp.ui.home

import android.text.SpannableStringBuilder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notetakingapp.utilities.FileManager

class EditNoteViewModel : ViewModel() {

    private val _noteTitle = MutableLiveData<String>().apply {
        value = "Notes"
    }
    val noteTitle: LiveData<String> = _noteTitle

    fun setNoteTitle(noteTitle: String){
        _noteTitle.value = noteTitle
    }

    fun updateText(noteID: Long, text : String) {
        val manager = FileManager.instance
        val note = manager?.getNote(noteID)
        if (note != null) {
            note.contents = SpannableStringBuilder(text)
        }
    }
}