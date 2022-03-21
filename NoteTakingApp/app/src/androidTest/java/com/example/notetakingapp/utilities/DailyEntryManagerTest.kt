package com.example.notetakingapp.utilities

import android.graphics.Color
import androidx.test.platform.app.InstrumentationRegistry
import com.example.notetakingapp.models.DailyEntryModel
import com.example.notetakingapp.models.DailyPromptModel
import com.example.notetakingapp.models.NoteModel
import org.junit.Assert
import org.junit.Test

internal class DailyEntryManagerTest {

    @Test
    fun getDailyEntryMap() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()
        Assert.assertEquals(HashMap<Long, DailyEntryModel>(), manager?.dailyEntryMap)
    }

    @Test
    fun getNoteMap() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()
        Assert.assertEquals(HashMap<Long, NoteModel>(), manager?.noteMap)
    }

    @Test
    fun getDailyPromptMap() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initFiles()
        Assert.assertEquals(HashMap<Long, DailyPromptModel>(), manager?.dailyPromptMap)
    }

    @Test
    fun testMoodEnum() {
        val mood = Moods.ANGRY
        Assert.assertEquals(Color.BLUE, mood.colour)
    }

    @Test
    fun initManager() {
    }

    @Test
    fun initFiles() {
    }

    @Test
    fun getDailyPrompt() {
    }

    @Test
    fun getDailyEntryByDate() {
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