package com.example.notetakingapp.models.sqlite

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.Context
import android.provider.BaseColumns
import android.text.Html
import android.util.Log
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import java.util.*
import kotlin.collections.ArrayList

private const val SQL_CREATE_NOTE_ENTRIES =
    "CREATE TABLE ${DatabaseHelper.DatabaseContract.NoteEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_TITLE} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_CONTENTS_RICH} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_CONTENTS_PLAIN} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_DATE_CREATED} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_DATE_MODIFIED} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_DATE_DELETED} TEXT," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_FOLDER_ID} INTEGER," +
            "${DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_IS_DIRTY} BOOLEAN," +
            "FOREIGN KEY ("+DatabaseHelper.DatabaseContract.NoteEntry.COLUMN_NAME_FOLDER_ID+") REFERENCES "+DatabaseHelper.DatabaseContract.FolderEntry.TABLE_NAME+"("+BaseColumns._ID+"))"


private const val SQL_CREATE_FOLDER_ENTRIES =
    "CREATE TABLE ${DatabaseHelper.DatabaseContract.FolderEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${DatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_TITLE} TEXT," +
            "${DatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_DATE_CREATED} TEXT," +
            "${DatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_DATE_MODIFIED} TEXT," +
            "${DatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_DATE_DELETED} TEXT," +
            "${DatabaseHelper.DatabaseContract.FolderEntry.COLUMN_NAME_IS_DIRTY} BOOLEAN)"

            private const val SQL_DELETE_NOTE_ENTRIES = "DROP TABLE IF EXISTS ${DatabaseHelper.DatabaseContract.NoteEntry.TABLE_NAME}"
private const val SQL_DELETE_FOLDER_ENTRIES = "DROP TABLE IF EXISTS ${DatabaseHelper.DatabaseContract.FolderEntry.TABLE_NAME}"


