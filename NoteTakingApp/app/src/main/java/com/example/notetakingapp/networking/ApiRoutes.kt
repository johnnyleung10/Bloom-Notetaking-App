package com.example.notetakingapp.networking

object ApiRoutes {
    private const val BASE_URL = "http://10.0.2.2:8080"
    const val NOTES = "$BASE_URL/notes"
    const val FOLDERS = "$BASE_URL/folders"
    const val TEST = "$BASE_URL/test"
}