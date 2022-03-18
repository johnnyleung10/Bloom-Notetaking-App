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

enum class Moods(id: MoodModel) {
    HAPPY(MoodModel(0, "Feeling happy", Color.RED)),
    MOTIVATED(MoodModel(1, "Feeling motivated", Color.YELLOW)),
    ENERGETIC(MoodModel(2, "Feeling energetic", Color.CYAN)),
    NEUTRAL(MoodModel(3, "Feeling neutral", Color.BLACK)),
    SAD(MoodModel(4, "Feeling sad", Color.MAGENTA)),
    ANGRY(MoodModel(5, "Feeling angry", Color.BLUE)),
    ENVIOUS(MoodModel(6, "Feeling envious", Color.GREEN)),
}

class DailyEntryManager {
    private lateinit var context : Context
    private lateinit var dailyEntryDatabaseHelper : DailyEntryDatabaseHelper
    // lateinit var dataSynchronizer: DataSynchronizer

    val dailyEntryMap = HashMap<Long, DailyEntryModel>()
    val noteMap = HashMap<Long, NoteModel>()
    val dailyPromptMap = HashMap<Long, DailyPromptModel>()
    val moodMap = HashMap<Long, MoodModel>()

    fun initManager(context: Context) {
        this.context = context
        dailyEntryDatabaseHelper = DailyEntryDatabaseHelper(context)
        //dataSynchronizer = DataSynchronizer(noteTakingDatabaseHelper)
    }

    fun initFiles() {
        initPrompts()
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
     * Creates a new daily entry with a random prompt
     */
    fun createDailyEntry(): DailyEntryModel {
        return DailyEntryModel("Daily Entry", context, getDailyPrompt().id)
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

        title?.let { dailyEntry?.title = title }
        title?.let { dailyEntry?.dailyPromptId = dailyPromptId }
        title?.let { dailyEntry?.promptResponse = promptResponse }
        title?.let { dailyEntry?.moodId = moodId }
        title?.let { dailyEntry?.dailyImage = dailyImage }
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