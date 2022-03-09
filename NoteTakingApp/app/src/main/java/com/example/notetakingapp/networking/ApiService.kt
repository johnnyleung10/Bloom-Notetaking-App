package com.example.notetakingapp.networking

import com.example.notetakingapp.networking.models.*
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

interface ApiService {

    suspend fun getNotes(): List<NoteResponseModel>
    suspend fun insertNote(noteCreationRequestModel: NoteCreationRequestModel): EmptyResponseModel?
    suspend fun updateNote(noteCreationRequestModel: NoteUpdateRequestModel): EmptyResponseModel?
    suspend fun deleteNote(noteDeletionRequestModel: NoteDeletionRequestModel): EmptyResponseModel?
    suspend fun getFolders(): List<FolderResponseModel>
    suspend fun insertFolder(folderRequestModel: FolderCreationRequestModel): EmptyResponseModel?
    suspend fun updateFolder(folderRequestModel: FolderUpdateRequestModel): EmptyResponseModel?
    suspend fun deleteFolder(folderDeletionRequestModel: FolderDeletionRequestModel): EmptyResponseModel?
    suspend fun getTest(): List<TestResponseModel>

    companion object {
        fun create(): ApiService {
            return ApiServiceImpl(
                client = HttpClient(Android) {
                    // Logging
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    // JSON
                    install(JsonFeature) {
                        serializer = KotlinxSerializer(json)
                        //or serializer = KotlinxSerializer()
                    }
                    // Timeout
                    install(HttpTimeout) {
                        requestTimeoutMillis = 15000L
                        connectTimeoutMillis = 15000L
                        socketTimeoutMillis = 15000L
                    }
                    // Apply to all requests
                    defaultRequest {
                        // Parameter("api_key", "some_api_key")
                        // Content Type
                        if (method != HttpMethod.Get) contentType(ContentType.Application.Json)
                        accept(ContentType.Application.Json)
                    }
                }
            )
        }

        private val json = kotlinx.serialization.json.Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
    }
}