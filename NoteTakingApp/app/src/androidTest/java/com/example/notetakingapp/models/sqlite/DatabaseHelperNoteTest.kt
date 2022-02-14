package com.example.notetakingapp.models.sqlite

import android.content.Context
import android.provider.BaseColumns
import androidx.test.platform.app.InstrumentationRegistry
import com.example.notetakingapp.models.NoteModel
import org.junit.Test
import org.junit.BeforeClass

/**
 * Testing DatabaseHelper with the Note and Folder Model
 */
internal class DatabaseHelperNoteTest {
    companion object {
        lateinit var context : Context
        lateinit var db : DatabaseHelper

        @BeforeClass @JvmStatic
        fun init() {
            context = InstrumentationRegistry.getInstrumentation().targetContext
            db = DatabaseHelper(context)
        }

        @Test
        fun insertNote() {
            val testNote = NoteModel("Test Note", context)
            db.insertNote(testNote)
            val queryString = "SELECT * FROM " +
                    DatabaseHelper.DatabaseContract.NoteEntry.TABLE_NAME +
                    " WHERE " + BaseColumns._ID + " = " + testNote.id
            val dbRead = db.readableDatabase

            val cursor = dbRead.rawQuery(queryString, null)

            val id = cursor.getInt(0)
            val title = cursor.getString(1)
            val content = cursor.getString(2)
            val dateCreated = cursor.getString(3)
            val dateModified = cursor.getString(4)
            val dateDeleted = cursor.getString(5)
            val folderID = cursor.getLong(6)

            val note = NoteModel(title, context, id.toLong(), folderID, content, dateCreated,
                dateModified, dateDeleted)

            assert(note.id == testNote.id)
            assert(note.title == testNote.title)
            assert(note.contents == testNote.contents)
            assert(note.getDateCreated() == testNote.getDateCreated())
            assert(note.getLastModifiedDate() == testNote.getLastModifiedDate())
            assert(note.getDeletionDate() == testNote.getDeletionDate())
            assert(note.folderID == testNote.folderID)
        }

        @Test
        fun insertFolder() {
        }

        @Test
        fun getNumberOfFolders() {
        }

        @Test
        fun getAllFolders() {
        }

        @Test
        fun getAllNotes() {
        }

        @Test
        fun deleteOneNote() {
        }

        @Test
        fun deleteOneFolder() {
        }

        @Test
        fun updateNote() {
        }

        @Test
        fun updateFolder() {
        }

        @Test
        fun onCreate() {
        }

        @Test
        fun onUpgrade() {
        }
    }
}