package com.example.notetakingapp.utilities

import android.content.Context
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.models.sqlite.DatabaseHelper

class FileManager(val context: Context) {
    private val databaseHelper = DatabaseHelper(context)

    val folderList = ArrayList<FolderModel>()

    fun initFiles() {
        folderList.add(FolderModel("SpareFolder", context))
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
            note.currFolder = folderList[i.toInt()].title
            folderList[i.toInt()].contains.add(note)
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

    fun deleteFolder(folderID : Long) {
        TODO("Delete all files inside?")
    }

    /**
     * Creates a new note in an empty folder
     */
    fun createNewNote(name : String) : NoteModel {
        val newNote = NoteModel(name, context)
        databaseHelper.insertNote(newNote)
        folderList[1].contains.add(newNote)

        return newNote
    }

    /**
     * Creates a new note inside the specified folder
     */
    fun createNewNote(name : String, folderID : Long) : NoteModel {
        val newNote = NoteModel(name, context)
        newNote.folderID = folderID
        newNote.currFolder = folderList[folderID.toInt()].title
        folderList[folderID.toInt()].contains.add(newNote)

        databaseHelper.insertNote(newNote)
        return newNote
    }

    fun deleteNote(note : NoteModel) {
        moveNote(note, 2) // move to recently deleted
        note.deleteFile()
    }

    /**
     * Moves a note to the specified folder
     */
    fun moveNote(note : NoteModel, folderID : Long) {
        val currFolderIndex = note.folderID.toInt()
        folderList[currFolderIndex].contains.remove(note)
        folderList[folderID.toInt()].contains.add(note)
        note.currFolder = folderList[folderID.toInt()].title
        databaseHelper.updateNote(note.id, folderId = folderID.toInt())
    }
}