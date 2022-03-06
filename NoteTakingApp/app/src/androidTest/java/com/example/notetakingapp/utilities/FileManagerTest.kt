package com.example.notetakingapp.utilities

import android.text.SpannableStringBuilder
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
        manager?.allNotes?.clear()
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
            Assert.assertEquals("Recently Deleted", manager.folderList[2]?.title!!)
        }
    }

    @Test
    fun initFiles2() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()
        manager?.createNewNote("New note", 2)
        manager?.createNewNote("New note", 1)
        manager?.createNewNote("New note", 1)

        manager?.folderList?.clear()
        manager?.allNotes?.clear()

        manager?.initFiles()
        if (manager != null) {
            Assert.assertEquals(2, manager.folderList[1]?.noteList?.size)
            Assert.assertEquals(1, manager.folderList[2]?.noteList?.size)
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
            manager.deleteNote(note.id)
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
            manager.permanentlyDeleteNote(note.id)
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
            manager.moveNote(note.id, 1)
        }
        if (manager != null) {
            Assert.assertEquals(1, manager.folderList[1]?.noteList?.size)
        }
    }

    @Test
    fun editNote() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()

        // Make a note
        val note = manager?.createNewNote("New note", 2)
        if (note != null) {
            manager.editNote(note.id, title = "Note 2", contents = SpannableStringBuilder("Stuff"))
        }
    }

    @Test
    fun restoreNote() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()

        // Make a note
        val note = manager?.createNewNote("New note", 1)
        if (note != null) {
            // Delete and restore
            manager.deleteNote(note.id)
            manager.restoreNote(note.id)
            Assert.assertEquals("", note.getDeletionDate())
        }
        if (manager != null) {
            Assert.assertEquals(1, manager.folderList[1]?.noteList?.size)
        }

        // Try in a new folder
        manager?.createNewFolder("New Folder 1")
        val note1 = manager?.createNewNote("New note 1", 3)
        if (note1 != null) {
            // Delete and restore
            manager.deleteNote(note1.id)
            manager.restoreNote(note1.id)
            Assert.assertEquals("", note1.getDeletionDate())
        }
        if (manager != null) {
            Assert.assertEquals(1, manager.folderList[3]?.noteList?.size)
        }

    }
}