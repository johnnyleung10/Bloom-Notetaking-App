package com.example.notetakingapp.utilities

import android.annotation.SuppressLint
import android.content.Context
import java.io.BufferedWriter
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class Profiler {
    var file: FileWriter? = null
    var writer: BufferedWriter? = null
    var context: Context? = null

    enum class TYPE { DEBUG, LOG, PROFILING}

    fun Date.toString(format: String): String = SimpleDateFormat(
        format, Locale.getDefault()).format(this)

    fun getCurrentDateTime(): Date = Calendar.getInstance().time

    fun init(context: Context) {
        this.context = context
    }

    fun open() {
        file = FileWriter(context?.filesDir.toString() + "/ProfileData.txt", true)
        writer = BufferedWriter(file)
    }
    fun debug(description: String, value: Long) = save(Entry(TYPE.DEBUG, description, value))

    fun log(description: String, value: Long) = save(Entry(TYPE.LOG, description, value))

    fun profile(description: String, value: Long) = save(Entry(TYPE.PROFILING, description, value))

    private fun save(entry: Entry) = writer?.write(entry.toString() + "\n")

    fun close() {
        writer?.close()
        file?.close()
    }

    data class Entry(val type: TYPE, val description: String, val value: Long) {
        override fun toString(): String {
            return "${Calendar.getInstance().time}, $type, $description, $value"
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var ourInstance: Profiler? = null
        val instance: Profiler?
            get() {
                if (ourInstance == null) {
                    ourInstance = Profiler()
                }
                return ourInstance
            }
    }
}