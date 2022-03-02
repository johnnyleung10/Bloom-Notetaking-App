package com.example.notetakingapp.ui.folders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notetakingapp.models.FolderCellViewModel
import com.example.notetakingapp.models.FolderModel

class FoldersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _folderCells = MutableLiveData<ArrayList<FolderCellViewModel>>()
    val folderCells: LiveData<ArrayList<FolderCellViewModel>> = _folderCells

    fun setFolders(folders: HashMap<Long, FolderModel>){
        val cells = ArrayList<FolderCellViewModel>()
        // Create FolderCellViewModels
        for(folder in folders.values){
            cells.add(FolderCellViewModel(folder.id, folder.title, folder.noteList.size))
        }

        _folderCells.value = cells
    }

}