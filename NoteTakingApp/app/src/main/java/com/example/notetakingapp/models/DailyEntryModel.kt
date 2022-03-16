package com.example.notetakingapp.models

import android.content.Context
import android.media.Image
import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DailyEntryModel (title : String,
    context: Context, var dailyPromptId : Long?
) : FileModel(title, context) {
    var promptResponse : String? = ""
    var moodId : Long? = null
    var dailyImage : Image? = null
    var linkedNoteId : Long? = null

    constructor (title: String, context: Context, id: Long, noteId : Long, dailyPromptId : Long,
                 promptResponse: String, moodId : Long, dailyImage: Image, dateCreated : String,
                 dateModified : String, dateDeleted : String) : this(title, context, dailyPromptId) {
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
        this.moodId = moodId
        this.dailyImage = dailyImage
    }

    fun imageToByteArray() : ByteArray {
        val buffer: ByteBuffer = dailyImage!!.getPlanes()[0].getBuffer()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        return bytes
    }

    fun getDate() : String {
        val isoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return dateCreated.format(isoFormat)
    }
}