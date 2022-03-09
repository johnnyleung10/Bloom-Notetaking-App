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

    fun findNote(noteId: Long): Note? {
        var NoteOptional = db.findById(noteId)
        if (NoteOptional.isPresent) {
            return NoteOptional.get()
        }
        return null
    }

    fun post(note: Note): Long {
        return db.save(note).id
    }

    fun delete(noteId: Long) {
        db.deleteById(noteId)
    }

    fun put(noteId: Long, title: String? = null, contentRich: String? = null, contentPlain: String? = null, dateModified: String? = null, dateDeleted: String? = null) {
        val noteOptional = db.findById(noteId)

        if (noteOptional.isPresent) {
            val note = noteOptional.get()
            title?.let { note.title = title }
            contentRich?.let { note.contentRich = contentRich }
            contentPlain?.let { note.contentPlain = contentPlain }
            dateModified?.let { note.dateModified = dateModified }
            dateDeleted?.let { note.dateDeleted = dateDeleted }
            db.save(note)
        }
    }
}