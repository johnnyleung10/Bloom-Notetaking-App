package com.example.notetakingapp.utilities

import com.example.notetakingapp.models.DailyEntryModel
import com.example.notetakingapp.models.FolderModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.models.sqlite.DailyEntryDatabaseHelper
import com.example.notetakingapp.models.sqlite.NoteTakingDatabaseHelper
import com.example.notetakingapp.networking.ApiService
import com.example.notetakingapp.networking.models.FolderUpdateRequestModel
import com.example.notetakingapp.networking.models.NoteUpdateRequestModel
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class DailyEntryDataSynchronizer(private val dailyEntryDatabaseHelper: DailyEntryDatabaseHelper) {

    private val apiService: ApiService = ApiService.create()

    /**
     * INSERTING
     */
    fun insertDailyEntry(dailyEntry: DailyEntryModel) {
        // Insert the daily entry locally first to populate the ID
        dailyEntryDatabaseHelper.insertDailyEntry(dailyEntry)
    }

    /**
     * Updating
     */
    fun updateDailyEntry(dailyEntry: DailyEntryModel) {
        // Insert the daily entry locally first to populate the ID
        // TODO: @Ajay DB helper method for updating daily entry
//        dailyEntryDatabaseHelper.updateDailyEntry(dailyEntry)
    }
}