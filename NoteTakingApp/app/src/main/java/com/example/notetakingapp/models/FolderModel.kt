package com.example.notetakingapp.models

import android.content.Context
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FolderModel(
    title: String,
    context: Context
) : FileModel(title, context) {

    var contains = ArrayList<NoteModel>()

    constructor(title: String, context: Context, id: Long, dateCreated : String,
                dateModified : String, dateDeleted : String) : this(title, context) {
        this.id = id

        // Handle dates
        val isoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val dateCreatedLCT = LocalDateTime.parse(dateCreated, isoFormat)
        this.dateCreated = dateCreatedLCT
        val dateModifiedLCT = LocalDateTime.parse(dateModified, isoFormat)
        this.lastModifiedDate = dateModifiedLCT
        val dateDeletedLCT = LocalDateTime.parse(dateDeleted, isoFormat)
        this.deletionDate = dateDeletedLCT
    }
}