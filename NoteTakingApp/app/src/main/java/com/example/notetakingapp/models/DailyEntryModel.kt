package com.example.notetakingapp.models

import android.content.Context
import android.media.Image
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DailyEntryModel (title : String,
    context: Context, var dailyPrompt : DailyPrompt
) : FileModel(title, context) {
    var promptResponse : String? = ""
    var mood : Mood? = null
    var dailyImage : Image? = null
    var linkedNoteId : Long? = null

    constructor (title: String, context: Context, id: Long, noteId : Long, dailyPrompt : DailyPrompt,
                 promptResponse: String, moodRating : Mood, dailyImage: Image, dateCreated : String,
                 dateModified : String, dateDeleted : String) : this(title, context, dailyPrompt) {
        this.title = title
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

        this.linkedNoteId = noteId
        this.promptResponse = promptResponse
        this.mood = moodRating
        this.dailyImage = dailyImage
    }
}