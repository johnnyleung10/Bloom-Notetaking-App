package com.example.NoteAppService.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "NOTE_TABLE")
data class Note (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long,
    var title: String,
    var content: String,
    var dataCreated: String,
    var dataModified: String,
    var dataDeleted: String
    )