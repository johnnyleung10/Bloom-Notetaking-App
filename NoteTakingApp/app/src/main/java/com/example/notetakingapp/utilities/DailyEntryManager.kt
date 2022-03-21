package com.example.notetakingapp.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.example.notetakingapp.models.*
import com.example.notetakingapp.models.sqlite.DailyEntryDatabaseHelper
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

enum class Mood(val id: Long, val description : String, val colour : Int) {
    NO_SELECTION(0, "No Mood", Color.BLUE),
    HAPPY(1, "Feeling happy", Color.RED),
    LOVING(2, "Feeling loving", Color.YELLOW),
    EXCITED(3, "Feeling excited", Color.CYAN),
    NEUTRAL(4, "Feeling neutral", Color.BLACK),
    SAD(5, "Feeling sad", Color.MAGENTA),
    ANGRY(6, "Feeling angry", Color.BLUE),
    DOUBTFUL(7, "Feeling doubtful", Color.GREEN);

    companion object {
        private val allValues = values()
        fun getMood(id: Long): Mood? = allValues.find { it.id == id }
        fun getDesc(id: Long): String? = allValues.find { it.id == id }?.description
        fun getColour(id: Long): Int? = allValues.find { it.id == id }?.colour
    }
}

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
     * Returns daily entry by date, format: "yyyy-mm-dd". If no entry exists one will be created.
     */
    fun getDailyEntriesByDate(month : Int, year: Int) : List<DailyEntryModel> {
        val retEntries = ArrayList<DailyEntryModel>()
        for (entry in dailyEntryMap.values) {
            if (entry.getMonth() == month && entry.getYear() == year) {
                retEntries.add(entry)
            }
        }
        return retEntries
    }

    /**
     * Creates a new daily entry with a random prompt
     */
    fun createDailyEntry(): DailyEntryModel {
        val dailyEntry = DailyEntryModel(dailyPrompt = getDailyPrompt())

        // Create new note
        val fileManager = FileManager.instance
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
    fun editDailyEntry(entryId : Long, dailyPrompt : DailyPromptModel? = null, promptResponse : String? = null,
                       mood : Mood? = null, dailyImage : Bitmap? = null) {
        // Get entry
        val dailyEntry = getDailyEntryById(entryId)

        dailyPrompt?.let { dailyEntry?.dailyPrompt = dailyPrompt }
        promptResponse?.let { dailyEntry?.promptResponse = promptResponse }
        mood?.let { dailyEntry?.mood = mood }
        dailyImage?.let { dailyEntry?.dailyImage = dailyImage }

        dailyEntry?.updateModifiedDate()

        // Update in database
        if (dailyEntry != null) {
            dailyEntryDatabaseHelper.updateDailyEntry(dailyEntry)
            dailyEntryDataSynchronizer.updateDailyEntry(dailyEntry)
        }
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