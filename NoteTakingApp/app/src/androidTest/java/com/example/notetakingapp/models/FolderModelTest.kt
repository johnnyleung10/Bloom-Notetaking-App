package com.example.notetakingapp.models

import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test

class FolderModelTest : TestCase() {
    @Test
    fun testGetNoteList() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val folder = FolderModel("New Folder 1", appContext)
        Assert.assertEquals(ArrayList<NoteModel>(), folder.noteList)
    }
}