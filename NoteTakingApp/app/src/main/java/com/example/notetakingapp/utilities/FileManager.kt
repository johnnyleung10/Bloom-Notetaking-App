package com.example.notetakingapp.utilities

import android.content.Context
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.models.sqlite.DatabaseHelper

class FileManager(val context: Context) {
    private val databaseHelper = DatabaseHelper(context)

    val folderList = ArrayList<FolderModel>()

    fun initFiles() {
        initFolders()
    }

    private fun initFolders() {
        if (databaseHelper.getNumberOfFolders() == 0) {
            // Create default folders
            folderList.add(createNewFolder("Uncategorized"))
            folderList.add(createNewFolder("Recently Deleted"))
        } else {
            folderList.addAll(databaseHelper.getAllFolders())
        }
    }

    private fun initNotes() {


    }

    fun createNewFolder(name : String) : FolderModel {
        val newFolder = FolderModel(name, context)
        databaseHelper.insertFolder(newFolder)
        return newFolder
    }

    fun createNewNote(name : String) : NoteModel {
        val newNote = NoteModel(name, context)
        databaseHelper.insertNote(newNote)
        return newNote
    }
}