package com.example.notetakingapp.utilities

import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.models.sqlite.DatabaseHelper
import com.example.notetakingapp.networking.ApiService
import com.example.notetakingapp.networking.models.FolderUpdateRequestModel
import com.example.notetakingapp.networking.models.NoteUpdateRequestModel
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class DataSynchronizer(private val databaseHelper: DatabaseHelper) {

    private val apiService: ApiService = ApiService.create()

    /**
     * INSERTING
     */
    fun insertNote(note: NoteModel) {

        // Insert the note locally first to populate the ID
        databaseHelper.insertNote(note)

        val noteCreationRequest = note.toNoteCreationRequestModel()
        var isDirty = false
        runBlocking {
            val response = async { apiService.insertNote(noteCreationRequest) }
            isDirty = response.await() == null
        }

        if(isDirty){
            databaseHelper.updateNote(note, isDirty)
        }
    }

    fun insertFolder(folder: FolderModel) {

        // Insert the folder locally first to populate the ID
        databaseHelper.insertFolder(folder)

        val folderCreationRequest = folder.toFolderCreationRequestModel()
        var isDirty = false
        runBlocking {
            val response = async { apiService.insertFolder(folderCreationRequest) }
            isDirty = response.await() == null
        }

        if(isDirty) {
            databaseHelper.updateFolder(folder, isDirty)
        }
    }


    /**
     * DELETING
     */
    fun deleteOneNote(note: NoteModel) {
        var deletedSuccess = false
        runBlocking {
            val response = async { apiService.deleteNote(note.id) }
            deletedSuccess = response.await() != null
        }
        if(deletedSuccess){
            databaseHelper.deleteOneNote(note.id)
        } else {
            databaseHelper.updateNote(note, isPermanentlyDeleted = true)
        }
    }

    fun deleteOneFolder(folder: FolderModel) {
        var deletedSuccess = false
        runBlocking {
            val response = async { apiService.deleteFolder(folder.id) }
            deletedSuccess = response.await() != null
        }
        if(deletedSuccess){
            databaseHelper.deleteOneFolder(id = folder.id)
        } else {
            databaseHelper.updateFolder(folder, isPermanentlyDeleted = true)
        }
    }

    /**
     * UPDATING
     */
    fun updateNote(note: NoteModel) {
        val noteUpdateRequest = NoteUpdateRequestModel(
            note.id,
            note.title,
            note.spannableStringToText(),
            note.contents.toString(),
            note.getDateCreated(),
            note.getLastModifiedDate(),
            note.getDeletionDate(),
            note.folderID
        )

        var isDirty = false
        runBlocking {
            val response = async { apiService.updateNote(noteUpdateRequest) }
            isDirty = response.await() == null
        }

        databaseHelper.updateNote(note, isDirty=isDirty)
    }

    fun updateFolder(folder: FolderModel) {
        val folderUpdateRequest = FolderUpdateRequestModel(folder.id, folder.title, folder.getDateCreated(), folder.getLastModifiedDate(), folder.getDeletionDate())
        var isDirty = false
        runBlocking {
            val response = async { apiService.updateFolder(folderUpdateRequest) }
            isDirty = response.await() == null
        }

        databaseHelper.updateFolder(folder, isDirty = isDirty)
    }

    /* SYNCHRONIZING */

    fun handleDirtyData(){

        // NOTES

        // We need to handle permanently deleted notes first since they could also be dirty
        val permanentlyDeletedNotes: List<NoteModel> = databaseHelper.getAllNotes(onlyPermanentlyDeleted = true)

        for(permanentlyDeletedNote: NoteModel in permanentlyDeletedNotes){
            deleteOneNote(permanentlyDeletedNote)
        }

        // Now we can handle dirty notes
        val dirtyNotes: List<NoteModel> = databaseHelper.getAllNotes(onlyDirty = true)

        for(dirtyNote: NoteModel in dirtyNotes){
            updateNote(dirtyNote)
        }

        // FOLDERS
        // We need to handle permanently deleted notes first since they could also be dirty
        val permanentlyDeletedFolders: List<FolderModel> = databaseHelper.getAllFolders(onlyPermanentlyDeleted = true)

        for(permanentlyDeletedFolder: FolderModel in permanentlyDeletedFolders){
            deleteOneFolder(permanentlyDeletedFolder)
        }

        // Now we can handle dirty folders
        val dirtyFolders: List<FolderModel> = databaseHelper.getAllFolders(onlyDirty = true)

        for(dirtyFolder: FolderModel in dirtyFolders){
            updateFolder(dirtyFolder)
        }

    }
}