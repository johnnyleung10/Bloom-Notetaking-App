package com.example.notetakingapp.networking

object ApiRoutes {
    private const val BASE_URL = "http://10.0.2.2:8080"
    const val NOTES = "$BASE_URL/note"
    const val FOLDERS = "$BASE_URL/folder"
    const val TEST = "$BASE_URL/test"
}