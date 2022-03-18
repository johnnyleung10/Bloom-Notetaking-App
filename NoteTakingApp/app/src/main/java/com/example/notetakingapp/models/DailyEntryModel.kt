package com.example.notetakingapp.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DailyEntryModel (title : String,
    context: Context, var dailyPromptId : Long?
) : FileModel(title, context) {
    var promptResponse : String? = ""
    var moodId : Long? = null
    var dailyImage : Bitmap? = null
    var linkedNoteId : Long? = null

    constructor (context: Context, id: Long, noteId : Long, dailyPromptId : Long,
                 promptResponse: String, moodId : Long, dailyImage: ByteArray, dateCreated : String,
                 dateModified : String, dateDeleted : String) : this("", context, dailyPromptId) {
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
        this.dailyImage = byteArrayToImage(dailyImage)
    }

    fun imageToByteArray() : ByteArray {
        val stream = ByteArrayOutputStream()
        dailyImage?.compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    }

    private fun byteArrayToImage(data : ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    fun getMonth() : Int {
        return dateCreated.monthValue
    }

    fun getYear() : Int {
        return dateCreated.year
    }

    fun getInt() : Int {
        return dateCreated.dayOfMonth
    }
}