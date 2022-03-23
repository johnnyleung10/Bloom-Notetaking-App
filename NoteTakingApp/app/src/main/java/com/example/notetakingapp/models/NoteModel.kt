package com.example.notetakingapp.models

import android.content.Context
import android.text.Html
import android.text.SpannableStringBuilder
import androidx.core.text.toHtml
import com.example.notetakingapp.networking.models.NoteCreationRequestModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteModel(
    title : String,
) : FileModel(title) {

    var contents : SpannableStringBuilder = SpannableStringBuilder("")
    var currFolder : String = ""
    var folderID : Long = 1

    constructor(title: String, id: Long, folderID : Long, contents : String, dateCreated : String,
                dateModified : String, dateDeleted : String) : this(title) {
        this.id = id
        this.folderID = folderID

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

        if (contents.length >= 2) {
            val textContent = Html.fromHtml(contents)
            // Conversion to HTML adds additional tab characters at the end which need to be removed
            this.contents =
                SpannableStringBuilder(textContent.subSequence(0, textContent.length - 2))
        }
    }

    /**
     * Converts spannableString to HTML for storage
     */
    fun spannableStringToText(): String {
        return contents.toHtml()
    }

    fun toNoteCreationRequestModel(): NoteCreationRequestModel{
        return NoteCreationRequestModel(id, title, spannableStringToText(), contents.toString(), getDateCreated(), getLastModifiedDate(), getDeletionDate(), folderID)
    }

}
