package com.example.notetakingapp.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableStringBuilder
import android.util.Log
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.models.sqlite.NoteTakingDatabaseHelper

private const val UNCATEGORIZED_FOLDER : Long = 1
private const val RECENTLY_DELETED_FOLDER : Long = 2
private const val DAILY_ENTRY_FOLDER : Long = 3
private const val UNIDENTIFIED_FOLDER : String = "Unidentified Folder"

class FileManager {
    private lateinit var context : Context
    lateinit var noteTakingDatabaseHelper : NoteTakingDatabaseHelper
    lateinit var noteDataSynchronizer: NoteDataSynchronizer

    val folderList = HashMap<Long, FolderModel>()
    val allNotes = HashMap<Long, NoteModel>()

    fun initManager(context: Context) {
        this.context = context
        noteTakingDatabaseHelper = NoteTakingDatabaseHelper(context)
        noteDataSynchronizer = NoteDataSynchronizer(noteTakingDatabaseHelper)
    }

    /**
     * Sets up class by creating folders and notes from the database
     */
    fun initFiles() {
        initFolders()
        initNotes()
    }

    /**
     * Initializes existing folders from database, or creates the default folders if none exist
     */
    private fun initFolders() {
        if (noteTakingDatabaseHelper.getNumberOfFolders() == 0) {
            // Create default folders
            createNewFolder("Uncategorized")
            createNewFolder("Recently Deleted")
            createNewFolder("Daily Entries")
        } else {
            for (folder in noteTakingDatabaseHelper.getAllFolders()) {
                folderList[folder.id] = folder
            }
        }
    }

    /**
     * Initializes existing notes from database, and assigns it to respective folders
     */
    private fun initNotes() {
        for (note in noteTakingDatabaseHelper.getAllNotes()) {
            val i = note.folderID
            note.currFolder = folderList[i]?.title ?: UNIDENTIFIED_FOLDER
            folderList[i]?.noteList?.add(note)

            allNotes[note.id] = note
        }
    }

    /**
     * Creates a new folder and adds it to folderList
     */
    fun createNewFolder(name : String) : FolderModel {
        val newFolder = FolderModel(name)
        noteDataSynchronizer.insertFolder(newFolder)
        folderList[newFolder.id] = newFolder
        return newFolder
    }

    /**
     * Edits folder and updates the database
     */
    fun editFolder(folderID: Long, title: String? = null) {
        if (folderID == UNCATEGORIZED_FOLDER || folderID == RECENTLY_DELETED_FOLDER || folderID == DAILY_ENTRY_FOLDER) {
            Log.d("FileManager", "You can't edit default folders")
            return
        }

        val folder = folderList[folderID]

        title?.let { folder?.title = title }
        folder?.updateModifiedDate()

        if(folder != null){
            noteDataSynchronizer.updateFolder(folder)
        }
    }

    /**
     * Delete folder specified by folderID
     */
    fun deleteFolder(folderID : Long) : Boolean {
        val folder = folderList[folderID]

        if (folder == null) return false
        if (folderID == UNCATEGORIZED_FOLDER || folderID == RECENTLY_DELETED_FOLDER || folderID == DAILY_ENTRY_FOLDER) return false

        // Move notes to uncategorized
        val currNotes : ArrayList<Long> = ArrayList()
        for (note in folderList[folderID]?.noteList!!) {
            currNotes.add(note.id)
        }
        for (note in currNotes) {
            deleteNote(note)
        }

        // Remove from database
        noteDataSynchronizer.deleteOneFolder(folder)

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
        val newNote = NoteModel(name)
        newNote.folderID = folderID
        newNote.currFolder = folderList[folderID]?.title ?: UNIDENTIFIED_FOLDER
        folderList[folderID]?.noteList?.add(newNote)

        // Update in database
        noteDataSynchronizer.insertNote(newNote)
        allNotes[newNote.id] = newNote
        return newNote
    }

    /**
     * Edits a note
     */
    fun editNote(noteID : Long, title: String? = null, contents : SpannableStringBuilder? = null) {
        // Get note
        val note = getNote(noteID)

        title?.let { note?.title = title }
        contents?.let { note?.contents = contents }
        note?.updateModifiedDate()

        // Update the database
        if(note != null){
            noteDataSynchronizer.updateNote(note)
        }
    }

