package com.example.notetakingapp.utilities

import androidx.test.platform.app.InstrumentationRegistry
import com.example.notetakingapp.R
import com.example.notetakingapp.models.DailyEntryModel
import com.example.notetakingapp.models.sqlite.DailyEntryDatabaseHelper
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.junit.Assert
import org.junit.Test
import java.io.File
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
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)

        Assert.assertNotEquals(null, manager?.dailyEntryDatabaseHelper)
    }


    @Test
    fun initEntries() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initEntries()

        Assert.assertEquals(0, manager?.dailyEntryMap?.size)
    }

    @Test
    fun getDailyEntriesByDate() {
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
    fun getDailyEntryByDate() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initEntries()

        val today = LocalDateTime.now()
        Assert.assertEquals(null, manager?.getDailyEntryByDate(today.monthValue, today.dayOfMonth, today.year))
        manager?.createDailyEntry()
        Assert.assertNotEquals(null, manager?.getDailyEntryByDate(today.monthValue, today.dayOfMonth, today.year))
    }

    @Test
    fun createDailyEntry() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initEntries()

        val entry = manager?.createDailyEntry()
        Assert.assertEquals(1.toLong(), entry?.id)
        val entry2 = manager?.createDailyEntry()
        Assert.assertEquals(1.toLong(), entry2?.id) // should be same
    }

    @Test
    fun getDailyEntryById() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initEntries()

        val entry = manager?.createDailyEntry()
        Assert.assertEquals(entry, manager?.getDailyEntryById(entry!!.id))
    }

    @Test
    fun updateDailyEntry() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initEntries()

        val dbHelper = DailyEntryDatabaseHelper(appContext)

        val entry = manager?.createDailyEntry()
        entry?.mood = Mood.HAPPY
        manager?.updateDailyEntry(entry!!)

        testAsync(Mood.HAPPY, dbHelper)
    }

    fun testAsync(mood : Mood, databaseHelper: DailyEntryDatabaseHelper): Deferred<Int> = GlobalScope.async {
        delay(1000)
        Assert.assertEquals(mood, databaseHelper.getAllDailyEntries()[0])
        return@async 42
    }

    @Test
    fun createLinkedNote() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initEntries()

        val entry = manager?.createDailyEntry()
        entry?.promptResponse = "I am very happy"
        entry?.mood = Mood.HAPPY
        manager?.updateDailyEntry(entry!!)
        val noteId = manager?.createLinkedNote(entry!!)

        val fileManager : FileManager? = FileManager.instance
        manager?.initManager(appContext)
        manager?.initEntries()

        Assert.assertEquals("Daily Entry " +entry?.getMonth() +"-" +entry?.getDay() +"-"
                +entry?.getYear(), fileManager?.getNote(noteId!!)?.title)
    }

    @Test
    fun deleteDailyEntry() {
        cleanupManager()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val manager : DailyEntryManager? = DailyEntryManager.instance
        manager?.initManager(appContext)
        manager?.initEntries()

        val today = LocalDateTime.now()
        val entry = manager?.createDailyEntry()
        Assert.assertNotEquals(null, manager?.getDailyEntryByDate(today.monthValue, today.dayOfMonth, today.year))
        manager?.deleteDailyEntry(entry!!.id)
        Assert.assertEquals(null, manager?.getDailyEntryByDate(today.monthValue, today.dayOfMonth, today.year))
    }
}