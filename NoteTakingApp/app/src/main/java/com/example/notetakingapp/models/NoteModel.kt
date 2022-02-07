package com.example.notetakingapp.models

import java.util.*

abstract class NoteModel {
    var title : String = ""
    var content : String = ""
    var dateCreated : Date = Date()
    var lastModifiedDate : Date = Date()
    var deletionDate : Date = Date()

}