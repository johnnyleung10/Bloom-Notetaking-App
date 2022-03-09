package com.example.notetakingapp.utilities

import android.text.Html
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.models.sqlite.DatabaseHelper
import com.example.notetakingapp.networking.ApiService
import com.example.notetakingapp.networking.models.FolderDeletionRequestModel
import com.example.notetakingapp.networking.models.FolderUpdateRequestModel
import com.example.notetakingapp.networking.models.NoteDeletionRequestModel
import com.example.notetakingapp.networking.models.NoteUpdateRequestModel
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class DataSynchronizer(private val databaseHelper: DatabaseHelper) {

    private val apiService: ApiService = ApiService.create()

    /**
     * INSERTING
     */
    fun insertNote(note: NoteModel): Long {
        val noteCreationRequest = note.toNoteCreationRequestModel()
        var isDirty = true
        runBlocking {
            val response = async { apiService.insertNote(noteCreationRequest) }
            isDirty = response.await() == null
        }

        return databaseHelper.insertNote(note, isDirty)
    }

    fun insertFolder(folder: FolderModel): Long {
        val folderCreationRequest = folder.toFolderCreationRequestModel()
        var isDirty = true
        runBlocking {
            val response = async { apiService.insertFolder(folderCreationRequest) }
            isDirty = response.await() == null
        }

        return databaseHelper.insertFolder(folder, isDirty)
    }


    /**
     * DELETING
     */
    fun deleteOneNote(id: Long) {
        val noteDeletionRequest = NoteDeletionRequestModel(id)
        var deletedSuccess = false
        runBlocking {
            val response = async { apiService.deleteNote(noteDeletionRequest) }
            deletedSuccess = response.await() != null
        }
        if(deletedSuccess){
            databaseHelper.deleteOneNote(id)
        } else {
            databaseHelper.updateNote(id, isPermanentlyDeleted = true)
        }
    }

    fun deleteOneFolder(id: Long) {
        val folderDeletionRequest = FolderDeletionRequestModel(id)
        var deletedSuccess = false
        runBlocking {
            val response = async { apiService.deleteFolder(folderDeletionRequest) }
            deletedSuccess = response.await() != null
        }
        if(deletedSuccess){
            databaseHelper.deleteOneFolder(id = id)
        } else {
            databaseHelper.updateFolder(id, isPermanentlyDeleted = true)
        }
    }

    /**
     * UPDATING
     */
    fun updateNote(id: Long, title: String? = null, content: String? = null, dateModified: String? = null, dateDeleted: String? = null, folderId: Long? = null) {
        val noteUpdateRequest = NoteUpdateRequestModel(id, title, content, Html.fromHtml(content).toString(), dateModified, dateDeleted, folderId)
        var isDirty = true
        runBlocking {
            val response = async { apiService.updateNote(noteUpdateRequest) }
            isDirty = response.await() == null
        }

        databaseHelper.updateNote(id, title = title, content = content, dateModified = dateModified, isDirty=isDirty)
    }

    fun updateFolder(id: Long, title: String? = null, dateModified: String? = null, dateDeleted: String? = null) {
        val folderUpdateRequest = FolderUpdateRequestModel(id, title, dateModified, dateDeleted)
        var isDirty = true
        runBlocking {
            val response = async { apiService.updateFolder(folderUpdateRequest) }
            isDirty = response.await() == null
        }

        databaseHelper.updateFolder(id, title = title, dateModified = dateModified, isDirty = isDirty)
    }

    /* SYNCHRONIZING */

    fun handleDirtyData(){

        // NOTES

        // We need to handle permanently deleted notes first since they could also be dirty
        val permanentlyDeletedNotes: List<NoteModel> = databaseHelper.getAllNotes(onlyPermanentlyDeleted = true)

        for(permanentlyDeletedNote: NoteModel in permanentlyDeletedNotes){
            deleteOneNote(permanentlyDeletedNote.id)
        }

        // Now we can handle dirty notes
        val dirtyNotes: List<NoteModel> = databaseHelper.getAllNotes(onlyDirty = true)

        for(dirtyNote: NoteModel in dirtyNotes){
            updateNote(dirtyNote.id, dirtyNote.title, dirtyNote.spannableStringToText(), dirtyNote.getLastModifiedDate(), dirtyNote.getDeletionDate(), dirtyNote.folderID)
        }

        // FOLDERS
        // We need to handle permanently deleted notes first since they could also be dirty
        val permanentlyDeletedFolders: List<FolderModel> = databaseHelper.getAllFolders(onlyPermanentlyDeleted = true)

        for(permanentlyDeletedFolder: FolderModel in permanentlyDeletedFolders){
            deleteOneFolder(permanentlyDeletedFolder.id)
        }

        // Now we can handle dirty folders
        val dirtyFolders: List<FolderModel> = databaseHelper.getAllFolders(onlyDirty = true)

        for(dirtyFolder: FolderModel in dirtyFolders){
            updateFolder(dirtyFolder.id, dirtyFolder.title, dirtyFolder.getLastModifiedDate(), dirtyFolder.getDeletionDate())
        }

    }
}