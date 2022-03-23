package com.example.notetakingapp.utilities

import android.graphics.Color
import androidx.test.platform.app.InstrumentationRegistry
import com.example.notetakingapp.R
import com.example.notetakingapp.models.DailyEntryModel
import com.example.notetakingapp.models.DailyPromptModel
import com.example.notetakingapp.models.NoteModel
import com.example.notetakingapp.models.sqlite.DailyEntryDatabaseHelper
import com.example.notetakingapp.models.sqlite.NoteTakingDatabaseHelper
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime

internal class DailyEntryManagerTest {

    private fun cleanupManager() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val db = DailyEntryDatabaseHelper(appContext)
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.dailyEntryMap?.clear()
        db.clearDatabase()
    }

    @Test
    fun getDailyEntryMap() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initEntries()
        Assert.assertEquals(HashMap<Long, DailyEntryModel>(), manager?.dailyEntryMap)
    }

    @Test
    fun testMoodEnum() {
        val mood = Mood.ANGRY
        Assert.assertEquals(R.color.angry, mood.colour)
        Assert.assertEquals(R.color.angry, Mood.getColour(6))
    }

    @Test
    fun initManager() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initEntries()

        //TODO: Clear database
    }


    @Test
    fun initEntries() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initEntries()

        //TODO: Clear database
    }

    @Test
    fun getDailyEntryByDate() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initEntries()

        val today = LocalDateTime.now()
        Assert.assertEquals(0 ,manager?.getDailyEntriesByDate(today.monthValue, today.year)?.size)
        manager?.createDailyEntry()
        Assert.assertEquals(1 ,manager?.getDailyEntriesByDate(today.monthValue, today.year)?.size)
    }

    @Test
    fun createDailyEntry() {
    }

    @Test
    fun getDailyEntry() {
    }

    @Test
    fun editDailyEntry() {
    }

    @Test
    fun deleteDailyEntry() {
    }
}