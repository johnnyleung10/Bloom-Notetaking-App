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
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newNote = NoteModel("New Note", appContext)
        Assert.assertEquals(-1, newNote.id)
    }

    @Test
    fun getTitle() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newNote = NoteModel("New Note", appContext)
        Assert.assertEquals("New Note", newNote.title)
    }

    @Test
    fun setTitle() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newNote = NoteModel("New Note", appContext)
        newNote.title = "New Title"
        Assert.assertEquals("New Title", newNote.title)
    }

    @Test
    fun testGetDateCreated() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val newNote = NoteModel("New Note", appContext)
        val timeNow = LocalDateTime.now()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        Assert.assertEquals(timeNow.format(dateFormat), newNote.getDateCreated().substring(0, 16))
    }

    @Test
    fun testGetLastModifiedDate() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val newNote = NoteModel("New Note", appContext)
        val timeNow = LocalDateTime.now()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        Assert.assertEquals(timeNow.format(dateFormat), newNote.getLastModifiedDate().substring(0, 16))
    }

    @Test
    fun testGetDeletionDate() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newNote = NoteModel("New Note", appContext)
        Assert.assertEquals("", newNote.getDeletionDate())
    }

    @Test
    fun updateDeletionDate() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val newNote = NoteModel("New Note", appContext)
        newNote.updateDeletionDate()
        val timeNow = LocalDateTime.now()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        Assert.assertEquals(timeNow.format(dateFormat), newNote.getDeletionDate().substring(0, 16))
    }

    @Test
    fun restoreFileDate() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val newNote = NoteModel("New Note", appContext)
        newNote.updateDeletionDate()
        newNote.restoreFileDate()
        Assert.assertEquals("", newNote.getDeletionDate())
    }

    @Test
    fun getContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.notetakingapp", appContext.packageName)
    }

    @Test
    fun getContents() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newNote = NoteModel("New Note", appContext)
        Assert.assertEquals("", newNote.contents.toString())
    }

    @Test
    fun setContents() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newNote = NoteModel("New Note", appContext)
        newNote.contents = SpannableStringBuilder("Hi there")
        Assert.assertEquals("Hi there", newNote.contents.toString())
    }

    @Test
    fun getCurrFolder() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val newNote = NoteModel("New Note", appContext)
        Assert.assertEquals("", newNote.currFolder)
    }

    @Test
    fun setCurrFolder() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val newNote = NoteModel("New Note", appContext)
        newNote.currFolder = "Uncategorized"
        Assert.assertEquals("Uncategorized", newNote.currFolder)
    }

    @Test
    fun spannableStringToText() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newNote = NoteModel("New Note", appContext)
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