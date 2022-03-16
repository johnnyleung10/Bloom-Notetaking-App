package com.example.notetakingapp.utilities

import android.annotation.SuppressLint
import android.content.Context
import com.example.notetakingapp.models.*
import com.example.notetakingapp.models.sqlite.NoteTakingDatabaseHelper

class DailyEntryManager {
    private lateinit var context : Context
    private lateinit var noteTakingDatabaseHelper : NoteTakingDatabaseHelper
    // lateinit var dataSynchronizer: DataSynchronizer

    val dailyEntryMap = HashMap<Long, DailyEntryModel>()
    val noteMap = HashMap<Long, NoteModel>()
    val dailyPromptMap = HashMap<Long, DailyPrompt>()
    val moodMap = HashMap<Long, Mood>()

    fun initManager(context: Context) {
        this.context = context
        noteTakingDatabaseHelper = NoteTakingDatabaseHelper(context)
        //dataSynchronizer = DataSynchronizer(noteTakingDatabaseHelper)
    }

    fun initFiles() {

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