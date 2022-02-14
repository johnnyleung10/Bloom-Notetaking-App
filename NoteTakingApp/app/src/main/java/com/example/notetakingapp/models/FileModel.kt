package com.example.notetakingapp.models

import android.content.Context
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class FileModel(
    var title : String,
    val context: Context,
) {
    var id : Long = -1
        internal set

    // Dates
    private val isoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    protected var dateCreated : LocalDateTime = LocalDateTime.now()
    protected var lastModifiedDate : LocalDateTime = dateCreated
    protected var deletionDate : LocalDateTime? = null

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
     * Delete note, sends to recently deleted folder
     */
    fun updateDeletionDate() {
        deletionDate = lastModifiedDate
    }

    /**
     * Restore note from recently deleted
     */
    fun restoreFile() {
        lastModifiedDate = LocalDateTime.now()
        deletionDate = null
    }
}