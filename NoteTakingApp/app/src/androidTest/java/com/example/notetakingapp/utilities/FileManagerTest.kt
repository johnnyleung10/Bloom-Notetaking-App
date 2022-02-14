package com.example.notetakingapp.utilities

import androidx.test.platform.app.InstrumentationRegistry
import com.example.notetakingapp.models.FolderModel
import org.junit.Assert
import org.junit.Test

internal class FileManagerTest {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun getFolderList() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager = FileManager(appContext)
        Assert.assertEquals(HashMap<Long, FolderModel>(), manager.folderList)
    }

    @Test
    fun initFiles() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager = FileManager(appContext)
        manager.initFiles()
        Assert.assertEquals(2, manager.folderList.size)
    }

    @Test
    fun createNewFolder() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager = FileManager(appContext)
        //manager.createNewFolder()
        //Assert.assertEquals(2, manager.folderList.size)
    }

    @Test
    fun editFolder() {
    }

    @Test
    fun deleteFolder() {
    }

    @Test
    fun createNewNote() {
    }

    @Test
    fun testCreateNewNote() {
    }

    @Test
    fun deleteNote() {
    }

    @Test
    fun permanentlyDeleteNote() {
    }

    @Test
    fun moveNote() {
    }

    @Test
    fun getContext() {
    }
}