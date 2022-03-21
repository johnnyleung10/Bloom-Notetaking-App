package com.example.notetakingapp.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.example.notetakingapp.models.*
import com.example.notetakingapp.models.sqlite.DailyEntryDatabaseHelper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

enum class Mood(id: MoodModel) {
    NO_SELECTION(MoodModel(0, "No Mood", Color.BLUE)),
    HAPPY(MoodModel(1, "Feeling happy", Color.RED)),
    LOVING(MoodModel(2, "Feeling loving", Color.YELLOW)),
    EXCITED(MoodModel(3, "Feeling excited", Color.CYAN)),
    NEUTRAL(MoodModel(4, "Feeling neutral", Color.BLACK)),
    SAD(MoodModel(5, "Feeling sad", Color.MAGENTA)),
    ANGRY(MoodModel(6, "Feeling angry", Color.BLUE)),
    DOUBTFUL(MoodModel(7, "Feeling doubtful", Color.GREEN)),
}

class DailyEntryManager {
    private lateinit var context : Context
    lateinit var dailyEntryDatabaseHelper : DailyEntryDatabaseHelper
    lateinit var dailyEntryDataSynchronizer: DailyEntryDataSynchronizer

    val dailyEntryMap = HashMap<Long, DailyEntryModel>()
    val dailyPromptMap = HashMap<Long, DailyPromptModel>()

    fun initManager(context: Context) {
        this.context = context
        this.dailyEntryDatabaseHelper = DailyEntryDatabaseHelper(context)
        this.dailyEntryDataSynchronizer = DailyEntryDataSynchronizer(dailyEntryDatabaseHelper)
    }

    fun initEntries() {
        initPrompts()
        initDailyEntries()
    }

    /**
     * Gets all prompts from database and stores it in dailyPromptMap
     */
    private fun initPrompts() {
        for (prompt in dailyEntryDatabaseHelper.getAllPrompts()) {
            dailyPromptMap[prompt.id] = prompt
        }
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
    fun getDailyPrompt() : DailyPromptModel {
        val random = Random()
        return dailyPromptMap.entries.elementAt(random.nextInt(dailyPromptMap.size)).value // get a random prompt
    }

    /**
     * Returns daily entry by date, format: "yyyy-mm-dd". If no entry exists one will be created.
     */
    fun getDailyEntryByDate(month : Int, year: Int) : List<DailyEntryModel> {
        val retEntries = ArrayList<DailyEntryModel>()
        for (entry in dailyEntryMap.values) {
            if (entry.getMonth() == month && entry.getYear() == year) {
                retEntries.add(entry)
            }
        }
        return retEntries
    }

    /**
     * Creates a new daily entry
     */
    fun createDailyEntry(promptResponse : String? = "",
                         moodId : Long? = null,
                         dailyImage : Bitmap? = null,
                         linkedNoteId : Long? = null,
                         dailyPromptId : Long?): DailyEntryModel {

        val dailyEntry = DailyEntryModel(promptResponse, moodId, dailyImage, linkedNoteId, dailyPromptId)

        // Update in database
        dailyEntryDataSynchronizer.insertDailyEntry(dailyEntry)
        dailyEntryMap[dailyEntry.id] = dailyEntry
        return dailyEntry
    }

    /**
     * Returns daily entry by Id
     */
    fun getDailyEntry(entryId : Long) : DailyEntryModel? {
        return dailyEntryMap[entryId]
    }

    /**
     * Updates daily entry data
     */
    fun editDailyEntry(entryId : Long, title: String? = null, dailyPromptId : Long? = null,
                       promptResponse : String? = null, moodId : Long? = null, dailyImage : Bitmap? = null) {
        // Get entry
        val dailyEntry = getDailyEntry(entryId)

        dailyPromptId?.let { dailyEntry?.dailyPromptId = dailyPromptId }
        promptResponse?.let { dailyEntry?.promptResponse = promptResponse }
        moodId?.let { dailyEntry?.moodId = moodId }
        dailyImage?.let { dailyEntry?.dailyImage = dailyImage }
    }

    /**
     * Delete daily entry from database and from local storage
     */
    fun deleteDailyEntry(entryId: Long) : Boolean {
        if (dailyEntryDatabaseHelper.deleteDailyEntry(entryId)) {
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