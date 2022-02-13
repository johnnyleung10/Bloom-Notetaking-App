package com.example.notetakingapp.models.sqlite

import android.app.Application
import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.Context
import android.provider.BaseColumns
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel

private const val SQL_CREATE_NOTE_ENTRIES =
    "CREATE TABLE ${DatabaseHelper.DatabaseContract.NoteEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_TITLE} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_CONTENTS} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_FOLDER_ID} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_DATE_CREATED} DATE," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_DATE_MODIFIED} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_DATE_DELETED} TEXT)"

private const val SQL_CREATE_FOLDER_ENTRIES =
    "CREATE TABLE ${DatabaseHelper.DatabaseContract.FolderEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${DatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_TITLE} TEXT," +
            "${DatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_DATE_CREATED} DATE," +
            "${DatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_DATE_MODIFIED} TEXT," +
            "${DatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_DATE_DELETED} TEXT)"


class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //private val dbWrite = this.writableDatabase
    //private val dbRead = this.readableDatabase

    fun insertNote(note: NoteModel): Long {
        val values = ContentValues().apply {
            put(NoteEntry.COLUMN_NAME_TITLE, note.title)
            put(NoteEntry.COLUMN_NAME_CONTENTS, note.spannableStringToText())
            put(NoteEntry.COLUMN_NAME_DATE_CREATED, note.getDateCreated())
            put(NoteEntry.COLUMN_NAME_DATE_MODIFIED, note.getLastModifiedDate())
            put(NoteEntry.COLUMN_NAME_DATE_DELETED, note.getDeletionDate())
        }
        val dbWrite = this.writableDatabase
        val id = dbWrite.insert(NoteEntry.TABLE_NAME, null, values)
        note.id = id
        dbWrite.close()
        return id
    }

    fun insertFolder(folder: FolderModel): Long {
        val values = ContentValues().apply {
            put(FolderEntry.COLUMN_NAME_TITLE, folder.title)
            put(FolderEntry.COLUMN_NAME_DATE_CREATED, folder.getDateCreated())
            put(FolderEntry.COLUMN_NAME_DATE_MODIFIED, folder.getLastModifiedDate())
            put(FolderEntry.COLUMN_NAME_DATE_DELETED, folder.getDeletionDate())
        }
        val dbWrite = this.writableDatabase
        val id = dbWrite.insert(FolderEntry.TABLE_NAME, null, values)
        folder.id = id
        dbWrite.close()
        return id
    }

    fun getNumberOfFolders(): Int {
        val dbRead = this.readableDatabase
        val queryString = "SELECT COUNT(*) FROM " + FolderEntry.TABLE_NAME
        val cursor = dbRead.rawQuery(queryString, null)
        var retVal = 0

        if (cursor.moveToFirst()) {
            retVal = cursor.getInt(0)
        }
        dbRead.close()
        cursor.close()
        return retVal
    }

    fun getAllFolders(): List<FolderModel> {
        val retList : List<FolderModel> = listOf()
        val queryString = "SELECT * FROM" + FolderEntry.TABLE_NAME
        val dbRead = this.readableDatabase

        val cursor = dbRead.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val dateCreated = cursor.getString(2)
                val dateModified = cursor.getString(3)
                val dateDeleted = cursor.getString(4)

                //val folder = FolderModel()
            } while (cursor.moveToFirst())
        }
        dbRead.close()
        cursor.close()
        return retList
    }

    fun getAllNotes(): List<NoteModel> {
        val retList : List<NoteModel> = listOf()
        val queryString = "SELECT * FROM" + NoteEntry.TABLE_NAME
        val dbRead = this.readableDatabase

        val cursor = dbRead.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val content = cursor.getString(2)
                val folderID = cursor.getLong(3)
                val dateCreated = cursor.getString(4)
                val dateModified = cursor.getString(5)
                val dateDeleted = cursor.getString(6)

                //val folder = FolderModel()
            } while (cursor.moveToFirst())
        }

        dbRead.close()
        cursor.close()
        return retList
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_NOTE_ENTRIES)
        db?.execSQL(SQL_CREATE_FOLDER_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    companion object DatabaseContract {
        const val DATABASE_NAME = "app.db"
        const val DATABASE_VERSION = 1
        private lateinit var application: Application

        object NoteEntry : BaseColumns {
            const val TABLE_NAME = "note_table"
            const val COLUMN_NAME_TITLE = "title"
            const val COLUMN_NAME_CONTENTS = "contents"
            const val COLUMN_NAME_FOLDER_ID = "folder_id"
            const val COLUMN_NAME_DATE_CREATED = "date_created"
            const val COLUMN_NAME_DATE_MODIFIED = "date_modified"
            const val COLUMN_NAME_DATE_DELETED = "date_deleted"
        }

        object FolderEntry : BaseColumns {
            const val TABLE_NAME = "folder_table"
            const val COLUMN_NAME_TITLE = "title"
            const val COLUMN_NAME_DATE_CREATED = "date_created"
            const val COLUMN_NAME_DATE_MODIFIED = "date_modified"
            const val COLUMN_NAME_DATE_DELETED = "date_deleted"
        }
    }

}