package com.example.notetakingapp.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.models.sqlite.DatabaseHelper

private const val UNCATEGORIZED_FOLDER : Long = 1
private const val RECENTLY_DELETED_FOLDER : Long = 2
private const val UNIDENTIFIED_FOLDER : String = "Unidentified Folder"

class FileManager() {
    private lateinit var context : Context
    private lateinit var databaseHelper : DatabaseHelper

    val folderList = HashMap<Long, FolderModel>()
    private val allNotes = HashMap<Long, NoteModel>()

    fun initManager(context: Context) {
        this.context = context
        databaseHelper = DatabaseHelper(context)
    }

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
            for (folder in databaseHelper.getAllFolders()) {
                folderList[folder.id] = folder
            }
        }
    }

    /**
     * Initializes existing notes from database, and assigns it to respective folders
     */
    private fun initNotes() {
        for (note in databaseHelper.getAllNotes()) {
            val i = note.folderID
            note.currFolder = folderList[i]?.title ?: UNIDENTIFIED_FOLDER
            folderList[UNCATEGORIZED_FOLDER]?.noteList?.add(note)

            allNotes[note.id] = note
        }
    }

    /**
     * Creates a new folder and adds it to folderList
     */
    fun createNewFolder(name : String) : FolderModel {
        val newFolder = FolderModel(name, context)
        databaseHelper.insertFolder(newFolder)
        folderList[newFolder.id] = newFolder
        return newFolder
    }

    /**
     * Edits folder and updates the database
     */
    fun editFolder(folderID: Long, title: String? = null) {
        if (folderID == UNCATEGORIZED_FOLDER || folderID == RECENTLY_DELETED_FOLDER) {
            Log.d("FileManager", "You can't edit default folders")
            return
        }

        title?.let { folderList[folderID]?.title = title }
        folderList[folderID]?.updateModifiedDate()

        // Update the database
        databaseHelper.updateFolder(folderID, title = title,
            dateModified = folderList[folderID]?.getLastModifiedDate())
    }

    /**
     * Delete folder specified by folderID
     */
    fun deleteFolder(folderID : Long) : Boolean {
        if (folderList[folderID] == null) return false
        if (folderID == UNCATEGORIZED_FOLDER || folderID == RECENTLY_DELETED_FOLDER) return false

        // Move notes to uncategorized
        for (note in folderList[folderID]?.noteList!!) {
            moveNote(note, UNCATEGORIZED_FOLDER)
        }

        // Remove from database
        databaseHelper.deleteOneFolder(id = folderID)
        folderList.remove(folderID)

        return true
    }

    /**
     * Creates a new note in uncategorized folder
     */
    fun createNewNote(name : String) : NoteModel {
        return createNewNote(name, UNCATEGORIZED_FOLDER)
    }

    /**
     * Creates a new note inside the specified folder
     */
    fun createNewNote(name : String, folderID : Long) : NoteModel {
        // Create note and assign it to folder
        val newNote = NoteModel(name, context)
        newNote.folderID = folderID
        newNote.currFolder = folderList[folderID]?.title ?: UNIDENTIFIED_FOLDER
        folderList[folderID]?.noteList?.add(newNote)

        allNotes[newNote.id] = newNote
        // Update in database
        databaseHelper.insertNote(newNote)
        return newNote
    }

    /**
     * Moves note to recently deleted folder
     */
    fun deleteNote(note : NoteModel) {
        note.updateDeletionDate()
        moveNote(note, RECENTLY_DELETED_FOLDER) // move to recently deleted
    }

    fun getNote(id : Long) : NoteModel? {
        return allNotes[id]
    }

    /**
     * Removes a note from database
     */
    fun permanentlyDeleteNote(note : NoteModel) : Boolean {
        if (note.folderID != RECENTLY_DELETED_FOLDER) return false
        folderList[RECENTLY_DELETED_FOLDER]?.noteList?.remove(note)
        allNotes.remove(note.id)
        databaseHelper.deleteOneNote(note.id)

        return true
    }

    /**
     * Moves a note to the specified folder
     */
    fun moveNote(note : NoteModel, folderID : Long) {
        // Remove note from current folder
        val currFolderIndex = note.folderID
        folderList[currFolderIndex]?.noteList?.remove(note)

        // Add note to new folder
        folderList[folderID]?.noteList?.add(note)
        note.currFolder = folderList[folderID]?.title ?: UNIDENTIFIED_FOLDER

        note.updateModifiedDate()

        // Update in database
        databaseHelper.updateNote(note.id, folderId = folderID.toInt(),
            dateModified = note.getLastModifiedDate(), dateDeleted = note.getDeletionDate())
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var ourInstance: FileManager? = null
        val instance: FileManager?
            get() {
                if (ourInstance == null) {
                    ourInstance = FileManager()
                }
                return ourInstance
            }
    }
}