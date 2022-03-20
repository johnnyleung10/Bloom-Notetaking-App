package com.example.notetakingapp.models

import android.graphics.Bitmap
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime

internal class DailyEntryModelTest {

    @Test
    fun getPromptResponse() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        Assert.assertEquals("", newEntry.promptResponse)
    }

    @Test
    fun setPromptResponse() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        newEntry.promptResponse = "I love the rain"
        Assert.assertEquals("I love the rain", newEntry.promptResponse)
    }

    @Test
    fun getMoodId() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        Assert.assertEquals(null, newEntry.moodId)
    }

    @Test
    fun setMoodId() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        newEntry.moodId = 0
        Assert.assertEquals(0.toLong(), newEntry.moodId)
    }

    @Test
    fun getDailyImage() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        Assert.assertEquals(null, newEntry.dailyImage)
    }

    @Test
    fun setDailyImage() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        val conf = Bitmap.Config.ARGB_8888
        newEntry.dailyImage = Bitmap.createBitmap(100, 100, conf)
        Assert.assertEquals(conf, newEntry.dailyImage?.config)
    }

    @Test
    fun getLinkedNoteId() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        Assert.assertEquals(null, newEntry.linkedNoteId)
    }

    @Test
    fun setLinkedNoteId() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        newEntry.linkedNoteId = 1
        Assert.assertEquals(1.toLong(), newEntry.linkedNoteId)
    }

    @Test
    fun imageToByteArray() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        val conf = Bitmap.Config.ARGB_8888
        newEntry.dailyImage = Bitmap.createBitmap(100, 100, conf)
        val byteArray = newEntry.imageToByteArray()
        Assert.assertEquals(148, byteArray.size)
    }

    @Test
    fun getMonth() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        val todayDate = LocalDateTime.now()
        Assert.assertEquals(todayDate.monthValue, newEntry.getMonth())
    }

    @Test
    fun getYear() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        val todayDate = LocalDateTime.now()
        Assert.assertEquals(todayDate.year, newEntry.getYear())
    }

    @Test
    fun getDay() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        val todayDate = LocalDateTime.now()
        Assert.assertEquals(todayDate.dayOfMonth, newEntry.getDay())
    }

    @Test
    fun getDailyPromptId() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        Assert.assertEquals(0.toLong(), newEntry.dailyPromptId)
    }

    @Test
    fun setDailyPromptId() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val newEntry = DailyEntryModel("DailyEntry", appContext, 0)
        newEntry.dailyPromptId = 12
        Assert.assertEquals(12.toLong(), newEntry.dailyPromptId)
    }
}