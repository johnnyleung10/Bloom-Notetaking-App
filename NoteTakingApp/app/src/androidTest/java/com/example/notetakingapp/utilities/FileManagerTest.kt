package com.example.notetakingapp.utilities

import android.text.SpannableStringBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.sqlite.DatabaseHelper
import org.junit.Assert
import org.junit.Test

internal class FileManagerTest {

    private fun cleanupManager() {
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
        val id = note?.id

        // Reset
        manager?.folderList?.clear()
        manager?.allNotes?.clear()

        manager?.initFiles()
        if (manager != null) {
            val testNote = manager.getNote(id!!)
            Assert.assertEquals("Note 2", testNote?.title)
            //Assert.assertEquals(SpannableStringBuilder("Stuff"), testNote?.contents)
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

    @Test
    fun restoreNote2() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()

        // Delete a folder and try restoring the note
        val folder2 = manager?.createNewFolder("New Folder 2")
        val note2 = manager?.createNewNote("New note 1", folder2!!.id)

        if (folder2 != null) {
            manager.deleteFolder(folder2.id) // Delete folder2
            Assert.assertEquals(1, manager.folderList[2]?.noteList?.size)
            if (note2 != null) {
                manager.restoreNote(note2.id)
            }
            Assert.assertEquals("", note2?.getDeletionDate())
            Assert.assertEquals(false, manager.folderList.containsKey(folder2.id))
            Assert.assertEquals(1, manager.folderList[1]?.noteList?.size)
        }
    }


    @Test
    fun sortNotes() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()

        // Make notes
        val note1 = manager?.createNewNote("B Note", 1)
        val note2 = manager?.createNewNote("A Note", 1)
        val note3 = manager?.createNewNote("C Note", 1)

        // Test sort by title
        manager?.sortNotes(DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_TITLE, 1) // ASC
        if (manager != null) {
            Assert.assertEquals(note2?.title, manager.folderList[1]?.noteList?.get(0)?.title)
            Assert.assertEquals(note1?.title, manager.folderList[1]?.noteList?.get(1)?.title)
            Assert.assertEquals(note3?.title, manager.folderList[1]?.noteList?.get(2)?.title)
        }
        manager?.sortNotes(DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_TITLE, 1, descending = true) // DESC
        if (manager != null) {
            Assert.assertEquals(note3?.title, manager.folderList[1]?.noteList?.get(0)?.title)
        }

        // Test sort by createdDate
        manager?.sortNotes(DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_DATE_CREATED, 1) // ASC
        if (manager != null) {
            Assert.assertEquals(note1?.title, manager.folderList[1]?.noteList?.get(0)?.title)
        }
        manager?.sortNotes(DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_DATE_CREATED, 1, descending = true) // DESC
        if (manager != null) {
            Assert.assertEquals(note3?.title, manager.folderList[1]?.noteList?.get(0)?.title)
            Assert.assertEquals(note2?.title, manager.folderList[1]?.noteList?.get(1)?.title)
            Assert.assertEquals(note1?.title, manager.folderList[1]?.noteList?.get(2)?.title)
        }
    }

    @Test
    fun searchNotes() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()

        // Make notes
        val note1 = manager?.createNewNote("B Note", 1)
        val note2 = manager?.createNewNote("A Note", 1)
        val note3 = manager?.createNewNote("C Note", 1)

        // Search
        var resultList = manager?.searchNotes("A",1)
        if (manager != null) {
            Assert.assertEquals(note2?.id, resultList?.get(0))
        }
        resultList = manager?.searchNotes("B",1)
        if (manager != null) {
            Assert.assertEquals(note1?.id, resultList?.get(0))
        }
        resultList = manager?.searchNotes("C",1)
        if (manager != null) {
            Assert.assertEquals(note3?.id, resultList?.get(0))
        }
        resultList = manager?.searchNotes("Note",1)
        if (manager != null) {
            Assert.assertEquals(3, resultList?.size)
        }
    }

    @Test
    fun searchFolders() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()

        // Make folders
        val folder1 = manager?.createNewFolder("B Folder")
        val folder2 = manager?.createNewFolder("A Folder")
        val folder3 = manager?.createNewFolder("C Folder")

        // Search
        var resultList = manager?.searchFolders("A")
        if (manager != null) {
            Assert.assertEquals(2, resultList?.size)
        }
        resultList = manager?.searchFolders("B")
        if (manager != null) {
            Assert.assertEquals(folder1?.id, resultList?.get(0))
        }
        resultList = manager?.searchFolders("C")
        if (manager != null) {
            Assert.assertEquals(3, resultList?.size)
        }
        resultList = manager?.searchFolders("Folder")
        if (manager != null) {
            Assert.assertEquals(3, resultList?.size)
        }
    }

}