package com.example.NoteAppService.services

import com.example.NoteAppService.models.Note
import com.example.NoteAppService.repositories.NoteRepository
import org.springframework.stereotype.Service

@Service
class NoteService(private val db: NoteRepository){

    fun findNotes(): List<Note> {
        val noteList: MutableList<Note> = arrayListOf()
        db.findAll().forEach{noteList.add(it)}
        return noteList
    }

    fun post(note: Note) {
        db.save(note)
    }
}