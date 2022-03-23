package com.example.notetakingapp.models

import android.text.Html
import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class NoteModelTest {

    @Test
    fun getId() {
        val newNote = NoteModel("New Note")
        Assert.assertEquals(-1, newNote.id)
    }

    @Test
    fun getTitle() {
        val newNote = NoteModel("New Note")
        Assert.assertEquals("New Note", newNote.title)
    }

    @Test
    fun setTitle() {
        val newNote = NoteModel("New Note")
        newNote.title = "New Title"
        Assert.assertEquals("New Title", newNote.title)
    }

    @Test
    fun testGetDateCreated() {
        val newNote = NoteModel("New Note")
        val timeNow = LocalDateTime.now()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        Assert.assertEquals(timeNow.format(dateFormat), newNote.getDateCreated().substring(0, 16))
    }

    @Test
    fun testGetLastModifiedDate() {
        val newNote = NoteModel("New Note")
        val timeNow = LocalDateTime.now()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        Assert.assertEquals(timeNow.format(dateFormat), newNote.getLastModifiedDate().substring(0, 16))
    }

    @Test
    fun testGetDeletionDate() {
        val newNote = NoteModel("New Note")
        Assert.assertEquals("", newNote.getDeletionDate())
    }

    @Test
    fun updateDeletionDate() {
        val newNote = NoteModel("New Note")
        newNote.updateDeletionDate()
        val timeNow = LocalDateTime.now()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        Assert.assertEquals(timeNow.format(dateFormat), newNote.getDeletionDate().substring(0, 16))
    }

    @Test
    fun restoreFileDate() {
        val newNote = NoteModel("New Note")
        newNote.updateDeletionDate()
        newNote.restoreFileDate()
        Assert.assertEquals("", newNote.getDeletionDate())
    }

    @Test
    fun getContents() {
        val newNote = NoteModel("New Note")
        Assert.assertEquals("", newNote.contents.toString())
    }

    @Test
    fun setContents() {
        val newNote = NoteModel("New Note")
        newNote.contents = SpannableStringBuilder("Hi there")
        Assert.assertEquals("Hi there", newNote.contents.toString())
    }

    @Test
    fun getCurrFolder() {
        val newNote = NoteModel("New Note")
        Assert.assertEquals("", newNote.currFolder)
    }

    @Test
    fun setCurrFolder() {
        val newNote = NoteModel("New Note")
        newNote.currFolder = "Uncategorized"
        Assert.assertEquals("Uncategorized", newNote.currFolder)
    }

    @Test
    fun spannableStringToText() {
        val newNote = NoteModel("New Note")
        newNote.contents = SpannableStringBuilder("Hi there")
        var htmlContent = newNote.spannableStringToText()
        Assert.assertEquals("<p dir=\"ltr\">Hi there</p>\n", htmlContent)
        var textContent = Html.fromHtml(htmlContent)
        Assert.assertEquals("Hi there", textContent.subSequence(0, textContent.length - 2).toString())

        // Add bolding
        newNote.contents.bold { append(" bold test") }
        htmlContent = newNote.spannableStringToText()
        Assert.assertEquals("<p dir=\"ltr\">Hi there<b> bold test</b></p>\n", htmlContent)
    }
}