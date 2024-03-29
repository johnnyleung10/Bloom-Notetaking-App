package com.example.notetakingapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notetakingapp.models.NoteCellViewModel
import com.example.notetakingapp.models.NoteModel

class NotesViewModel : ViewModel() {

    private val _folderTitle = MutableLiveData<String>().apply {
        value = "Notes"
    }
    val folderTitle: LiveData<String> = _folderTitle
    var folderID: Long = 1

    fun setFolderTitle(folderTitle: String){
        _folderTitle.value = folderTitle
    }


    private val _noteCells = MutableLiveData<ArrayList<NoteCellViewModel>>()
    val noteCells: LiveData<ArrayList<NoteCellViewModel>> = _noteCells

    fun setNotes(notes: ArrayList<NoteModel>){
        val noteCells = ArrayList<NoteCellViewModel>()
        // Create NoteCellViewModels
        for(note in notes){
            noteCells.add(NoteCellViewModel(note.id, note.title))
        }

        _noteCells.value = noteCells
    }

}