package com.example.notetakingapp.models

import android.text.SpannableStringBuilder
import java.time.LocalDateTime

abstract class NoteModel(
    var title : String,
    var contents : SpannableStringBuilder,
    var currFolder : String
) {
    var folderID : Int = 0
    var noteID : Int = 0

    val dateCreated : LocalDateTime = LocalDateTime.now()
    var lastModifiedDate : LocalDateTime = dateCreated
    var deletionDate : LocalDateTime? = null

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