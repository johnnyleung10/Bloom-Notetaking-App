package com.example.notetakingapp.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import android.text.SpannableStringBuilder
import com.example.notetakingapp.models.*
import com.example.notetakingapp.models.sqlite.NoteTakingDatabaseHelper
import java.util.*
import kotlin.collections.HashMap

class DailyEntryManager {
    private lateinit var context : Context
    private lateinit var noteTakingDatabaseHelper : NoteTakingDatabaseHelper
    // lateinit var dataSynchronizer: DataSynchronizer

    val dailyEntryMap = HashMap<Long, DailyEntryModel>()
    val noteMap = HashMap<Long, NoteModel>()
    val dailyPromptMap = HashMap<Long, DailyPromptModel>()
    val moodMap = HashMap<Long, MoodModel>()

    fun initManager(context: Context) {
        this.context = context
        noteTakingDatabaseHelper = NoteTakingDatabaseHelper(context)
        //dataSynchronizer = DataSynchronizer(noteTakingDatabaseHelper)
    }

    fun initFiles() {
        initPrompts()
        initMoods()
    }

    /**
     * Gets all prompts from database and stores it in dailyPromptMap
     */
    private fun initPrompts() {

    }

    /**
     * Gets all moods from database and stores it in moodMap
     */
    private fun initMoods() {

    }

    /**
     * Returns daily entry by date, format: "yyyy-mm-dd". If no entry exists one will be created.
     */
    fun getDailyEntryByDate(date : String) : DailyEntryModel {
        for (entry in dailyEntryMap.values) {
            if (entry.getDate() == date) {
                return entry
            }
        }
        // Create new entry
        return createDailyEntry()
    }

    /**
     * Creates a new daily entry with a random prompt
     */
    fun createDailyEntry() : DailyEntryModel {
        val random = Random()
        val prompt = dailyPromptMap.entries.elementAt(random.nextInt(dailyPromptMap.size)).key // get a random prompt
        val dailyEntry = DailyEntryModel("Daily Entry", context, prompt)
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
                       promptResponse : String? = null, moodId : Long? = null, dailyImage : Image? = null) {
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