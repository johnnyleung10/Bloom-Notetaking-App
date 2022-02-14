package com.example.notetakingapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotesViewModel : ViewModel() {

    private val _folderTitle = MutableLiveData<String>().apply {
        value = "Notes"
    }
    val folderTitle: LiveData<String> = _folderTitle

    fun setFolderTitle(folderTitle: String){
        _folderTitle.value = folderTitle
    }
}