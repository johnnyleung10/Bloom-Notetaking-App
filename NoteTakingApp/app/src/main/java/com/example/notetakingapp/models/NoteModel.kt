package com.example.notetakingapp.models

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.format.DateFormat.format
import androidx.core.text.toHtml
import com.example.notetakingapp.models.sqlite.DatabaseHelper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class NoteModel(
    var title : String,
    var contents : SpannableStringBuilder,
    var currFolder : String,
    var context: Context
) {
    var folderID : Int = -1
    var noteID : Long = -1

    val isoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    val dateCreated : LocalDateTime = LocalDateTime.now()
    var lastModifiedDate : LocalDateTime = dateCreated
    var deletionDate : LocalDateTime? = null

    /**
     * On note creation, sync with SQLite
     */

    /**
     * Converts spannableString to HTML for storage
     */
    fun spannableStringToText(): String {
        return contents.toHtml()
    }

    private fun dateToISO(date : LocalDateTime?) : String {
        if (date == null) return("")
        return date.format(isoFormat)
    }
    fun getDateCreated() : String {return dateToISO(dateCreated)}
    fun getLastModifiedDate() : String {return dateToISO(lastModifiedDate)}
    fun getDeletionDate() : String {return dateToISO(deletionDate)}

    /**
     * Delete note, sends to recently deleted folder
     */
    fun deleteNote() {
        lastModifiedDate = LocalDateTime.now()
        deletionDate = lastModifiedDate
        currFolder = "Recently Deleted"
        folderID = -1
    }

    /**
     * Restore note from recently deleted
     */
    fun restoreNote() {
        lastModifiedDate = LocalDateTime.now()
        deletionDate = null
        currFolder = "Recently Deleted"  // Bring back to original folder
        folderID = -1                    // Bring back to original folder
    }
}