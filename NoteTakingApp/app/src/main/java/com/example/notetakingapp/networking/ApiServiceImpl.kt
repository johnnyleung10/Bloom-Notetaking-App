package com.example.notetakingapp.networking

import com.example.notetakingapp.networking.models.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import java.net.ConnectException
import java.net.SocketTimeoutException

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
        } catch (ex: ConnectException) {
            println("Error: ConnectException")
            emptyList()
        } catch (ex: SocketTimeoutException){
            println("Error: Socket Timeout Connection")
            emptyList()
        }
    }

    override suspend fun insertNote(noteCreationRequest: NoteCreationRequestModel): EmptyResponseModel? {
        return try {

            client.post<EmptyResponseModel> {
                url(ApiRoutes.NOTES)
                body = noteCreationRequest
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
        } catch (ex: ConnectException) {
            println("Error: ConnectException")
            null
        } catch (ex: SocketTimeoutException){
            println("Error: Socket Timeout Connection")
            null
        }
    }

    override suspend fun updateNote(noteUpdateRequest: NoteUpdateRequestModel): EmptyResponseModel? {
        return try {

            client.put<EmptyResponseModel> {
                url(ApiRoutes.NOTES)
                body = noteUpdateRequest
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
        } catch (ex: ConnectException) {
            println("Error: ConnectException")
            null
        } catch (ex: SocketTimeoutException){
            println("Error: Socket Timeout Connection")
            null
        }
    }

    override suspend fun deleteNote(noteId: Long): EmptyResponseModel? {
        return try {

            client.delete<EmptyResponseModel> {
                url(ApiRoutes.NOTES)
                parameter("noteId", noteId)
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
        } catch (ex: ConnectException) {
            println("Error: ConnectException")
            null
        } catch (ex: SocketTimeoutException){
            println("Error: Socket Timeout Connection")
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
        } catch (ex: ConnectException) {
            println("Error: ConnectException")
            emptyList()
        } catch (ex: SocketTimeoutException){
            println("Error: Socket Timeout Connection")
            emptyList()
        }
    }

    override suspend fun insertFolder(folderRequest: FolderCreationRequestModel): EmptyResponseModel? {
        return try {

            client.post {
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
        } catch (ex: ConnectException) {
            println("Error: ConnectException")
            null
        } catch (ex: SocketTimeoutException){
            println("Error: Socket Timeout Connection")
            null
        }
    }

    override suspend fun updateFolder(folderRequest: FolderUpdateRequestModel): EmptyResponseModel? {
        return try {

            client.put<EmptyResponseModel> {
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
        } catch (ex: ConnectException) {
            println("Error: ConnectException")
            null
        } catch (ex: SocketTimeoutException){
            println("Error: Socket Timeout Connection")
            null
        }
    }

    override suspend fun deleteFolder(folderId: Long): EmptyResponseModel? {
        return try {

            client.delete<EmptyResponseModel> {
                url(ApiRoutes.FOLDERS)
                parameter("folderId", folderId)
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
        } catch (ex: ConnectException) {
            println("Error: ConnectException")
            null
        } catch (ex: SocketTimeoutException){
            println("Error: Socket Timeout Connection")
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
        } catch (ex: ConnectException) {
            println("Error: ConnectException")
            emptyList()
        } catch (ex: SocketTimeoutException){
            println("Error: Socket Timeout Connection")
            emptyList()
        }
    }
}