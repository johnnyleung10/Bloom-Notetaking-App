package com.example.notetakingapp.models.sqlite

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.Context
import android.provider.BaseColumns
import com.example.notetakingapp.models.NoteFile

private const val SQL_CREATE_NOTE_ENTRIES =
    "CREATE TABLE ${DatabaseHelper.DatabaseContract.NoteEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_TITLE} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_CONTENTS} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_CURR_FOLDER} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_DATE_CREATED} DATE," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_DATE_MODIFIED} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_DATE_DELETED} TEXT)"

private const val SQL_CREATE_FOLDER_ENTRIES =
    "CREATE TABLE ${DatabaseHelper.DatabaseContract.FolderEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${DatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_TITLE} TEXT," +
            "${DatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_DATE_CREATED} TEXT," +
            "${DatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_DATE_MODIFIED} TEXT," +
            "${DatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_DATE_DELETED} TEXT)"

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    public fun insertNote(db: SQLiteDatabase, note: NoteFile): Long {
        val values = ContentValues().apply {
            put(NoteEntry.COLUMN_NAME_TITLE, note.title)
            put(NoteEntry.COLUMN_NAME_CONTENTS, note.spannableStringToText())
            put(NoteEntry.COLUMN_NAME_DATE_CREATED, note.getDateCreated())
            put(NoteEntry.COLUMN_NAME_DATE_MODIFIED, note.getLastModifiedDate())
            put(NoteEntry.COLUMN_NAME_DATE_DELETED, note.getDeletionDate())
        }
        return db.insert(NoteEntry.TABLE_NAME, null, values)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_NOTE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    companion object DatabaseContract {
        const val DATABASE_NAME = "app.db"
        const val DATABASE_VERSION = 1
        object NoteEntry : BaseColumns {
            const val TABLE_NAME = "note_table"
            const val COLUMN_NAME_TITLE = "title"
            const val COLUMN_NAME_CONTENTS = "contents"
            const val COLUMN_NAME_CURR_FOLDER = "curr_folder"
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