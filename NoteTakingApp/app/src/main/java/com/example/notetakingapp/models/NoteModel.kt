package com.example.notetakingapp.models

import android.content.Context
import android.text.SpannableStringBuilder
import androidx.core.text.toHtml
import com.example.notetakingapp.models.sqlite.DatabaseHelper

class NoteModel(
    title : String,
    context: Context
) : FileModel(title, context) {

    var contents : SpannableStringBuilder = SpannableStringBuilder("")
    var currFolder : String = ""

    /**
     * On note creation, sync with SQLite
     */

    /**
     * Converts spannableString to HTML for storage
     */
    fun spannableStringToText(): String {
        return contents.toHtml()
    }

    /**
     * Delete note, sends to recently deleted folder
     */
    fun deleteNote() {
        currFolder = "Recently Deleted"
        deleteFile()
    }

    /**
     * Restore note from recently deleted
     */
    fun restoreNote() {
        currFolder = "Recently Deleted"  // Bring back to original folder
        deleteFile()
    }
}