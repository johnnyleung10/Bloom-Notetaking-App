package com.example.NoteAppService.controllers

import com.example.NoteAppService.models.Note
import com.example.NoteAppService.services.NoteService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/notes")
class NoteResource(val service: NoteService) {

    @GetMapping
    fun index(): List<Note> {
        return service.findNotes()
    }

    @PostMapping
    fun post(@RequestBody note: Note): Note {
        service.post(note)
        // TODO: check if insert was successful!
        return note
    }
}