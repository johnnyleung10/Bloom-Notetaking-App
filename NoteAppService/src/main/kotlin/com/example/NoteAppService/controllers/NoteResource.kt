package com.example.NoteAppService.controllers

import com.example.NoteAppService.models.Note
import com.example.NoteAppService.services.NoteService
import org.springframework.web.bind.annotation.*

@RestController
class NoteResource(val service: NoteService) {

    @GetMapping
    fun index(): List<Note> {
        return service.findNotes()
    }

    @PostMapping
    fun post(@RequestBody note: Note): Long {
        return service.post(note)
    }

    @PutMapping
    fun put(@RequestParam noteId: Long, @RequestParam(required = false) title: String?,
            @RequestParam(required = false) contentRich: String?, @RequestParam(required = false) contentPlain: String?,
            @RequestParam(required = false) dateModified: String?, @RequestParam(required = false) dateDeleted: String?,
            @RequestParam(required = false) folderId: Long?) : Long {
        return service.put(noteId, title=title, contentRich=contentRich, contentPlain=contentPlain,
                dateModified=dateModified, dateDeleted=dateDeleted, folderId=folderId)
    }

    @DeleteMapping
    fun delete(@RequestParam noteId: Long) : Boolean {
        return service.delete(noteId)
    }
}