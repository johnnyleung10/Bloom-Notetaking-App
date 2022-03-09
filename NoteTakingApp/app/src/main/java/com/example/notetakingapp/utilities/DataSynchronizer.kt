package com.example.notetakingapp.utilities

import android.content.Context
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.models.sqlite.DatabaseHelper
import com.example.notetakingapp.networking.ApiService
import com.example.notetakingapp.networking.models.FolderUpdateRequestModel
import com.example.notetakingapp.networking.models.NoteUpdateRequestModel
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class DataSynchronizer(private val context: Context, private val databaseHelper: DatabaseHelper) {

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
    fun deleteOneNote(id: Long) : Boolean {
        return databaseHelper.deleteOneNote(id)
    }

    fun deleteOneFolder(id: Long) : Boolean {
        return databaseHelper.deleteOneFolder(id = id)
    }

    /**
     * UPDATING
     */
    fun updateNote(id: Long, title: String? = null, content: String? = null, dateModified: String? = null, dateDeleted: String? = null, folderId: Long? = null) {
        val noteUpdateRequest = NoteUpdateRequestModel(id, title, content, dateModified, dateDeleted, folderId)
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
}