class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /**
     * INSERTING
     */
    fun insertNote(note: NoteModel, isDirty:Boolean = false): Long {

        val values = ContentValues().apply {
            put(NoteEntry.COLUMN_NAME_TITLE, note.title)
            put(NoteEntry.COLUMN_NAME_CONTENTS_RICH, note.spannableStringToText())
            put(NoteEntry.COLUMN_NAME_CONTENTS_PLAIN, note.contents.toString())
            put(NoteEntry.COLUMN_NAME_DATE_CREATED, note.getDateCreated())
            put(NoteEntry.COLUMN_NAME_DATE_MODIFIED, note.getLastModifiedDate())
            put(NoteEntry.COLUMN_NAME_DATE_DELETED, note.getDeletionDate())
            put(NoteEntry.COLUMN_NAME_FOLDER_ID, note.folderID)
            put(NoteEntry.COLUMN_NAME_IS_DIRTY, isDirty)
        }
        val dbWrite = this.writableDatabase
        val id = dbWrite.insert(NoteEntry.TABLE_NAME, null, values)
        note.id = id
        dbWrite.close()
        return id
    }

    fun insertFolder(folder: FolderModel, isDirty:Boolean = false): Long {
        val values = ContentValues().apply {
            put(FolderEntry.COLUMN_NAME_TITLE, folder.title)
            put(FolderEntry.COLUMN_NAME_DATE_CREATED, folder.getDateCreated())
            put(FolderEntry.COLUMN_NAME_DATE_MODIFIED, folder.getLastModifiedDate())
            put(FolderEntry.COLUMN_NAME_DATE_DELETED, folder.getDeletionDate())
            put(FolderEntry.COLUMN_NAME_IS_DIRTY, isDirty)
        }
        val dbWrite = this.writableDatabase
        val id = dbWrite.insert(FolderEntry.TABLE_NAME, null, values)
        folder.id = id
        dbWrite.close()
        return id
    }

    /**
     * Returns number of rows from Folder table
     */
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

    // QUERYING
    fun getAllFolders(): List<FolderModel> {
        val retList : ArrayList<FolderModel> = arrayListOf()
        val queryString = "SELECT * FROM " + FolderEntry.TABLE_NAME
        val dbRead = this.readableDatabase

       val cursor = dbRead.rawQuery(queryString, null)

       if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val dateCreated = cursor.getString(2)
                val dateModified = cursor.getString(3)
                val dateDeleted = cursor.getString(4)

                val folder = FolderModel(title, context, id.toLong(), dateCreated, dateModified, dateDeleted)
                retList.add(folder)
            } while (cursor.moveToNext())
       }
        cursor.close()
        dbRead.close()
        return retList
    }

    fun getAllNotes(onlyDirty: Boolean = false): List<NoteModel> {
        val retList : ArrayList<NoteModel> = arrayListOf()
        var queryString = "SELECT * FROM " + NoteEntry.TABLE_NAME
        if (onlyDirty) {
            queryString.plus(" WHERE " + NoteEntry.COLUMN_NAME_IS_DIRTY + "=0")
        }
        val dbRead = this.readableDatabase

        val cursor = dbRead.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val content = cursor.getString(2)
                val dateCreated = cursor.getString(4)
                val dateModified = cursor.getString(5)
                val dateDeleted = cursor.getString(6)
                val folderID = cursor.getLong(7)

                val note = NoteModel(title, context, id.toLong(), folderID, content, dateCreated,
                    dateModified, dateDeleted)
                retList.add(note)
            } while (cursor.moveToNext())
        }

        dbRead.close()
        cursor.close()
        return retList
    }

    fun searchNote(column: String, searchTerm: String, folderId: Long, ): List<Long> {
        val retList : ArrayList<Long> = arrayListOf()
        val queryString = "SELECT * FROM " + NoteEntry.TABLE_NAME + " WHERE folder_Id=" + folderId + " AND " + column + " LIKE '%$searchTerm%'"
        val dbRead = this.readableDatabase

        val cursor = dbRead.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
//                val title = cursor.getString(1)
//                val content = cursor.getString(2)
//                val dateCreated = cursor.getString(3)
//                val dateModified = cursor.getString(4)
//                val dateDeleted = cursor.getString(5)
//                val folderID = cursor.getLong(6)

//                val note = NoteModel(title, context, id.toLong(), folderID, content, dateCreated,
//                    dateModified, dateDeleted)
                retList.add(id.toLong())
            } while (cursor.moveToNext())
        }

        dbRead.close()
        cursor.close()
        return retList
    }

    fun getSortedNotes(columnName: String, folderId: Long, descending: Boolean? = false): List<Long> {
        val retList : ArrayList<Long> = arrayListOf()
        var queryString = "SELECT * FROM " + NoteEntry.TABLE_NAME + " WHERE folder_Id=" + folderId + " ORDER BY " + columnName
        if (descending == true) queryString += " DESC" // Descending
        val dbRead = this.readableDatabase

        val cursor = dbRead.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)

                retList.add(id.toLong())
            } while (cursor.moveToNext())
        }

        dbRead.close()
        cursor.close()
        return retList
    }

    // DELETING
    fun deleteOneNote(id: Long) : Boolean {
        val queryString = "DELETE FROM " + NoteEntry.TABLE_NAME + " WHERE " + BaseColumns._ID + " = " + id
        val dbRead = this.readableDatabase

        val cursor = dbRead.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            dbRead.close()
            cursor.close()
            return true
        }
        dbRead.close()
        cursor.close()
        return false
    }

    fun deleteOneFolder(id: Long) : Boolean {
        val queryString = "DELETE FROM " + FolderEntry.TABLE_NAME + " WHERE " + BaseColumns._ID + " = " + id
        val dbRead = this.readableDatabase

        val cursor = dbRead.rawQuery(queryString, null)

        if (cursor.moveToFirst()) {
            dbRead.close()
            cursor.close()
            return true
        }
        dbRead.close()
        cursor.close()
        return false
    }

    /**
     * UPDATING
     */
    fun updateNote(id: Long, title: String? = null, content: String? = null, dateModified: String? = null, dateDeleted: String? = null, folderId: Int? = null, isDirty:Boolean = false) {

        val dbWrite = this.writableDatabase
        val values = ContentValues().apply {
            title?.let { put(NoteEntry.COLUMN_NAME_TITLE, title) }
            content?.let { put(NoteEntry.COLUMN_NAME_CONTENTS_RICH, content) }
            content?.let { put(NoteEntry.COLUMN_NAME_CONTENTS_PLAIN, Html.fromHtml(content).toString()) }
            dateModified?.let {
                put(
                    NoteEntry.COLUMN_NAME_DATE_MODIFIED,
                    dateModified
                )
            }
            dateDeleted?.let { put(NoteEntry.COLUMN_NAME_DATE_DELETED, dateDeleted) }
            folderId?.let { put(NoteEntry.COLUMN_NAME_FOLDER_ID, folderId) }
            put(NoteEntry.COLUMN_NAME_IS_DIRTY, isDirty)
        }
        dbWrite.update(NoteEntry.TABLE_NAME, values, BaseColumns._ID + " = ?",
            arrayOf(id.toString()))
        dbWrite.close()
    }

    fun updateFolder(id: Long, title: String? = null, dateModified: String? = null, dateDeleted: String? = null, isDirty:Boolean = false) {
        val dbWrite = this.writableDatabase
        val values = ContentValues().apply {
            title?.let { put(FolderEntry.COLUMN_NAME_TITLE, title) }
            dateModified?.let {
                put(
                    FolderEntry.COLUMN_NAME_DATE_MODIFIED,
                    dateModified
                )
            }
            dateDeleted?.let { put(FolderEntry.COLUMN_NAME_DATE_DELETED, dateDeleted) }
            put(FolderEntry.COLUMN_NAME_IS_DIRTY, isDirty)
        }
        dbWrite.update(FolderEntry.TABLE_NAME, values, BaseColumns._ID + " = ?",
            arrayOf(id.toString()))
        dbWrite.close()
    }

    // CLEAR DATABASE
    fun clearDatabase() {
        val dbWrite = this.writableDatabase
        dbWrite.execSQL(SQL_DELETE_NOTE_ENTRIES)
        dbWrite.execSQL(SQL_DELETE_FOLDER_ENTRIES)
        onCreate(dbWrite)
        dbWrite.close()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_NOTE_ENTRIES)
        db?.execSQL(SQL_CREATE_FOLDER_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        clearDatabase()
        onCreate(db)
    }

    companion object DatabaseContract {
        const val DATABASE_NAME = "app.db"
        const val DATABASE_VERSION = 4

        object NoteEntry : BaseColumns {
            const val TABLE_NAME = "note_table"
            const val COLUMN_NAME_ID = BaseColumns._ID
            const val COLUMN_NAME_TITLE = "title"
            const val COLUMN_NAME_CONTENTS_RICH = "contents_rich"
            const val COLUMN_NAME_CONTENTS_PLAIN = "contents_plain"
            const val COLUMN_NAME_FOLDER_ID = "folder_id"
            const val COLUMN_NAME_DATE_CREATED = "date_created"
            const val COLUMN_NAME_DATE_MODIFIED = "date_modified"
            const val COLUMN_NAME_DATE_DELETED = "date_deleted"
            const val COLUMN_NAME_IS_DIRTY = "is_dirty"
        }

        object FolderEntry : BaseColumns {
            const val TABLE_NAME = "folder_table"
            const val COLUMN_NAME_TITLE = "title"
            const val COLUMN_NAME_DATE_CREATED = "date_created"
            const val COLUMN_NAME_DATE_MODIFIED = "date_modified"
            const val COLUMN_NAME_DATE_DELETED = "date_deleted"
            const val COLUMN_NAME_IS_DIRTY = "is_dirty"
        }
    }

}