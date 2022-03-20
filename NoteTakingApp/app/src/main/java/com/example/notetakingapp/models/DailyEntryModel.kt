package com.example.notetakingapp.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DailyEntryModel (title : String, var dailyPromptId : Long?
) : FileModel(title) {
    var promptResponse : String? = ""
    var moodId : Long? = null
    var dailyImage : Bitmap? = null
    var linkedNoteId : Long? = null

    constructor (id: Long, noteId : Long, dailyPromptId : Long,
                 promptResponse: String, moodId : Long, dailyImage: ByteArray, dateCreated : String,
                 dateModified : String, dateDeleted : String) : this("", dailyPromptId) {
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

    /**
     * Converts an image to byteArray for storage
     */
    fun imageToByteArray() : ByteArray {
        val stream = ByteArrayOutputStream()
        dailyImage?.compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    }

    /**
     * Converts byteArray to Image upon construction of model
     */
    private fun byteArrayToImage(data : ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    /**
     * Returns month of creationDate
     */
    fun getMonth() : Int {
        return dateCreated.monthValue
    }

    /**
     * Returns year of creationDate
     */
    fun getYear() : Int {
        return dateCreated.year
    }

    /**
     * Returns day of creationDate
     */
    fun getDay() : Int {
        return dateCreated.dayOfMonth
    }
}