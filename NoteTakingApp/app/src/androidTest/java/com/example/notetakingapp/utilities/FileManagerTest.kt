package com.example.notetakingapp.utilities

import androidx.test.platform.app.InstrumentationRegistry
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.sqlite.DatabaseHelper
import org.junit.Assert
import org.junit.Test
import org.testng.annotations.BeforeMethod

internal class FileManagerTest {

    @BeforeMethod(alwaysRun = true)
    fun cleanupManager() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val db = DatabaseHelper(appContext)
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.folderList?.clear()
        db.clearDatabase()
    }

    @Test
    fun getFolderList() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        Assert.assertEquals(HashMap<Long, FolderModel>(), manager?.folderList)
    }

    @Test
    fun initFiles() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()
        Assert.assertEquals(2, manager?.folderList?.size)
        if (manager != null) {
            Assert.assertEquals("Uncategorized", manager.folderList[1]?.title!!)
        }
        if (manager != null) {
            Assert.assertEquals("Recently Deleted", manager.folderList[2]?.title!!)
        }
    }

    @Test
    fun createNewFolder() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()
        manager?.createNewFolder("New Folder 1")
        Assert.assertEquals(3, manager?.folderList?.size)
        if (manager != null) {
            Assert.assertEquals("New Folder 1", manager.folderList[3]?.title!!)
        }
    }

    @Test
    fun editFolder() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()
        manager?.createNewFolder("New Folder 1")
        manager?.editFolder(3, title = "New Folder 3")
        if (manager != null) {
            Assert.assertEquals("New Folder 3", manager.folderList[3]?.title!!)
        }
    }

    @Test
    fun deleteFolder() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()
        manager?.createNewFolder("New Folder 1")
        manager?.deleteFolder(3)
        if (manager != null) {
            Assert.assertEquals(2, manager.folderList.size)
        }
    }


    @Test
    fun fileManagerMultiTest1() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
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
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()

        // Make a note
        if (manager != null) {
            Assert.assertEquals(0, manager.folderList[1]?.noteList?.size)
        }
        manager?.createNewNote("New note")
        if (manager != null) {
            Assert.assertEquals(1, manager.folderList[1]?.noteList?.size)
        }
    }

    @Test
    fun testCreateNewNote() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()

        // Make a note
        if (manager != null) {
            Assert.assertEquals(0, manager.folderList[2]?.noteList?.size)
        }
        manager?.createNewNote("New note", 2)
        if (manager != null) {
            Assert.assertEquals(1, manager.folderList[2]?.noteList?.size)
        }
    }

    @Test
    fun deleteNote() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()

        // Make a note
        val note = manager?.createNewNote("New note", 1)
        if (note != null) {
            manager.deleteNote(note)
        }
        if (manager != null) {
            Assert.assertEquals(1, manager.folderList[2]?.noteList?.size)
        }
    }

    @Test
    fun permanentlyDeleteNote() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()

        // Make a note
        val note = manager?.createNewNote("New note", 2)
        if (note != null) {
            manager.permanentlyDeleteNote(note)
        }
        if (manager != null) {
            Assert.assertEquals(0, manager.folderList[2]?.noteList?.size)
        }
    }

    @Test
    fun moveNote() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()

        // Make a note
        val note = manager?.createNewNote("New note", 2)
        if (note != null) {
            manager.moveNote(note, 1)
        }
        if (manager != null) {
            Assert.assertEquals(1, manager.folderList[1]?.noteList?.size)
        }
    }
}