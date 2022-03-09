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
}