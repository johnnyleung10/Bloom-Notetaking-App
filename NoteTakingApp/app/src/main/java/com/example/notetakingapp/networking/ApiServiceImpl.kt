package com.example.notetakingapp.networking

import com.example.notetakingapp.networking.models.NoteRequestModel
import com.example.notetakingapp.networking.models.NoteResponseModel
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*

class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {

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

    override suspend fun createNote(noteRequest: NoteRequestModel): NoteResponseModel? {
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
}