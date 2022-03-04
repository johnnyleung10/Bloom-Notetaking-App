package com.example.notetakingapp.ui.editnote

import android.text.SpannableStringBuilder
import androidx.lifecycle.ViewModel
import com.example.notetakingapp.utilities.FileManager

class EditNoteViewModel : ViewModel() {

    fun updateText(noteID: Long, text : String) {
        val manager = FileManager.instance
        val note = manager?.getNote(noteID)
        if (note != null) {
            note.contents = SpannableStringBuilder(text)
        }
    }
}