package com.example.NoteAppService.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "FOLDER_TABLE")
data class Folder(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long,
    var title: String,
    var dateCreated: String,
    var dateModified: String,
    var dateDeleted: String,
)