    /**
     * Moves note to recently deleted folder
     */
    fun deleteNote(noteID : Long) {
        val note = allNotes[noteID]
        note?.updateDeletionDate()
        moveNote(noteID, RECENTLY_DELETED_FOLDER) // move to recently deleted
    }

    /**
     * Moves note to recently deleted folder
     */
    fun restoreNote(noteID : Long) : Boolean {
        val note = allNotes[noteID]
        if (note != null) {
            if (!folderList[RECENTLY_DELETED_FOLDER]?.noteList?.contains(note)!!) return false
            note.restoreFileDate()
            // Check if original folder exists
            if (folderList.containsKey(note.folderID)) {
                moveNote(noteID, note.folderID) // Restore to original folder
            } else {
                moveNote(noteID, UNCATEGORIZED_FOLDER) // Restore to uncategorized
            }

        }
        return true
    }

    fun getNote(id : Long) : NoteModel? {
        return allNotes[id]
    }

    /**
     * Removes a note from database
     */
    fun permanentlyDeleteNote(noteID : Long) : Boolean {
        val note = allNotes[noteID]
        if (!folderList[RECENTLY_DELETED_FOLDER]?.noteList?.contains(note)!!) return false
        folderList[RECENTLY_DELETED_FOLDER]?.noteList?.remove(note)

        allNotes.remove(noteID)
        if(note != null){
            noteDataSynchronizer.deleteOneNote(note)
        }

        return true
    }

    /**
     * Moves a note to the specified folder
     */
    fun moveNote(noteID : Long, folderID : Long) {
        val note = allNotes[noteID]

        // Remove note from current folder
        val currFolderIndex = note?.folderID
        folderList[currFolderIndex]?.noteList?.remove(note)

        // Add note to new folder
        folderList[folderID]?.noteList?.add(note!!)
        if (note?.getDeletionDate() == "") { // Only change folderID and folderName if not deleted
            note?.currFolder = folderList[folderID]?.title ?: UNIDENTIFIED_FOLDER
            note?.folderID = folderID
            note?.updateModifiedDate()
        }

        // Update in database
        if (note != null) {
            noteDataSynchronizer.updateNote(note)
        }
    }

    /**
     * Sort notes by columnName in specified folder
     */
    fun sortNotes(columnName : String, folderID: Long, descending: Boolean? = false) {
        val newOrder = noteTakingDatabaseHelper.getSortedNotes(columnName, folderID, descending)
        folderList[folderID]!!.noteList.clear()
        for (noteID : Long in newOrder) {
            val note : NoteModel? = allNotes[noteID]
            folderList[folderID]!!.noteList.add(note!!)
        }
    }

    /**
     * Search notes by title in the specified folder. Returns a list of noteIDs that match search
     * criteria
     */
    fun searchNotes(searchTerm : String, folderID: Long) : List<Long> {
        return noteTakingDatabaseHelper.searchNote(NoteTakingDatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_TITLE, searchTerm, folderID)
    }

    /**
     * Sort notes by columnName in specified folder
     */
    fun sortFolders(columnName : String, descending: Boolean? = false) : List<Long> {
        val ret = ArrayList<Long>()
        ret.add(UNCATEGORIZED_FOLDER)
        ret.add(RECENTLY_DELETED_FOLDER)
        ret.add(DAILY_ENTRY_FOLDER)
        val newOrder = noteTakingDatabaseHelper.getSortedFolders(columnName, descending)
        for (folderID : Long in newOrder) {
            if (folderID != UNCATEGORIZED_FOLDER && folderID != RECENTLY_DELETED_FOLDER && folderID != DAILY_ENTRY_FOLDER) {
                ret.add(folderID)
            }

        }
        return ret
    }

    /**
     * Search folders by title. Returns a list of folderIDs that match search criteria
     */
    fun searchFolders(searchTerm : String) : List<Long> {
        return noteTakingDatabaseHelper.searchFolder(NoteTakingDatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_TITLE, searchTerm)
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