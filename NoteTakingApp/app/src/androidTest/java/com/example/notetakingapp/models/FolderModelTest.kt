package com.example.notetakingapp.models

import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test

class FolderModelTest : TestCase() {
    @Test
    fun testGetNoteList() {
        val folder = FolderModel("New Folder 1")
        Assert.assertEquals(ArrayList<NoteModel>(), folder.noteList)
    }
}