package com.example.notetakingapp.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DailyEntryModel (var dailyPrompt: DailyPromptModel)  {

    var id : Long = -1
        internal set
    var linkedNoteId: Long? = null
    var promptResponse: String = ""
    var moodId: Long? = null
    var dailyImage: Bitmap? = null

    private val isoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    var dateCreated : LocalDateTime = LocalDateTime.now()
    var lastModifiedDate : LocalDateTime = dateCreated
    var deletionDate : LocalDateTime? = null

    constructor (id: Long, noteId : Long, dailyPrompt : DailyPromptModel,
                 promptResponse: String, moodId : Long, dailyImage: ByteArray, dateCreated : String,
                 dateModified : String, dateDeleted : String) : this(dailyPrompt=dailyPrompt) {

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

    private fun dateToISO(date : LocalDateTime?) : String {
        if (date == null) return("")
        return date.format(isoFormat)
    }
    fun getDateCreated() : String {return dateToISO(dateCreated)}
    fun getLastModifiedDate() : String {return dateToISO(lastModifiedDate)}
    fun getDeletionDate() : String {return dateToISO(deletionDate)}

    fun updateModifiedDate() {
        lastModifiedDate = LocalDateTime.now()
    }

    /**
     * Update the deletion date to lastsModified
     */
    fun updateDeletionDate() {
        updateModifiedDate()
        deletionDate = lastModifiedDate
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