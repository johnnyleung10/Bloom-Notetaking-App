package com.example.notetakingapp.networking

import com.example.notetakingapp.networking.models.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*

class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {

    /* NOTES */

    override suspend fun getNotes(): List<NoteResponseModel> {
        return try {
            client.get { url(ApiRoutes.NOTES) }
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            emptyList()
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            emptyList()
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            emptyList()
        }
    }

    override suspend fun insertNote(noteRequest: NoteRequestModel): NoteResponseModel? {
        return try {

            client.post<NoteResponseModel> {
                url(ApiRoutes.NOTES)
                body = noteRequest
            }
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            null
        }
    }

    override suspend fun updateNote(noteRequest: NoteRequestModel): NoteResponseModel? {
        return try {

            client.put<NoteResponseModel> {
                url(ApiRoutes.NOTES)
                body = noteRequest
            }
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            null
        }
    }

    /* FOLDERS */

    override suspend fun getFolders(): List<FolderResponseModel> {
        return try {
            client.get { url(ApiRoutes.FOLDERS) }
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            emptyList()
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            emptyList()
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            emptyList()
        }
    }

    override suspend fun insertFolder(folderRequest: FolderRequestModel): FolderResponseModel? {
        return try {

            client.post<FolderResponseModel> {
                url(ApiRoutes.FOLDERS)
                body = folderRequest
            }
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            null
        }
    }

    override suspend fun updateFolder(folderRequest: FolderRequestModel): FolderResponseModel? {
        return try {

            client.put<FolderResponseModel> {
                url(ApiRoutes.FOLDERS)
                body = folderRequest
            }
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            null
        }
    }

    /* Test endpoints */

    override suspend fun getTest(): List<TestResponseModel> {
        return try {
            client.get { url(ApiRoutes.TEST) }
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            emptyList()
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            emptyList()
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            emptyList()
        }
    }
}