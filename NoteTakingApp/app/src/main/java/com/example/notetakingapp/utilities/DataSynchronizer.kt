package com.example.notetakingapp.utilities

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.text.SpannableStringBuilder
import android.util.Log
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.models.sqlite.DatabaseHelper
import com.example.notetakingapp.networking.ApiService
import com.example.notetakingapp.networking.models.NoteRequestModel
import kotlinx.coroutines.runBlocking

class DataSynchronizer(private val context: Context, private val databaseHelper: DatabaseHelper) {

    private val apiService: ApiService = ApiService.create()

    /**
     * INSERTING
     */
    fun insertNote(note: NoteModel): Long {
        val noteRequest = NoteRequestModel(note)
        runBlocking {
            val success = apiService.insertNote(noteRequest)
        }
        // TODO: insert dirty if necessary

        return databaseHelper.insertNote(note)
    }

    fun insertFolder(folder: FolderModel): Long {
        return databaseHelper.insertFolder(folder)
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
    fun updateNote(id: Long, title: String? = null, content: String? = null, dateModified: String? = null, dateDeleted: String? = null, folderId: Int? = null) {
        databaseHelper.updateNote(id, title = title, content = content, dateModified = dateModified)
    }

    fun updateFolder(id: Long, title: String? = null, dateModified: String? = null, dateDeleted: String? = null) {
        databaseHelper.updateFolder(id, title = title, dateModified = dateModified)
    }
}