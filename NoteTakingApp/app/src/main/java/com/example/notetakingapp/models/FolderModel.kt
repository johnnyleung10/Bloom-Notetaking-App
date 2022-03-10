package com.example.notetakingapp.models

import android.content.Context
import com.example.notetakingapp.networking.models.FolderCreationRequestModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FolderModel(
    title: String,
    id: Long,
    context: Context
) : FileModel(title, id, context) {

    // ID = 1 for uncategorized, ID = 2 for recently deleted

    val noteList = ArrayList<NoteModel>()

    constructor(title: String, context: Context, id: Long, dateCreated : String,
                dateModified : String, dateDeleted : String) : this(title, id, context) {
        this.id = id

        // Handle dates
        val isoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val dateCreatedLCT = LocalDateTime.parse(dateCreated, isoFormat)
        this.dateCreated = dateCreatedLCT
        val dateModifiedLCT = LocalDateTime.parse(dateModified, isoFormat)
        this.lastModifiedDate = dateModifiedLCT
        if (dateDeleted != "") {
            val dateDeletedLCT = LocalDateTime.parse(dateDeleted, isoFormat)
            this.deletionDate = dateDeletedLCT
        }
    }

    fun toFolderCreationRequestModel(): FolderCreationRequestModel {
        return FolderCreationRequestModel(id, title, getDateCreated(), getLastModifiedDate(), getDeletionDate())
    }
}