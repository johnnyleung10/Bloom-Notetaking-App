package com.example.notetakingapp.networking.models

import kotlinx.serialization.Serializable

@Serializable
data class TestResponseModel(
    val test: String
)