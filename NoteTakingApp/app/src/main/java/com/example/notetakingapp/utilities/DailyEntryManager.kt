package com.example.notetakingapp.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.notetakingapp.R
import com.example.notetakingapp.models.*
import com.example.notetakingapp.models.sqlite.DailyEntryDatabaseHelper
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

enum class Mood(val id: Long, val description : String, val colour : Int) {
    NO_SELECTION(0, "No Mood", R.color.no_selection),
    HAPPY(1, "Feeling happy", R.color.happy),
    LOVING(2, "Feeling loving", R.color.loving),
    EXCITED(3, "Feeling excited", R.color.excited),
    NEUTRAL(4, "Feeling neutral", R.color.neutral),
    SAD(5, "Feeling sad", R.color.sad),
    ANGRY(6, "Feeling angry", R.color.angry),
    DOUBTFUL(7, "Feeling doubtful", R.color.doubtful);

    companion object {
        private val allValues = values()
        fun getMood(id: Long): Mood? = allValues.find { it.id == id }
        fun getColour(id: Long): Int? = allValues.find { it.id == id }?.colour
    }
}

/**
 * Global hashmap containing all dailyPrompts
 */
val DailyPrompts : HashMap<Long, DailyPromptModel> = hashMapOf(
    0.toLong() to DailyPromptModel(0, "How are you feeling today?"),
    1.toLong() to DailyPromptModel(1, "What reminds you of home?"))

class DailyEntryManager {
    private lateinit var context : Context
    lateinit var dailyEntryDatabaseHelper : DailyEntryDatabaseHelper
    lateinit var dailyEntryDataSynchronizer: DailyEntryDataSynchronizer

    val dailyEntryMap = HashMap<Long, DailyEntryModel>()

    fun initManager(context: Context) {
        this.context = context
        dailyEntryDatabaseHelper = DailyEntryDatabaseHelper(context)
        dailyEntryDatabaseHelper.onCreate(dailyEntryDatabaseHelper.writableDatabase)
        dailyEntryDataSynchronizer = DailyEntryDataSynchronizer(dailyEntryDatabaseHelper)
    }

    fun initEntries() {
        initDailyEntries()
    }

    /**
     * Gets all DailyEntries from database and stores it in dailyEntryMap
     */
    private fun initDailyEntries() {
        for (entry in dailyEntryDatabaseHelper.getAllDailyEntries()) {
            dailyEntryMap[entry.id] = entry
        }
    }

    /**
     * Returns a random daily prompt for today
     */
    private fun getDailyPrompt() : DailyPromptModel {
        val random = Random()
        return DailyPrompts.entries.elementAt(random.nextInt(DailyPrompts.size)).value // get a random prompt
    }

    /**
     * Returns today's daily entry. If no entry exists one will be created.
     */
    fun getDailyEntryToday(): DailyEntryModel{
        val today = LocalDateTime.now()
        for (entry in dailyEntryMap.values) {
            if (entry.getMonth() == today.monthValue && entry.getYear() == today.year &&
                entry.getDay() == today.dayOfMonth) {
                return entry
            }
        }
        return createDailyEntry()
    }

    /**
     * Returns list of daily entries in a given month and year
     *
     * @param month month value
     * @param year year value
     */
    fun getDailyEntriesByDate(month : Int, year: Int) : HashMap<Int, DailyEntryModel> {
        val retEntries = HashMap<Int, DailyEntryModel>()
        for (entry in dailyEntryMap.values) {
            if (entry.getMonth() == month && entry.getYear() == year) {
                retEntries[entry.getDay()] = entry
            }
        }
        return retEntries
    }

    /**
     * Returns list of daily entries in a given month, day and year
     *
     * @param month month value
     * @param day day value
     * @param year year value
     */
    fun getDailyEntryByDate(month : Int, day : Int, year: Int) : DailyEntryModel? {
        for (entry in dailyEntryMap.values) {
            if (entry.getMonth() == month && entry.getYear() == year && entry.getDay() == day) {
                return entry
            }
        }
        return null
    }

    /**
     * Creates a new daily entry with a random prompt. If one already exists for today, it will be returned
     */
    fun createDailyEntry(): DailyEntryModel {
        // Check if a daily entry already exists
        val today = LocalDateTime.now()
        var dailyEntry = getDailyEntryByDate(today.monthValue, today.dayOfMonth, today.year)
        if (dailyEntry != null) return dailyEntry

        // If not create a new one
        dailyEntry = DailyEntryModel(dailyPrompt = getDailyPrompt())
        dailyEntry.linkedNoteId = null

        dailyEntryDataSynchronizer.insertDailyEntry(dailyEntry)
        dailyEntryMap[dailyEntry.id] = dailyEntry
        return dailyEntry
    }

    /**
     * Creates a note and links it to the given dailyEntry
     */
    fun createLinkedNote(dailyEntry: DailyEntryModel): Long? {
        val fileManager = FileManager.instance
        fileManager?.initManager(context)
        val newNote = fileManager?.createNewNote("Daily Entry " +dailyEntry.getMonth() +"-"
                +dailyEntry.getDay() +"-" +dailyEntry.getYear(), 3)
        dailyEntry.linkedNoteId = newNote?.id
        updateDailyEntry(dailyEntry)

        return dailyEntry.linkedNoteId
    }

    /**
     * Returns daily entry by Id
     */
    fun getDailyEntryById(entryId : Long) : DailyEntryModel? {
        return dailyEntryMap[entryId]
    }

    /**
     * Updates daily entry data
     */
    fun updateDailyEntry(dailyEntry: DailyEntryModel) {

        dailyEntry.updateModifiedDate()

        // Update in database
        compressAndSyncData(dailyEntry)
    }

    // Compresses dailyImage so that it can be pushed to database
    private fun compressAndSyncData(dailyEntry: DailyEntryModel): Deferred<Int> = GlobalScope.async {

        var imgByte = getDailyEntryToday().imageToByteArray()
        var resized = getDailyEntryToday().dailyImage
        // COMPRESS
        while (imgByte.size > 500000) {
            val bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size)
            resized = Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * 0.8).toInt(), (bitmap.height * 0.8).toInt(), true
            )
            val stream = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream)
            imgByte = stream.toByteArray()
        }
        getDailyEntryToday().dailyImage = resized

        dailyEntryDatabaseHelper.updateDailyEntry(dailyEntry)
        dailyEntryDataSynchronizer.updateDailyEntry(dailyEntry)

        return@async 42
    }

    /**
     * Delete daily entry from database and from local storage
     */
    fun deleteDailyEntry(entryId: Long) : Boolean {
        if (dailyEntryDatabaseHelper.deleteDailyEntry(entryId)) {
            // Delete note
            val fileManager = FileManager.instance
            if (dailyEntryMap[entryId]?.linkedNoteId != null) {
                fileManager?.deleteNote(dailyEntryMap[entryId]?.linkedNoteId!!)
            }

            // Delete entry
            dailyEntryMap.remove(entryId)
            dailyEntryDatabaseHelper.deleteDailyEntry(entryId)
            // TODO: dailyEntryDataSynchronizer
            return true
        }
        return false
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var ourInstance: DailyEntryManager? = null
        val instance: DailyEntryManager?
            get() {
                if (ourInstance == null) {
                    ourInstance = DailyEntryManager()
                }
                return ourInstance
            }
    }
}