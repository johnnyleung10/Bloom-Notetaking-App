package com.example.notetakingapp.models

import java.time.LocalDateTime

class FolderModel(
    var title: String,
) {
    // var files : ArrayList<NoteModel> = ArrayList<NoteModel>()

    var id : Int = 0

    val dateCreated : LocalDateTime = LocalDateTime.now()
    var lastModifiedDate : LocalDateTime = dateCreated
    var deletionDate : LocalDateTime? = null
}