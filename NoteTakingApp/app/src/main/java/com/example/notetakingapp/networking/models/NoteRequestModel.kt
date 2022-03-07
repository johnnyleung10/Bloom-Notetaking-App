package com.example.notetakingapp.networking.models

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequestModel(
    val id: Long,
    val title: String,
    val contentRich: String,
    val contentPlain: String,
    val dateCreated: String,
    val dateModified: String,
    val dateDeleted: String,
    val folderId: Long
)