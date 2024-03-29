package com.example.notetakingapp.models.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.notetakingapp.models.DailyEntryModel
import com.example.notetakingapp.models.DailyPromptModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.utilities.DailyPrompts
import com.example.notetakingapp.utilities.Mood

private const val SQL_CREATE_DAILY_ENTRIES =
    "CREATE TABLE IF NOT EXISTS ${DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.TABLE_NAME} (" +
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
            "FOREIGN KEY ("+DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.COLUMN_NAME_LINKED_NOTE_ID+") REFERENCES "+NoteTakingDatabaseHelper.DatabaseContract.NoteEntry.TABLE_NAME+"("+BaseColumns._ID+"))"

private const val SQL_CREATE_DAILY_PROMPTS =
    "CREATE TABLE IF NOT EXISTS ${DailyEntryDatabaseHelper.DatabaseContract.DailyPrompt.TABLE_NAME} (" +
            "${DailyEntryDatabaseHelper.DatabaseContract.DailyPrompt.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
            "${DailyEntryDatabaseHelper.DatabaseContract.DailyPrompt.COLUMN_NAME_PROMPT} TEXT)"

private const val SQL_DELETE_DAILY_ENTRIES = "DROP TABLE IF EXISTS ${DailyEntryDatabaseHelper.DatabaseContract.DailyEntry.TABLE_NAME}"

class DailyEntryDatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context,
        DATABASE_NAME, null,
        DATABASE_VERSION
    ) {

    // INSERTING
    fun insertDailyEntry(dailyEntry: DailyEntryModel): Long {
        val values = ContentValues().apply {
            put(DailyEntry.COLUMN_NAME_DAILY_PROMPT_ID, dailyEntry.dailyPrompt.id)
            put(DailyEntry.COLUMN_NAME_DAILY_PROMPT_ANSWER, dailyEntry.promptResponse)
            put(DailyEntry.COLUMN_NAME_DAILY_IMAGE, dailyEntry.imageToByteArray())
            put(DailyEntry.COLUMN_NAME_DATE_CREATED, dailyEntry.getDateCreated())
            put(DailyEntry.COLUMN_NAME_DATE_MODIFIED, dailyEntry.getLastModifiedDate())
            put(DailyEntry.COLUMN_NAME_DATE_DELETED, dailyEntry.getDeletionDate())
            put(DailyEntry.COLUMN_NAME_MOOD_ID, dailyEntry.mood?.id ?: 0)
            put(DailyEntry.COLUMN_NAME_LINKED_NOTE_ID, dailyEntry.linkedNoteId)
        }
        val dbWrite = this.writableDatabase
        val id = dbWrite.insert(DailyEntry.TABLE_NAME, null, values)
        dailyEntry.id = id
        dbWrite.close()
        return id
    }

    fun insertDailyPrompt(dailyPrompt: DailyPromptModel): Long {
        val values = ContentValues().apply {
            put(DailyPrompt.COLUMN_NAME_PROMPT, dailyPrompt.prompt)
        }
        val dbWrite = this.writableDatabase
        val id = dbWrite.insert(DailyPrompt.TABLE_NAME, null, values)
        dailyPrompt.id = id
        dbWrite.close()
        return id
    }

    // UPDATING
    fun updateDailyEntry(dailyEntry: DailyEntryModel){

        val dbWrite = this.writableDatabase
        val values = ContentValues().apply {
            put(DailyEntry.COLUMN_NAME_DATE_MODIFIED, dailyEntry.getLastModifiedDate())
            put(DailyEntry.COLUMN_NAME_DATE_DELETED, dailyEntry.getDeletionDate())
            put(DailyEntry.COLUMN_NAME_DAILY_PROMPT_ID, dailyEntry.dailyPrompt.id)
            put(DailyEntry.COLUMN_NAME_DAILY_PROMPT_ANSWER, dailyEntry.promptResponse)
            put(DailyEntry.COLUMN_NAME_DAILY_IMAGE, dailyEntry.imageToByteArray())
            put(DailyEntry.COLUMN_NAME_MOOD_ID, dailyEntry.mood?.id ?: 0)
            put(DailyEntry.COLUMN_NAME_LINKED_NOTE_ID, dailyEntry.linkedNoteId)
        }
        dbWrite.update(
            DailyEntry.TABLE_NAME, values, BaseColumns._ID + " = ?",
            arrayOf(dailyEntry.id.toString()))
        dbWrite.close()
    }

    // QUERYING
    fun getAllDailyEntries() : List<DailyEntryModel>{
        val retList : ArrayList<DailyEntryModel> = arrayListOf()
        val queryString = "SELECT * FROM " + DailyEntry.TABLE_NAME

        val dbRead = this.readableDatabase

        val cursor = dbRead.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(0)
                val dailyPromptId = cursor.getLong(1)
                val dailyPromptAnswer = cursor.getString(2)
                val dailyImage = cursor.getBlob(3)
                val linkedNoteId = cursor.getInt(4)
                val moodId = cursor.getInt(5)
                val dateCreated = cursor.getString(6)
                val dateModified = cursor.getString(7)
                val dateDeleted = cursor.getString(8)

                val dailyPrompt = DailyPrompts[dailyPromptId]
                val mood = Mood.getMood(moodId.toLong()) ?: Mood.NO_SELECTION
                val dailyEntry = DailyEntryModel(id, linkedNoteId.toLong(), dailyPrompt!!,
                    dailyPromptAnswer, mood, dailyImage, dateCreated, dateModified,
                    dateDeleted)
                retList.add(dailyEntry)
            } while (cursor.moveToNext())
        }

        dbRead.close()
        cursor.close()
        return retList
    }

    fun getAllPrompts() : List<DailyPromptModel>{
        val retList : ArrayList<DailyPromptModel> = arrayListOf()
        val queryString = "SELECT * FROM " + DailyPrompt.TABLE_NAME

        val dbRead = this.readableDatabase

        val cursor = dbRead.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(0)
                val promptText = cursor.getString(1)

                val dailyPrompt = DailyPromptModel(id, promptText)
                retList.add(dailyPrompt)
            } while (cursor.moveToNext())
        }

        dbRead.close()
        cursor.close()
        return retList
    }

    // DELETE
    fun deleteDailyEntry(id: Long) : Boolean {
        val queryString = "DELETE FROM " + DailyEntry.TABLE_NAME + " WHERE " + BaseColumns._ID + " = " + id
        val dbWrite = this.writableDatabase

        val cursor = dbWrite.rawQuery(queryString, null)

        dbWrite.close()
        cursor.close()
        return true
    }

    // CLEAR DATABASE
    fun clearDatabase() {
        val dbWrite = this.writableDatabase
        dbWrite.execSQL(SQL_DELETE_DAILY_ENTRIES)
        onCreate(dbWrite)
        dbWrite.close()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_DAILY_ENTRIES)
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

        object DailyPrompt : BaseColumns {
            const val TABLE_NAME = "daily_prompt_table"
            const val COLUMN_NAME_ID = BaseColumns._ID
            const val COLUMN_NAME_PROMPT = "prompt"
        }
    }
}