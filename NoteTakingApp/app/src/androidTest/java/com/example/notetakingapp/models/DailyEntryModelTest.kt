package com.example.notetakingapp.models

import android.graphics.Bitmap
import com.example.notetakingapp.utilities.DailyPrompts
import com.example.notetakingapp.utilities.Mood
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime

internal class DailyEntryModelTest {

    @Test
    fun getPromptResponse() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        Assert.assertEquals("", newEntry?.promptResponse)
    }

    @Test
    fun setPromptResponse() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        newEntry?.promptResponse = "I love the rain"
        Assert.assertEquals("I love the rain", newEntry?.promptResponse)
    }

    @Test
    fun getMoodId() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        Assert.assertEquals(null, newEntry?.mood)
    }

    @Test
    fun setMoodId() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        newEntry?.mood = Mood.DOUBTFUL
        Assert.assertEquals(Mood.DOUBTFUL, newEntry?.mood)
    }

    @Test
    fun getDailyImage() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        Assert.assertEquals(null, newEntry?.dailyImage)
    }

    @Test
    fun setDailyImage() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        val conf = Bitmap.Config.ARGB_8888
        newEntry?.dailyImage = Bitmap.createBitmap(100, 100, conf)
        Assert.assertEquals(conf, newEntry?.dailyImage?.config)
    }

    @Test
    fun getLinkedNoteId() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        Assert.assertEquals(null, newEntry?.linkedNoteId)
    }

    @Test
    fun setLinkedNoteId() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        newEntry?.linkedNoteId = 1
        Assert.assertEquals(1.toLong(), newEntry?.linkedNoteId)
    }

    @Test
    fun imageToByteArray() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        val conf = Bitmap.Config.ARGB_8888
        newEntry?.dailyImage = Bitmap.createBitmap(100, 100, conf)
        val byteArray = newEntry?.imageToByteArray()
        Assert.assertEquals(148, byteArray?.size)
    }

    @Test
    fun getMonth() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        val todayDate = LocalDateTime.now()
        Assert.assertEquals(todayDate.monthValue, newEntry?.getMonth())
    }

    @Test
    fun getYear() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        val todayDate = LocalDateTime.now()
        Assert.assertEquals(todayDate.year, newEntry?.getYear())
    }

    @Test
    fun getDay() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        val todayDate = LocalDateTime.now()
        Assert.assertEquals(todayDate.dayOfMonth, newEntry?.getDay())
    }

    @Test
    fun getDailyPromptId() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        Assert.assertEquals(0.toLong(), newEntry?.dailyPrompt?.id)
    }

    @Test
    fun setDailyPromptId() {
        val prompt = DailyPrompts[0]
        val newEntry = prompt?.let { DailyEntryModel(it) }
        newEntry?.dailyPrompt = DailyPrompts[1]!!
        Assert.assertEquals(1.toLong(), newEntry?.dailyPrompt?.id)
    }
}