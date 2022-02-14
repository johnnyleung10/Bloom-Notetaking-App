package com.example.notetakingapp.utilities

import android.content.Context
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.models.sqlite.DatabaseHelper

class FileManager(val context: Context) {
    private val databaseHelper = DatabaseHelper(context)

    val folderList = ArrayList<FolderModel>()
    val noteList = ArrayList<NoteModel>()

    fun initFiles() {
        initFolders()
        initNotes()
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
        noteList.addAll(databaseHelper.getAllNotes())

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