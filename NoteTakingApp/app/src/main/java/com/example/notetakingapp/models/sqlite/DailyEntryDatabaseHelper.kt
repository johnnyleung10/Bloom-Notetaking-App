package com.example.notetakingapp.models.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

private const val SQL_CREATE_DAILY_ENTRIES =
    "CREATE TABLE ${DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.TABLE_NAME} (" +
            "${DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
            "${DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.COLUMN_NAME_DAILY_PROMPT_ID} TEXT," +
            "${DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.COLUMN_NAME_DAILY_PROMPT_ANSWER} TEXT," +
            "${DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.COLUMN_NAME_DAILY_IMAGE} BLOB," +
            "${DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.COLUMN_NAME_LINKED_NOTE_ID} INTEGER," +
            "${DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.COLUMN_NAME_MOOD_ID} INTEGER," +
            "${DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.COLUMN_NAME_DATE_CREATED} TEXT," +
            "${DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.COLUMN_NAME_DATE_MODIFIED} TEXT," +
            "${DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.COLUMN_NAME_DATE_DELETED} TEXT," +
            "FOREIGN KEY ("+DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.COLUMN_NAME_DAILY_PROMPT_ID+") REFERENCES "+DailyEntryDatabaseHelper.DatabaseContract.DailyPrompt.TABLE_NAME+"("+BaseColumns._ID+")," +
            "FOREIGN KEY ("+DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.COLUMN_NAME_LINKED_NOTE_ID+") REFERENCES "+NoteTakingDatabaseHelper.DatabaseContract.NoteEntry.TABLE_NAME+"("+BaseColumns._ID+")," +
            "FOREIGN KEY ("+DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.COLUMN_NAME_MOOD_ID+") REFERENCES "+DailyEntryDatabaseHelper.DatabaseContract.Mood.TABLE_NAME+"("+BaseColumns._ID+"))"

private const val SQL_CREATE_MOODS =
    "CREATE TABLE ${DailyEntryDatabaseHelper.DatabaseContract.Mood.TABLE_NAME} (" +
            "${DailyEntryDatabaseHelper.DatabaseContract.Mood.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
            "${DailyEntryDatabaseHelper.DatabaseContract.Mood.COLUMN_NAME_DESCRIPTION} TEXT," +
            "${DailyEntryDatabaseHelper.DatabaseContract.Mood.COLUMN_NAME_COLOUR} TEXT)"

private const val SQL_CREATE_DAILY_PROMPTS =
    "CREATE TABLE ${DailyEntryDatabaseHelper.DatabaseContract.DailyPrompt.TABLE_NAME} (" +
            "${DailyEntryDatabaseHelper.DatabaseContract.DailyPrompt.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
            "${DailyEntryDatabaseHelper.DatabaseContract.DailyPrompt.COLUMN_NAME_PROMPT} TEXT)"

class DailyEntryDatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_DAILY_ENTRIES)
        db?.execSQL(SQL_CREATE_MOODS)
        db?.execSQL(SQL_CREATE_DAILY_PROMPTS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }

    companion object DatabaseContract {
        const val DATABASE_NAME = "app.db"
        const val DATABASE_VERSION = 4

        object DailyEntry : BaseColumns {
            const val TABLE_NAME = "daily_entry_table"
            const val COLUMN_NAME_ID = BaseColumns._ID
            const val COLUMN_NAME_DATE_CREATED = "date_created"
            const val COLUMN_NAME_DATE_MODIFIED = "date_modified"
            const val COLUMN_NAME_DATE_DELETED = "date_deleted"
            const val COLUMN_NAME_DAILY_PROMPT_ID = "daily_prompt_id"
            const val COLUMN_NAME_DAILY_PROMPT_ANSWER = "daily_prompt_answer"
            const val COLUMN_NAME_DAILY_IMAGE = "daily_image"
            const val COLUMN_NAME_MOOD_ID = "mood_id"
            const val COLUMN_NAME_LINKED_NOTE_ID = "linked_note_id"
        }

        object Mood : BaseColumns {
            const val TABLE_NAME = "mood_table"
            const val COLUMN_NAME_ID = BaseColumns._ID
            const val COLUMN_NAME_DESCRIPTION = "description"
            const val COLUMN_NAME_COLOUR = "colour"
        }

        object DailyPrompt : BaseColumns {
            const val TABLE_NAME = "daily_prompt_table"
            const val COLUMN_NAME_ID = BaseColumns._ID
            const val COLUMN_NAME_PROMPT = "prompt"
        }
    }
}