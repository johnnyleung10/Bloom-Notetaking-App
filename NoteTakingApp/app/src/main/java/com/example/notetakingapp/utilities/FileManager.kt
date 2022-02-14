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
        initNotes()
    }

    /**
     * Initializes existing folders from database, or creates the default folders if none exist
     */
    private fun initFolders() {
        if (databaseHelper.getNumberOfFolders() == 0) {
            // Create default folders
            createNewFolder("Uncategorized")
            createNewFolder("Recently Deleted")
        } else {
            folderList.addAll(databaseHelper.getAllFolders())
        }
    }

    /**
     * Initializes existing notes from database, and assigns it to respective folders
     */
    private fun initNotes() {
        for (note in databaseHelper.getAllNotes()) {
            val i = note.folderID
            note.currFolder = folderList[i.toInt() - 1].title
            folderList[i.toInt() - 1].contains.add(note)
        }
    }

    /**
     * Creates a new folder and adds it to folderList
     */
    fun createNewFolder(name : String) : FolderModel {
        val newFolder = FolderModel(name, context)
        databaseHelper.insertFolder(newFolder)
        folderList.add(newFolder)
        return newFolder
    }

    /**
     * Creates a new note in an empty folder
     */
    fun createNewNote(name : String) : NoteModel {
        val newNote = NoteModel(name, context)
        databaseHelper.insertNote(newNote)
        folderList[0].contains.add(newNote)

        return newNote
    }

    /**
     * Creates a new note inside the specified folder
     */
    fun createNewNote(name : String, folderID : Long) : NoteModel {
        val newNote = NoteModel(name, context)
        newNote.folderID = folderID
        newNote.currFolder = folderList[folderID.toInt() - 1].title
        folderList[folderID.toInt() - 1].contains.add(newNote)

        databaseHelper.insertNote(newNote)
        return newNote
    }

    fun moveNote(note : NoteModel, folderID : Long) {
        val currFolderIndex = note.folderID.toInt() - 1
        folderList[currFolderIndex].contains.remove(note)
        folderList[folderID.toInt() - 1].contains.add(note)
        databaseHelper.updateNote(note.id, folderId = folderID.toInt())
    }
}