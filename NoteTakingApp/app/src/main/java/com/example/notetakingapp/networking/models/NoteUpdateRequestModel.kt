package com.example.notetakingapp.networking.models

import kotlinx.serialization.Serializable

@Serializable
data class NoteUpdateRequestModel(
    val id: Long,
    val title: String?,
    val content: String?,
    val dateModified: String?,
    val dateDeleted: String?,
    val folderId: Long?
)