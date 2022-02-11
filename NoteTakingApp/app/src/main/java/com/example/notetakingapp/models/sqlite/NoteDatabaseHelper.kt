package com.example.notetakingapp.models.sqlite

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.Context
import android.provider.BaseColumns

class NoteDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION) {

    private val SQL_CREATE_NOTE_ENTRIES =
        "CREATE TABLE ${DatabaseContract.NoteEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${DatabaseContract.NoteEntry.COLUMN_NAME_TITLE} TEXT," +
                "${DatabaseContract.NoteEntry.COLUMN_NAME_CONTENTS} TEXT," +
                "${DatabaseContract.NoteEntry.COLUMN_NAME_CURR_FOLDER} TEXT)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_NOTE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    object DatabaseContract {
        const val DATABASE_NAME = "app.db"
        const val DATABASE_VERSION = 1
        object NoteEntry : BaseColumns {
            const val TABLE_NAME = "note_table"
            const val COLUMN_NAME_TITLE = "title"
            const val COLUMN_NAME_CONTENTS = "contents"
            const val COLUMN_NAME_CURR_FOLDER = "curr_folder"
        }
    }

}