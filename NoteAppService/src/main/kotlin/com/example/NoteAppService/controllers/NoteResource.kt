package com.example.NoteAppService.controllers

import com.example.NoteAppService.models.Note
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.example.NoteAppService.services.NoteService

@RestController
class NoteResource(val service: NoteService) {

    @GetMapping
    fun index(): List<Note> {
        return service.findNotes()
    }

    @PostMapping
    fun post(@RequestBody note: Note) {
        service.post(note)
    }
}