package com.example.notetakingapp.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.notetakingapp.R
import com.example.notetakingapp.models.*
import com.example.notetakingapp.models.sqlite.DailyEntryDatabaseHelper
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
    1.toLong() to DailyPromptModel(1, "What reminds you of home"))

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
     * Creates a new daily entry with a random prompt
     */
    fun createDailyEntry(): DailyEntryModel {
        // TODO: Check that no daily entry exists for today?
        val dailyEntry = DailyEntryModel(dailyPrompt = getDailyPrompt())

        // Create new note
        val fileManager = FileManager.instance
        fileManager?.initManager(context)
        val newNote = fileManager?.createNewNote("Daily Entry " +dailyEntry.getMonth() +"-"
                +dailyEntry.getDay() +"-" +dailyEntry.getYear())
        dailyEntry.linkedNoteId = newNote?.id

        dailyEntryDataSynchronizer.insertDailyEntry(dailyEntry)
        dailyEntryMap[dailyEntry.id] = dailyEntry
        return dailyEntry
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

        dailyEntry?.updateModifiedDate()

        // Update in database
        dailyEntryDataSynchronizer.updateDailyEntry(dailyEntry)
    }

    /**
     * Delete daily entry from database and from local storage
     */
    fun deleteDailyEntry(entryId: Long) : Boolean {
        if (dailyEntryDatabaseHelper.deleteDailyEntry(entryId)) {
            // Delete note
            val fileManager = FileManager.instance
            fileManager?.deleteNote(dailyEntryMap[entryId]?.linkedNoteId!!)

            // Delete entry
            dailyEntryMap.remove(entryId)
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