package com.example.NoteAppService.services

import com.example.NoteAppService.models.Note
import com.example.NoteAppService.repositories.NoteRepository
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service


@Service
class NoteService(private val db: NoteRepository){

    fun findNotes(): List<Note> {
        try {
            val noteList: MutableList<Note> = arrayListOf()
            db.findAll().forEach{noteList.add(it)}
            return noteList
        } catch (ex: DataAccessException) {
            throw(ex)
        }
    }

    fun insertOrUpdateNote(note: Note) {
        try {
            db.save(note)
        } catch(ex: DataAccessException){
            throw(ex)
        }
    }

    fun delete(noteId: Long) : Boolean {
        try {
            val noteOptional = db.findById(noteId)
            if (noteOptional.isPresent) {
                db.deleteById(noteId)
                return true
            }
            return false
        } catch(ex: DataAccessException){
            throw(ex)
        }

    }
}