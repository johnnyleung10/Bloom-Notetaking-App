package com.example.notetakingapp.networking.models

import kotlinx.serialization.Serializable

@Serializable
data class NoteDeletionRequestModel(
    val id: Long,
)