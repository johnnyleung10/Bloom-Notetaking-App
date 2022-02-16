package com.example.notetakingapp.utilities

import androidx.test.platform.app.InstrumentationRegistry
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.sqlite.DatabaseHelper
import org.junit.Assert
import org.junit.Test

internal class FileManagerTest {
    /*
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
        Assert.assertEquals("Uncategorized", manager.folderList[1]?.title!!)
        Assert.assertEquals("Recently Deleted", manager.folderList[2]?.title!!)
    }

    @Test
    fun createNewFolder() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager = FileManager(appContext)
        manager.initFiles()
        manager.createNewFolder("New Folder 1")
        Assert.assertEquals(3, manager.folderList.size)
        Assert.assertEquals("New Folder 1", manager.folderList[3]?.title!!)
    }

    @Test
    fun editFolder() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager = FileManager(appContext)
        manager.initFiles()
        manager.createNewFolder("New Folder 1")
        manager.editFolder(3, title = "New Folder 3")
        Assert.assertEquals("New Folder 3", manager.folderList[3]?.title!!)
    }

    @Test
    fun deleteFolder() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager = FileManager(appContext)
        manager.initFiles()
        manager.createNewFolder("New Folder 1")
        manager.deleteFolder(3)
        Assert.assertEquals("New Folder 2", manager.folderList.size)
    }
    */

    @Test
    fun fileManagerTesting() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        //val db = DatabaseHelper(appContext)
        //db.clearDatabase()
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        if (manager != null) {
            Assert.assertEquals(HashMap<Long, FolderModel>(), manager.folderList)
        }
        manager?.initFiles()
        Assert.assertEquals(2, manager?.folderList?.size)
        if (manager != null) {
            Assert.assertEquals("Uncategorized", manager.folderList[1]?.title!!)
            Assert.assertEquals("Recently Deleted", manager.folderList[2]?.title!!)
        }
        manager?.createNewFolder("New Folder 1")
        if (manager != null) {
            Assert.assertEquals(3, manager.folderList.size)
            Assert.assertEquals("New Folder 1", manager.folderList[3]?.title!!)
        }
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