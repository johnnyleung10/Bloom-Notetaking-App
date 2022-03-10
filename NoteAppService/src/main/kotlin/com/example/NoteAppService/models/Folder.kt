package com.example.NoteAppService.models

import javax.persistence.*

@Entity
@Table(name = "FOLDER_TABLE")
data class Folder(
    @Id
    var id: Long,

    var title: String,

    var dateCreated: String,

    var dateModified: String,

    var dateDeleted: String,
)
