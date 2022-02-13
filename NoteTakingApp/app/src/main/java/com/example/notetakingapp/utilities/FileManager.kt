package com.example.notetakingapp.utilities

import android.content.Context
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.models.sqlite.DatabaseHelper

class FileManager(val context: Context) {
    private val databaseHelper = DatabaseHelper(context)

    fun initFiles() {

    }



    fun createNewFolder(name : String) {
        val newFolder = FolderModel(name, context)
        databaseHelper.insertFolder(newFolder)
    }

    fun createNewNote(name : String) {
        val newNote = NoteModel(name, context)
        databaseHelper.insertNote(newNote)
    }
}